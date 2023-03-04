package model;


/**
 * A class used to store data regarding Authentication tokens
 */

public class Authtoken {

    /**
     * the token assigned for authentication
     */
    private String authtoken;

    /**
     * the username associated with the authentication token
     */
    private String username;

    /**
     * The constructor for the Authtoken class
     * @param authtoken
     * @param username
     */
    public Authtoken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
