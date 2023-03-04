package dao;

import model.Authtoken;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDaoTest {
    private Database db;
    private Event bestEvent;
    private EventDao eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        // and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO, so it can access the database.
        eDao = new EventDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
        eDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Here we close the connection to the database file, so it can be opened again later.
        // We will set commit to false because we do not want to save the changes to the database
        // between test cases.
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Start by inserting an event into the database.
        eDao.insertEvent(bestEvent);
        // Let's use a find method to get the event that we just put in back out.
        Event compareTest = eDao.findEvent(bestEvent.getEventID());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the Event class.
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.
        eDao.insertEvent(bestEvent);

        // However, our sql table is set up so that the column "eventID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertThrows(DataAccessException.class, () -> eDao.insertEvent(bestEvent));
    }

    @Test
    public void deletePass() throws DataAccessException {
        // Start by inserting an event into the database.
        eDao.insertEvent(bestEvent);
        // Let's use a find method to get the event that we just put in back out.
        Event compareTest = eDao.findEvent(bestEvent.getEventID());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the Event class.
        eDao.deleteEvent(bestEvent.getEventID());
        compareTest = eDao.findEvent(bestEvent.getEventID());

        assertNull(compareTest);
    }


    @Test
    public void FindPass() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.
        eDao.insertEvent(bestEvent);

        // However, our sql table is set up so that the column "eventID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.
        Event testcase = eDao.findEvent(bestEvent.getEventID());
        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertEquals(bestEvent,testcase);
    }
    @Test
    public void FindFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.

        eDao.insertEvent(bestEvent);
        eDao.deleteEvent(bestEvent.getEventID());
        Event testcase = eDao.findEvent(bestEvent.getEventID());
        // However, our sql table is set up so that the column "eventID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.
        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertNull(testcase);
    }
    @Test
    public void clearPass() throws DataAccessException {
        // Start by inserting an event into the database.
        eDao.insertEvent(bestEvent);
        Event compareTest = eDao.findEvent((bestEvent.getEventID()));
        assertNotNull(compareTest);

        eDao.clear();

        compareTest = eDao.findEvent(bestEvent.getEventID());

        assertNull(compareTest);

    }

    @Test
    public void exportPass() throws DataAccessException {
        Authtoken authtoken = new Authtoken("1234","cohenand");
        Authtoken authtoken_good = new Authtoken("1244","Gale");
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insert_token(authtoken);
        aDao.insert_token(authtoken_good);
        eDao.insertEvent(bestEvent);
        Event[] events = eDao.exportEvents(authtoken.getAuthtoken());
        assertEquals(0, events.length);


        events = eDao.exportEvents(authtoken_good.getAuthtoken());
        assertNotNull(events[0]);
        assertEquals(events[0],bestEvent);



    }

    @Test
    public void exportFail() throws DataAccessException {
        Authtoken authtoken = new Authtoken("1234","cohenand");
        eDao.insertEvent(bestEvent);
        assertThrows(NullPointerException.class, ()->eDao.exportEvents(authtoken.getAuthtoken()));
    }

}
