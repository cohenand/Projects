package result;

import model.Event;


/**
 * A class used as a return type for the EventService class
 */

public class EventResult {

    private Event[] data;
    private final boolean success;
    private String message;


    /**
     * The Constructor for the EventResult class that stores the status and output data of the operation
     * @param data
     */
    public EventResult(Event[] data,boolean success) {
        if(success == false) {
            this.success = false;
            message = "Error: [Description]";
        }
        else {
            this.success = true;
            this.data = data;
        }


    }


    public Event[] getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
