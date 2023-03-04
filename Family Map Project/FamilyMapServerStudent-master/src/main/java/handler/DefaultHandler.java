package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.ClearRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class DefaultHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;


        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                String urlPath = exchange.getRequestURI().toString();
                String filePath;
                if (urlPath.equals("/")||urlPath.equals(null)) {
                    urlPath = "/index.html";
                }

                filePath = "web" + urlPath;

                File file = new File(filePath);
                if(!file.exists()) {
                    OutputStream respBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND,0);
                    file = new File("web/HTML/404.html");
                    Files.copy(file.toPath(),respBody);
                    exchange.getResponseBody().close();
                    success = true;
                }
                else {
                    OutputStream respBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                    Files.copy(file.toPath(),respBody);
                    respBody.close();
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
