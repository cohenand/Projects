package request;
/**
 * A class used as an argument for the FillService class
 */
public class FillRequest {

    private final String username;
    private final int generations;

    /**
     * A constructor for the FillRequest class that allows the number of generations to be defined explicitly
     * @param username
     * @param generations
     */
    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    /**
     * A constructor for the FillRequest class that does not require a number of generations
     * @param username
     */
    public FillRequest(String username) {
        this.username = username;
        this.generations = 4;
    }

    public String getUsername() {
        return username;
    }

    public int getGenerations() {
        return generations;
    }
}
