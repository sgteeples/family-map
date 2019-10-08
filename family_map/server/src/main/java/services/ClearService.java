package services;

import database_access.Database;
import database_access.DatabaseException;
import results.Result;

/** Service responsible for deleting ALL data from the database, including user accounts,
 * auth tokens, and generated person and event data */
public class ClearService {

    /** Clears ALL data from the database, including user accounts,
     * auth tokens, and generated person and event data
     *
     * @return A ClearResult object containing info about whether database was successfully cleared
     */
    public Result clear() {
        Database db = new Database();
        Result result;

        try {
            db.openConnection();
            db.clearTables();
            result = new Result("Clear succeeded.");
            db.closeConnection(true);
        } catch (DatabaseException e) {
            result = new Result(e.toString());
            db.closeConnection(false);
        }

        return result;
    }

}

