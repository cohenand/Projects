package request;

/**
 * A class used as an argument for the PersonLookupService class
 */

public class PersonLookupRequest {

    private final String personID;

    /**
     * The constructor for the PersonLookupRequest that stores the ID of the person to be searched
     * @param personID
     */
    public PersonLookupRequest(String personID) {
        this.personID = personID;
    }


    public String getPersonID() {
        return personID;
    }
}
