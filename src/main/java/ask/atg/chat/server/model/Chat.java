/**
 * 
 */
package ask.atg.chat.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Model of a chat conversation.
 * 
 * @author ask
 *
 */
public abstract class Chat
{
    /**
     * Get unique identifier for {@link Chat}.
     * 
     * @return
     */
    public abstract String getId();

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
     * Get recipient off the given {@link Message}.
     * 
     * @param message
     * @return
     */
    public String getRecipient(Message message)
    {
        return message.getAuthor().equals(getContact().fromUser())
                ? getContact().toUser().getUsername()
                : getContact().fromUser().getUsername();
    }

    /**
     * Get the all messages in chronological order.
     *
     * @return
     */
    public List<Message> getMessagesChronological()
    {
        return getMessagesChronological(true);
    }

    /**
     * Get the all messages in chronological order.
     *
     * @return
     */
    public List<Message> getMessagesChronological(boolean ascending)
    {
        List<Message> sortedMessages = getMessages();
        Collections.sort(sortedMessages, new Comparator<Message>()
        {
            @Override
            public int compare(Message msgA, Message msgB)
            {
                return Long.valueOf(msgA.getDate().getTime() - msgB.getDate().getTime()).intValue();
            }
        });
        // If descending.
        if (!ascending)
        {
            Collections.reverse(sortedMessages);
        }
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

            private UUID uuid = UUID.randomUUID();

            @Override
            public String getId()
            {
                return uuid.toString();
            }

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
