/**
 * 
 */
package ask.atg.chat.server.service;

import static ask.atg.chat.server.i18n.Errors.newIllegalArgumentException;

import java.util.Collection;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ask.atg.chat.server.logic.ChatDao;
import ask.atg.chat.server.logic.ContactDao;
import ask.atg.chat.server.logic.UserDao;
import ask.atg.chat.server.model.Chat;
import ask.atg.chat.server.model.Contact;
import ask.atg.chat.server.model.Message;
import ask.atg.chat.server.model.User;

/**
 * Service facade for managing chat functionality.
 * 
 * @author Anders Skoglund
 *
 */
@Singleton
public class ChatServiceImpl implements ChatService
{
    private UserDao userDao;

    private ContactDao contactDao;

    private ChatDao chatDao;

    @Inject
    public ChatServiceImpl(UserDao userDao, ContactDao contactDao, ChatDao chatDao)
    {
        this.userDao = userDao;
        this.contactDao = contactDao;
        this.chatDao = chatDao;
    }

    /*
     * Chat methods
     */

    @Override
    public void save(Chat chat) throws Exception
    {
        chatDao.save(chat);
    }

    @Override
    public Chat addMessage(final Message message, final User toUser) throws IllegalArgumentException
    {
        User author =
                userDao.findByName(message.getAuthor()).orElseThrow(
                    () -> newIllegalArgumentException("error.user.notexisting", message.getAuthor()));

        Contact contact =
                contactDao.findMutual(author, toUser).orElseThrow(
                    () -> newIllegalArgumentException(
                        "error.contact.notexisting",
                        message.getAuthor(),
                        toUser.getUsername()));

        return chatDao.findBy(contact).orElseGet(() -> Chat.create(contact).message(message));
    }

    @Override
    public Optional<Chat> findByChatId(String id)
    {
        return chatDao.findById(id);
    }

    @Override
    public Optional<Chat> findByContact(Contact contact)
    {
        return chatDao.findBy(contact);
    }

    @Override
    public boolean exists(Contact contact)
    {
        return chatDao.exists(contact);
    }

    /*
     * Contact methods
     */

    @Override
    public void save(Contact contact) throws Exception
    {
        contactDao.save(contact);
    }

    @Override
    public void delete(Contact contact) throws Exception
    {
        contactDao.delete(contact);
    }

    @Override
    public boolean exists(User user, User anotherUser)
    {
        return contactDao.exists(user, anotherUser);
    }

    @Override
    public Collection<Contact> listContacts(User user)
    {
        return contactDao.findMutualContacts(user);
    }

    @Override
    public Collection<Contact> findContactRequests(User user)
    {
        return contactDao.findContactRequestsToUser(user);
    }

    @Override
    public Optional<Contact> findMutual(User fromUser, User toUser)
    {
        return contactDao.findMutual(fromUser, toUser);
    }

    /*
     * User methods
     */

    @Override
    public void save(User user) throws Exception
    {
        userDao.save(user);
    }

    @Override
    public boolean exists(String username)
    {
        return userDao.exists(username);
    }

    @Override
    public Optional<User> login(String username, String password)
    {
        return userDao.authenticate(username, password);
    }

    @Override
    public Optional<User> findByName(String username)
    {
        return userDao.findByName(username);
    }

}
