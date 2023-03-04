package com.example.familymap.ServerCom;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    private static final String LOG_TAG = "HttpClient";
    public static final String REGISTER_EXTENSION = "/user/register";
    public static final String LOGIN_EXTENSION = "/user/login";
    public static final String CLEAR_EXTENSION = "/clear";
    public static final String FILL_EXTENSION = "/fill";
    public static final String LOAD_EXTENSION = "/load";
    public static final String PERSON_EXTENSION = "/person";
    public static final String EVENT_EXTENSION = "/event";
    public static final String FILE_EXTENSION = "/";



    public String getURL(String host, String port, String extension, String data) {
        try {


            URL url = new URL("http://" + host + ":" + port + extension);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", data);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                StringBuilder sb = new StringBuilder();
                char [] buf = new char[1024];
                int len;
                while ((len = inputStreamReader.read(buf))>0) {
                    sb.append(buf,0,len);
                }
                return sb.toString();

            }

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(),e);
        }
        return null;
    }

    public String postURL(String host, String port, String extension, String data) {
        try {

            URL url = new URL("http://" + host + ":" + port + extension);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
            outputStream.close();
            int resp = connection.getResponseCode();
            

            if (resp == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                StringBuilder sb = new StringBuilder();
                char [] buf = new char[1024];
                int len;
                while ((len = inputStreamReader.read(buf))>0) {
                    sb.append(buf,0,len);
                }
                return sb.toString();
            }


        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(),e);
        }
        return null;
    }

}
