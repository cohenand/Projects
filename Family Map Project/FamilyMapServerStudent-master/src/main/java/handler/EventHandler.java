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
import request.ClearRequest;
import request.EventLookupRequest;
import request.EventRequest;
import result.EventLookupResult;
import result.EventResult;
import service.EventLookupService;
import service.EventService;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class EventHandler implements HttpHandler {


    Gson gson = new Gson();
    String json;
    AuthtokenDao authtokenDao;
    Database db;


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        EventResult result = new EventResult(null,false);

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                db = new Database();
                Connection conn = db.getConnection();
                authtokenDao = new AuthtokenDao(conn);
                if(reqHeaders.containsKey("Authorization")) {

                    String authToken = reqHeaders.getFirst("Authorization");

                    if (authtokenDao.find_token(authToken) != null) {

                        InputStream reqBody = exchange.getRequestBody();
                        String reqData = Decoder.readString(reqBody);
                        gson = new Gson();
                        EventRequest request;


                        request = new EventRequest(authToken);


                        EventService service = new EventService();
                        result = service.event(request);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream resBody = exchange.getResponseBody();
                        json = gson.toJson(result);
                        Encoder.writeString(json, resBody);
                        resBody.close();

                        success = true;
                    }
                }
                db.closeConnection(false);
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                OutputStream resBody = exchange.getResponseBody();
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
