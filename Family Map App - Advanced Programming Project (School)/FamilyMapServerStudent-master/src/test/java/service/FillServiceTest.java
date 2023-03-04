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
import result.*;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {


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
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void fillTestPass() {

        RegisterRequest registerRequest = new RegisterRequest("cohenand","pass","cohenand@byu.edu","Andrew","Cohen","m");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest);
        assertTrue(registerResult.isSuccess());





        FillService fillService = new FillService();
        FillRequest fillRequest = new FillRequest("cohenand");
        FillResult r = fillService.fill(fillRequest);
        assertTrue(r.isSuccess());

        fillService = new FillService();
        fillRequest = new FillRequest("cohenand",3);
        r = fillService.fill(fillRequest);
        assertTrue(r.isSuccess());
        assertNotNull(r.getMessage());
        assertNotNull(new EventService().event(new EventRequest(registerResult.getAuthtoken())));




    }

    @Test
    public void fillTestFail() {

        ClearService clearService = new ClearService();
        clearService.clear();

        FillService fillService = new FillService();
        FillRequest fillRequest = new FillRequest("cohenand");
        assertThrows(NullPointerException.class, ()->fillService.fill(fillRequest));

    }


}
