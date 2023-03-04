package result;

import model.Person;


/**
 * A class used as a return type for the PersonService class
 */
public class PersonResult {

    private Person[] data;
    private final boolean success;
    private String message;

    /**
     * The Constructor for the PersonResult class that stores the status and output data of the operation
     * @param data
     */

    public PersonResult(Person[] data, boolean success) {
        if(success == false) {
            this.success = success;
            message = "Error: The data extraction failed";
        }
        else {
            this.success = success;
            this.data = data;
        }
    }

    public Person[] getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
