package model;

/** Has the structure of the Person table in the database */
public class Person {

    private final String personID;
    private final String descendant;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private String father;
    private String mother;
    private String spouse;

    /** Constructs a Person object
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
    public Person(String personID, String descendant, String firstName, String lastName,
                  String gender, String father, String mother, String spouse) {
        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public String getPersonID() {
        return personID;
    }

    public String getDescendant() {
        return descendant;
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

    public void setFather(String father) {
        this.father = father;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o instanceof Person) {
            Person oPerson = (Person) o;
            return oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getDescendant().equals(getDescendant()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender()) &&
                    oPerson.getFather().equals(getFather()) &&
                    oPerson.getMother().equals(getMother()) &&
                    oPerson.getSpouse().equals(getSpouse());
        }
        return false;
    }
}