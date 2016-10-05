/**
 * 
 */
package ask.atg.chat.server.service;

import java.util.Collection;
import java.util.Optional;

import ask.atg.chat.server.logic.ChatDao;
import ask.atg.chat.server.logic.ContactDao;
import ask.atg.chat.server.logic.UserDao;
import ask.atg.chat.server.model.Chat;
import ask.atg.chat.server.model.Contact;
import ask.atg.chat.server.model.Message;
import ask.atg.chat.server.model.User;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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
    public Chat addMessage(Message message, User toUser) throws IllegalArgumentException
    {
        Optional<User> author = userDao.findByName(message.getAuthor());
        if (author.isPresent())
        {
            Optional<Contact> contact = contactDao.findMutual(author.get(), toUser);
            if (contact.isPresent())
            {
                Optional<Chat> chat = chatDao.findBy(contact.get());
                return addMessage(chat.isPresent() ? chat.get() : Chat.create(contact.get()), message);
            }
            else
            {
                throw new IllegalArgumentException("Users '" +
                    message.getAuthor() +
                    "' and '" +
                    toUser.getUsername() +
                    "' are not contacts.");
            }
        }
        else
        {
            throw new IllegalArgumentException("No user named '" + message.getAuthor() + "' exists.");
        }
    }

    @Override
    public Chat addMessage(Chat chat, Message message)
    {
        chat.addMessage(message);
        return chat;
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
