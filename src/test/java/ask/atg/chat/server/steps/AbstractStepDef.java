/**
 * 
 */
package ask.atg.chat.server.steps;

import ask.atg.chat.server.helpers.ChatHelper;
import ask.atg.chat.server.helpers.ContactHelper;
import ask.atg.chat.server.helpers.Helper;
import ask.atg.chat.server.helpers.MessageHelper;
import ask.atg.chat.server.helpers.UserHelper;
import ask.atg.chat.server.logic.Clearable;

import com.google.inject.Inject;

/**
 * Abstract step definition. Containing constants and utility methods.
 * 
 * @author Anders Skoglund
 *
 */
public abstract class AbstractStepDef
{
    @Inject
    private UserHelper userHelper;

    @Inject
    private ContactHelper contactHelper;

    @Inject
    private ChatHelper chatHelper;

    @Inject
    private MessageHelper messageHelper;

    public void after()
    {
        clear(userHelper, contactHelper, chatHelper, messageHelper);
    }

    protected void clear(Helper<?>... helpers)
    {
        for (Helper<?> helper : helpers)
        {
            if (helper instanceof Clearable)
            {
                ((Clearable) helper).clear();
            }
        }
    }

    public UserHelper getUserHelper()
    {
        return userHelper;
    }

    public ContactHelper getContactHelper()
    {
        return contactHelper;
    }

    public ChatHelper getChatHelper()
    {
        return chatHelper;
    }

    public MessageHelper getMessageHelper()
    {
        return messageHelper;
    }
}
