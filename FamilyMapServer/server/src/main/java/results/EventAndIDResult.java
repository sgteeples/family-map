package results;

/** Holds information about the result of a request to the Event API with a specific ID */
public class EventAndIDResult extends Result {

    private String descendant;
    private String eventID;
    private String personID;
    private Double latitude;
    private Double longitude;
    private String country;
    private String city;
    private String eventType;
    private Integer year;

    /** Constructs an EventAndIDResult object with just a message
     *
     * @param message A message (practically an error message)
     */
    public EventAndIDResult(String message) {
        super(message);
    }

    /** Constructs an EventAndIDResult object
     *
     * @param eventID Unique identifier for this event (non-empty string)
     * @param descendant User (Username) to which this person belongs
     * @param personID ID of person to which this event belongs
     * @param latitude Latitude of event’s location
     * @param longitude Longitude of event’s location
     * @param country Country in which event occurred
     * @param city City in which event occurred
     * @param eventType Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param year Year in which event occurred
     */
    public EventAndIDResult(String eventID, String descendant, String personID, double latitude,
                            double longitude, String country, String city, String eventType, int year) {
        super(null);
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

    public String getDescendant() {
        return descendant;
    }

    public String getEventID() {
        return eventID;
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
}
