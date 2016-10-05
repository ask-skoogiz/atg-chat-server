/**
 * 
 */
package ask.atg.chat.server.steps;

import static ask.atg.chat.server.helpers.UserHelper.ANOTHER_PASSWORD;
import static ask.atg.chat.server.helpers.UserHelper.ANOTHER_USERNAME;
import static ask.atg.chat.server.helpers.UserHelper.DEFAULT_PASSWORD;
import static ask.atg.chat.server.helpers.UserHelper.DEFAULT_USERNAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import ask.atg.chat.server.model.User;
import ask.atg.chat.server.service.ChatService;

import com.google.inject.Inject;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * User test steps definition.
 * 
 * @author Anders Skoglund
 *
 */
@ScenarioScoped
public class UserStepDef extends AbstractStepDef
{
    private ChatService service;

    private User user;

    private Optional<User> resultUser;

    private String username;
    private String password;

    private Exception expectedException = null;

    @Inject
    public UserStepDef(ChatService service)
    {
        this.service = service;
    }

    @After
    public void after()
    {
        super.after();
    }

    @Given("^I have a new user$")
    public void i_have_a_new_user()
    {
        user = getUserHelper().createDefault();
        username = DEFAULT_USERNAME;
    }

    @When("^I create new user$")
    public void i_create_new_user()
    {
        try
        {
            service.save(user);
        }
        catch (Exception e)
        {
            expectedException = e;
        }
    }

    @Then("^a user is created$")
    public void a_user_is_created()
    {
        assertThat("No exception was thrown", expectedException, is(nullValue()));
        assertTrue("User exists", service.exists(username));
    }

    @Given("^a user exists$")
    public void a_user_exists() throws Exception
    {
        getUserHelper().prepareDefault();
    }

    @Given("^another user exists$")
    public void another_user_exists() throws Throwable
    {
        getUserHelper().prepare(ANOTHER_USERNAME, DEFAULT_PASSWORD);
    }

    @Then("^a user is not created$")
    public void a_user_is_not_created()
    {
        assertThat("Exception was thrown", expectedException, is(IllegalArgumentException.class));
    }

    @Given("^I enter valid user credentials$")
    public void i_enter_valid_user_credentials()
    {
        username = DEFAULT_USERNAME;
        password = DEFAULT_PASSWORD;
    }

    @Given("^I enter invalid user credentials$")
    public void i_enter_invalid_user_credentials() throws Throwable
    {
        username = DEFAULT_USERNAME;
        password = ANOTHER_PASSWORD;
    }

    @When("^I login$")
    public void i_login()
    {
        resultUser = service.login(username, password);
    }

    @Then("^the user is authenticated$")
    public void the_user_is_authenticated() throws Throwable
    {
        assertThat("User retrieved", resultUser.isPresent(), is(true));
        user = resultUser.get();
    }

    @Then("^user data is retrieved$")
    public void user_data_is_retrieved() throws Throwable
    {
        assertThat("User available", user, is(notNullValue()));
        assertThat("Username matches", user.getUsername(), is(username));
        assertTrue("Password matches", user.passwordMatches(password));
    }

    @Then("^user authentication fails$")
    public void user_authentication_fails() throws Throwable
    {
        assertThat("User result is empty", resultUser.isPresent(), is(false));
    }

    @Then("^user data is not retrieved$")
    public void user_data_is_not_retrieved() throws Throwable
    {
        assertThat("No user available", user, is(nullValue()));
    }
}
