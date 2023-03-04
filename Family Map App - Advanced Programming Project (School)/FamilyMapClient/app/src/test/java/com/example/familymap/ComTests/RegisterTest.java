package com.example.familymap.ComTests;


import static org.junit.Assert.*;

import com.google.gson.Gson;

import request.RegisterRequest;
import result.RegisterResult;

import com.example.familymap.ServerCom.HttpClient;

import org.junit.Before;
import org.junit.Test;

public class RegisterTest {

    @Test @Before
    public void RegistrationTest() {
        String host="192.168.1.2";
        String port="8080";
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
        RegisterResult registerResult = gson.fromJson(output, RegisterResult.class);
        assertTrue(registerResult.isSuccess());
        assertNotNull(registerResult.getAuthtoken());
        assertEquals(username,registerResult.getUsername());

    }

    @Test
    public void validRegistration() {
        Gson gson = new Gson();
        HttpClient httpClient = new HttpClient();
        String host="192.168.1.2";
        String port="8080";
        String username="cohenand";
        String password="andrew_haha";
        String email = "fake@gmail.com";
        String firstname = "Andrew";
        String lastname = "Cohen";
        String gender = "m";

        RegisterRequest registerRequest = new RegisterRequest(username, password,email,firstname,lastname,gender);
        String json = gson.toJson(registerRequest);
        String output = httpClient.postURL(host,port,HttpClient.REGISTER_EXTENSION,json);
        RegisterResult registerResult = gson.fromJson(output, RegisterResult.class);

    }

    @Test
    public void invalidRegistration() {
        Gson gson = new Gson();
        HttpClient httpClient = new HttpClient();
        String host="192.168.1.2";
        String port="8080";
        String username="sheila";
        String password="parker";
        String email = "fakeiii@gmail.com";
        String firstname = "Sheila";
        String lastname = "Parker";
        String gender = "f";
        RegisterRequest registerRequest = new RegisterRequest(username, password,email,firstname,lastname,gender);
        String json = gson.toJson(registerRequest);
        String output = httpClient.postURL(host,port,HttpClient.REGISTER_EXTENSION,json);
        RegisterResult registerResult = gson.fromJson(output, RegisterResult.class);
        assertNull(registerResult);
    }
}
