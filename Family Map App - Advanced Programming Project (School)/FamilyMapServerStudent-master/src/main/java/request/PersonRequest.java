package request;

public class PersonRequest {

    private String authToken;

    public PersonRequest(String authToken) {
        this.authToken = authToken;
    }


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
