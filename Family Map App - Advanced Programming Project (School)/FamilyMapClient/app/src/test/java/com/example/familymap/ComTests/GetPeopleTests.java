package com.example.familymap.ComTests;


import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

import com.google.gson.Gson;

import request.RegisterRequest;
import result.PersonResult;
import result.RegisterResult;

import com.example.familymap.ServerCom.HttpClient;

public class GetPeopleTests {

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
    public void testGoodPeopleRetrieval() {
        String personOutput1 = null;
        String authToken1 = registerResult1.getAuthtoken();
        personOutput1 = httpClient.getURL(host, port, HttpClient.PERSON_EXTENSION, authToken1);
        PersonResult personResult1 = gson.fromJson(personOutput1, PersonResult.class);
        assertNotNull(personResult1);
        assertEquals(31,personResult1.getData().length);
        assertEquals(registerResult1.getUsername(), personResult1.getData()[0].getAssociatedUsername());

    }

    @Test
    public void testNotBadPeopleRetrieval() {
        String personOutput1 = null;
        String personOutput2 = null;
        String authToken1 = registerResult1.getAuthtoken();
        String authToken2 = registerResult2.getAuthtoken();
        assertNotEquals(authToken1,authToken2);

        personOutput1 = httpClient.getURL(host, port, HttpClient.PERSON_EXTENSION, authToken1);
        PersonResult personResult1 = gson.fromJson(personOutput1, PersonResult.class);
        personOutput2 = httpClient.getURL(host, port, HttpClient.PERSON_EXTENSION, authToken2);
        PersonResult personResult2 = gson.fromJson(personOutput2, PersonResult.class);

        assertNotEquals(personResult1.getData()[0].getAssociatedUsername(),personResult2.getData()[0].getAssociatedUsername());
        assertNotEquals(personResult1.getData()[0].getPersonID(),personResult2.getData()[0].getPersonID());
        assertNotEquals(personResult1.getData()[3].getPersonID(),personResult2.getData()[3].getPersonID());


    }


}
