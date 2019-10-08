package services;

import java.util.List;

import database_access.Database;
import database_access.DatabaseException;
import model.AuthToken;
import model.Person;
import results.PersonResult;

/** Service responsible for returning ALL family members of the current user */
public class PersonService {

    /** Returns ALL family members of the current user. The current user is determined
     * from the provided auth token.
     *
     * @param userToken The authorization token of the current user
     * @return A PersonResult object with an array of Person objects or an error message
     */
    public PersonResult retrieveUserFamily(String userToken) {
        PersonResult result;
        Database db = new Database();

        try {
            db.openConnection();

            if (db.getAuthTokenDao().find("authToken", userToken).isEmpty()) {
                result = new PersonResult("Invalid authorization token");
            } else {
                // Get username associated with the authtoken we have
                AuthToken token = (AuthToken)db.getAuthTokenDao().find("authToken", userToken).get(0);
                List<Object> foundPeople = db.getPersonDao().find("descendant", token.getUsername());

                Person[] familyMembers = foundPeople.toArray(new Person[foundPeople.size()]);

                result = new PersonResult(familyMembers);
            }
        } catch (DatabaseException e) {
            result = new PersonResult(e.getMessage());
        } finally {
            db.closeConnection(false);
        }

        return result;
    }

}
