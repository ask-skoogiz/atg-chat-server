/**
 * 
 */
package ask.atg.chat.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

/**
 * Guice {@link InjectorSource} for dependency injection while testing the chat server implementation.
 * 
 * @author Anders Skoglund
 *
 */
public class GuiceInjectorSource implements InjectorSource
{

    @Override
    public Injector getInjector()
    {
        return Guice.createInjector(Stage.DEVELOPMENT, CucumberModules.SCENARIO, new ChatModule());
    }
}
