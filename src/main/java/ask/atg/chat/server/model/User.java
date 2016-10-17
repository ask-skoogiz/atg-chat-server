/**
 * 
 */
package ask.atg.chat.server.model;

import java.util.UUID;

import ask.atg.chat.server.json.JsonMorphable;
import ask.atg.chat.server.json.model.UserJson;

/**
 * Abstract model of a User of the chat server.
 * 
 * @author Anders Skoglund
 *
 */
public abstract class User implements JsonMorphable
{
    /**
     * Get unique identifier for user.
     * 
     * @return
     */
    public abstract String getId();

    /**
     * Get the users name.
     * 
     * @return
     */
    public abstract String getUsername();

    /**
     * Get the users password.
     * 
     * @return
     */
    public abstract String getPassword();

    /**
     * Check if password matches the users password.
     * 
     * @param password
     * @return
     */
    public boolean passwordMatches(String password)
    {
        return getPassword().equals(password);
    }

    /**
     * Is user the same as this based on username.
     * 
     * @param user
     * @return
     */
    public boolean is(User user)
    {
        return getUsername().equals(user.getUsername());
    }

    /**
     * Factory method to construct an {@link User}.
     * 
     * @param username
     * @return
     */
    public static User create(final String username, final String password)
    {
        return new User()
        {
            private UUID uuid = UUID.randomUUID();

            @Override
            public String getId()
            {
                return uuid.toString();
            }

            @Override
            public String getUsername()
            {
                return username;
            }

            @Override
            public String getPassword()
            {
                return password;
            }
        };
    }

    /**
     * Factory method to construct an {@link User}.
     * 
     * @param username
     * @return
     */
    public static User create(final UserJson json)
    {
        return create(json.username, json.password);
    }

    /**
     * Create a {@link User} from a {@link UserJson} bean.
     * 
     * @param json
     * @return
     */
    public static User fromJson(final UserJson json)
    {
        return new User()
        {
            @Override
            public String getId()
            {
                return json.id;
            }

            @Override
            public String getUsername()
            {
                return json.username;
            }

            @Override
            public String getPassword()
            {
                return json.password;
            }

        };
    }

    @Override
    public UserJson toJson()
    {
        return new UserJson(getId(), getUsername(), getPassword());
    }

}
