package database_access;

/** Exception to be thrown if there's a problem with the database */
public class DatabaseException extends Exception {

    /** Constructs a DatabaseException object
     *
     * @param exceptionMessage Message to be printed if the exception is thrown
     */
    public DatabaseException(String exceptionMessage) {
        super(exceptionMessage);
    }
}