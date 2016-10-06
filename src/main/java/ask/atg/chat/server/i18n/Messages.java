/**
 * 
 */
package ask.atg.chat.server.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Utility class for that loads internationalized messages.
 * 
 * @author Anders Skoglund
 *
 */
public final class Messages
{
    private static ResourceBundle bundle;

    public static String get(String key)
    {
        if (bundle == null)
        {
            bundle = ResourceBundle.getBundle("ask.atg.chat.server.i18n.messages");
        }
        return bundle.getString(key);
    }

    public static String get(String key, Object... args)
    {
        return MessageFormat.format(get(key), args);
    }
}
