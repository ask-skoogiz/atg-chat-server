/**
 * 
 */
package ask.atg.chat.server.model;

import java.util.Date;

/**
 * Model interface for a message from an {@link User}.
 * 
 * @author Anders Skoglund
 *
 */
public interface Message
{
    /**
     * Get a message author.
     * 
     * @return an username
     */
    public abstract String getAuthor();

    /**
     * Get the content of the message.
     * 
     * @return the massage
     */
    public abstract String getText();

    /**
     * Get the date of the message.
     * 
     * @return create date
     */
    public abstract Date getDate();

    /**
     * Factory method to construct a {@link Message}.
     * 
     * @param author
     * @param msg
     * @return the new {@link Message}
     */
    public static Message create(final User author, final String msg)
    {
        return new Message()
        {
            @Override
            public String getText()
            {
                return msg;
            }

            @Override
            public Date getDate()
            {
                return new Date();
            }

            @Override
            public String getAuthor()
            {
                return author.getUsername();
            }
        };
    }

}
