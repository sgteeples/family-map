package requests;

import model.Event;
import model.Person;
import model.User;

/** Contains information relevant to user request for loading information */
public class LoadRequest {

    private final User[] usersToLoad;
    private final Person[] personsToLoad;
    private final Event[] eventsToLoad;

    /** Constructs a LoadRequest object
     *
     * @param usersToLoad Array of User objects to load into the database
     * @param personsToLoad Array of Person objects to load into the database
     * @param eventsToLoad Array of Event objects to load into the database
     */
    public LoadRequest(User[] usersToLoad, Person[] personsToLoad, Event[] eventsToLoad) {
        this.usersToLoad = usersToLoad;
        this.personsToLoad = personsToLoad;
        this.eventsToLoad = eventsToLoad;
    }

    public User[] getUsersToLoad() {
        return usersToLoad;
    }

    public Person[] getPersonsToLoad() {
        return personsToLoad;
    }

    public Event[] getEventsToLoad() {
        return eventsToLoad;
    }
    
}
