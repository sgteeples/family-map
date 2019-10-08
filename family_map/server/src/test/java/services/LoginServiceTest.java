package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import database_access.Database;
import database_access.DatabaseException;
import requests.LoginRequest;
import results.LoginResult;

import static org.junit.Assert.*;

public class LoginServiceTest {

    private final Database db = new Database();

    /** Before each test we create the database tables so they're available. We also
     * insert a user in the database so that user can be logged in in the tests
     */
    @Before
    public void setUp() {
        db.createTables();

        try {
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT into Users values (\"username\", \"password\", \"email\", " +
                    "\"firstNames\", \"lastName\", \"m\", \"personID\")");
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
            db.closeConnection(true);
            e.printStackTrace();
        }
    }

    /** We confirm that the login function creates a User/AuthToken pair in the AuthTokens
     * table, and that the resulting message is correct and contains an AuthToken
     */
    @Test
    public void login() {
        String generatedAuthToken = "";

        LoginRequest request = new LoginRequest("username", "password");
        LoginResult result = new LoginService().login(request);

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT * FROM AuthTokens WHERE username=\"username\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            generatedAuthToken = stmt.executeQuery().getString("authToken");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertEquals(generatedAuthToken, result.getAuthToken());
        assertEquals("personID", result.getPersonID());
        assertEquals("username", result.getUsername());
    }

    /** We confirm that attempting to login an unregistered user results in an error message
     * and no generation of a User/Authtoken pair
     */
    @Test
    public void loginFailing() {
        boolean loginPairGenerated = true;

        LoginRequest request = new LoginRequest("fakeUsername", "password");
        LoginResult result = new LoginService().login(request);

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT * FROM AuthTokens WHERE username=\"fakeUsername\"";
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (!stmt.executeQuery().next()) {
                loginPairGenerated = false;
            }
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertFalse(loginPairGenerated);
        assertEquals("No registered user with this username and password", result.getMessage());
        assertNull(result.getAuthToken());
        assertNull(result.getPersonID());
        assertNull(result.getUsername());
    }
}