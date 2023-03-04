package result;

import model.Event;

/**
 * A class used as a return type for the EventLookupService class
 */
public class EventLookupResult {


    private String associatedUsername;
    private String eventID;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;
    private final boolean success;
    private String message;

    /**
     * The Constructor for the EventLookupResult class that stores the status and output data of the operation
     * @param event
     */


    public EventLookupResult(Event event) {
        if (event == null) {
            this.success = false;
            message = "Error: The event could not be found.";
        }
        else {
            this.success = true;
            this.associatedUsername = event.getAssociatedUsername();
            this.eventID = event.getEventID();
            this.personID = event.getPersonID();
            this.latitude = event.getLatitude();
            this.longitude = event.getLongitude();
            this.country = event.getCountry();
            this.city = event.getCity();
            this.eventType = event.getEventType();
            this.year = event.getYear();
        }
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
