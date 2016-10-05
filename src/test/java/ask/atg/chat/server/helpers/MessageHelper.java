package ask.atg.chat.server.helpers;

import ask.atg.chat.server.model.Message;
import ask.atg.chat.server.model.User;

/**
 * 
 * Helper utility to use when working with {@link Message} instances.
 * 
 * @author Anders Skoglund
 *
 */
public class MessageHelper implements Helper<Message>
{
    /*
     * Test data constants
     */

    public static final String DEFAULT_MESSAGE = "May the force be with you";

    public static final String DEFAULT_USER_PHRASE_1 = "You're a Jedi Knight, aren't you?";
    public static final String DEFAULT_USER_PHRASE_2 =
            "I saw your laser sword. Only Jedi carry that kind of weapon.";
    public static final String DEFAULT_USER_PHRASE_3 = "I don't think so. No one can kill a Jedi.";

    public static final String ANOTHER_USER_PHRASE_1 = "What makes you think that?";
    public static final String ANOTHER_USER_PHRASE_2 = "Perhaps I killed a Jedi and took it from him.";
    public static final String ANOTHER_USER_PHRASE_3 = "I wish that were so.";

    /**
     * Create {@link Message}
     * 
     * @param user
     * @param message
     * @return
     */
    public Message create(User user, String message)
    {
        return Message.create(user, message);
    }

}
