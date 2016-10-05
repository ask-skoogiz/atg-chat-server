/**
 * 
 */
package ask.atg.chat.server;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Test runner to get Cucumber stories run as JUnit tests.
 * 
 * @author Anders Skoglund
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin =
    { "pretty" })
public class RunCucumberTest
{

}
