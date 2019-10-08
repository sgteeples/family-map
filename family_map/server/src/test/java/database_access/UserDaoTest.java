package database_access;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import model.User;

public class UserDaoTest {

    private final Database db = new Database();
    private final User testUser = new User("username", "password", "email",
            "first", "last", "m", "personID");

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

    /** We add a new User to the table, clear the table, and then make sure
     *  that the entry we made is gone
     */
    @Test
    public void clearTable() {
        boolean noUserFound = false;

        try {
            db.openConnection();
            db.getUserDao().insert(testUser);
            db.getUserDao().clearTable();
            noUserFound = db.getUserDao().find("username", testUser.getUsername()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(noUserFound);
    }

    /** We attempt to clear the User table when it's empty, and make sure that runs normally
     * and doesn't throw an exception, which is the desired behavior
     */
    @Test
    public void clearTableFailing() {
        boolean noException = true;

        try {
            db.openConnection();
            db.getUserDao().clearTable();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            noException = false;
            db.closeConnection(false);
        }

        assertTrue(noException);
    }

    /** We insert an User into an empty table and then make sure we can find it */
    @Test
    public void insert() {
        boolean userFound = false;

        try {
            db.openConnection();
            db.getUserDao().insert(testUser);
            userFound = !db.getUserDao().find("username", testUser.getUsername()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(userFound);
    }

    /** We insert an User and then try to insert the same User again, then
     * check to make sure an exception is thrown
     */
    @Test
    public void insertFailing() {
        boolean exceptionThrown = false;

        try {
            db.openConnection();
            db.getUserDao().insert(testUser);
            db.getUserDao().insert(testUser);
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    /** We insert two Users, each associated with a different user. We then
     * remove data associated with one user and make sure that entry is gone while the other
     * entry is not
     */
    @Test
    public void removeUserData() {
        boolean testUserFound = false;
        boolean testUser2Found = false;

        User testUser2 = new User("username2", "password2", "email2",
                "first2", "last2", "f", "personID2");

        try {
            db.openConnection();
            db.getUserDao().insert(testUser);
            db.getUserDao().insert(testUser2);

            db.getUserDao().removeUserData(testUser.getUsername());

            testUserFound = !db.getUserDao().find("username", testUser.getUsername()).isEmpty();
            testUser2Found = !db.getUserDao().find("username", testUser2.getUsername()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertFalse(testUserFound);
        assertTrue(testUser2Found);
    }

    /** We attempt to remove User data for a user when there is none in the table,
     * and make sure there is no exception, which is the desired behavior
     */
    @Test
    public void removeUserDataFailing() {
        boolean exceptionThrown = false;

        try {
            db.openConnection();
            db.getUserDao().removeUserData(testUser.getUsername());
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
            exceptionThrown = true;
        }

        assertFalse(exceptionThrown);
    }

    /** We insert an User and then make sure we can find it */
    @Test
    public void find() {
        boolean userFound = false;

        try {
            db.openConnection();
            db.getUserDao().insert(testUser);
            userFound = !db.getUserDao().find("username", testUser.getUsername()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(userFound);
    }

    /** We attempt to find an User that doesn't exist and insure we can't find it */
    @Test
    public void findFailing() {
        boolean userNotFound = false;

        try {
            db.openConnection();
            userNotFound = db.getUserDao().find("username", testUser.getUsername()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(userNotFound);
    }
}