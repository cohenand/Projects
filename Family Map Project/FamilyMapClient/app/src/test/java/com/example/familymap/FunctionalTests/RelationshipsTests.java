package com.example.familymap.FunctionalTests;

import com.example.familymap.DataContainers.FamilyMemberNode;
import com.example.familymap.DataContainers.FamilyTree;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class RelationshipsTests {
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
    }





    @Test
    public void correctRelationshipTest() {

        familyTree = FamilyTree.load_Tree(user,events,people,authToken1);
        assertNotNull(familyTree);
        FamilyMemberNode mother = familyTree.root.mother;
        assertEquals(user.getPersonID(),mother.childrenIDs[0]);
        assertEquals(mother.spouse,familyTree.root.father.person.getPersonID());
        assertEquals(familyTree.root.father.spouse,familyTree.root.mother.person.getPersonID());
        familyTree = null;
    }

    @Test
    public void noDirectRelationshipTest() {


        familyTree = FamilyTree.load_Tree(user,events,people,authToken1);
        assertNotNull(familyTree);
        FamilyMemberNode maternalGrandmother = familyTree.root.mother.mother;
        FamilyMemberNode paternalGrandmother = familyTree.root.father.mother;
        assertNotEquals(maternalGrandmother.person.getPersonID(), paternalGrandmother.person.getPersonID());
        assertNotEquals(maternalGrandmother.childrenIDs[0], paternalGrandmother.childrenIDs[0]);
        assertNotEquals(maternalGrandmother.spouse,paternalGrandmother.spouse);
        assertEquals(maternalGrandmother.person.getAssociatedUsername(),paternalGrandmother.person.getAssociatedUsername());
        familyTree = null;
    }
}
