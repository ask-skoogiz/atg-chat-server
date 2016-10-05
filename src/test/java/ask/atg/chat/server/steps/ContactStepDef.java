/**
 * 
 */
package ask.atg.chat.server.steps;

import static ask.atg.chat.server.helpers.UserHelper.ANOTHER_USERNAME;
import static ask.atg.chat.server.helpers.UserHelper.DEFAULT_PASSWORD;
import static ask.atg.chat.server.helpers.UserHelper.YET_ANOTHER_USERNAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import ask.atg.chat.server.model.Contact;
import ask.atg.chat.server.model.Contact.Type;
import ask.atg.chat.server.model.User;
import ask.atg.chat.server.service.ChatService;

import com.google.inject.Inject;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Contact step definition.
 * 
 * @author Anders Skoglund
 *
 */
@ScenarioScoped
public class ContactStepDef extends AbstractStepDef
{
    private ChatService service;

    private User user;

    private User anotherUser;

    private Collection<User> users;

    private Collection<User> onlineUsers;

    private Contact contact;

    private Collection<Contact> contacts;

    private Optional<Contact> resultContact;

    private Exception expectedException = null;

    @Inject
    public ContactStepDef(ChatService service)
    {
        this.service = service;
    }

    @After
    public void after()
    {
        super.after();
    }

    @Given("^a contact request exists$")
    public void a_contact_request_exists() throws Exception
    {
        user = getUserHelper().prepareDefault();

        anotherUser = getUserHelper().prepare(ANOTHER_USERNAME, DEFAULT_PASSWORD);

        contact = getContactHelper().prepare(user, anotherUser);

        assertThat("Contact is available", contact, is(notNullValue()));
        assertThat("Contact type is 'request'", contact.getType(), is(Contact.Type.REQUEST));
        assertTrue("Contact between named users", contact.isBetween(user, anotherUser));
    }

    @Given("^has a contact$")
    public void has_a_contact() throws Throwable
    {
        // Get existing user
        user = getUserHelper().fetchDefaultUser();

        // Prepare another user
        anotherUser = getUserHelper().prepare(ANOTHER_USERNAME, DEFAULT_PASSWORD);

        // Create contact
        getContactHelper().prepareContacts(user, Type.MUTUAL, anotherUser);
    }

    @Given("^has contacts$")
    public void has_contacts() throws Throwable
    {
        // Get existing user
        user = getUserHelper().fetchDefaultUser();

        User[] prepContacts =
            { getUserHelper().prepare(ANOTHER_USERNAME, DEFAULT_PASSWORD),
                getUserHelper().prepare(YET_ANOTHER_USERNAME, DEFAULT_PASSWORD) };

        users = Arrays.asList(prepContacts);

        // Create contacts
        getContactHelper().prepareContacts(user, Type.MUTUAL, prepContacts);
    }

    @Given("^a contact is online$")
    public void a_contact_is_online() throws Throwable
    {
        onlineUsers = Arrays.asList(getUserHelper().fetchUser(ANOTHER_USERNAME));
    }

    @When("^user adds another user as contact$")
    public void user_adds_another_user_as_contact()
    {
        // Get existing user
        user = getUserHelper().fetchDefaultUser();

        // Get the other existing user
        anotherUser = getUserHelper().fetchUser(ANOTHER_USERNAME);

        contact = getContactHelper().create(user, anotherUser);
        try
        {
            service.save(contact);
        }
        catch (Exception e)
        {
            expectedException = e;
        }
    }

    @When("^user accepts contact request$")
    public void user_accepts_contact_request() throws Throwable
    {
        service.save(contact.replace(Type.MUTUAL));
    }

    @When("^user rejects contact request$")
    public void user_rejects_contact_request() throws Throwable
    {
        service.delete(contact);
    }

    @When("^list contacts$")
    public void list_contacts() throws Throwable
    {
        contacts = service.listContacts(user);
    }

    @When("^filter online contacts$")
    public void filter_online_contacts() throws Throwable
    {
        contacts =
                contacts.stream().filter(cont -> onlineUsers.contains(cont.toUser())).collect(Collectors.toList());
    }

    @Then("^a contact request is available$")
    public void a_contact_request_is_available() throws Throwable
    {
        assertThat("No exception was thrown", expectedException, is(nullValue()));
        assertThat("Contact is available", contact, is(notNullValue()));
        assertThat("Contact type is 'request'", contact.getType(), is(Contact.Type.REQUEST));
        assertTrue("Contact between named users", contact.isBetween(user, anotherUser));
    }

    @Then("^contacted user is notified$")
    public void contacted_user_is_notified() throws Throwable
    {
        Collection<Contact> notifications = service.findContactRequests(anotherUser);
        assertThat("Notification exists", notifications.size(), is(1));

        contact = notifications.iterator().next();
        assertThat("Notification is from", contact.fromUser(), is(user));
    }

    @Then("^the contact is mutual$")
    public void the_contact_is_mutual() throws Throwable
    {
        resultContact = service.findMutual(user, anotherUser);
        assertTrue("Mutual contact exists", resultContact.isPresent());

        contact = resultContact.get();
        assertThat("Contact type is 'mutual'", contact.getType(), is(Contact.Type.MUTUAL));
        assertTrue("Contact between named users", contact.isBetween(user, anotherUser));

    }

    @Then("^the request is removed$")
    public void the_request_is_removed() throws Throwable
    {
        assertFalse("Contact no longer exists", service.exists(user, anotherUser));
    }

    @Then("^return all contacts$")
    public void return_all_contacts() throws Throwable
    {
        assertThat("User has two contacts", contacts.size(), is(2));
        contacts.forEach((contact) -> assertTrue("Contacts contain user", users.contains(contact.toUser())));
    }

    @Then("^return all contacts that are online$")
    public void return_all_contacts_that_are_online() throws Throwable
    {
        assertThat("User has one contacts online", contacts.size(), is(1));

        assertThat(
            "Online user is correct",
            contacts.iterator().next().toUser().getUsername(),
            is(ANOTHER_USERNAME));
    }
}
