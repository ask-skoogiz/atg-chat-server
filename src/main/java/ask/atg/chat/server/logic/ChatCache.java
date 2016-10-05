/**
 * 
 */
package ask.atg.chat.server.logic;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import ask.atg.chat.server.model.Chat;
import ask.atg.chat.server.model.Contact;

import com.google.inject.Singleton;

/**
 * 
 * 
 * @author Anders Skoglund
 *
 */
@Singleton
public class ChatCache implements ChatDao, Clearable
{
    private Set<Chat> cache = new HashSet<>();

    @Override
    public void save(Chat chat) throws Exception
    {
        Contact contact = chat.getContact();
        if (!exists(contact))
        {
            cache.add(chat);
        }
        else
        {
            throw new IllegalArgumentException("A chat between '" +
                contact.fromUser().getUsername() +
                "' and '" +
                contact.toUser().getUsername() +
                "' already exists.");
        }
    }

    @Override
    public void delete(Chat chat) throws Exception
    {
        if (exists(chat.getContact()))
        {
            cache.remove(chat);
        }
    }

    @Override
    public boolean exists(Contact contact)
    {
        return cache
            .stream()
            .filter((chat) -> chat.getContact().isBetween(contact.fromUser(), contact.toUser()))
            .findFirst()
            .isPresent();
    }

    @Override
    public Optional<Chat> findBy(Contact contact)
    {
        return cache
            .stream()
            .filter((chat) -> chat.getContact().isBetween(contact.fromUser(), contact.toUser()))
            .findAny();
    }

    @Override
    public void clear()
    {
        cache.clear();
    }

}
