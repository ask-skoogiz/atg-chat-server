package ask.atg.chat.server.helpers;

import ask.atg.chat.server.logic.ChatDao;
import ask.atg.chat.server.model.Chat;
import ask.atg.chat.server.model.Contact;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * Helper utility to use when working with {@link Chat} instances.
 * 
 * @author Anders Skoglund
 *
 */
@Singleton
public class ChatHelper extends DaoHelper<Chat, ChatDao>
{
    private ChatDao dao;

    public Chat create(Contact contact)
    {
        return Chat.create(contact);
    }

    @Inject
    public ChatHelper(ChatDao dao)
    {
        this.dao = dao;
    }

    @Override
    public ChatDao getDao()
    {
        return dao;
    }
}
