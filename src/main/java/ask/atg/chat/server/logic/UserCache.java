/**
 * 
 */
package ask.atg.chat.server.logic;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import ask.atg.chat.server.i18n.Messages;
import ask.atg.chat.server.model.User;

import com.google.inject.Singleton;

/**
 * 
 * 
 * @author Anders Skoglund
 *
 */
@Singleton
public class UserCache implements UserDao, Clearable
{
    private Set<User> userCache = new HashSet<User>();

    @Override
    public void save(User user)
    {
        if (!exists(user.getUsername()))
            userCache.add(user);
        else
            throw new IllegalArgumentException(Messages.get("error.user.exists", user.getUsername()));
    }

    @Override
    public void delete(User user) throws Exception
    {
        userCache.remove(user);
    }

    @Override
    public boolean exists(String username)
    {
        return userCache.stream()
            .filter(cachedUser -> cachedUser.getUsername().equals(username))
            .findFirst().isPresent();
    }

    @Override
    public Optional<User> findByName(String username)
    {
        return userCache.stream().filter(
            (user) -> username.equals(user.getUsername())).findAny();
    }

    @Override
    public Optional<User> authenticate(String username, String password)
    {
        return userCache.stream().filter(
            (user) -> username.equals(user.getUsername()) && user.passwordMatches(password)).findAny();
    }

    @Override
    public void clear()
    {
        userCache.clear();
    }

}
