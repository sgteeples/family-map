package results;

import model.Event;

/** Holds information about the result of a request to the Event API */
public class EventResult extends Result {

    private Event[] data;

    /** Constructs an EventResult object
     *
     * @param userFamilyEvents An array of Events associated with a user's family
     */
    public EventResult(Event[] userFamilyEvents) {
        super(null);
        data = userFamilyEvents;
    }

    /** Constructs an EventResult object with just a message
     *
     * @param message A message (practically an error message)
     */
    public EventResult(String message) {
        super(message);
    }

    public Event[] getData() {
        return data;
    }
}
