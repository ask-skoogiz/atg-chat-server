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
public class JsonMessage implements JsonBean
{

    public String from, to;

    public String text;

    public Long timestamp;

    public String chat_id;

    public JsonMessage()
    {
    }

    public JsonMessage(String from, String text, Long timestamp)
    {
        this.from = from;
        this.text = text;
        this.timestamp = timestamp;
    }

    public void newTimestamp()
    {
        this.timestamp = System.currentTimeMillis();
    }

}
