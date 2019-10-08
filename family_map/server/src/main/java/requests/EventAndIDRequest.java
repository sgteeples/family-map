package requests;

/** Contains information relevant to user request for retrieving info about a specific event by ID */
public class EventAndIDRequest {

    private final String eventID;
    private final String authToken;

    /** Constructs a EventAndIDRequest object
     *
     * @param eventID ID of the event to retrieve
     * @param authToken Authorization token presented by the user
     */
    public EventAndIDRequest(String eventID, String authToken) {
        this.eventID = eventID;
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public String getAuthToken() {
        return authToken;
    }
}
