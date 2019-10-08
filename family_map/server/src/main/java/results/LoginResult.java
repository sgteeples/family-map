package results;

/** Holds information about the result of a request to the Login API */
public class LoginResult extends Result {

    private String authToken;
    private String username;
    private String personID;

    /** Constructs a new LoginResult object with just a message
     *
     * @param message A message (practically an error message)
     */
    public LoginResult(String message) {
        super(message);
    }

    /** Constructs a new LoginResult object
     *
     * @param authToken Authorization token for the login
     * @param username Username of the user who logged in
     * @param personID PersonID associated with the user who logged in
     */
    public LoginResult(String authToken, String username, String personID) {

        super(null);
        this.authToken = authToken;
        this.username = username;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }
}
