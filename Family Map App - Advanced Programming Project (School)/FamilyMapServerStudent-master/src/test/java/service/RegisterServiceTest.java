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
import result.FillResult;
import result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {


    User user;
    Person person;
    Authtoken authtoken;
    Event event;
    LoadRequest loadRequest;
    LoginRequest loginRequest;

    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();

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
    public void registerTestPass() {

        RegisterRequest registerRequest = new RegisterRequest("cohenand","pass","cohenand@byu.edu","Andrew","Cohen","m");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest);
        assertTrue(registerResult.isSuccess());


        assertNotNull(new EventService().event(new EventRequest(registerResult.getAuthtoken())));
        assertTrue(new EventService().event(new EventRequest(registerResult.getAuthtoken())).getData().length > 1);
        assertTrue(new PersonService().person(new PersonRequest(registerResult.getAuthtoken())).getData().length > 1);




    }

    @Test
    public void registerTestFail() {

        RegisterRequest registerRequest = new RegisterRequest("cohenand","pass","cohenand@byu.edu","Andrew","Cohen","m");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest);
        assertTrue(registerResult.isSuccess());

        registerRequest = new RegisterRequest("cohenand","pass","cohenand@byu.edu","Andrew","Cohen","m");
        registerService = new RegisterService();
        registerResult = registerService.register(registerRequest);
        assertFalse(registerResult.isSuccess());


    }


}
