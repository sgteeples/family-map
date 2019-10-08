package requests;

/** Contains information relevant to user request for filling in database information */
public class FillRequest {

    private final String username;
    private final int generations;

    /** Constructs a FillRequest object
     *
     * @param username Username of the user to fill information for
     * @param generations Number of generations of information to fill
     */
    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    public String getUsername() {
        return username;
    }

    public int getGenerations() {
        return generations;
    }

}
