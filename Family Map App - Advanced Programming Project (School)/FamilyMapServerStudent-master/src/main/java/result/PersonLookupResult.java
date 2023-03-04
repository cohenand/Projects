package result;

import model.Person;


/**
 * A class used as a return type for the PersonLookupService class
 */
public class PersonLookupResult {

    private String associatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private final boolean success;
    private String message;


    /**
     * The Constructor for the PersonLookupResult class that stores the status and output data of the operation
     * @param person
     */
    public PersonLookupResult(Person person) {
        if (person == null) {
            this.success = false;
            message = "Error: The person was not found";
        }
        else {
            this.success = true;
            this.associatedUsername = person.getAssociatedUsername();
            this.personID = person.getPersonID();
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
            this.gender = person.getGender();
            this.fatherID = person.getFatherID();
            this.motherID = person.getMotherID();
            this.spouseID = person.getSpouseID();

        }
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
