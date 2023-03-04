package request;


/**
 * A class used as an argument for the RegisterService class
 */

public class RegisterRequest {

    private final String username;
    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String gender;


    /**
     * The Constructor for the RegisterRequest class that stores and passes on the registration info of the new user
     * @param username
     * @param password
     * @param email
     * @param firstName
     * @param lastName
     * @param gender
     */

    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}
