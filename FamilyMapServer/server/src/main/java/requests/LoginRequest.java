package requests;

/** Holds information about a user's login request */
public class LoginRequest {

    private final String userName;
    private final String password;

    /** Constructs a LoginRequest object
     *
     * @param userName User's username
     * @param password User's password
     */
    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
