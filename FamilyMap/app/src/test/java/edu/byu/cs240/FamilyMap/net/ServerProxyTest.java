package edu.byu.cs240.FamilyMap.net;

import org.junit.Before;
import org.junit.Test;

import edu.byu.cs240.FamilyMap.model.Event;
import edu.byu.cs240.FamilyMap.model.EventArray;
import edu.byu.cs240.FamilyMap.model.LoginResult;
import edu.byu.cs240.FamilyMap.model.Person;
import edu.byu.cs240.FamilyMap.model.PersonArray;
import edu.byu.cs240.FamilyMap.model.RegisterResult;
import edu.byu.cs240.FamilyMap.utils.Deserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ServerProxyTest {

    private ServerProxy proxy;

    @Before
    public void setUp() {
        // This should be set to current network IP address
        String ipAddress = "192.168.1.123";
        // This should be set to the server port
        String port = "8080";
        proxy = new ServerProxy(ipAddress, port);
    }

    @Test
    public void registerUserPassing() {
        RegisterResult result = proxy.registerUser("username1", "password1",
                "email", "first", "last", "m");

        // A correct registration should result in a RegisterResult object with a null message
        // and non-null other fields
        assertEquals(result.getUsername(), "username1");
        assertNotNull(result.getAuthToken());
        assertNotNull(result.getPersonID());
        assertNull(result.getMessage());
    }

    @Test
    public void registerUserFailingDuplicateRegistration() {
        proxy.registerUser("username2", "password2",
                "email", "first", "last", "m");

        // Registering a user already in the database
        RegisterResult result = proxy.registerUser("username2", "password2",
                "email", "first", "last", "m");

        // Since we're registering a user that already exists we should get an error message
        // and the other fields should be null
        assertNull(result.getUsername());
        assertNull(result.getAuthToken());
        assertNull(result.getPersonID());
        assertEquals(result.getMessage(), "[SQLITE_CONSTRAINT]  Abort due to constraint " +
                "violation (column username is not unique)");
        assertNotNull(result.getMessage());
    }

    @Test
    public void loginUserPassing() {
        // Register a fake user
        proxy.registerUser("username3", "password3",
                "email", "first", "last", "m");

        // Log that user in
        LoginResult result = proxy.loginUser("username3", "password3");

        // A correct registration should result in a LoginResult object with a null message
        // and non-null other fields
        assertNotNull(result.getAuthToken());
        assertNotNull(result.getPersonID());
        assertNull(result.getMessage());
    }

    @Test
    public void loginUserFailingUnregisteredUser() {
        // Trying to log in a user that has never been registered
        LoginResult result = proxy.loginUser("fakeUsername", "fakePassword");

        // Because we're trying to log in a user that has never been registered we
        // should get an error message and the other fields should be null
        assertNull(result.getAuthToken());
        assertNull(result.getPersonID());
        assertEquals(result.getMessage(), "No registered user with this username and password");
        assertNotNull(result.getMessage());
    }

    @Test
    public void getAllUserPersonsPassing() {
        RegisterResult result = proxy.registerUser("username4", "password4",
                "email", "first", "last", "m");

        String userPersonsJSON = proxy.getAllUserPersons(result.getAuthToken());
        Deserializer d = new Deserializer();
        Person[] userPersonsArray = ((PersonArray)d.deserialize(userPersonsJSON, PersonArray.class)).getData();

        // After registering a new user four generations of data are created for them,
        // amounting to 31 generated people. We insure that we can get data for the 31 people
        assertEquals(userPersonsArray.length, 31);
    }

    @Test
    public void getAllUserPersonsInvalidAuthToken() {
        String userPersonsJSON = proxy.getAllUserPersons("Fake Authorization Token");
        System.out.println(userPersonsJSON);
        Deserializer d = new Deserializer();
        Person[] userPersonsArray = ((PersonArray)d.deserialize(userPersonsJSON, PersonArray.class)).getData();

        // Since we're trying to get all persons associated with an unregistered user we should
        // get an error message and the array of persons associated with the user should be null
        assertEquals(userPersonsJSON, "{\"message\":\"Provided auth token is not registered\"}");
        assertNull(userPersonsArray);
    }

    @Test
    public void getAllUserEventsPassing() {
        RegisterResult result = proxy.registerUser("username5", "password5",
                "email", "first", "last", "m");

        String userEventsJSON = proxy.getAllUserEvents(result.getAuthToken());
        Deserializer d = new Deserializer();
        Event[] userEventsArray = ((EventArray)d.deserialize(userEventsJSON, EventArray.class)).getData();

        // After registering a new user four generations of data are created for them,
        // amounting to 91 generated events. We insure that we can get data for the 91 events
        assertEquals(userEventsArray.length, 91);
    }

    @Test
    public void getAllUserEventsFailingInvalidAuthToken() {
        String userEventsJSON = proxy.getAllUserEvents("Fake Authorization Token");
        Deserializer d = new Deserializer();
        Event[] userEventsArray = ((EventArray)d.deserialize(userEventsJSON, EventArray.class)).getData();

        // Since we're trying to get all events associated with an unregistered user we should
        // get an error message and the array of events associated with the user should be null
        assertEquals(userEventsJSON, "{\"message\":\"Provided auth token is not registered\"}");
        assertNull(userEventsArray);
    }
}