package results;

/** Holds information about the result of a request to the Person API with a specific ID */
public class PersonAndIDResult extends Result {

    private String descendant;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;

    /** Constructs a new PersonAndIDResult object with just a message
     *
     * @param message A message (practically an error message)
     */
    public PersonAndIDResult(String message) {
        super(message);
    }

    /** Constructs a new PersonAndIDResult object
     *
     * @param personID Unique identifier for this person (non-empty string)
     * @param descendant User (Username) to which this person belongs
     * @param firstName Person’s first name (non-empty string)
     * @param lastName Person’s last name (non-empty string)
     * @param gender Person’s gender (string: “f” or “m”)
     * @param father ID of person’s father (possibly null)
     * @param mother ID of person’s mother (possibly null)
     * @param spouse ID of person’s spouse (possibly null)
     */
    public PersonAndIDResult(String personID, String descendant, String firstName, String lastName,
                             String gender, String father, String mother, String spouse) {
        super(null);
        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFather() {
        return father;
    }

    public String getMother() {
        return mother;
    }

    public String getSpouse() {
        return spouse;
    }
}
