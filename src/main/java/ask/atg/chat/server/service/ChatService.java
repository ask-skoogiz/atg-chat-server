/**
 * 
 */
package ask.atg.chat.server.service;

import java.util.Collection;
import java.util.Optional;

import ask.atg.chat.server.model.Chat;
import ask.atg.chat.server.model.Contact;
import ask.atg.chat.server.model.Message;
import ask.atg.chat.server.model.User;

/**
 * 
 * Service working as a manager for chat functionality.
 * 
 * @author Anders Skoglund
 *
 */
public interface ChatService
{
    /**
     * Add message to {@link Chat} and create new {@link Chat} if non exist between the users.
     * 
     * @param message
     * @param toUser
     * @return
     */
    Chat addMessage(Message message, User toUser);

    /**
     * Save the given {@link User}.
     * 
     * @param user
     * @throws Exception
     */
    void save(User user) throws Exception;

    /**
     * Save the given {@link Contact}.
     * 
     * @param user
     * @throws Exception
     */
    void save(Contact contact);

    /**
     * Save the given {@link Chat}.
     * 
     * @param user
     * @throws Exception
     */
    void save(Chat chat);

    /**
     * Login with a given user and retrieve user info.
     * 
     * @param username
     * @param password
     * @return
     */
    Optional<User> login(String username, String password);

    /**
     * Check if {@link User} with given username exists.
     * 
     * @param username
     * @return
     */
    boolean exists(String username);

    /**
     * Find {@link Contact} between given users.
     * 
     * @param fromUser
     * @param toUser
     * @return
     */
    Optional<Contact> findMutual(User fromUser, User toUser);

    /**
     * Check if {@link Chat} involving {@link Contact} exists.
     * 
     * @param contact
     * @return
     */
    boolean exists(Contact contact);

    /**
     * Delete the given {@link Contact}
     * 
     * @param contact
     * @throws Exception
     */
    void delete(Contact contact) throws Exception;

    /**
     * List all {@link Contact}s of given {@link User}.
     * 
     * @param user
     * @return
     */
    Collection<Contact> listContacts(User user);

    /**
     * List all {@link Contact}s requests to given {@link User}
     * 
     * @param anotherUser
     * @return
     */
    Collection<Contact> findContactRequests(User user);

    /**
     * Check if {@link Contact} exists between users.
     * 
     * @param user
     * @param anotherUser
     * @return
     */
    boolean exists(User user, User anotherUser);

    /**
     * Find {@link User} by given username.
     * 
     * @param username
     * @return
     */
    Optional<User> findByName(String username);

    /**
     * Find {@link Chat} by given {@link Contact}.
     * 
     * @param contact
     * @return
     */
    Optional<Chat> findByContact(Contact contact);

    /**
     * Find {@link Chat} by given id.
     * 
     * @param contact
     * @return
     */
    Optional<Chat> findByChatId(String id);
}
