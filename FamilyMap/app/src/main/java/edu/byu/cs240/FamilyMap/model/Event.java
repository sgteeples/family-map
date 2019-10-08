package edu.byu.cs240.FamilyMap.model;

public class Event {

    private final String eventID;
    private final String descendant;
    private final String personID;
    private final Double latitude;
    private final Double longitude;
    private final String country;
    private final String city;
    private final String eventType;
    private Integer year;

    public Event(String eventID, String descendant, String personID, double latitude,
                 double longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.descendant = descendant;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    private String getEventID() {
        return eventID;
    }

    private String getDescendant() {
        return descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            return oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getDescendant().equals(getDescendant()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
                    oEvent.getLatitude().equals(getLatitude()) &&
                    oEvent.getLongitude().equals(getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear().equals(getYear());
        }
        return false;
    }
}