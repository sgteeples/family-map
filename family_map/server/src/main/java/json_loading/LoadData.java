package json_loading;

import model.Event;
import model.Person;
import model.User;

/** Holds data for loading into the database from the Load API */
public class LoadData {

    private User[] users;
    private Person[] persons;
    private Event[] events;

    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }
}
