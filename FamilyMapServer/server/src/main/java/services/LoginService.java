package services;

import database_access.Database;
import database_access.DatabaseException;
import model.AuthToken;
import model.User;
import requests.LoginRequest;
import results.LoginResult;
import utils.UuidGenerator;

/** Responsible for handling getting a user logged int */
public class LoginService {

    /** Logs a user in
     *
     * @param req Request for login with requester's username and password
     * @return A LoginResult giving information about the login or an error message
     */
    public LoginResult login(LoginRequest req) {
        boolean commit = false;
        LoginResult result;
        Database myDb = new Database();
        User user;

        try {
            myDb.openConnection();

            // If the user is registered
            if (!myDb.getUserDao().find("username", req.getUserName()).isEmpty()) {
                user = (User)myDb.getUserDao().find("username", req.getUserName()).get(0);

                // Check that username password combination are in users table
                if (user != null && user.getPassword().equals(req.getPassword())) {
                    // Generate an authToken for the username
                    String authTokenString = new UuidGenerator().generateUUID();
                    AuthToken newToken = new AuthToken(authTokenString, user.getUsername());
                    myDb.getAuthTokenDao().insert(newToken);

                    // Make result with AuthToken, username, personID
                    result = new LoginResult(authTokenString, req.getUserName(), user.getPersonID());

                    commit = true;
                } else {
                    result = new LoginResult("No registered user with this username and password");
                }
            } else {
                result = new LoginResult("No registered user with this username and password");
            }
        } catch (DatabaseException e) {
            result = new LoginResult(e.getMessage());
        } finally {
            myDb.closeConnection(commit);
        }

        return result;
    }

}
