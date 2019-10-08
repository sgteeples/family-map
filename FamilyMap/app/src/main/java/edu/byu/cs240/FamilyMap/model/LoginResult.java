package edu.byu.cs240.FamilyMap.model;

public class LoginResult extends Result {

    private String authToken;
    private String username;
    private String personID;

    public LoginResult(String message) {
        super(message);
    }

    public LoginResult(String authToken, String username, String personID) {
        super(null);
        this.authToken = authToken;
        this.username = username;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPersonID() {
        return personID;
    }
}
