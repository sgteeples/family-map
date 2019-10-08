package database_access;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import model.Person;

public class PersonDaoTest {

    private final Database db = new Database();
    private final Person testPerson = new Person("personID", "descendant",
            "firstName", "lastName", "m", "father",
            "mother", "spouse");

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

    /** We add a new Person to the table, clear the table, and then make sure
     *  that the entry we made is gone
     */
    @Test
    public void clearTable() {
        boolean noPersonFound = false;

        try {
            db.openConnection();
            db.getPersonDao().insert(testPerson);
            db.getPersonDao().clearTable();
            noPersonFound = db.getPersonDao().find("personID", testPerson.getPersonID()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(noPersonFound);
    }

    /** We attempt to clear the Persons table when it's empty, and make sure that runs normally
     * and doesn't throw an exception, which is the desired behavior
     */
    @Test
    public void clearTableFailing() {
        boolean noException = true;

        try {
            db.openConnection();
            db.getPersonDao().clearTable();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            noException = false;
            db.closeConnection(false);
        }

        assertTrue(noException);
    }

    /** We insert a Person into an empty table and then make sure we can find it */
    @Test
    public void insert() {
        boolean personFound = false;

        try {
            db.openConnection();
            db.getPersonDao().insert(testPerson);
            personFound = !db.getPersonDao().find("personID", testPerson.getPersonID()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(personFound);
    }

    /** We insert a Person and then try to insert the same Person again, then
     * check to make sure an exception is thrown
     */
    @Test
    public void insertFailing() {
        boolean exceptionThrown = false;

        try {
            db.openConnection();
            db.getPersonDao().insert(testPerson);
            db.getPersonDao().insert(testPerson);
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    /** We insert two Persons, each associated with a different user. We then
     * remove data associated with one user and make sure that entry is gone while the other
     * entry is not
     */
    @Test
    public void removeUserData() {
        boolean testPersonFound = false;
        boolean testPerson2Found = false;

        Person testPerson2 = new Person("personID2", "descendant2",
                "firstName2", "lastName2", "f", "father2",
                "mother2", "spouse2");

        try {
            db.openConnection();
            db.getPersonDao().insert(testPerson);
            db.getPersonDao().insert(testPerson2);

            db.getPersonDao().removeUserData(testPerson.getDescendant());

            testPersonFound = !db.getPersonDao().find("personID", testPerson.getPersonID()).isEmpty();
            testPerson2Found = !db.getPersonDao().find("personID", testPerson2.getPersonID()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertFalse(testPersonFound);
        assertTrue(testPerson2Found);
    }

    /** We attempt to remove Person data for a user when there is none in the table,
     * and make sure there is no exception, which is the desired behavior
     */
    @Test
    public void removeUserDataFailing() {
        boolean exceptionThrown = false;

        try {
            db.openConnection();
            db.getPersonDao().removeUserData(testPerson.getDescendant());
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
            exceptionThrown = true;
        }

        assertFalse(exceptionThrown);
    }

    /** We insert a Person and then make sure we can find it */
    @Test
    public void find() {
        boolean personFound = false;

        try {
            db.openConnection();
            db.getPersonDao().insert(testPerson);
            personFound = !db.getPersonDao().find("personID", testPerson.getPersonID()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(personFound);
    }

    /** We attempt to find a Person that doesn't exist and insure we can't find it */
    @Test
    public void findFailing() {
        boolean personNotFound = false;

        try {
            db.openConnection();
            personNotFound = db.getPersonDao().find("personID", testPerson.getPersonID()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(personNotFound);
    }
}