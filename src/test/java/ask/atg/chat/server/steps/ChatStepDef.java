/**
 * 
 */
package ask.atg.chat.server.steps;

import static ask.atg.chat.server.helpers.MessageHelper.ANOTHER_USER_PHRASE_1;
import static ask.atg.chat.server.helpers.MessageHelper.ANOTHER_USER_PHRASE_2;
import static ask.atg.chat.server.helpers.MessageHelper.ANOTHER_USER_PHRASE_3;
import static ask.atg.chat.server.helpers.MessageHelper.DEFAULT_MESSAGE;
import static ask.atg.chat.server.helpers.MessageHelper.DEFAULT_USER_PHRASE_1;
import static ask.atg.chat.server.helpers.MessageHelper.DEFAULT_USER_PHRASE_2;
import static ask.atg.chat.server.helpers.MessageHelper.DEFAULT_USER_PHRASE_3;
import static ask.atg.chat.server.helpers.UserHelper.ANOTHER_USERNAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ask.atg.chat.server.model.Chat;
import ask.atg.chat.server.model.Contact;
import ask.atg.chat.server.model.Message;
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
public class ChatStepDef extends AbstractStepDef
{
    private ChatService service;

    private Collection<User> onlineUsers;

    private User user;

    private User anotherUser;

    private Contact contact;

    private Message message;

    private Chat chat;

    private List<Message> messages;

    @Inject
    public ChatStepDef(ChatService service)
    {
        this.service = service;
    }

    @After
    public void after()
    {
        super.after();
    }

    @Given("^contact is online$")
    public void contact_is_online() throws Throwable
    {
        assertThat(
            "Users contact exists",
            (anotherUser = getUserHelper().fetchUser(ANOTHER_USERNAME)),
            is(notNullValue()));
        onlineUsers = Arrays.asList(anotherUser);
    }

    @Given("^has a message$")
    public void has_a_message() throws Throwable
    {
        assertThat("User exists", (user = getUserHelper().fetchDefaultUser()), is(notNullValue()));
        message = getMessageHelper().create(user, DEFAULT_MESSAGE);
    }

    @Given("^chat history don't exist$")
    public void chat_history_don_t_exist() throws Throwable
    {
        assertThat("User exists", (user = getUserHelper().fetchDefaultUser()), is(notNullValue()));
        assertThat(
            "Users contact exists",
            (anotherUser = getUserHelper().fetchUser(ANOTHER_USERNAME)),
            is(notNullValue()));
        assertThat(
            "Users are contacts",
            (contact = getContactHelper().fetchContact(user, anotherUser)),
            is(notNullValue()));
        assertFalse("No previous chat exists", service.exists(contact));
    }

    @Given("^chat history exist$")
    public void chat_history_exist() throws Throwable
    {
        assertThat("User exists", (user = getUserHelper().fetchDefaultUser()), is(notNullValue()));
        assertThat(
            "Users contact exists",
            (anotherUser = getUserHelper().fetchUser(ANOTHER_USERNAME)),
            is(notNullValue()));
        assertThat(
            "Users are contacts",
            (contact = getContactHelper().fetchContact(user, anotherUser)),
            is(notNullValue()));

        chat = getChatHelper().create(contact);
        chat.message(msg(user, DEFAULT_USER_PHRASE_1))
            .message(msg(anotherUser, ANOTHER_USER_PHRASE_1))
            .message(msg(user, DEFAULT_USER_PHRASE_2))
            .message(msg(anotherUser, ANOTHER_USER_PHRASE_2))
            .message(msg(user, DEFAULT_USER_PHRASE_3))
            .message(msg(anotherUser, ANOTHER_USER_PHRASE_3));
        service.save(chat);

        assertTrue("A chat exists", service.exists(contact));
    }

    @When("^send message$")
    public void send_message() throws Exception
    {
        if (onlineUsers.contains(anotherUser))
        {
            if (chat == null)
            {
                chat = service.addMessage(message, anotherUser);
            }
            else
            {
                chat.message(message);
            }
        }
    }

    @When("^list chat history chronologically$")
    public void list_chat_history_chronologically() throws Throwable
    {
        messages = chat.getMessagesChronological();
    }

    @Then("^chat exists$")
    public void chat_exists()
    {
        assertThat("Chat exists", chat, is(notNullValue()));
    }

    @Then("^has new message$")
    public void has_new_message() throws Throwable
    {
        assertTrue("Chat has the new message", chat.getMessages().contains(message));
    }

    @Then("^retrieve chat history$")
    public void retrieve_chat_history() throws Throwable
    {
        assertThat("Messages has messages", messages.size(), is(6));
        Message msg;
        msg = messages.get(0);
        assertThat("Fist message is from", msg.getAuthor(), is(user.getUsername()));
        assertThat("Fist message is", msg.getText(), is(DEFAULT_USER_PHRASE_1));
        msg = messages.get(5);
        assertThat("Fist message is from", msg.getAuthor(), is(anotherUser.getUsername()));
        assertThat("Fist message is", msg.getText(), is(ANOTHER_USER_PHRASE_3));
    }

    // Utility methods

    private Message msg(User user, String message)
    {
        return getMessageHelper().create(user, message);
    }

}
