package handler;

import coder.Decoder;
import coder.Encoder;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.Database;
import request.ClearRequest;
import request.FillRequest;
import result.ClearResult;
import result.FillResult;
import service.ClearService;
import service.FillService;

import java.io.*;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {


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
                FillRequest request;


                String urlPath = exchange.getRequestURI().toString();
                String [] args = urlPath.split("/");
                if (args.length == 4) {
                    request = new FillRequest(args[2],Integer.parseInt(args[3]));
                }
                else {
                    request = new FillRequest(args[2]);
                }





                FillService service = new FillService();
                FillResult result = service.fill(request);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                OutputStream resBody = exchange.getResponseBody();
                String json = gson.toJson(result);
                Encoder.writeString(json,resBody);
                resBody.close();

                success = true;

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
