package com.example.familymap.FunctionalTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


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

public class ChronoOrderTests {
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

        familyTree.root.events = new Event[4];
    }

    @Test
    public void easySortTest() {
        Event[] output;
        output = familyTree.root.mother.getEventsInOrder();
        assertTrue(output[0].getYear() < output[1].getYear());
        assertTrue(output[0].getEventType().equals("birth"));
        assertTrue(output[2].getEventType().equals("death"));
    }
    @Test
    public void mediumSortTest() {
        Event[] output;
        familyTree.root.events[0] = new Event("asdf123124","me","fake",(float)123.3123,(float)16.2312,"No","ok","dancing",2021);
        familyTree.root.events[1] = new Event("rqwqt34","me","fake",(float)123.3123,(float)16.2312,"No","ok","death",2070);
        familyTree.root.events[2] = new Event("gaga3r4124","me","fake",(float)123.3123,(float)16.2312,"No","ok","birth",1998);
        familyTree.root.events[3] = new Event("a123as124","me","fake",(float)123.3123,(float)16.2312,"No","ok","graduation",2024);
        output = familyTree.root.getEventsInOrder();
        assertTrue(output[0].getYear() < output[1].getYear());
        assertTrue(output[1].getYear() < output[2].getYear());
        assertTrue(output[2].getYear() < output[3].getYear());
        assertTrue(output[0].getEventType().equals("birth"));
        assertTrue(output[3].getEventType().equals("death"));
    }

    @Test
    public void hardSortTest() {
        Event[] output;
        familyTree.root.events[0] = new Event("123124","me","fake",(float)123.3123,(float)16.2312,"No","ok","grandchilds graduation",2031);
        familyTree.root.events[1] = new Event("rqwqt34","me","fake",(float)123.3123,(float)16.2312,"No","ok","death",2030);
        familyTree.root.events[2] = new Event("3r4124","me","fake",(float)123.3123,(float)16.2312,"No","ok","birth",1998);
        familyTree.root.events[3] = new Event("a123as124","me","fake",(float)123.3123,(float)16.2312,"No","ok","parents marriage",1997);
        output = familyTree.root.getEventsInOrder();
        assertTrue(output[0].getYear() > output[1].getYear());
        assertTrue(output[1].getYear() < output[2].getYear());
        assertTrue(output[2].getYear() > output[3].getYear());
        assertTrue(output[0].getEventType().equals("birth"));
        assertTrue(output[3].getEventType().equals("death"));
    }


}
