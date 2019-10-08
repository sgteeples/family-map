package edu.byu.cs240.FamilyMap.model;

public class RegisterResult extends Result {

    private String authToken;
    private String username;
    private String personID;

    public RegisterResult(String message) {
        super(message);
    }

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
