/**
 * 
 */
package ask.atg.chat.server.model;

/**
 * Abstract model of a User of the chat server.
 * 
 * @author Anders Skoglund
 *
 */
public abstract class User
{
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

    public abstract boolean passwordMatches(String password);

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
            @Override
            public String getUsername()
            {
                return username;
            }

            @Override
            public boolean passwordMatches(String pwd)
            {
                return password.equals(pwd);
            }
        };
    }

}
