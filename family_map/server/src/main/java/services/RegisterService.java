package services;

import database_access.Database;
import database_access.DatabaseException;
import model.User;
import requests.FillRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;
import results.Result;
import utils.UuidGenerator;

/** Service responsible for creating a new user account, generating 4 generations of ancestor
 * data for the new user, logging the user in, and returning an auth token
 */
public class RegisterService {

    /** Creates a new user, generates 4 generations of ancestor data for the new user,
     * logs the user in and returns an auth token
     *
     * @param req A RegisterRequest object containing information about the registration request
     * @return A RegisterResult object with registration information or an error message
     */
    public RegisterResult register(RegisterRequest req) {
        boolean commit = false;
        Database db = new Database();
        RegisterResult result = null;

        // Create a new user
        String newPersonID = new UuidGenerator().generateUUID();
        User userToInsert = new User(req.getUserName(), req.getPassword(), req.getEmail(),
                req.getFirstName(), req.getLastName(), req.getGender(), newPersonID);

        // Insert the user
        try {
            db.openConnection();
            db.getUserDao().insert(userToInsert);
            commit = true;
        } catch (DatabaseException e) {
            result = new RegisterResult(e.getMessage());
        } finally {
            db.closeConnection(commit);
        }

        // If we had exceptional behavior we return a message about it
        if (result != null) {
            return result;
        }

        // Generate 4 generations of ancestor data
        FillRequest fillRequest = new FillRequest(req.getUserName(), 4);
        FillService fillService = new FillService();
        Result fillResult = fillService.fill(fillRequest);

        // If fillResult is exceptional we should return it
        if (!fillResult.getMessage().contains("Successfully")) {
            return new RegisterResult(fillResult.getMessage());
        }

        // Log the user in
        LoginRequest loginRequest = new LoginRequest(req.getUserName(), req.getPassword());
        LoginService loginService = new LoginService();
        LoginResult loginResult = loginService.login(loginRequest);

        // If loginResult is exceptional we should return it
        if (loginResult.getMessage() != null) {
            return new RegisterResult(loginResult.getMessage());
        }

        result = new RegisterResult(loginResult.getAuthToken(),
                                    loginResult.getUsername(),
                                    loginResult.getPersonID());

        return result;
    }
}