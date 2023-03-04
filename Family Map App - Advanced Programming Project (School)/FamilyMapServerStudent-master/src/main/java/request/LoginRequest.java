package request;

/**
 * A class used as an argument for the LoginService class
 */

public class LoginRequest {

    private final String username;
    private final String password;

    /**
     * The Constructor for the LoginRequest class that stores the login username and password
     * @param username
     * @param password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
