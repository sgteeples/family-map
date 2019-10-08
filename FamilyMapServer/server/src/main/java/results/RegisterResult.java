package results;

/** Holds information about the result of a request to the Register API */
public class RegisterResult extends Result {

    private String authToken;
    private String username;
    private String personID;

    /** Constructs a RegisterResult object with just a message
     *
     * @param message A message (practically an error message)
     */
    public RegisterResult(String message) {
        super(message);
    }

    /** Constructs a RegisterResult object
     *
     * @param authToken Authorization token for the registered user
     * @param username Username of the registered user
     * @param personID PersonID of the registered user
     */
    public RegisterResult(String authToken, String username, String personID) {
        super(null);
        this.authToken = authToken;
        this.username = username;
        this.personID = personID;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAuthToken() {
        return authToken;
    }
}
