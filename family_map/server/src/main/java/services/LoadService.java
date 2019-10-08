package services;

import database_access.Database;
import database_access.DatabaseException;
import model.Event;
import model.Person;
import model.User;
import requests.LoadRequest;
import results.Result;

/** Service responsible for clearing all data from the database (just like the /clear API), and
 * then loading the posted user, person, and event data in */
public class LoadService {

    public Result load(LoadRequest req) {
        Result result;
        Database db = new Database();

        try {
            db.openConnection();
            db.clearTables();

            for (User user : req.getUsersToLoad()) {
                db.getUserDao().insert(user);
            }

            for (Person person : req.getPersonsToLoad()) {
                db.getPersonDao().insert(person);
            }

            for (Event event : req.getEventsToLoad()) {
                db.getEventDao().insert(event);
            }

            db.closeConnection(true);

            result = new Result("Successfully added " + Integer.toString(req.getUsersToLoad().length) +
                    " users, " + Integer.toString(req.getPersonsToLoad().length) + " persons, and " +
                    Integer.toString(req.getEventsToLoad().length) + " events to the database.");
            return result;

        } catch (DatabaseException e) {
            db.closeConnection(false);
            result = new Result(e.getMessage());
        }
        return result;
    }
}