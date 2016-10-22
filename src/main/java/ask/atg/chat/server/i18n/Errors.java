/**
 * 
 */
package ask.atg.chat.server.i18n;

/**
 * Utility class for that supplies internationalized exceptions.
 * 
 * @author Anders Skoglund
 *
 */
public final class Errors
{
    /**
     * Instantiate an internationalized {@link IllegalArgumentException}.
     * 
     * @param msgKey
     * @param args
     * @return
     */
    public static IllegalArgumentException newIllegalArgumentException(String msgKey, Object... args)
    {
        return new IllegalArgumentException(Messages.get(msgKey, args));
    }

    private Errors()
    {
    }
}
