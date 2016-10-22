/**
 * 
 */
package ask.atg.chat.server;

import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ask.atg.chat.server.i18n.Messages;
import ask.atg.chat.server.json.JsonParser;
import ask.atg.chat.server.json.model.JsonMessage;
import ask.atg.chat.server.json.model.JsonUser;
import ask.atg.chat.server.model.Chat;
import ask.atg.chat.server.model.Contact;
import ask.atg.chat.server.model.Message;
import ask.atg.chat.server.model.User;
import ask.atg.chat.server.service.ChatService;
import ask.atg.chat.server.service.ChatServiceImpl;
import ask.atg.chat.server.service.JWTFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Startup class for the chat server.
 * 
 * TODO: Move all JSON generation to a separate file. TODO: Take a closer look on all the ifs.
 * 
 * @author Anders Skoglund
 *
 */
public class Server
{
    // Initialize logger
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    // TODO: This kind of data would fit better in a fast db like Redis.
    static Map<Session, String> userUsernameMap = new HashMap<>();
    static Map<String, User> onlineUsers = new HashMap<>();

    // Guice Injector for dependency injection.
    private static Injector injector = Guice.createInjector(new ChatModule());

    /**
     * Getter to fetch an instance of {@link ChatService}.
     * 
     * @return
     */
    public static ChatService getService()
    {
        return injector.getInstance(ChatServiceImpl.class);
    }

    /**
     * Application starts here...
     * 
     * @param args
     */
    public static void main(String... args)
    {
        // Inject ChatService with Guice

        ChatService chatService = getService();

        final JWTFactory jwtFactory = JWTFactory.get();

        // Intro fluff
        intro();

        // Set location of web files
        staticFileLocation("/web");
        staticFiles.expireTime(1000);

        // Websocket route
        webSocket("/socket/chat", ChatWebSocket.class);
        init();

        // HTTP get route to fetch a chat
        get("/api/getChat/:from/:to", (request, response) -> {

            final String from = request.params(":from");
            final String to = request.params(":to");

            Optional<User> fromUser = chatService.findByName(from);
            Optional<User> toUser = chatService.findByName(to);

            JSONObject result = new JSONObject();

            if (fromUser.isPresent() && toUser.isPresent())
            {
                Optional<Contact> contactOpt = chatService.findMutual(fromUser.get(), toUser.get());
                Contact contact;
                if (contactOpt.isPresent())
                {
                    contact = contactOpt.get();
                }
                else
                {
                    contact = Contact.create(fromUser.get(), toUser.get()).replace(
                        Contact.Type.MUTUAL);
                    chatService.save(contact);
                }

                Optional<Chat> chatOpt = chatService.findByContact(contact);
                Chat chat;
                if (chatOpt.isPresent())
                {
                    chat = chatOpt.get();
                }
                else
                {
                    chat = Chat.create(contact);
                    chatService.save(chat);
                }

                result.put("chat_id", chat.getId());
                result.put("messages", getMessages(chat));

            }

            response.status(200);
            response.type("application/json");

            return String.valueOf(result);
        });

        post("/api/register", (request, response) -> {

            Optional<JsonUser> json = JsonParser.fromJson(request.body(), JsonUser.class);

            JSONObject result = new JSONObject();

            if (json.isPresent())
            {
                try
                {
                    chatService.save(User.create(json.get()));
                    result.put("success", true);

                }
                catch (IllegalArgumentException e)
                {
                    result.put("msg", e.getMessage());
                }
            }
            else
            {
                result.put("success", false);
            }

            response.status(200);
            response.type("application/json");

            return String.valueOf(result);
        });

        post("/api/authenticate", (request, response) -> {

            JSONObject obj = new JSONObject(request.body());

            final String username = obj.getString("username");
            final String password = obj.getString("password");

            response.status(200);
            response.type("application/json");

            JSONObject token = new JSONObject();

            Optional<User> existingUser = chatService.login(username, password);

            if (existingUser.isPresent())
            {
                User user = existingUser.get();
                token.put("token", jwtFactory.createToken(user));
                onlineUsers.put(username, user);
            }

            return String.valueOf(token);
        });

    }

