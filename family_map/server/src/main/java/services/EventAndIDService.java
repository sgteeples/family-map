package services;

import database_access.Database;
import model.AuthToken;
import model.Event;
import requests.EventAndIDRequest;
import results.EventAndIDResult;

/** Service responsible for returning a person object with a specified ID */
public class EventAndIDService {

    /** Returns a person object from the database with the ID specified by the user
     *
     * @param req A PersonAndIDRequest object including a PersonID
     * @return A PersonAndIDResult object with info on the person associated with the provided PersonID
     */
    public EventAndIDResult retrieveEvent(EventAndIDRequest req) {
        boolean commit = false;
        Database myDb = new Database();
        EventAndIDResult result;

        try {
            myDb.openConnection();

            if (myDb.getEventDao().find("eventID", req.getEventID()).isEmpty()) {
                result = new EventAndIDResult("No event exists with that eventID");
            } else {
                Event found = (Event)myDb.getEventDao().find("eventID",
                        req.getEventID()).get(0);
                AuthToken a = (AuthToken)myDb.getAuthTokenDao().find("authToken",
                        req.getAuthToken()).get(0);

                if (!found.getDescendant().equals(a.getUsername())) {
                    result = new EventAndIDResult("The requested event does not belong to " +
                            "user with this authorization token");
                } else {
                    result = new EventAndIDResult(found.getEventID(), found.getDescendant(),
                            found.getPersonID(), found.getLatitude(), found.getLongitude(),
                            found.getCountry(), found.getCity(), found.getEventType(), found.getYear());
                    commit = true;
                }
            }
        } catch (Exception e) {
            result = new EventAndIDResult(e.getMessage());
        } finally {
            myDb.closeConnection(commit);
        }

        return result;
    }
}
