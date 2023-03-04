package request;


/**
 * A class used as an argument for the EventLookupService class
 */
public class EventLookupRequest {


    /**
     * The event ID to be looked up
     */
    private final String eventID;

    /**
     * The constructor for the EventLookupRequest class
     * @param eventID
     */
    public EventLookupRequest(String eventID) {
        this.eventID = eventID;
    }


    public String getEventID() {
        return eventID;
    }


}
