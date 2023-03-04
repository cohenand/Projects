package com.example.familymap.ComTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.example.familymap.ServerCom.HttpClient;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import request.RegisterRequest;
import result.EventResult;
import result.RegisterResult;

public class GetEventsTests {
    private RegisterResult registerResult1;
    private RegisterResult registerResult2;
    private HttpClient httpClient = new HttpClient();
    private Gson gson = new Gson();
    String host="192.168.1.2";
    String port="8080";

    @Before
    public void RegistrationSetup() {
        host="192.168.1.2";
        port="8080";
        Gson gson = new Gson();
        HttpClient httpClient = new HttpClient();
        String output = httpClient.postURL(host,port,HttpClient.CLEAR_EXTENSION,"");

        String username="sheila";
        String password="parker";
        String email = "fakeyikes@gmail.com";
        String firstname = "Sheila";
        String lastname = "Parker";
        String gender = "f";
        RegisterRequest registerRequest = new RegisterRequest(username, password,email,firstname,lastname,gender);
        String json = gson.toJson(registerRequest);
        output = httpClient.postURL(host,port,HttpClient.REGISTER_EXTENSION,json);
        registerResult1 = gson.fromJson(output, RegisterResult.class);

        host="192.168.1.2";
        port="8080";
        username="cohenand";
        password="andrew_haha";
        email = "fake@gmail.com";
        firstname = "Andrew";
        lastname = "Cohen";
        gender = "m";

        registerRequest = new RegisterRequest(username, password,email,firstname,lastname,gender);
        json = gson.toJson(registerRequest);
        output = httpClient.postURL(host,port,HttpClient.REGISTER_EXTENSION,json);
        registerResult2 = gson.fromJson(output, RegisterResult.class);

    }

    @Test
    public void testGoodEventsRetrieval() {
        String eventOutput1 = null;
        String authToken1 = registerResult1.getAuthtoken();
        eventOutput1 = httpClient.getURL(host, port, HttpClient.EVENT_EXTENSION, authToken1);
        EventResult eventResult1 = gson.fromJson(eventOutput1, EventResult.class);
        assertNotNull(eventResult1);
        assertEquals(91,eventResult1.getData().length);
        assertEquals(registerResult1.getUsername(), eventResult1.getData()[0].getAssociatedUsername());

    }

    @Test
    public void testNotBadEventsRetrieval() {
        String eventOutput1 = null;
        String eventOutput2 = null;
        String authToken1 = registerResult1.getAuthtoken();
        String authToken2 = registerResult2.getAuthtoken();
        assertNotEquals(authToken1,authToken2);

        eventOutput1 = httpClient.getURL(host, port, HttpClient.EVENT_EXTENSION, authToken1);
        EventResult eventResult1 = gson.fromJson(eventOutput1, EventResult.class);
        eventOutput2 = httpClient.getURL(host, port, HttpClient.EVENT_EXTENSION, authToken2);
        EventResult eventResult2 = gson.fromJson(eventOutput2, EventResult.class);

        assertNotEquals(eventResult1.getData()[0].getAssociatedUsername(),eventResult2.getData()[0].getAssociatedUsername());
        assertNotEquals(eventResult1.getData()[0].getEventID(),eventResult2.getData()[0].getEventID());
        assertNotEquals(eventResult1.getData()[6].getEventID(),eventResult2.getData()[6].getEventID());


    }



}
