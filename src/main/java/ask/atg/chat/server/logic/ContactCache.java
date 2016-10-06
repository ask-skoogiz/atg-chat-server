/**
 * 
 */
package ask.atg.chat.server.logic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ask.atg.chat.server.i18n.Messages;
import ask.atg.chat.server.model.Contact;
import ask.atg.chat.server.model.Contact.Type;
import ask.atg.chat.server.model.User;

import com.google.inject.Singleton;

/**
 * 
 * 
 * @author Anders Skoglund
 *
 */
@Singleton
public class ContactCache implements ContactDao, Clearable
{
    private Set<Contact> cache = new HashSet<>();

    @Override
    public void save(Contact contact) throws Exception
    {
        if (!exists(contact))
        {
            cache.add(contact);
        }
        else if (Type.MUTUAL == contact.getType())
        {
            update(contact);
        }
        else
        {
            throw new IllegalArgumentException(Messages.get("error.contact.exists",
                contact.fromUser().getUsername(),
                contact.toUser().getUsername()));
        }
    }

    @Override
    public void delete(Contact contact)
    {
        if (cache.contains(contact))
            cache.remove(contact);
    }

    @Override
    public boolean exists(Contact contact)
    {
        return exists(contact.fromUser(), contact.toUser());
    }

    @Override
    public boolean exists(final User user, final User anotherUser)
    {
        return cache
            .stream()
            .filter((contact) -> contact.isBetween(user, anotherUser))
            .findFirst().isPresent();
    }

    public void update(Contact contact)
    {
        if (exists(contact))
        {
            delete(contact);
            cache.add(contact);
        }
    }

    @Override
    public Collection<Contact> findContactRequestsToUser(User user)
    {
        return cache
            .stream()
            .filter((contact) -> contact.getType() == Type.REQUEST && contact.contains(user))
            .collect(Collectors.toSet());
    }

    @Override
    public Optional<Contact> findMutual(User user, User anotherUser)
    {
        return cache
            .stream()
            .filter((contact) -> contact.getType() == Type.MUTUAL && contact.isBetween(user, anotherUser))
            .findFirst();
    }

    @Override
    public Collection<Contact> findMutualContacts(User user)
    {
        return cache
            .stream()
            .filter((contact) -> contact.getType() == Type.MUTUAL && contact.contains(user))
            .collect(Collectors.toSet());
    }

    @Override
    public void clear()
    {
        cache.clear();
    }

}
