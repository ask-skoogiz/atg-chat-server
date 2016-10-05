/**
 * 
 */
package ask.atg.chat.server.logic;

/**
 * Base Dao interface defining basic data access methods.
 * 
 * @author Anders Skoglund
 *
 */
public interface Dao<T>
{
    /**
     * Save the given object.
     *
     * @param object
     */
    void save(T object) throws Exception;

    /**
     * Delete the given object.
     *
     * @param object
     */
    void delete(T object) throws Exception;

}
