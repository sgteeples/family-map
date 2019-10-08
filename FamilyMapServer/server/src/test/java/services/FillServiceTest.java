package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import database_access.Database;
import database_access.DatabaseException;

import requests.FillRequest;
import results.Result;

import static org.junit.Assert.*;

public class FillServiceTest {

    private final Database db = new Database();

    /** Before each test we create the database tables so they're available. We also
     * insert a user in the database so that user can be logged in in the tests
     */
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

    /** We test that the default fill with four generations on an already registered user produces
     * the right changes to the database and the right result message
     */
    @Test
    public void FillDefault() {
        int numPeopleGenerated = 0;
        int numEventsGenerated = 0;

        // Make a new user
        try {
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT into Users values (\"username\", \"password\", \"email\", " +
                    "\"firstNames\", \"lastName\", \"m\", \"personID\")");
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        FillRequest request = new FillRequest("username", 4);
        Result result = new FillService().fill(request);

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT count(*) as rows FROM Persons WHERE descendant=\"username\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            numPeopleGenerated = stmt.executeQuery().getInt("rows");
            sql = "SELECT count(*) as rows FROM Events WHERE descendant=\"username\"";
            stmt = conn.prepareStatement(sql);
            numEventsGenerated = stmt.executeQuery().getInt("rows");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertEquals(31, numPeopleGenerated);
        assertEquals(91, numEventsGenerated);
        assertEquals("Successfully added 31 persons and 91 events " +
                "to the database", result.getMessage());
    }

    /** We test that fill with six generations (non-default example) on an already registered user produces
     * the right changes to the database and the right result message
     */
    @Test
    public void FillSixGens() {
        int numPeopleGenerated = 0;
        int numEventsGenerated = 0;

        // Make a new user
        try {
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT into Users values (\"username\", \"password\", \"email\", " +
                    "\"firstNames\", \"lastName\", \"m\", \"personID\")");
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        FillRequest request = new FillRequest("username", 6);
        Result result = new FillService().fill(request);

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT count(*) as rows FROM Persons WHERE descendant=\"username\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            numPeopleGenerated = stmt.executeQuery().getInt("rows");
            sql = "SELECT count(*) as rows FROM Events WHERE descendant=\"username\"";
            stmt = conn.prepareStatement(sql);
            numEventsGenerated = stmt.executeQuery().getInt("rows");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertEquals(127, numPeopleGenerated);
        assertEquals(379, numEventsGenerated);
        assertEquals("Successfully added 127 persons and 379 events " +
                "to the database", result.getMessage());
    }

    /** We test that fill with zero generations on an already registered user produces
     * the right changes to the database and the right result message
     */
    @Test
    public void FillZeroGens() {
        int numPeopleGenerated = 0;
        int numEventsGenerated = 0;

        // Make a new user
        try {
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT into Users values (\"username\", \"password\", \"email\", " +
                    "\"firstNames\", \"lastName\", \"m\", \"personID\")");
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        FillRequest request = new FillRequest("username", 0);
        Result result = new FillService().fill(request);

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT count(*) as rows FROM Persons WHERE descendant=\"username\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            numPeopleGenerated = stmt.executeQuery().getInt("rows");
            sql = "SELECT count(*) as rows FROM Events WHERE descendant=\"username\"";
            stmt = conn.prepareStatement(sql);
            numEventsGenerated = stmt.executeQuery().getInt("rows");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertEquals(1, numPeopleGenerated);
        assertEquals(1, numEventsGenerated);
        assertEquals("Successfully added 1 persons and 1 events " +
                "to the database", result.getMessage());
    }

    /** We test that fill with a negative number of generations on an already registered user produces
     * the no changes to the database and the right error message
     */
    @Test
    public void FillNegativeGens() {
        int numPeopleGenerated = 0;
        int numEventsGenerated = 0;

        // Make a new user
        try {
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT into Users values (\"username\", \"password\", \"email\", " +
                    "\"firstNames\", \"lastName\", \"m\", \"personID\")");
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        FillRequest request = new FillRequest("username", -1);
        Result result = new FillService().fill(request);

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT count(*) as rows FROM Persons WHERE descendant=\"username\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            numPeopleGenerated = stmt.executeQuery().getInt("rows");
            sql = "SELECT count(*) as rows FROM Events WHERE descendant=\"username\"";
            stmt = conn.prepareStatement(sql);
            numEventsGenerated = stmt.executeQuery().getInt("rows");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertEquals(0, numPeopleGenerated);
        assertEquals(0, numEventsGenerated);
        assertEquals("Generations must be a nonnegative integer", result.getMessage());
    }

    /** We test that the default fill with four generations on an unregistered user produces
     * no changes to the database and the right error message
     */
    @Test
    public void FillUnregistered() {
        int numPeopleGenerated = 0;
        int numEventsGenerated = 0;

        FillRequest request = new FillRequest("username", 4);
        Result result = new FillService().fill(request);

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT count(*) as rows FROM Persons WHERE descendant=\"username\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            numPeopleGenerated = stmt.executeQuery().getInt("rows");
            sql = "SELECT count(*) as rows FROM Events WHERE descendant=\"username\"";
            stmt = conn.prepareStatement(sql);
            numEventsGenerated = stmt.executeQuery().getInt("rows");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertEquals(0, numPeopleGenerated);
        assertEquals(0, numEventsGenerated);
        assertEquals("The user you are trying to fill for has " +
                "not been registered", result.getMessage());
    }

    /** We test that running fill twice has the same effect as running it once, since all
     * data should be removed at each fill
     */
    @Test
    public void FillTwice() {
        int numPeopleGenerated = 0;
        int numEventsGenerated = 0;

        FillRequest request = new FillRequest("username", 4);
        Result result = new FillService().fill(request);
        result = new FillService().fill(request);


        try {
            Connection conn = db.openConnection();
            String sql = "SELECT count(*) as rows FROM Persons WHERE descendant=\"username\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            numPeopleGenerated = stmt.executeQuery().getInt("rows");
            sql = "SELECT count(*) as rows FROM Events WHERE descendant=\"username\"";
            stmt = conn.prepareStatement(sql);
            numEventsGenerated = stmt.executeQuery().getInt("rows");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertEquals(0, numPeopleGenerated);
        assertEquals(0, numEventsGenerated);
        assertEquals("The user you are trying to fill for has " +
                "not been registered", result.getMessage());
    }
}