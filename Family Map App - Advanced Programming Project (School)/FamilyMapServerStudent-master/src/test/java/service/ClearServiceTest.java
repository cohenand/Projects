package service;

import dao.AuthtokenDao;
import dao.DataAccessException;
import dao.Database;
import model.Authtoken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import result.ClearResult;
import result.LoadResult;

import javax.xml.crypto.Data;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

    User user;
    Person person;
    Authtoken authtoken;
    Event event;
    LoadRequest loadRequest;

    @BeforeEach
    public void setUp() throws DataAccessException {

        user = new User("cohenand","pass","cohenand@byu.edu","Andrew","Cohen","m","12301230");
        person = new Person("12301230","cohenand","Andrew","Cohen","m");
        authtoken = new Authtoken("12312414","cohenand");
        event = new Event("4343","cohenand","12301230",(float) 22.32,(float) 55.677,"USA","Pullman","birth",1998);

        User [] users = new User[1];
        Person [] persons = new Person[1];
        Event [] events = new Event[1];

        users[0] = user;
        persons[0] = person;
        events[0] = event;

        loadRequest = new LoadRequest(users,persons,events);
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void clearTestPass() {

        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.load(loadRequest);
        assertTrue(loadResult.isSuccess());



        ClearService clearService = new ClearService();
        ClearResult clearResult = clearService.clear();
        assertTrue(clearResult.isSuccess());
        assertFalse(new PersonLookupService().person_lookup(new PersonLookupRequest("12301230")).isSuccess());
        assertFalse(new EventLookupService().event_lookup(new EventLookupRequest("4343")).isSuccess());


    }

    @Test
    public void clearTestFail() {
        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.load(loadRequest);
        assertTrue(loadResult.isSuccess());


        ClearRequest clearRequest = new ClearRequest();
        ClearService clearService = new ClearService();
        ClearResult clearResult = clearService.clear();
        assertTrue(clearResult.isSuccess());
        assertFalse(new PersonLookupService().person_lookup(new PersonLookupRequest("12301230")).isSuccess());
        assertFalse(new EventLookupService().event_lookup(new EventLookupRequest("4343")).isSuccess());
        assertThrows(NullPointerException.class , () -> new EventService().event(new EventRequest("12312414")).getData());
    }





}
