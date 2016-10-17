/**
 * 
 */
package ask.atg.chat.server;

import ask.atg.chat.server.logic.ChatCache;
import ask.atg.chat.server.logic.ChatDao;
import ask.atg.chat.server.logic.ContactCache;
import ask.atg.chat.server.logic.ContactDao;
import ask.atg.chat.server.logic.UserCache;
import ask.atg.chat.server.logic.UserDao;
import ask.atg.chat.server.service.ChatService;
import ask.atg.chat.server.service.ChatServiceImpl;

import com.google.inject.AbstractModule;

import cucumber.runtime.java.guice.InjectorSource;

/**
 * Guice {@link InjectorSource} for dependency injection while testing the chat server implementation.
 * 
 * @author Anders Skoglund
 *
 */
public class ChatModule extends AbstractModule
{
    public void configure()
    {
        bind(ChatService.class).to(ChatServiceImpl.class);
        bind(UserDao.class).to(UserCache.class);
        bind(ContactDao.class).to(ContactCache.class);
        bind(ChatDao.class).to(ChatCache.class);
    }
}
