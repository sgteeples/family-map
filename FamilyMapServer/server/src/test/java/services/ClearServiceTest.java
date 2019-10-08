package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import database_access.DatabaseException;
import database_access.Database;

import results.*;

import static org.junit.Assert.*;

public class ClearServiceTest {

    private final Database db = new Database();

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

    /** We add a User to the Users table, an AuthToken to the AuthTokens table, a Person to the
     * Persons table, and a Event to the Events table, then insure they are all removed when
     * clear is called. Finally we check that we get a success message
     */
    @Test
    public void clear() {
        boolean insertedAuthtokenNotFound = false;
        boolean insertedUserNotFound = false;
        boolean insertedEventNotFound = false;
        boolean insertedPersonNotFound = false;

        // Insert the test entries
        try {
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT into AuthTokens values (\"authToken\", \"username\")");
            stmt.executeUpdate("INSERT into Events values (\"eventID\", \"username\", " +
                    "\"personID\", 0, 0, \"country\", \"city\", \"eventType\", 0)");
            stmt.executeUpdate("INSERT into Users values (\"username\", \"password\", " +
                    "\"email\", \"firstName\", \"lastName\", \"gender\", \"personID\")");
            stmt.executeUpdate("INSERT into Persons values (\"personID\", \"username\", " +
                    "\"firstName\", \"lastName\", \"m\", \"father\", \"mother\", \"spouse\")");
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        // Run the clear service clear function
        Result result = new ClearService().clear();

        // Attempt to find the inserted entries
        try {
            Connection conn = db.openConnection();
            String sql = "SELECT count(*) as rows FROM Users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            insertedUserNotFound = stmt.executeQuery().getString("rows").equals("0");
            sql = "SELECT count(*) as rows FROM Events";
            stmt = conn.prepareStatement(sql);
            insertedEventNotFound = stmt.executeQuery().getString("rows").equals("0");
            sql = "SELECT count(*) as rows FROM Persons";
            stmt = conn.prepareStatement(sql);
            insertedPersonNotFound = stmt.executeQuery().getString("rows").equals("0");
            sql = "SELECT count(*) as rows FROM AuthTokens";
            stmt = conn.prepareStatement(sql);
            insertedAuthtokenNotFound = stmt.executeQuery().getString("rows").equals("0");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertTrue(insertedAuthtokenNotFound);
        assertTrue(insertedEventNotFound);
        assertTrue(insertedPersonNotFound);
        assertTrue(insertedUserNotFound);
        assertEquals("Clear succeeded.", result.getMessage());
    }

    /** We run clear service on all empty tables and insure that we still get a success message */
    @Test
    public void clearFailing() {
        Result result = new ClearService().clear();

        assertEquals("Clear succeeded.", result.getMessage());
    }
}