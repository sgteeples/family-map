package services;

import java.util.List;

import database_access.Database;
import database_access.DatabaseException;
import model.AuthToken;
import model.Event;
import results.EventResult;

/** Service responsible for returning ALL family members of the current user */
public class EventService {

    /** Returns ALL family members of the current user. The current user is determined
     * from the provided auth token.
     *
     * @param userToken The authorization token of the current user
     * @return A PersonResult object with an array of Person objects or an error message
     */
    public EventResult retrieveUserFamilyEvents(String userToken) {
        EventResult result;
        Database db = new Database();

        try {
            db.openConnection();

            if (db.getAuthTokenDao().find("authToken", userToken).isEmpty()) {
                result = new EventResult("Invalid authorization token");
            } else {
                // Get username associated with the authorization token we have
                AuthToken token = (AuthToken)db.getAuthTokenDao().find("authToken", userToken).get(0);
                List<Object> foundEvents = db.getEventDao().find("descendant", token.getUsername());

                Event[] familyEvents = foundEvents.toArray(new Event[foundEvents.size()]);

                result = new EventResult(familyEvents);
            }
        } catch (DatabaseException e) {
            result = new EventResult(e.getMessage());
        } finally {
            db.closeConnection(false);
        }

        return result;
    }
}
