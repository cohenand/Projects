package service;

import dao.DataAccessException;
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

import static org.junit.jupiter.api.Assertions.*;

public class EventLookupServiceTest {


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

        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void eventLookupTestPass() {

        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.load(loadRequest);
        assertTrue(loadResult.isSuccess());


        assertTrue(new EventLookupService().event_lookup(new EventLookupRequest("4343")).isSuccess());
        assertEquals("4343",new EventLookupService().event_lookup(new EventLookupRequest("4343")).getEventID());
        assertEquals("12301230",new EventLookupService().event_lookup(new EventLookupRequest("4343")).getPersonID());


    }

    @Test
    public void eventLookupTestFail() {

        assertFalse(new EventLookupService().event_lookup(new EventLookupRequest("4343")).isSuccess());
    }





}
