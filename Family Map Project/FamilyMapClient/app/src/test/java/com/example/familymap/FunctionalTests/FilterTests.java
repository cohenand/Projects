package com.example.familymap.FunctionalTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.example.familymap.DataContainers.FamilyTree;
import com.example.familymap.DataContainers.Settings;
import com.example.familymap.ServerCom.HttpClient;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import model.Event;
import model.Person;
import request.RegisterRequest;
import result.EventResult;
import result.PersonResult;
import result.RegisterResult;

public class FilterTests {

    private RegisterResult registerResult1;
    private RegisterResult registerResult2;
    private HttpClient httpClient = new HttpClient();
    private Gson gson = new Gson();
    String host="192.168.1.2";
    String port="8080";
    private FamilyTree familyTree = null;
    Person[] people;
    Event[] events;
    Person user;
    String authToken1;
    Settings settings;

    @Before
    public void setUp(){
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
        String personOutput1 = null;
        authToken1 = registerResult1.getAuthtoken();
        personOutput1 = httpClient.getURL(host, port, HttpClient.PERSON_EXTENSION, authToken1);
        PersonResult personResult1 = gson.fromJson(personOutput1, PersonResult.class);
        people = personResult1.getData();
        String eventOutput1 = null;
        authToken1 = registerResult1.getAuthtoken();
        eventOutput1 = httpClient.getURL(host, port, HttpClient.EVENT_EXTENSION, authToken1);
        EventResult eventResult1 = gson.fromJson(eventOutput1, EventResult.class);
        events = eventResult1.getData();
        user = null;

        for(Person person:people) {
            if(person.getPersonID().equals(registerResult1.getPersonID())) {
                user = person;
                break;
            }
        }
        familyTree = FamilyTree.load_Tree(user,events,people,authToken1);
    }





    @Test
    public void FilterAppliedTest() {
        settings = new Settings(false,false,false,false,false,false,false);
        Event[] output;
        output = familyTree.applySettings(settings);
        assertEquals(0,output.length);

        settings = new Settings(false,false,false,false,false,true,false);
        output = familyTree.applySettings(settings);
        assertEquals(0,output.length);

        settings = new Settings(false,false,false,false,false,false,true);
        output = familyTree.applySettings(settings);
        assertEquals(1,output.length);
        assertEquals("birth",output[0].getEventType());
    }

    @Test
    public void noFilterAppliedTest() {
        Event[] output;
        settings = new Settings(false,false,false,true,true,true,true);
        output = familyTree.applySettings(settings);
        assertEquals(91,output.length);


    }


    @Test
    public void checkGenderTest() {
        Event[] output;
        settings = new Settings(false,false,false,true,true,false,true);
        output = familyTree.applySettings(settings);
        assertEquals(46,output.length);
        boolean male = false;
        for (Event event : output) {
            if(familyTree.get(event.getPersonID()).person.getGender().equals("m")) {
                male = true;
            }
        }
        assertFalse(male);


        settings = new Settings(false,false,false,true,true,true,false);
        output = familyTree.applySettings(settings);
        assertEquals(45,output.length);
        boolean female = false;
        for (Event event : output) {
            if(familyTree.get(event.getPersonID()).person.getGender().equals("f")) {
                female = true;
            }
        }
        assertFalse(female);
    }
}
