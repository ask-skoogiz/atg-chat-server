/**
 * 
 */
package ask.atg.chat.server.json.model;

import ask.atg.chat.server.json.JsonBean;

/**
 * JSON model representing a user.
 * 
 * @author Anders Skoglund
 *
 */
public class JsonUser implements JsonBean
{

    public String id;

    public String username;

    public String password;

    public JsonUser()
    {
    }

    public JsonUser(String id, String username, String password)
    {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
    }

}
