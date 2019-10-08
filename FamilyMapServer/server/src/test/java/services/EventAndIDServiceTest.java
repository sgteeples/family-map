package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import database_access.*;
import requests.*;
import results.EventAndIDResult;

import static org.junit.Assert.*;

public class EventAndIDServiceTest {

    private final Database db = new Database();

    /** Before each test we create the database tables so they're available. We also
     * insert an authToken/username pair in the AuthTokens table and an Event in the Events
     * table for use in our tests
     */
    @Before
    public void setUp() {
        db.createTables();

        try {
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT into AuthTokens values (\"authToken\", \"username\")");
            stmt.executeUpdate("INSERT into Events values (\"eventID\", \"username\", " +
                    "\"personID\", 0, 0, \"country\", \"city\", \"eventType\", 0)");
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }
    }

    /** After each test we clear the database tables so we can have a fresh start between tests */
    @After
    public void tearDown() {
        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
        } catch (DatabaseException e) {
            db.closeConnection(false);
            e.printStackTrace();
        }
    }

    /** We attempt to retrieve an existing event by its ID and insure that we don't get an error
     * message and that we do get an event reflecting the one in the database
     */
    @Test
    public void retrieveEvent() {
        EventAndIDRequest request = new EventAndIDRequest("eventID", "authToken");
        EventAndIDResult result = new EventAndIDService().retrieveEvent(request);

        assertNull(result.getMessage());
        assertEquals("eventID", result.getEventID());
        assertEquals("username", result.getDescendant());
        assertEquals("personID", result.getPersonID());
        assertEquals(0, result.getLongitude(), 0.001);
        assertEquals(0, result.getLatitude(), 0.001);
        assertEquals("country", result.getCountry());
        assertEquals("city", result.getCity());
        assertEquals("eventType", result.getEventType());
        assertEquals(0, result.getYear(), 0.001);
    }

    /** We attempt to retrieve an event with a nonexistent ID and insure that we get a warning
     * message and nulls for the other result members
     */
    @Test
    public void retrieveEventInvalidID() {
        EventAndIDRequest request = new EventAndIDRequest("notAnEventID", "authToken");
        EventAndIDResult result = new EventAndIDService().retrieveEvent(request);

        assertEquals("No event exists with that eventID", result.getMessage());
        assertNull(result.getEventID());
        assertNull(result.getDescendant());
        assertNull(result.getPersonID());
        assertNull(result.getLongitude());
        assertNull(result.getLatitude());
        assertNull(result.getCountry());
        assertNull(result.getCity());
        assertNull(result.getEventType());
        assertNull(result.getYear());
    }

    /** We attempt to retrieve an event that is associated with a different user and insure that
     * a warning message is returned and the other result members are null
     */
    @Test
    public void retrieveEventWrongUser() {
        try {
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT into AuthTokens values (\"authToken2\", \"username2\")");
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        EventAndIDRequest request = new EventAndIDRequest("eventID", "authToken2");
        EventAndIDResult result = new EventAndIDService().retrieveEvent(request);

        assertEquals("The requested event does not belong " +
                "to user with this authorization token", result.getMessage());
        assertNull(result.getEventID());
        assertNull(result.getDescendant());
        assertNull(result.getPersonID());
        assertNull(result.getLongitude());
        assertNull(result.getLatitude());
        assertNull(result.getCountry());
        assertNull(result.getCity());
        assertNull(result.getEventType());
        assertNull(result.getYear());
    }
}