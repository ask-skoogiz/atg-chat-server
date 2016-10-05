/**
 * 
 */
package ask.atg.chat.server.helpers;

import ask.atg.chat.server.logic.Clearable;
import ask.atg.chat.server.logic.Dao;

/**
 * 
 * Implement {@link DaoHelper} classes to help working with persisted model classes during test steps.
 * 
 * @author Anders Skoglund
 *
 */
public abstract class DaoHelper<M, D extends Dao<M>> implements Helper<M>, Clearable
{
    public void clear()
    {
        if (getDao() instanceof Clearable)
        {
            ((Clearable) getDao()).clear();
        }
    }

    public abstract D getDao();
}
