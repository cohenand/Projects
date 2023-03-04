package result;


/**
 * A class used as a return type for the RegisterService class
 */

public class RegisterResult {

    private String authtoken;
    private String username;
    private String personID;
    private final boolean success;
    private String message;

    /**
     * The Constructor for the RegisterResult class that stores the status and output data of the operation
     * @param authtoken
     * @param username
     * @param personID
     */
    public RegisterResult(String authtoken, String username, String personID) {
        if (authtoken == null) {
            success = false;
            message = "Error: Could not register the new user";
        }
        else {
            success = true;
            this.authtoken = authtoken;
            this.username = username;
            this.personID = personID;
        }
    }


    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
