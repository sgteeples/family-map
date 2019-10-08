package requests;

/** Holds information about a user's request to register */
public class RegisterRequest {

    private final String userName;
    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String gender;

    /** Constructs a RegisterRequest object
     *
     * @param userName Unique user name (non-empty string)
     * @param password User’s password (non-empty string)
     * @param email User’s email address (non-empty string)
     * @param firstName User’s first name (non-empty string)
     * @param lastName User’s last name (non-empty string)
     * @param gender User’s gender (string: “f” or “m”)
     */
    public RegisterRequest(String userName, String password, String email,
                           String firstName, String lastName, String gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

}
