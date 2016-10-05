/**
 * 
 */
package ask.atg.chat.server.helpers;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import ask.atg.chat.server.logic.UserDao;
import ask.atg.chat.server.model.User;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * Helper utility to use when working with {@link User} instances.
 * 
 * @author Anders Skoglund
 *
 */
@Singleton
public class UserHelper extends DaoHelper<User, UserDao>
{
    /*
     * Test data constants
     */

    // Default test user
    public static final String DEFAULT_USERNAME = "Anakin";
    public static final String DEFAULT_PASSWORD = "pa$$word";

    // Another test user when more then 1 test user is needed
    public static final String ANOTHER_USERNAME = "Qui-Gon Jinn";
    public static final String ANOTHER_PASSWORD = "dorw$$ap";

    // Yet another user name to have access to when needed
    public static final String YET_ANOTHER_USERNAME = "Obi-Wan Kenobi";

    private UserDao dao;

    @Inject
    public UserHelper(UserDao userDao)
    {
        this.dao = userDao;
    }

    public User createDefault()
    {
        return create(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    public User create(String username, String password)
    {
        return User.create(username, password);
    }

    public User prepareDefault() throws Exception
    {
        return prepare(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    public User prepare(String username, String password) throws Exception
    {
        User user = create(username, password);
        dao.save(user);
        return user;
    }

    public User fetchDefaultUser()
    {
        return fetchUser(DEFAULT_USERNAME);
    }

    public User fetchUser(String username)
    {
        Optional<User> result = findUser(username);
        assertTrue("User exists", result.isPresent());
        return result.get();
    }

    public Optional<User> findUser(String username)
    {
        return dao.findByName(username);
    }

    @Override
    public UserDao getDao()
    {
        return dao;
    }

}
