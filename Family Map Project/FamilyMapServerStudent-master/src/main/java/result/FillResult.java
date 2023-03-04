package result;

/**
 * A class used as a return type for the FillService class
 */

public class FillResult {

    private final String message;
    private final boolean success;



    /**
     * The Constructor for the FillResult class that stores the status of the operation
     * @param success
     */
    public FillResult(boolean success, int gen) {
        this.success = success;
        if (success == false) {
            this.message = "Error: Filling operation failed";
        }
        else {
            int numP = (int) (Math.pow(2,(gen+1)) - 1);
            int numE = 3*(numP-1);
            this.message = "Successfully added "+ numP +" persons and " + numE + " events to the database.";
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
