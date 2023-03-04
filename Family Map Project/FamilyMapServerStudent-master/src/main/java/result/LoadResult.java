package result;


/**
 * A class used as a return type for the LoadService class
 */

public class LoadResult {

    private final boolean success;
    private final String message;

    /**
     * The Constructor for the LoadResult class that stores the status of the operation
     * @param success
     */
    public LoadResult(boolean success, int ucount, int pcount, int ecount) {
        this.success = success;
        if (success == false) {
            message = "Something";
        }
        else {
            /// Change X,Y,Z
            message = "Successfully added " + ucount + " users, " + pcount + " persons, and " + ecount + " events to the database.";
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
