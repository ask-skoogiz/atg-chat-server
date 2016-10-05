/**
 * 
 */
package ask.atg.chat.server.logic;

import java.util.Collection;
import java.util.Optional;

import ask.atg.chat.server.model.Contact;
import ask.atg.chat.server.model.User;

/**
 * Data Access Object for managing contacts.
 * 
 * @author Anders Skoglund
 *
 */
public interface ContactDao extends Dao<Contact>
{

    /**
     * Check if contact exists.
     * 
     * @param username
     * @return
     */
    boolean exists(Contact contact);

    /**
     * Check if contact exists between users.
     * 
     * @param user
     * @param anotherUser
     * @return
     */
    boolean exists(User user, User anotherUser);

    /**
     * Find contact requests sent to {@link User}.
     * 
     * @param username
     * @return the named {@link User}
     */
    Collection<Contact> findContactRequestsToUser(User user);

    /**
     * Find mutual contact based on the two users.
     * 
     * @param user
     * @param anotherUser
     * @return
     */
    Optional<Contact> findMutual(User user, User anotherUser);

    /**
     * Find mutual contacts of user.
     * 
     * @param user
     * @param anotherUser
     * @return
     */
    Collection<Contact> findMutualContacts(User user);

}
