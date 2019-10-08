package results;
import model.Person;

/** Holds information about the result of a request to the Person API */
public class PersonResult extends Result {

    private Person[] data;

    /** Constructs a new PersonResult object with just a message
     *
     * @param message A message (practically an error message)
     */
    public PersonResult(String message) {
        super(message);
    }

    /** Constructs a new PersonResult object
     *
     * @param userFamilyMembers Array of the people related to the person
     */
    public PersonResult(Person[] userFamilyMembers) {
        super(null);
        this.data = userFamilyMembers;
    }

    public Person[] getData() {
        return data;
    }
}
