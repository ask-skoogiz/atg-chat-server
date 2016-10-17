/**
 * 
 */
package ask.atg.chat.server.json;

/**
 * Object implementing this interface are able to morph into a {@link JsonBean}.
 * 
 * @author Anders Skoglund
 *
 */
public interface JsonMorphable
{
    /**
     * Morph instance into a {@link JsonBean}.
     * 
     * @return
     */
    JsonBean toJson();
}
