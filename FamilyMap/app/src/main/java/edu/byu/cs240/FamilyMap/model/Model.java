package edu.byu.cs240.FamilyMap.model;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Model {

    // Singleton stuff
    private static Model model = new Model();
    private Model() { }
    public static Model getInstance() {
        return model;
    }

    // Setting defaults for various settings
    private int spouseLinesColor = Color.BLUE;
    private int familyTreeLinesColor = Color.GREEN;
    private int lifeStoryLinesColor = Color.RED;

    private boolean showMale = true;
    private boolean showFemale = true;
    private boolean showFathersSide = true;
    private boolean showMothersSide = true;

    private boolean showSpouseLines = true;
    private boolean showFamilyTreeLines = true;
    private boolean showLifeStoryLines = true;

    private int spouseLinesColorIndex = 4;
    private int familyTreeLinesColorIndex = 0;
    private int lifeStoryLinesColorIndex = 6;

    private int mapType = GoogleMap.MAP_TYPE_NORMAL;

    // User information
    private String userID;
    private final List<String> filteredEventTypes = new ArrayList<>();
    private final List<Polyline> currentPolylines = new ArrayList<>();
    private String authToken;
    private List<Person> allPersonsList;
    private String currentlySelectedPersonID = null;
    private List<String> eventTypes;
    private List<String> userFathersSidePeople = new ArrayList<>();
    private List<String> userMothersSidePeople = new ArrayList<>();
    private HashMap<String, List<Event>> personEvents = new HashMap<>();
    private final HashMap<String, Person> userPersons = new HashMap<>();
    private final HashMap<String, String> personChildMap = new HashMap<>();

    public void setUserFathersSidePeople(List<String> userFathersSidePeople) {
        this.userFathersSidePeople = userFathersSidePeople;
    }

    public void setUserMothersSidePeople(List<String> userMothersSidePeople) {
        this.userMothersSidePeople = userMothersSidePeople;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setPersonEvents(HashMap<String, List<Event>> personEvents) {
        this.personEvents = personEvents;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public List<String> getFilteredEventTypes() {
        return filteredEventTypes;
    }

    public void addFilteredEventType(String eType) {
        filteredEventTypes.add(eType);
    }

    public void removeFilteredEventType(String eType) {
        filteredEventTypes.remove(eType);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isShowMale() {
        return showMale;
    }

    public void setShowMale(boolean showMale) {
        this.showMale = showMale;
    }

    public boolean isShowFemale() {
        return showFemale;
    }

    public void setShowFemale(boolean showFemale) {
        this.showFemale = showFemale;
    }

    public boolean isShowFathersSide() {
        return showFathersSide;
    }

    public void setShowFathersSide(boolean showFathersSide) {
        this.showFathersSide = showFathersSide;
    }

    public boolean isShowMothersSide() {
        return showMothersSide;
    }

    public void setShowMothersSide(boolean showMothersSide) {
        this.showMothersSide = showMothersSide;
    }

    public int getSpouseLinesColorIndex() {
        return spouseLinesColorIndex;
    }

    public void setSpouseLinesColorIndex(int spouseLinesColorIndex) {
        this.spouseLinesColorIndex = spouseLinesColorIndex;
    }

    public int getFamilyTreeLinesColorIndex() {
        return familyTreeLinesColorIndex;
    }

    public void setFamilyTreeLinesColorIndex(int familyTreeLinesColorIndex) {
        this.familyTreeLinesColorIndex = familyTreeLinesColorIndex;
    }

    public int getLifeStoryLinesColorIndex() {
        return lifeStoryLinesColorIndex;
    }

    public void setLifeStoryLinesColorIndex(int lifeStoryLinesColorIndex) {
        this.lifeStoryLinesColorIndex = lifeStoryLinesColorIndex;
    }

    public boolean isShowSpouseLines() {
        return showSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    public boolean isShowFamilyTreeLines() {
        return showFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(boolean showFamilyTreeLines) {
        this.showFamilyTreeLines = showFamilyTreeLines;
    }

    public boolean isShowLifeStoryLines() {
        return showLifeStoryLines;
    }

    public void setShowLifeStoryLines(boolean showLifeStoryLines) {
        this.showLifeStoryLines = showLifeStoryLines;
    }

    public int getSpouseLinesColor() {
        return spouseLinesColor;
    }

    public void setSpouseLinesColor(int spouseLinesColor) {
        this.spouseLinesColor = spouseLinesColor;
    }

    public int getFamilyTreeLinesColor() {
        return familyTreeLinesColor;
    }

    public void setFamilyTreeLinesColor(int familyTreeLinesColor) {
        this.familyTreeLinesColor = familyTreeLinesColor;
    }

    public int getLifeStoryLinesColor() {
        return lifeStoryLinesColor;
    }

    public void setLifeStoryLinesColor(int lifeStoryLinesColor) {
        this.lifeStoryLinesColor = lifeStoryLinesColor;
    }

    public void addCurrentPolyLine(Polyline p) {
        currentPolylines.add(p);
    }

    public void removeAllCurrentPolylines() {
        for (Polyline polyline : currentPolylines)
        {
            polyline.remove();
        }

        currentPolylines.clear();
    }

    public void reset() {
        model = new Model();
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public String getCurrentlySelectedPersonID() {
        return currentlySelectedPersonID;
    }

    public void setCurrentlySelectedPersonID(String currentlySelectedPersonID) {
        this.currentlySelectedPersonID = currentlySelectedPersonID;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    private boolean personIsFiltered(Person p) {
        return ((!showMale && p.getGender().equals("m")) ||
                (!showFemale && p.getGender().equals("f")) ||
                (!showFathersSide && userFathersSidePeople.contains(p.getPersonID())) ||
                (!showMothersSide && userMothersSidePeople.contains(p.getPersonID())));
    }

    public List<Person> getPersonFilteredFamily(String personID) {
        List<Person> filteredFamily = new ArrayList<>();
        Person inPerson = getPersonByPersonID(personID);

        if (inPerson.getSpouse() != null) {
            Person spouse = getPersonByPersonID(inPerson.getSpouse());
            if (!personIsFiltered(spouse)) {
                filteredFamily.add(spouse);
            }
        }
        if (userPersons.get(personChildMap.get(personID)) != null) {
            Person child = getPersonByPersonID(personChildMap.get(personID));
            if (!personIsFiltered(child)) {
                filteredFamily.add(child);
            }
        }
        if (inPerson.getFather() != null) {
            Person father = getPersonByPersonID(inPerson.getFather());
            if (!personIsFiltered(father)) {
                filteredFamily.add(father);
            }
        }
        if (inPerson.getMother() != null) {
            Person mother = getPersonByPersonID(inPerson.getMother());
            if (!personIsFiltered(mother)) {
                filteredFamily.add(mother);
            }
        }

        return filteredFamily;
    }

    public String getRelationship(Person p1, Person p2) {
        // id2 is the _____ of id1

        if (p1.getSpouse() != null && p1.getSpouse().equals(p2.getPersonID())) {
            return "Spouse";
        } else if (p1.getFather() != null && p1.getFather().equals(p2.getPersonID())) {
            return "Father";
        } else if (p1.getMother() != null && p1.getMother().equals(p2.getPersonID())) {
            return "Mother";
        } else if (p2.getFather() != null && p2.getFather().equals(p1.getPersonID())) {
            return "Child";
        } else if (p2.getMother() != null && p2.getMother().equals(p1.getPersonID())) {
            return "Child";
        } else {
            return null;
        }
    }

    public void setUserEvents(List<Event> userEvents) {
        Set<String> eventTypesSet = new HashSet<>();
        for (Event e : userEvents) {
            eventTypesSet.add(e.getEventType().toLowerCase());
        }

        this.eventTypes = new ArrayList<>(eventTypesSet);

        for (Event event : userEvents) {
            if (personEvents.get(event.getPersonID()) != null) {
                personEvents.get(event.getPersonID()).add(event);
            } else {
                List<Event> newEventList = new ArrayList<>();
                newEventList.add(event);
                personEvents.put(event.getPersonID(), newEventList);
            }
        }
    }

    private void setFamilySides() {
        // Get the root user
        Person root = getPersonByPersonID(getUserID());

        // Set root user's father to father side
        if (root.getFather() != null) {
            setPersonToFatherSide(root.getFather());
        }

        // Set root user's mother to mother side
        if (root.getMother() != null) {
            setPersonToMothersSide(root.getMother());
        }
    }

    private void setPersonToFatherSide(String personID) {
        userFathersSidePeople.add(personID);

        Person p = getPersonByPersonID(personID);

        if (p.getFather() != null) {
            setPersonToFatherSide(p.getFather());
        }

        if (p.getMother() != null) {
            setPersonToFatherSide(p.getMother());
        }
    }

    private void setPersonToMothersSide(String personID) {
        userMothersSidePeople.add(personID);

        Person p = getPersonByPersonID(personID);

        if (p.getFather() != null) {
            setPersonToMothersSide(p.getFather());
        }

        if (p.getMother() != null) {
            setPersonToMothersSide(p.getMother());
        }
    }

    public void setUserPersons(List<Person> persons) {
        allPersonsList = persons;

        for (Person person : persons) {
            userPersons.put(person.getPersonID(), person);
            if (person.getFather() != null) {
                personChildMap.put(person.getFather(), person.getPersonID());
            }
            if (person.getMother() != null) {
                personChildMap.put(person.getMother(), person.getPersonID());
            }
        }

        setFamilySides();
    }

    public HashMap<String, Person> getPersonMap() {
        return userPersons;
    }

    public Person getPersonByPersonID(String personID) {
        return userPersons.get(personID);
    }

    public List<Person> getAllUnfilteredPersons() {
        return getListUnfilteredPersons(allPersonsList);
    }

    public List<Person> getListUnfilteredPersons(List<Person> l) {
        List<Person> unfilteredPersons = new ArrayList<>();

        for (Person p : l) {
            if (!personIsFiltered(p)) {
                unfilteredPersons.add(p);
            }
        }

        return unfilteredPersons;
    }

    public List<Event> getAllUnfilteredEvents() {
        return getListUnfilteredEvents(getAllUnfilteredPersons());
    }

    public List<Event> getListUnfilteredEvents(List<Person> l) {
        List<Event> unfilteredEvents = new ArrayList<>();

        for (Person p : l) {
            for (Event e : personEvents.get(p.getPersonID())) {
                if (!filteredEventTypes.contains(e.getEventType().toLowerCase())) {
                    unfilteredEvents.add(e);
                }
            }
        }

        return unfilteredEvents;
    }

    public Event getPersonEarliestEvent(String personID) {
        List<Event> thisPersonEvents = getOrderedPersonEvents(personID);
        if (thisPersonEvents.size() != 0) {
            return thisPersonEvents.get(0);
        } else {
            return null;
        }
    }

    public List<Event> getFilteredPersonEvents(String personID) {
        List<Event> thisPersonEvents = new ArrayList<>();
        for (Event e : getAllUnfilteredEvents()) {
            if (e.getPersonID().equals(personID)) {
                thisPersonEvents.add(e);
            }
        }

        return thisPersonEvents;
    }

    public List<Event> sortEventsChronologically(List<Event> l) {
        Collections.sort(l, new Comparator<Event>(){
            public int compare(Event e1, Event e2)
            {
                if (e1.getYear().compareTo(e2.getYear()) != 0) {
                    return e1.getYear().compareTo(e2.getYear());
                } else {
                    return e1.getEventType().toLowerCase().compareTo(e2.getEventType().toLowerCase());
                }
            }
        });

        // As per the specs, birth should always be first if it is present
        moveEventToIndex(l, "birth", 0);

        // As per the specs, death should always be last if it is present
        moveEventToIndex(l, "death", l.size() - 1);

        return l;
    }

    public List<Event> getOrderedPersonEvents(String personID) {
        List<Event> thisPersonEvents = getFilteredPersonEvents(personID);
        return sortEventsChronologically(thisPersonEvents);
    }

    private void moveEventToIndex(List<Event> l, String eventType, int index) {
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).getEventType().toLowerCase().equals(eventType)) {
                Event targetEvent = l.get(i);
                l.remove(i);
                l.add(index, targetEvent);
            }
        }
    }

    public List<Person> getPersonsInSearch(List<Person> personList, String s) {
        List<Person> filteredPersons = new ArrayList<>();

        for (Person p : personList) {
            if (p.getFirstName().toLowerCase().contains(s) || p.getLastName().toLowerCase().contains(s)) {
                filteredPersons.add(p);
            }
        }

        return filteredPersons;
    }

    public List<Event> getEventsInSearch(List<Event> eventList, String s) {
        List<Event> filteredEvents = new ArrayList<>();

        for (Event e : eventList) {
            if (e.getCountry().toLowerCase().contains(s) ||
                    e.getCity().toLowerCase().contains(s) ||
                    e.getEventType().toLowerCase().contains(s) ||
                    Integer.toString(e.getYear()).toLowerCase().contains(s)) {
                filteredEvents.add(e);
            }
        }

        return filteredEvents;
    }
}