package services;

import java.util.Random;

import database_access.Database;
import database_access.DatabaseException;
import json_loading.JsonLoader;
import json_loading.Location;
import model.Event;
import model.Person;
import model.User;
import requests.FillRequest;
import results.Result;
import utils.UuidGenerator;

/** Fills the database with information about the ancestors of the person associated
 * with the user
 */
public class FillService {

    private final UuidGenerator uuidGenerator = new UuidGenerator();
    private int peopleAdded = 0;
    private int eventsAdded = 0;
    private final int generationGap = 25;
    private final int userBirthYear = 1995;
    private final JsonLoader jsonLoader = new JsonLoader();

    /** Fills in generations of ancestor data for the current user
     *
     * @param req A FillRequest object with information about the current user and how many
     *            generations to generate
     * @return A Result object with information about the success or failure of the service
     */
    public Result fill(FillRequest req) {
        boolean commit = false;
        Database db = new Database();
        Result result;
        User fillUser;

        if (req.getGenerations() < 0) {
            return new Result("Generations must be a nonnegative integer");
        }

        // Fill data for the user after clearing their old data
        try {
            db.openConnection();

            if(!db.getUserDao().find("username", req.getUsername()).isEmpty()) {
                fillUser = (User)db.getUserDao().find("username", req.getUsername()).get(0);
            } else {
                return new Result("The user you are trying to fill for has not been registered");
            }

            db.getPersonDao().removeUserData(fillUser.getUsername());
            db.getEventDao().removeUserData(fillUser.getUsername());

            Person fillPerson = new Person(fillUser.getPersonID(), fillUser.getUsername(),
                    fillUser.getFirstName(), fillUser.getLastName(),
                    fillUser.getGender(), null, null, null);

            // Fill r.generations() of data for the person
            buildGenerations(fillPerson, req.getGenerations(), 1, fillUser.getUsername(), db);

            result = new Result("Successfully added " + Integer.toString(peopleAdded)+
                    " persons and " + Integer.toString(eventsAdded) + " events to the database");
            commit = true;
        } catch (DatabaseException e) {
            result = new Result(e.getMessage());
        } finally {
            db.closeConnection(commit);
        }

        return result;
    }

    private void buildGenerations(Person p, int totalGens, int curGen, String descendantUsername, Database db) {
        String motherFirstName = jsonLoader.getFemaleNames().getRandomName();
        String motherLastName = jsonLoader.getSurnames().getRandomName();
        String motherPersonID = uuidGenerator.generateUUID();
        String fatherPersonID = uuidGenerator.generateUUID();
        String fatherFirstName = jsonLoader.getMaleNames().getRandomName();

        p.setFather(fatherPersonID);
        p.setMother(motherPersonID);

        try {
            // The -1 is because we're inserting the person in the younger generation from the one
            // we're generating
            generateBirthEvent(p, curGen - 1, db);
            peopleAdded += 1;
            db.getPersonDao().insert(p);
        } catch(DatabaseException e) {
            e.printStackTrace();
        }

        // Recursive base case
        if (totalGens == 0) {
            return;
        }

        Person mother = new Person(motherPersonID, descendantUsername, motherFirstName, motherLastName,
                "f", null, null, fatherPersonID);
        Person father = new Person(fatherPersonID, descendantUsername, fatherFirstName, motherLastName,
                "m", null, null, motherPersonID);

        // Add events for the people
        Location marriageLoc = jsonLoader.getLocations().getRandomLocation();
        generateMarriageEvents(mother, marriageLoc, curGen, db);
        generateMarriageEvents(father, marriageLoc, curGen, db);
        generateOtherEvents(father, curGen, db);
        generateOtherEvents(mother, curGen, db);

        // Call this recursively for mother and father
        if (curGen != totalGens) {
            buildGenerations(mother, totalGens, curGen + 1,
                    descendantUsername, db);
            buildGenerations(father, totalGens, curGen + 1,
                    descendantUsername, db);
        } else {
            try {
                peopleAdded += 2;
                generateBirthEvent(mother, curGen, db);
                generateBirthEvent(father, curGen, db);
                db.getPersonDao().insert(mother);
                db.getPersonDao().insert(father);
            } catch(DatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateBirthEvent(Person p, int gen, Database db) {
        eventsAdded += 1;
        String birthID = uuidGenerator.generateUUID();
        Location birthLoc = jsonLoader.getLocations().getRandomLocation();

        Event birthEvent = new Event(birthID, p.getDescendant(), p.getPersonID(),
                birthLoc.getLatitude(), birthLoc.getLongitude(), birthLoc.getCountry(),
                birthLoc.getCity(), "Birth", userBirthYear - gen * generationGap);

        try {
            db.getEventDao().insert(birthEvent);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void generateMarriageEvents(Person p, Location marriageLoc, int gen, Database db) {
        // Make marriage events for mother and father
        String marriageID = uuidGenerator.generateUUID();

        int ageMarriageOccurs = 20;
        Event motherMarriageEvent = new Event(marriageID, p.getDescendant(), p.getPersonID(),
                marriageLoc.getLatitude(), marriageLoc.getLongitude(), marriageLoc.getCountry(),
                marriageLoc.getCity(), "Marriage",
                userBirthYear + ageMarriageOccurs - gen * generationGap);

        try {
            eventsAdded += 1;
            db.getEventDao().insert(motherMarriageEvent);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void generateOtherEvents(Person p, int gen, Database db) {
        // Make one more event for mother and father
        String[] otherEvents = {"Ran a marathon",
                "Started a new job",
                "Ran for office",
                "Won the lottery",
                "Developed a new ice cream flavor",
                "Went on a vacation",
                "Learned to roller skate",
                "Published a novel",
                "Discovered buried treasure",
                "Sang in a concert",
                "Earned a black belt"};

        String eventID = uuidGenerator.generateUUID();
        Location eventLoc = jsonLoader.getLocations().getRandomLocation();
        Random generator = new Random();
        int randomIndex = generator.nextInt(otherEvents.length);
        String eventType = otherEvents[randomIndex];

        int ageLifeEventOccurs = 30;
        Event otherEvent = new Event(eventID, p.getDescendant(), p.getPersonID(),
                eventLoc.getLatitude(), eventLoc.getLongitude(), eventLoc.getCountry(),
                eventLoc.getCity(), eventType,
                userBirthYear + ageLifeEventOccurs - gen * generationGap);

        try {
            eventsAdded += 1;
            db.getEventDao().insert(otherEvent);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}