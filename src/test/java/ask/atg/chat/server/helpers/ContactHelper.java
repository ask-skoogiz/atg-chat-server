/**
 * 
 */
package ask.atg.chat.server.helpers;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import ask.atg.chat.server.logic.ContactDao;
import ask.atg.chat.server.model.Contact;
import ask.atg.chat.server.model.Contact.Type;
import ask.atg.chat.server.model.User;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * Helper utility to use when working with {@link Contact} instances.
 * 
 * @author Anders Skoglund
 *
 */
@Singleton
public class ContactHelper extends DaoHelper<Contact, ContactDao>
{
    private ContactDao dao;

    @Inject
    public ContactHelper(ContactDao contactDao)
    {
        this.dao = contactDao;
    }

    /**
     * Create a {@link Contact}.
     * 
     * @param user
     * @param anotherUser
     * @return a new instance of a {@link Contact}
     */
    public Contact create(User user, User anotherUser)
    {
        return Contact.create(user, anotherUser);
    }

    /**
     * Create a {@link Contact} of given type.
     * 
     * @param user
     * @param anotherUser
     * @param type
     * @return a new instance of a {@link Contact} of given typetype
     */
    public Contact create(User user, User anotherUser, Type type)
    {
        return Contact.create(user, anotherUser).replace(type);
    }

    /**
     * Prepare a {@link Contact} of given type between the given users.
     * 
     * @param user
     * @param anotherUser
     * @return
     * @throws Exception
     */
    public Contact prepare(User user, User anotherUser) throws Exception
    {
        return prepare(user, anotherUser, Type.REQUEST);
    }

    /**
     * Prepare a {@link Contact} of given type between the given users.
     * 
     * @param user
     * @param anotherUser
     * @return
     * @throws Exception
     */
    public Contact prepare(User user, User anotherUser, Type type) throws Exception
    {
        Contact contact = create(user, anotherUser);
        dao.save(type == Type.REQUEST ? contact : (contact = contact.replace(type)));
        return contact;
    }

    /**
     * Prepare a {@link Contact} between the given user and a other users this user want to have connections to.
     * 
     * @param user
     * @param type
     * @param otherUsers
     * @return
     * @throws Exception
     */
    public Collection<Contact> prepareContacts(User user, Type type, User... otherUsers) throws Exception
    {
        List<Contact> contacts = new ArrayList<Contact>();
        for (User anotherUser : otherUsers)
        {
            contacts.add(prepare(user, anotherUser, type));
        }
        return contacts;
    }

    /**
     * Fetch existing {@link Contact}.
     * 
     * @param user
     * @param anotherUser
     * @return
     */
    public Contact fetchContact(User user, User anotherUser)
    {
        Optional<Contact> result = findContact(user, anotherUser);
        assertTrue("Contact exists", result.isPresent());
        return result.get();
    }

    /**
     * Search {@link Contact} between given users.
     * 
     * @param user
     * @param anotherUser
     * @return
     */
    public Optional<Contact> findContact(User user, User anotherUser)
    {
        return dao.findMutual(user, anotherUser);
    }

    @Override
    public ContactDao getDao()
    {
        return dao;
    }
}
