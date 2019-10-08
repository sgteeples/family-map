package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import database_access.*;
import requests.*;
import results.PersonAndIDResult;

import static org.junit.Assert.*;

public class PersonAndIDServiceTest {

    private final Database db = new Database();

    /** Before each test we create the database tables so they're available. We also
     * insert an authToken/username pair in the AuthTokens table and a Person in the Persons
     * table for use in our tests
     */
    @Before
    public void setUp() {
        db.createTables();

        try {
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT into AuthTokens values (\"authToken\", \"username\")");
            stmt.executeUpdate("INSERT into Persons values (\"personID\", \"username\", " +
                    "\"firstName\", \"lastName\", \"m\", \"father\", \"mother\", \"spouse\")");
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

    /** We attempt to retrieve an existing person by its ID and insure that we don't get an error
     * message and that we do get a person reflecting the one in the database
     */
    @Test
    public void retrievePerson() {
        PersonAndIDRequest request = new PersonAndIDRequest("personID", "authToken");
        PersonAndIDResult result = new PersonAndIDService().retrievePerson(request);

        assertNull(result.getMessage());
        assertEquals("personID", result.getPersonID());
        assertEquals("username", result.getDescendant());
        assertEquals("firstName", result.getFirstName());
        assertEquals("lastName", result.getLastName());
        assertEquals("m", result.getGender());
        assertEquals("father", result.getFather());
        assertEquals("mother", result.getMother());
        assertEquals("spouse", result.getSpouse());
    }

    /** We attempt to retrieve a person with a nonexistent ID and insure that we get a warning
     * message and nulls for the other result members
     */
    @Test
    public void retrievePersonInvalidID() {
        PersonAndIDRequest request = new PersonAndIDRequest("notAPersonID", "authToken");
        PersonAndIDResult result = new PersonAndIDService().retrievePerson(request);

        assertEquals("No person exists with that personID", result.getMessage());
        assertNull(result.getPersonID());
        assertNull(result.getDescendant());
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getGender());
        assertNull(result.getFather());
        assertNull(result.getMother());
        assertNull(result.getSpouse());
    }

    /** We attempt to retrieve a person that is associated with a different user and insure that
     * a warning message is returned and the other result members are null
     */
    @Test
    public void retrievePersonWrongUser() {
        try {
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT into AuthTokens values (\"authToken2\", \"username2\")");
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        PersonAndIDRequest request = new PersonAndIDRequest("personID", "authToken2");
        PersonAndIDResult result = new PersonAndIDService().retrievePerson(request);

        assertEquals("The requested person does not belong " +
                "to user with this authorization token", result.getMessage());
        assertNull(result.getPersonID());
        assertNull(result.getDescendant());
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getGender());
        assertNull(result.getFather());
        assertNull(result.getMother());
        assertNull(result.getSpouse());
    }
}