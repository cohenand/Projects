package handler;

import coder.Decoder;
import coder.Encoder;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.AuthtokenDao;
import dao.DataAccessException;
import dao.Database;
import model.Authtoken;
import request.ClearRequest;
import request.EventLookupRequest;
import request.FillRequest;
import request.LoadRequest;
import result.EventLookupResult;
import result.LoadResult;
import result.PersonLookupResult;
import service.EventLookupService;
import service.LoadService;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class EventLookupHandler implements HttpHandler {

    Gson gson;
    AuthtokenDao authtokenDao;
    Database db;
    String json;

    OutputStream resBody;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        gson = new Gson();

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();

                db = new Database();
                Connection conn = db.getConnection();
                authtokenDao = new AuthtokenDao(conn);
                if(reqHeaders.containsKey("Authorization")) {

                    String authToken = reqHeaders.getFirst("Authorization");
                    Authtoken AT = authtokenDao.find_token(authToken);
                    if (AT != null) {

                        InputStream reqBody = exchange.getRequestBody();
                        String reqData = Decoder.readString(reqBody);

                        EventLookupRequest request;


                        String urlPath = exchange.getRequestURI().toString();
                        String[] args = urlPath.split("/");

                        request = new EventLookupRequest(args[2]);


                        EventLookupService service = new EventLookupService();
                        EventLookupResult result = service.event_lookup(request);

                        json = gson.toJson(result);


                        if (!result.isSuccess() || !result.getAssociatedUsername().equals(AT.getUsername())) {

                        }
                        else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            resBody = exchange.getResponseBody();
                            Encoder.writeString(json, resBody);
                            resBody.close();
                            success = true;
                        }
                    }
                }
                db.closeConnection(false);
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                resBody = exchange.getResponseBody();
                EventLookupResult result = new EventLookupResult(null);
                json = gson.toJson(result);
                Encoder.writeString(json, resBody);
                resBody.close();
            }
        }
        catch (IOException | DataAccessException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR,0);
            exchange.getResponseBody().close();
            e.printStackTrace();

        }


    }



}
