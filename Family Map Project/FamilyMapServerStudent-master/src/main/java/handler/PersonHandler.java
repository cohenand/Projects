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
import request.EventRequest;
import request.PersonRequest;
import result.EventResult;
import result.PersonResult;
import service.EventService;
import service.PersonService;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class PersonHandler implements HttpHandler {

    Gson gson = new Gson();
    String json;
    AuthtokenDao authtokenDao;
    Database db;


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        PersonResult result = new PersonResult(null, false);


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
                        PersonRequest request;


                        request = new PersonRequest(authToken);


                        PersonService service = new PersonService();
                        result = service.person(request);
                        if (result.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            OutputStream resBody = exchange.getResponseBody();
                            json = gson.toJson(result);
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