    /**
     * Get all messages as json from the given {@link Chat}.
     * 
     * @param chat
     * @return
     */
    private static Collection<JSONObject> getMessages(Chat chat)
    {
        return chat.getMessagesChronological(false)
            .stream()
            .map(msg -> {

                JSONObject json = new JSONObject();

                try
                {
                    json.put("from", msg.getAuthor());
                    json.put("to", chat.getRecipient(msg));
                    json.put("timestamp", msg.getDate().getTime());
                    json.put("text", msg.getText());
                    json.put("chat_id", chat.getId());
                }
                catch (JSONException e)
                {
                    logger.error(e.getMessage(), e);
                }

                return json;
            })
            .collect(Collectors.toList());
    }

    /**
     * Method logs intro
     */
    private static void intro()
    {
        logger.info(Messages.get("app.start.logo"));
    }

    /**
     * Broadcast {@link User} info from the server.
     * 
     * @param type
     * @param sender
     * @param message
     */
    public static void broadcastUserInfo(String type, String sender, String message)
    {
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(
            session -> {
                try
                {
                    session.getRemote().sendString(
                        String.valueOf(new JSONObject()
                            .put("type", type)
                            .put("message", createUserMessage(type, sender, message))
                            .put("userlist", createContacts(session))
                            ));
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            });
    }

    /**
     * Create contacts.
     * 
     * @param sender
     * @param message
     * @return
     * @throws JSONException
     */
    private static Collection<JSONObject> createContacts(Session session)
    {
        return new LinkedHashSet<String>(userUsernameMap.values()).stream().filter(
            name -> !userUsernameMap.get(session).equals(name)).map(
            name -> createContact(name)).collect(
            Collectors.toSet());
    }

    /**
     * Create contacts.
     * 
     * @param sender
     * @param message
     * @return
     * @throws JSONException
     */
    private static JSONObject createContact(String name)
    {
        JSONObject contact = new JSONObject();
        try
        {
            contact.put("contact", name);
        }
        catch (JSONException e)
        {
            logger.error(e.getMessage(), e);
        }
        return contact;
    }

    /**
     * Broadcast a message to the members of a given chat.
     * 
     * @param message
     */
    public static void broadcastMessage(String message)
    {
        ChatService chatService = getService();

        try
        {
            JSONObject j = new JSONObject(message);

            Optional<JsonMessage> msg = JsonParser.fromJson(j.getString("message"), JsonMessage.class);

            if (msg.isPresent())
            {
                Optional<Chat> chat = chatService.findByChatId(msg.get().chat_id);

                if (chat.isPresent())
                {
                    chat.get().message(Message.create(msg.get().from, msg.get().text));
                    userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(
                        session -> {
                            try
                            {
                                if (userUsernameMap.get(session).equals(msg.get().from) ||
                                    userUsernameMap.get(session).equals(msg.get().to))
                                {
                                    session.getRemote().sendString(
                                        String.valueOf(createJsonMessageFromSender(message)));
                                }
                            }
                            catch (Exception e)
                            {
                                logger.error(e.getMessage(), e);
                            }
                        });
                }
            }

        }
        catch (JSONException e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Create message about the user.
     * 
     * @param sender
     * @param message
     * @return
     * @throws JSONException
     */
    private static JSONObject createUserMessage(String type, String sender, String message) throws JSONException
    {
        return new JSONObject()
            .put("type", type)
            .put("from", sender)
            .put("text", message)
            .put("timestamp", System.currentTimeMillis());
    }

    /**
     * Create a JSON message with an added timestamp.
     * 
     * @param message
     * @return
     * @throws JSONException
     */
    private static JSONObject createJsonMessageFromSender(String message) throws JSONException
    {
        // Parse message
        JSONObject messageObject = new JSONObject(message);

        // Get message part and add timestamp
        JSONObject content = messageObject.getJSONObject("message");
        content.put("timestamp", System.currentTimeMillis());

        // Put message back on response json
        messageObject.put("message", content);

        return messageObject;
    }
}
