package services;

import database_access.Database;
import model.AuthToken;
import model.Person;
import requests.PersonAndIDRequest;
import results.PersonAndIDResult;

/** Service responsible for returning a person object with a specified ID */
public class PersonAndIDService {

    /** Returns a person object from the database with the ID specified by the user
     *
     * @param r A PersonAndIDRequest object including a PersonID
     * @return A PersonAndIDResult object with info on the person associated with the provided PersonID
     */
    public PersonAndIDResult retrievePerson(PersonAndIDRequest r) {
        boolean commit = false;
        Database myDb = new Database();
        PersonAndIDResult result;

        try {
            myDb.openConnection();

            if (myDb.getPersonDao().find("personID", r.getPersonID()).isEmpty()) {
                result = new PersonAndIDResult("No person exists with that personID");
            } else {
                Person found = (Person)myDb.getPersonDao().find("personID",
                        r.getPersonID()).get(0);
                AuthToken a = (AuthToken)myDb.getAuthTokenDao().find("authToken",
                        r.getAuthToken()).get(0);

                if (!found.getDescendant().equals(a.getUsername())) {
                    result = new PersonAndIDResult("The requested person does not " +
                            "belong to user with this authorization token");
                } else {
                    result = new PersonAndIDResult(found.getPersonID(), found.getDescendant(),
                            found.getFirstName(), found.getLastName(), found.getGender(),
                            found.getFather(), found.getMother(), found.getSpouse());
                    commit = true;
                }
            }
        } catch (Exception e) {
            result = new PersonAndIDResult(e.getMessage());
        } finally {
            myDb.closeConnection(commit);
        }

        return result;
    }

}
