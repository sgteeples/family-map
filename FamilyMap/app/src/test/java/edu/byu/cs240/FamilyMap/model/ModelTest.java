package edu.byu.cs240.FamilyMap.model;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class ModelTest {

    private Person jamesPotter;
    private Person lilyPotter;
    private Person harryPotter;
    private Person ginnyWeasley;
    private Person mollyWeasley;
    private Person arthurWeasley;
    private Person albusPotter;

    private Event harryEvent1;
    private Event harryEvent2;
    private Event harryEvent3;
    private Event harryEvent4;
    private Event harryEvent5;
    private Event harryEvent6;

    private List<Event> harryEvents;
    private List<Person> allPersons;

    private Model model;

    @Before
    public void setUp() {
        // Model to use for the tests
        model = Model.getInstance();
        model.reset();

        // Some fake people for use in the tests
        jamesPotter = new Person("jPotter", "aPotter", "James",
                "Potter", "m", null, null, "lPotter");
        lilyPotter = new Person("lPotter", "aPotter", "Lily",
                "Potter", "f", null, null, "jPotter");
        harryPotter = new Person("hPotter", "aPotter", "Harry",
                "Potter", "m", "jPotter", "lPotter", "gWeasley");
        ginnyWeasley = new Person("gWeasley", "aPotter", "Ginny",
                "Weasley", "f", "aWeasley", "mWeasley", "hPotter");
        mollyWeasley = new Person("mWeasley", "aPotter", "Molly",
                "Weasley", "f", null, null, "aWeasley");
        arthurWeasley = new Person("aWeasley", "aPotter", "Arthur",
                "Weasley", "m", null, null, "mWeasley");
        albusPotter = new Person("aPotter", "aPotter", "Albus",
                "Potter", "m", "hPotter", "gWeasley", null);

        // Some fake events for use in the tests
        harryEvent1 = new Event("defeatVoldemort", "aPotter", "hPotter",
                51.5074, 0.1278, "England", "London",
                "Defeated Voldemort", 1997);
        harryEvent2 = new Event("harryBirth", "aPotter", "hPotter",
                51.5074, 0.1278, "England", "London", "Birth",
                1980);
        harryEvent3 = new Event("chocolateFrog", "aPotter", "hPotter",
                51.5074, 0.1278, "England",
                "London", "Ate Chocolate Frog", 1994);
        harryEvent4 = new Event("pumpkinJuice", "aPotter", "hPotter",
                51.5074, 0.1278, "England", "London",
                "Drank Pumpkin Juice", 1994);
        harryEvent5 = new Event("quidditch", "aPotter", "hPotter",
                51.5074, 0.1278, "England", "London",
                "Played Quidditch", 1994);
        harryEvent6 = new Event("death", "aPotter", "hPotter",
                51.5074, 0.1278, "England", "London",
                "Death", 2080);

        allPersons = new ArrayList<>();
        allPersons.add(jamesPotter);
        allPersons.add(lilyPotter);
        allPersons.add(harryPotter);
        allPersons.add(ginnyWeasley);
        allPersons.add(mollyWeasley);
        allPersons.add(arthurWeasley);
        allPersons.add(albusPotter);

        harryEvents = new ArrayList<>();
        harryEvents.add(harryEvent1);
        harryEvents.add(harryEvent2);
        harryEvents.add(harryEvent3);
        harryEvents.add(harryEvent4);
        harryEvents.add(harryEvent5);
        harryEvents.add(harryEvent6);

        // We start with showing everything
        setAllPersonFiltersToTrue();

        // Setting up mother and father sides
        model.setUserFathersSidePeople(Arrays.asList("jPotter", "lPotter", "hPotter"));
        model.setUserMothersSidePeople(Arrays.asList("aWeasley", "mWeasley", "gWeasley"));

        // Setting up person events
        HashMap<String, List<Event>> personEvents = new HashMap<>();
        personEvents.put("hPotter", harryEvents);
        model.setPersonEvents(personEvents);
    }

    // TESTING CALCULATING FAMILY RELATIONSHIPS (i.e. SPOUSES, PARENTS, CHILDREN)

    @Test
    public void getRelationship() {
        // James is the father of Harry
        assertEquals("Father", model.getRelationship(harryPotter, jamesPotter));

        // Lily is the mother of Harry
        assertEquals("Mother", model.getRelationship(harryPotter, lilyPotter));

        // Harry is the child of James
        assertEquals("Child", model.getRelationship(jamesPotter, harryPotter));

        // Harry is the child of Lily
        assertEquals("Child", model.getRelationship(lilyPotter, harryPotter));

        // Lily is the spouse of James
        assertEquals("Spouse", model.getRelationship(jamesPotter, lilyPotter));

        // James is the spouse of Lily
        assertEquals("Spouse", model.getRelationship(lilyPotter, jamesPotter));
    }

    @Test
    public void getRelationshipNoRelationship() {
        // Molly is not related as a father, mother, child, or spouse to James, Harry, or Lily
        assertNull(model.getRelationship(jamesPotter, mollyWeasley));
        assertNull(model.getRelationship(lilyPotter, mollyWeasley));
        assertNull(model.getRelationship(harryPotter, mollyWeasley));
    }

    // TESTING CHRONOLOGICALLY SORTING A PERSON'S INDIVIDUAL EVENTS (BIRTH FIRST, DEATH LAST, ETC.)

    @Test
    public void sortEventsChronologicallyWithTies() {
        model.sortEventsChronologically(harryEvents);

        // Here we are just insuring that ordering chronologically works smoothly,
        // even with ties
        assertEquals("Birth", harryEvents.get(0).getEventType());
        assertEquals("Ate Chocolate Frog", harryEvents.get(1).getEventType());
        assertEquals("Drank Pumpkin Juice", harryEvents.get(2).getEventType());
        assertEquals("Played Quidditch", harryEvents.get(3).getEventType());
        assertEquals("Defeated Voldemort", harryEvents.get(4).getEventType());
        assertEquals("Death", harryEvents.get(5).getEventType());
    }

    @Test
    public void sortEventsChoronologicallyBirthDeathCases() {
        // No matter what, birth should come first and death last. We'll change up
        // the dates in an unrealistic way and make sure that still holds
        harryEvent6.setYear(0); // Moving death to year 0
        harryEvent1.setYear(3000); // Moving birth to year 3000

        model.sortEventsChronologically(harryEvents);

        assertEquals("Birth", harryEvents.get(0).getEventType());
        assertEquals("Ate Chocolate Frog", harryEvents.get(1).getEventType());
        assertEquals("Drank Pumpkin Juice", harryEvents.get(2).getEventType());
        assertEquals("Played Quidditch", harryEvents.get(3).getEventType());
        assertEquals("Defeated Voldemort", harryEvents.get(4).getEventType());
        assertEquals("Death", harryEvents.get(5).getEventType());
    }

    // TESTING FILTERING EVENTS ACCORDING TO THE CURRENT FILTER SETTINGS

    private void setAllPersonFiltersToTrue() {
        model.setShowMale(true);
        model.setShowFemale(true);
        model.setShowFathersSide(true);
        model.setShowMothersSide(true);
    }

    @Test
    public void getListUnfilteredPersonsGenders() {
        // If we're not filtering anything people related, then all people should
        // show up after the filtering process
        assert(model.getListUnfilteredPersons(allPersons).equals(allPersons));

        // Filtering out just males
        model.setShowMale(false);
        assert(model.getListUnfilteredPersons(allPersons).contains(mollyWeasley));
        assert(model.getListUnfilteredPersons(allPersons).contains(ginnyWeasley));
        assert(model.getListUnfilteredPersons(allPersons).contains(lilyPotter));
        assertEquals(3, model.getListUnfilteredPersons(allPersons).size());

        // Filtering out just females
        setAllPersonFiltersToTrue();
        model.setShowFemale(false);
        assert(model.getListUnfilteredPersons(allPersons).contains(harryPotter));
        assert(model.getListUnfilteredPersons(allPersons).contains(albusPotter));
        assert(model.getListUnfilteredPersons(allPersons).contains(jamesPotter));
        assert(model.getListUnfilteredPersons(allPersons).contains(arthurWeasley));
        assertEquals(4, model.getListUnfilteredPersons(allPersons).size());

        // Filtering out males AND females
        model.setShowMale(false);
        assertEquals(0, model.getListUnfilteredPersons(allPersons).size());
    }

    @Test
    public void getListUnfilteredPersonsSides() {
        // Filtering out just father's side
        setAllPersonFiltersToTrue();
        model.setShowFathersSide(false);
        assert(model.getListUnfilteredPersons(allPersons).contains(ginnyWeasley));
        assert(model.getListUnfilteredPersons(allPersons).contains(arthurWeasley));
        assert(model.getListUnfilteredPersons(allPersons).contains(mollyWeasley));
        assert(model.getListUnfilteredPersons(allPersons).contains(albusPotter));
        assertEquals(4, model.getListUnfilteredPersons(allPersons).size());

        // Filtering out just mother's side
        setAllPersonFiltersToTrue();
        model.setShowMothersSide(false);
        assert(model.getListUnfilteredPersons(allPersons).contains(jamesPotter));
        assert(model.getListUnfilteredPersons(allPersons).contains(lilyPotter));
        assert(model.getListUnfilteredPersons(allPersons).contains(harryPotter));
        assert(model.getListUnfilteredPersons(allPersons).contains(albusPotter));
        assertEquals(4, model.getListUnfilteredPersons(allPersons).size());

        // Filtering out father's side AND mother's side
        model.setShowFathersSide(false);
        assert(model.getListUnfilteredPersons(allPersons).contains(albusPotter));
        assertEquals(1, model.getListUnfilteredPersons(allPersons).size());
    }

    @Test
    public void getListUnfilteredPersonsGenderSideMixes() {
        // Filtering out both sides of the family and females
        model.setShowFathersSide(false);
        model.setShowMothersSide(false);
        model.setShowFemale(false);
        assert(model.getListUnfilteredPersons(allPersons).contains(albusPotter));
        assertEquals(1, model.getListUnfilteredPersons(allPersons).size());

        // Filtering out both sides of the family and males
        model.setShowFemale(true);
        model.setShowMale(false);
        assertEquals(0, model.getListUnfilteredPersons(allPersons).size());

        // All filters on
        model.setShowMale(false);
        model.setShowFemale(false);
        assertEquals(0, model.getListUnfilteredPersons(allPersons).size());

        // If both male and female filters are on the other two filters should have no effect
        setAllPersonFiltersToTrue();
        model.setShowFemale(false);
        model.setShowMale(false);

        model.setShowFathersSide(false);
        assertEquals(0, model.getListUnfilteredPersons(allPersons).size());

        model.setShowFathersSide(true);
        model.setShowMothersSide(false);
        assertEquals(0, model.getListUnfilteredPersons(allPersons).size());

        // Just show males and mother's side
        setAllPersonFiltersToTrue();
        model.setShowFemale(false);
        model.setShowFathersSide(false);
        assert(model.getListUnfilteredPersons(allPersons).contains(arthurWeasley));
        assert(model.getListUnfilteredPersons(allPersons).contains(albusPotter));
        assertEquals(2, model.getListUnfilteredPersons(allPersons).size());

        // Just show males and father's side
        model.setShowMothersSide(false);
        model.setShowFathersSide(true);
        assert(model.getListUnfilteredPersons(allPersons).contains(jamesPotter));
        assert(model.getListUnfilteredPersons(allPersons).contains(harryPotter));
        assert(model.getListUnfilteredPersons(allPersons).contains(albusPotter));
        assertEquals(3, model.getListUnfilteredPersons(allPersons).size());

        // Just show females and mother's side
        setAllPersonFiltersToTrue();
        model.setShowMale(false);
        model.setShowFathersSide(false);
        assert(model.getListUnfilteredPersons(allPersons).contains(ginnyWeasley));
        assert(model.getListUnfilteredPersons(allPersons).contains(mollyWeasley));
        assertEquals(2, model.getListUnfilteredPersons(allPersons).size());

        // Just show females and father's side
        model.setShowMothersSide(false);
        model.setShowFathersSide(true);
        assert(model.getListUnfilteredPersons(allPersons).contains(lilyPotter));
        assertEquals(1, model.getListUnfilteredPersons(allPersons).size());
    }

    @Test
    public void getListUnfilteredEvents() {
        List<Event> unfilteredEvents;

        // If we don't filter_icon out any event types then all six events for Harry remain
        // unfiltered
        assertEquals(6, model.getListUnfilteredEvents(Arrays.asList(harryPotter)).size());

        // If we filter_icon out Played Quidditch events we are left with five remaining
        // unfiltered events
        model.addFilteredEventType("played quidditch");
        unfilteredEvents = model.getListUnfilteredEvents(Arrays.asList(harryPotter));
        assertEquals(5, unfilteredEvents.size());
        assertFalse(unfilteredEvents.contains(harryEvent5));

        // If we further filter_icon out Ate Chocolate Frog events too there should
        // be 4 unfiltered events remaining
        model.addFilteredEventType("ate chocolate frog");
        unfilteredEvents = model.getListUnfilteredEvents(Arrays.asList(harryPotter));
        assertEquals(4, unfilteredEvents.size());
        assertFalse(unfilteredEvents.contains(harryEvent5));
        assertFalse(unfilteredEvents.contains(harryEvent3));

        // If we filter_icon all the event types no events remain unfiltered
        model.addFilteredEventType("birth");
        model.addFilteredEventType("drank pumpkin juice");
        model.addFilteredEventType("defeated voldemort");
        model.addFilteredEventType("death");
        assertEquals(0, model.getListUnfilteredEvents(Arrays.asList(harryPotter)).size());
    }

    // TESTING SEARCHING FOR PEOPLE AND EVENTS

    @Test
    public void getPersonsInSearch() {
        List<Person> searchPersons;

        // If we don't have anything in the search bar, we expect all the people to show up
        assertEquals(7, model.getPersonsInSearch(allPersons, "").size());

        // If we type "Potter" in the search bar, we expect 4 people to show up
        searchPersons = model.getPersonsInSearch(allPersons, "potter");
        assertTrue(searchPersons.contains(harryPotter));
        assertTrue(searchPersons.contains(lilyPotter));
        assertTrue(searchPersons.contains(jamesPotter));
        assertTrue(searchPersons.contains(albusPotter));
        assertEquals(4, searchPersons.size());

        // If we type "a" in the search bar, we expect 6 people to show up
        // Note that this includes first and last name cases
        searchPersons = model.getPersonsInSearch(allPersons, "a");
        assertEquals(6, searchPersons.size());
        assertFalse(searchPersons.contains(lilyPotter));

        // If we type "popsicle" in the search bar, we expect 0 people to show up
        searchPersons = model.getPersonsInSearch(allPersons, "popsicle");
        assertEquals(0, searchPersons.size());
    }

    @Test
    public void getEventsInSearch() {
        List<Event> searchEvents;

        // If we don't have anything in the search bar, we expect all the events to show up
        assertEquals(6, model.getEventsInSearch(harryEvents, "").size());

        // If we have "1994" in the search bar, we expect 3 events to show up
        searchEvents = model.getEventsInSearch(harryEvents, "1994");
        assertEquals(3, searchEvents.size());
        assertTrue(searchEvents.contains(harryEvent3));
        assertTrue(searchEvents.contains(harryEvent4));
        assertTrue(searchEvents.contains(harryEvent5));

        // If we have "England" in the search bar, everything should show up
        assertEquals(6, model.getEventsInSearch(harryEvents, "england").size());

        // If we have "London" in the search bar, everything should show up
        assertEquals(6, model.getEventsInSearch(harryEvents, "london").size());

        // If we have "chocolate" in the search bar, one event should show up
        searchEvents = model.getEventsInSearch(harryEvents, "chocolate");
        assertEquals(1, searchEvents.size());
        assertTrue(searchEvents.contains(harryEvent3));
    }
}