package database_access;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import model.Event;

public class EventDaoTest {

    private final Database db = new Database();
    private final Event testEvent = new Event("eventID", "descendant",
            "personID", 0, 0, "country", "city",
            "eventType", 0);

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

    /** We add a new Event to the table, clear the table, and then make sure
     *  that the entry we made is gone
     */
    @Test
    public void clearTable() {
        boolean noEventFound = false;

        try {
            db.openConnection();
            db.getEventDao().insert(testEvent);
            db.getEventDao().clearTable();
            noEventFound = db.getEventDao().find("eventID", testEvent.getEventID()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(noEventFound);
    }

    /** We attempt to clear the Events table when it's empty, and make sure that runs normally
     * and doesn't throw an exception, which is the desired behavior
     */
    @Test
    public void clearTableFailing() {
        boolean noException = true;

        try {
            db.openConnection();
            db.getEventDao().clearTable();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            noException = false;
            db.closeConnection(false);
        }

        assertTrue(noException);
    }

    /** We insert an Event into an empty table and then make sure we can find it */
    @Test
    public void insert() {
        boolean eventFound = false;

        try {
            db.openConnection();
            db.getEventDao().insert(testEvent);
            eventFound = !db.getEventDao().find("eventID", testEvent.getEventID()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(eventFound);
    }

    /** We insert an Event and then try to insert the same Event again, then
     * check to make sure an exception is thrown
     */
    @Test
    public void insertFailing() {
        boolean exceptionThrown = false;

        try {
            db.openConnection();
            db.getEventDao().insert(testEvent);
            db.getEventDao().insert(testEvent);
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    /** We insert two Events, each associated with a different user. We then
     * remove data associated with one user and make sure that entry is gone while the other
     * entry is not
     */
    @Test
    public void removeUserData() {
        boolean testEventFound = false;
        boolean testEvent2Found = false;

        Event testEvent2 = new Event("eventID2", "descendant2", "personID2",
                0, 0, "country2", "city2", "eventType2", 0);

        try {
            db.openConnection();
            db.getEventDao().insert(testEvent);
            db.getEventDao().insert(testEvent2);

            db.getEventDao().removeUserData(testEvent.getDescendant());

            testEventFound = !db.getEventDao().find("eventID", testEvent.getEventID()).isEmpty();
            testEvent2Found = !db.getEventDao().find("eventID", testEvent2.getEventID()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertFalse(testEventFound);
        assertTrue(testEvent2Found);
    }

    /** We attempt to remove Event data for a user when there is none in the table,
     * and make sure there is no exception, which is the desired behavior
     */
    @Test
    public void removeUserDataFailing() {
        boolean exceptionThrown = false;

        try {
            db.openConnection();
            db.getEventDao().removeUserData(testEvent.getDescendant());
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
            exceptionThrown = true;
        }

        assertFalse(exceptionThrown);
    }

    /** We insert an Event and then make sure we can find it */
    @Test
    public void find() {
        boolean eventFound = false;

        try {
            db.openConnection();
            db.getEventDao().insert(testEvent);
            eventFound = !db.getEventDao().find("eventID", testEvent.getEventID()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(eventFound);
    }

    /** We attempt to find an Event that doesn't exist and insure we can't find it */
    @Test
    public void findFailing() {
        boolean eventNotFound = false;

        try {
            db.openConnection();
            eventNotFound = db.getEventDao().find("eventID", testEvent.getEventID()).isEmpty();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
        }

        assertTrue(eventNotFound);
    }
}