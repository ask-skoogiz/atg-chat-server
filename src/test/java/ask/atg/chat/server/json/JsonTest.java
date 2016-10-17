package ask.atg.chat.server.json;

import static ask.atg.chat.server.helpers.UserHelper.ANOTHER_USERNAME;
import static ask.atg.chat.server.helpers.UserHelper.DEFAULT_PASSWORD;
import static ask.atg.chat.server.helpers.UserHelper.DEFAULT_USERNAME;
import static ask.atg.chat.server.helpers.UserHelper.YET_ANOTHER_USERNAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ask.atg.chat.server.Server;
import ask.atg.chat.server.json.model.UserJson;
import ask.atg.chat.server.model.User;

public class JsonTest
{
    // Initialize logger
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    @Test
    public void test()
    {
        String json =
                String.format(
                    "{ \"username\" : \"%s\", \"password\" : \"%s\" }",
                    DEFAULT_USERNAME,
                    DEFAULT_PASSWORD);

        Optional<UserJson> jsonOptional = JsonParser.fromJson(json, UserJson.class);
        assertTrue(jsonOptional.isPresent());

        User user = User.fromJson(jsonOptional.get());
        assertThat(user.getId(), is(nullValue()));
        assertThat(user.getUsername(), is(DEFAULT_USERNAME));
        assertThat(user.getPassword(), is(DEFAULT_PASSWORD));

        User anotherUser = User.create(ANOTHER_USERNAME, DEFAULT_PASSWORD);

        Optional<String> resultJson = JsonParser.toJson(anotherUser.toJson());
        assertTrue(resultJson.isPresent());
        logger.info(resultJson.get());

        List<UserJson> users =
                Arrays.asList(user.toJson(), anotherUser.toJson(), User.create(
                    YET_ANOTHER_USERNAME,
                    DEFAULT_PASSWORD).toJson());

        resultJson = JsonParser.toJson(users);
        assertTrue(resultJson.isPresent());
        logger.info(resultJson.get());

    }
}
