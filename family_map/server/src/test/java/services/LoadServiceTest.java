package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import database_access.Database;
import database_access.DatabaseException;

import static org.junit.Assert.*;
import requests.*;
import results.*;
import model.*;

public class LoadServiceTest {

    private final Database db = new Database();
    private final User fakeUser1 = new User("username1", "password1", "email1",
            "first1", "last1", "m", "personID1");
    private final User fakeUser2 = new User("username2", "password2", "email2",
            "first2", "last2", "f", "personID2");
    private final Event fakeEvent1 = new Event("eventID1", "desc1", "personID1",
            0, 0, "country1", "city1", "eventType1", 0);
    private final Event fakeEvent2 = new Event("eventID2", "desc2", "personID2",
            0, 0, "country2", "city2", "eventType2", 0);
    private final Person fakePerson1 = new Person("personID1", "desc1", "first1",
            "last1", "m", "father1", "mother1", "spouse1");
    private final Person fakePerson2 = new Person("personID2", "desc2", "first2",
            "last2", "f", "father2", "mother2", "spouse2");
    private final Event[] events = {fakeEvent1, fakeEvent2};
    private final Person[] persons = {fakePerson1, fakePerson2};
    private final User[] users = {fakeUser1, fakeUser2};

    /** Before each test we create the database tables so they're available. We also register
     * a user we can load data for in the test
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

    /** We attempt to load 2 users, 2 events, and 2 persons into the database. We then
     * check to make sure they were added and that we get the correct result message
     */
    @Test
    public void load() {
        int numPeopleAdded = 0;
        int numEventsAdded = 0;
        int numUsersAdded = 0;

        LoadRequest request = new LoadRequest(users, persons, events);
        Result result = new LoadService().load(request);

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT count(*) as rows FROM Persons";
            PreparedStatement stmt = conn.prepareStatement(sql);
            numPeopleAdded = stmt.executeQuery().getInt("rows");
            sql = "SELECT count(*) as rows FROM Events";
            stmt = conn.prepareStatement(sql);
            numEventsAdded = stmt.executeQuery().getInt("rows");
            sql = "SELECT count(*) as rows FROM Users";
            stmt = conn.prepareStatement(sql);
            numUsersAdded = stmt.executeQuery().getInt("rows");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertEquals("Successfully added 2 users, 2 persons, and 2 " +
                "events to the database.", result.getMessage());
        assertEquals(2, numEventsAdded);
        assertEquals(2, numPeopleAdded);
        assertEquals(2, numUsersAdded);
    }

    /** We attempt to load 2 users, 2 events, and 2 persons into the database. We then
     * repeat that and make sure we get the same results as just loading once
     * since all database info should be cleared when load is called.
     */
    @Test
    public void loadTwice() {
        int numPeopleAdded = 0;
        int numEventsAdded = 0;
        int numUsersAdded = 0;

        LoadRequest request = new LoadRequest(users, persons, events);
        Result firstResult = new LoadService().load(request);
        Result result = new LoadService().load(request);

        try {
            Connection conn = db.openConnection();
            String sql = "SELECT count(*) as rows FROM Persons";
            PreparedStatement stmt = conn.prepareStatement(sql);
            numPeopleAdded = stmt.executeQuery().getInt("rows");
            sql = "SELECT count(*) as rows FROM Events";
            stmt = conn.prepareStatement(sql);
            numEventsAdded = stmt.executeQuery().getInt("rows");
            sql = "SELECT count(*) as rows FROM Users";
            stmt = conn.prepareStatement(sql);
            numUsersAdded = stmt.executeQuery().getInt("rows");
            stmt.close();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }

        assertEquals("Successfully added 2 users, 2 persons, and 2 " +
                "events to the database.", result.getMessage());
        assertEquals(2, numEventsAdded);
        assertEquals(2, numPeopleAdded);
        assertEquals(2, numUsersAdded);
    }

    // Note: I'm considering the above test case as my "failing" case. Though invalid
    // request data is a serious issue, that is thoroughly handled in the Load Handler,
    // and by the time we get to the service there's not a realistic way to break it
    // that will compile
}