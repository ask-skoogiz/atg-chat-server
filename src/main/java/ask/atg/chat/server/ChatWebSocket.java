package ask.atg.chat.server;

import java.util.List;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class ChatWebSocket
{
    private static Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);

    private static final String SERVER_INFO = "server_info";

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception
    {
        String username = findUser(session);
        Server.userUsernameMap.put(session, username);
        Server.broadcastUserInfo(SERVER_INFO, sender = "Server", msg = (username + " joined the chat"));
        logger.info(sender + ": " + msg);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason)
    {
        String username = Server.userUsernameMap.get(user);
        Server.userUsernameMap.remove(user);
        Server.broadcastUserInfo(SERVER_INFO, sender = "Server", msg = (username + " left the chat"));
        logger.info(sender + ": " + msg);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message)
    {
        try
        {
            JSONObject obj = new JSONObject(message);

            String type = obj.getString("type");

            switch (type)
            {
                case "send_message":
                    Server.broadcastMessage(msg = message);
                    break;
                default:
                    break;
            }
        }
        catch (JSONException e)
        {
            logger.error(e.getMessage(), e);
        }

    }

    public String findUser(Session session) throws Exception
    {
        List<String> users = session.getUpgradeRequest().getParameterMap().get("user");
        return users != null && !users.isEmpty() ? users.get(0) : "";
    }
}
