package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import database_access.*;
import requests.RegisterRequest;
import results.RegisterResult;

import static org.junit.Assert.*;

public class RegisterServiceTest {

    private final Database db = new Database();
    private final RegisterRequest request = new RegisterRequest("username", "password",
            "email", "firstName", "lastName", "m");
    private RegisterResult result;

    /** Before each test we create the database tables so they're available. We also
     * register a single user for our tests
     */
    @Before
    public void setUp() {
        db.createTables();
        result = new RegisterService().register(request);
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

    /** We confirm that register creates a new user account */
    @Test
    public void registerCreatesUser() {
        boolean userGenerated = false;

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT * FROM Users WHERE username=\"username\" AND password=\"password\" " +
                    "AND email=\"email\" AND firstName=\"firstName\" AND lastName=\"lastName\" AND " +
                    "gender=\"m\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (stmt.executeQuery().next()) {
                userGenerated = true;
            }
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertTrue(userGenerated);
    }

    /** We confirm that register generates 4 generations of ancestor data - 31 people and
     * 91 events
     */
    @Test
    public void registerCreatesGenerations() {
        int numPeopleGenerated = 0;
        int numEventsGenerated = 0;

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
    }

    /** We confirm that register logs the user in, generating the correct message, including
     * an Authtoken
     */
    @Test
    public void registerLogsUserIn() {
        String generatedAuthToken = "";
        String generatedPersonID = "";

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT * FROM AuthTokens WHERE username=\"username\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            generatedAuthToken = stmt.executeQuery().getString("authToken");
            sql = "SELECT * FROM Users WHERE username=\"username\"";
            stmt = conn.prepareStatement(sql);
            generatedPersonID = stmt.executeQuery().getString("personID");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertEquals(generatedAuthToken, result.getAuthToken());
        assertEquals(generatedPersonID, result.getPersonID());
        assertEquals("username", result.getUsername());
    }

    /** We confirm that trying to register a user who is already registered results in a
     * warning message - this also checks that you can't insert two people with the same username
     */
    @Test
    public void registerFailing() {
        assertEquals("[SQLITE_CONSTRAINT]  Abort due to constraint " +
                "violation (column username is not unique)", new RegisterService().register(request).getMessage());
    }
}