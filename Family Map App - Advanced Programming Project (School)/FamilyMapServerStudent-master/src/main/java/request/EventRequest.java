package request;

public class EventRequest {

    private String authToken;

    public EventRequest(String authToken) {
        this.authToken = authToken;
    }


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


}
