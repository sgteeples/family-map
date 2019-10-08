package requests;

/** Contains information relevant to user request for retrieving info about a specific person by ID */
public class PersonAndIDRequest {

    private final String personID;
    private final String authToken;

    /** Constructs a EventAndIDRequest object
     *
     * @param personID ID of the person to retrieve
     * @param authToken Authorization token presented by the user
     */
    public PersonAndIDRequest(String personID, String authToken) {
        this.personID = personID;
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAuthToken() {
        return authToken;
    }
}
