package results;

/** Generic result class - holds a message about the result of an API service */
public class Result {

    private final String message;

    /** Constructs a new Result object
     *
     * @param message A message String
     */
    public Result(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
