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
public class MessageJson implements JsonBean
{

    public String from, to;

    public String text;

    public Long timestamp;

    public String chat_id;

    public MessageJson()
    {
    }

    public MessageJson(String from, String text, Long timestamp)
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
