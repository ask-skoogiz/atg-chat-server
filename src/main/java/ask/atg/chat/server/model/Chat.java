/**
 * 
 */
package ask.atg.chat.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Model of a chat conversation.
 * 
 * @author ask
 *
 */
public abstract class Chat
{
    /**
     * Get the {@link Contact} between the chat {@link User}s.
     * 
     * @return
     */
    public abstract Contact getContact();

    /**
     * Get the all messages.
     *
     * @return
     */
    public abstract List<Message> getMessages();

    /**
     * Get the all messages in chronological order.
     *
     * @return
     */
    public List<Message> getMessagesChronological()
    {
        List<Message> sortedMessages = getMessages();
        Collections.sort(sortedMessages, new Comparator<Message>()
        {
            @Override
            public int compare(Message msgA, Message msgB)
            {
                return msgA.getDate().compareTo(msgB.getDate());
            }
        });
        return sortedMessages;
    }

    /**
     * Add {@link Message} to the conversation.
     *
     * @param message
     */
    public Chat message(Message message)
    {
        getMessages().add(message);
        return this;
    }

    /**
     * Factory method to construct a conversation between users of the chat server.
     * 
     * @param members
     * @return a new {@link Chat}
     */
    public static Chat create(final Contact contact)
    {
        return new Chat()
        {
            private final List<Message> conversation = new ArrayList<Message>();

            @Override
            public Contact getContact()
            {
                return contact;
            }

            @Override
            public List<Message> getMessages()
            {
                return conversation;
            }
        };
    }
}
