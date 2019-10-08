package model;

/** Has the structure of the AuthTokens table in the database */
public class AuthToken {

    private final String authToken;
    private final String username;

    /** Constructs an Authtoken object
     *
     * @param authToken Authorization token (non-empty string)
     * @param username Username of the user with the authorization token (non-empty string)
     */
    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o instanceof AuthToken) {
            AuthToken oAuthToken = (AuthToken) o;
            return oAuthToken.getAuthToken().equals(getAuthToken()) &&
                    oAuthToken.getUsername().equals(getUsername());
        }
        return false;
    }
}