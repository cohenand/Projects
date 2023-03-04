package com.example.familymap.ComTests;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.google.gson.Gson;

import request.LoginRequest;
import result.LoginResult;
import com.example.familymap.ServerCom.HttpClient;

public class LoginTest {

    @Before
    public void setUp() {

    }

    @Test
    public void validLogin() {
        Gson gson = new Gson();
        HttpClient httpClient = new HttpClient();
        String host="192.168.1.2";
        String port="8080";
        String username="sheila";
        String password="parker";
        LoginRequest loginRequest = new LoginRequest(username, password);
        String json = gson.toJson(loginRequest);
        String output = httpClient.postURL(host,port,HttpClient.LOGIN_EXTENSION,json);
        LoginResult loginResult = gson.fromJson(output, LoginResult.class);
        assertTrue(loginResult.isSuccess());
        assertNotNull(loginResult.getAuthtoken());
        assertEquals(username,loginResult.getUsername());
    }

    @Test
    public void invalidLogin() {
        Gson gson = new Gson();
        HttpClient httpClient = new HttpClient();
        String host="192.168.1.2";
        String port="8080";
        String username="andrew";
        String password="cohen";
        LoginRequest loginRequest = new LoginRequest(username, password);
        String json = gson.toJson(loginRequest);
        String output = httpClient.postURL(host,port,HttpClient.LOGIN_EXTENSION,json);
        LoginResult loginResult = gson.fromJson(output, LoginResult.class);
        assertNull(loginResult);
    }
}
