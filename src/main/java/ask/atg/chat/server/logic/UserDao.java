/**
 * 
 */
package ask.atg.chat.server.logic;

import java.util.Optional;

import ask.atg.chat.server.model.User;

/**
 * Data Access Object for managing users.
 * 
 * @author Anders Skoglund
 *
 */
public interface UserDao extends Dao<User>
{

    /**
     * Check if user with the given username exists.
     * 
     * @param username
     * @return
     */
    boolean exists(String username);

    /**
     * Find {@link User} by it's username.
     * 
     * @param username
     * @return the named {@link User}
     */
    Optional<User> findByName(String username);

    /**
     * Check that user exist and return the {@link User}.
     * 
     * @param username
     * @param password
     * @return the authorized {@link User}
     */
    Optional<User> authenticate(String username, String password);
}
