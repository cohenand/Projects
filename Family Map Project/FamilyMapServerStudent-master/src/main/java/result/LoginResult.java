package result;


/**
 * A class used as a return type for the LoginService class
 */


public class LoginResult {

    private String authtoken;
    private final String username;
    private final String personID;
    private final boolean success;
    private String message;


    /**
     * The Constructor for the LoginResult class that stores the status and output data of the operation
     * @param authtoken
     * @param username
     * @param personID
     */
    public LoginResult(String authtoken, String username, String personID) {
        if (authtoken == null) {
            this.success = false;
            this.message = "Error: Unable to login with provided username and password";
            this.authtoken = null;
            this.username = null;
            this.personID = null;
        }
        else {
            this.success = true;
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

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}
