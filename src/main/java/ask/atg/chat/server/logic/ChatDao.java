/**
 * 
 */
package ask.atg.chat.server.logic;

import java.util.Optional;

import ask.atg.chat.server.model.Chat;
import ask.atg.chat.server.model.Contact;

/**
 * Data Access Object for managing chats.
 * 
 * @author Anders Skoglund
 *
 */
public interface ChatDao extends Dao<Chat>
{
    boolean exists(Contact contact);

    Optional<Chat> findBy(Contact contact);
}
