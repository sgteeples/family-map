package database_access;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.AuthToken;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthTokenDaoTest {

    private final Database db = new Database();
    private final AuthToken testToken = new AuthToken("authToken", "username");

    /** Before each test we create the database tables so they're available */
    @Before
    public void setUp() {
        db.createTables();
    }

    /** After each test we clear the database tables so we can have a fresh start between tests */
    @After
    public void tearDown() {
        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(true);
            e.printStackTrace();
        }
    }

    /** We add a new AuthToken to the table, clear the table, and then make sure
     *  that the entry we made is gone
     */
    @Test
    public void clearTable() {
        boolean noTokenFound = false;

        try {
            db.openConnection();
            db.getAuthTokenDao().insert(testToken);
            db.getAuthTokenDao().clearTable();
            noTokenFound = db.getAuthTokenDao().find("authToken",
                    testToken.getAuthToken()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(noTokenFound);
    }

    /** We attempt to clear the AuthToken table when it's empty, and make sure that runs normally
     * and doesn't throw an exception, which is the desired behavior
     */
    @Test
    public void clearTableFailing() {
        boolean noException = true;

        try {
            db.openConnection();
            db.getAuthTokenDao().clearTable();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            noException = false;
            db.closeConnection(false);
        }

        assertTrue(noException);
    }

    /** We insert an AuthToken into an empty table and then make sure we can find it */
    @Test
    public void insert() {
        boolean tokenFound = false;

        try {
            db.openConnection();
            db.getAuthTokenDao().insert(testToken);
            tokenFound = !db.getAuthTokenDao().find("authToken",
                    testToken.getAuthToken()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(tokenFound);
    }

    /** We insert an AuthToken and then try to insert the same AuthToken again, then
     * check to make sure an exception is thrown
     */
    @Test
    public void insertFailing() {
        boolean exceptionThrown = false;

        try {
            db.openConnection();
            db.getAuthTokenDao().insert(testToken);
            db.getAuthTokenDao().insert(testToken);
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    /** We insert two AuthTokens, each associated with a different user. We then
     * remove data associated with one user and make sure that entry is gone while the other
     * entry is not
     */
    @Test
    public void removeUserData() {
        boolean testTokenFound = false;
        boolean testToken2Found = false;

        AuthToken testToken2 = new AuthToken("authToken2", "username2");

        try {
            db.openConnection();
            db.getAuthTokenDao().insert(testToken);
            db.getAuthTokenDao().insert(testToken2);

            db.getAuthTokenDao().removeUserData(testToken.getUsername());

            testTokenFound = !db.getAuthTokenDao().find("authToken",
                    testToken.getAuthToken()).isEmpty();
            testToken2Found = !db.getAuthTokenDao().find("authToken",
                    testToken2.getAuthToken()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertFalse(testTokenFound);
        assertTrue(testToken2Found);
    }

    /** We attempt to remove AuthToken data for a user when there is none in the table,
     * and make sure there is no exception, which is the desired behavior
     */
    @Test
    public void removeUserDataFailing() {
        boolean exceptionThrown = false;

        try {
            db.openConnection();
            db.getAuthTokenDao().removeUserData(testToken.getUsername());
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
            exceptionThrown = true;
        }

        assertFalse(exceptionThrown);
    }

    /** We insert an AuthToken and then make sure we can find it */
    @Test
    public void find() {
        boolean tokenFound = false;

        try {
            db.openConnection();
            db.getAuthTokenDao().insert(testToken);
            tokenFound = !db.getAuthTokenDao().find("authToken",
                    testToken.getAuthToken()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(tokenFound);
    }

    /** We attempt to find an AuthToken that doesn't exist and insure we can't find it */
    @Test
    public void findFailing() {
        boolean tokenNotFound = false;

        try {
            db.openConnection();
            tokenNotFound = db.getAuthTokenDao().find("authToken",
                    testToken.getAuthToken()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(tokenNotFound);
    }
}