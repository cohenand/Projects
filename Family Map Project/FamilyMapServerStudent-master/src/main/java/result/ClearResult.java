package result;


/**
 * A class used as a return type for the ClearService class
 */

public class ClearResult {

    private final boolean success;
    private final String message;

    /**
     * The Constructor for the ClearResult class that stores the status of the operation
     * @param success
     */
    public ClearResult(boolean success) {
        this.success = success;
        if (success == false) {
            message = "Error: [Description]";
        }
        else {
            message = "Clear succeeded.";
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
