package handler;

import coder.Decoder;
import coder.Encoder;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.*;
import model.Authtoken;
import request.ClearRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import service.LoginService;
import service.RegisterService;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class RegisterHandler implements HttpHandler {

    Gson gson;
    String json;


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        OutputStream resBody;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                Headers reqHeaders = exchange.getRequestHeaders();


                InputStream reqBody = exchange.getRequestBody();
                String reqData = Decoder.readString(reqBody);
                gson = new Gson();
                RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);

                RegisterService service = new RegisterService();
                RegisterResult result = service.register(request);


                resBody = exchange.getResponseBody();
                json = gson.toJson(result);

                if (result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                    Encoder.writeString(json,resBody);
                    resBody.close();
                    success = true;
                }

            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                resBody = exchange.getResponseBody();
                Encoder.writeString(json,resBody);
                resBody.close();
            }
        }
        catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR,0);
            exchange.getResponseBody().close();
            e.printStackTrace();

        }


    }




}
