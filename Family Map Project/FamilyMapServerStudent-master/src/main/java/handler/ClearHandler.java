package handler;

import coder.Decoder;
import coder.Encoder;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.ClearRequest;
import dao.*;
import request.LoginRequest;
import result.ClearResult;
import result.LoginResult;
import service.ClearService;
import service.LoginService;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class ClearHandler  implements HttpHandler {



    Gson gson;





    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;


        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                Headers reqHeaders = exchange.getRequestHeaders();



                InputStream reqBody = exchange.getRequestBody();
                String reqData = Decoder.readString(reqBody);
                gson = new Gson();
                ClearRequest request = gson.fromJson(reqData, ClearRequest.class);

                ClearService service = new ClearService();
                ClearResult result = service.clear();

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                OutputStream resBody = exchange.getResponseBody();
                String json = gson.toJson(result);
                Encoder.writeString(json,resBody);
                resBody.close();
                if (result.isSuccess()) {
                    success = true;
                }

            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR,0);
            exchange.getResponseBody().close();
            e.printStackTrace();

        }


    }

}
