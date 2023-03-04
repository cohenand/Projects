package service;

import dao.DataAccessException;
import model.Authtoken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.EventLookupRequest;
import request.EventRequest;
import request.LoadRequest;
import request.LoginRequest;
import result.EventResult;
import result.LoadResult;
import result.LoginResult;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {

    User user;
    Person person;
    Authtoken authtoken;
    Event event;
    LoadRequest loadRequest;
    LoginRequest loginRequest;

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
        loginRequest = new LoginRequest("cohenand","pass");


    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void eventTestPass() {

        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.load(loadRequest);
        assertTrue(loadResult.isSuccess());

        LoginService loginService = new LoginService();
        LoginResult result = loginService.login(loginRequest);



        EventService eventService = new EventService();
        EventRequest eventRequest = new EventRequest(result.getAuthtoken());
        EventResult r = eventService.event(eventRequest);


        assertTrue(r.isSuccess());
        assertEquals("4343",new EventService().event(new EventRequest(result.getAuthtoken())).getData()[0].getEventID());
        assertEquals("cohenand",new EventService().event(new EventRequest(result.getAuthtoken())).getData()[0].getAssociatedUsername());




    }

    @Test
    public void eventTestFail() {

        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.load(loadRequest);
        assertTrue(loadResult.isSuccess());


        EventService eventService = new EventService();
        EventRequest eventRequest = new EventRequest("12312414");

        assertThrows(NullPointerException.class, ()-> eventService.event(eventRequest).isSuccess());


    }


}
