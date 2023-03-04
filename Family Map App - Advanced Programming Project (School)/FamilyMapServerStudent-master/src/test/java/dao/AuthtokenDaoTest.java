package dao;

import model.Authtoken;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthtokenDaoTest {

    private Database db;
    private Authtoken bestAuthtoken;
    private AuthtokenDao aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        // and a new event with random data
        bestAuthtoken = new Authtoken("asdfjasdfljkasfd","Cohenand");

        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO, so it can access the database.
        aDao = new AuthtokenDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
        aDao.clear();
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
        aDao.insert_token(bestAuthtoken);
        // Let's use a find method to get the event that we just put in back out.
        Authtoken compareTest = aDao.find_token(bestAuthtoken.getAuthtoken());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the Event class.
        assertEquals(bestAuthtoken.getAuthtoken(), compareTest.getAuthtoken());
        assertEquals(bestAuthtoken.getUsername(), compareTest.getUsername());
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.
        aDao.insert_token(bestAuthtoken);

        // However, our sql table is set up so that the column "eventID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertThrows(DataAccessException.class, () -> aDao.insert_token(bestAuthtoken));
    }



    @Test
    public void FindPass() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.
        aDao.insert_token(bestAuthtoken);

        // However, our sql table is set up so that the column "eventID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.
        Authtoken testcase = aDao.find_token(bestAuthtoken.getAuthtoken());
        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertEquals(bestAuthtoken.getAuthtoken(),testcase.getAuthtoken());
        assertEquals(bestAuthtoken.getUsername(),testcase.getUsername());
    }
    @Test
    public void FindFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.

        aDao.insert_token(bestAuthtoken);
        aDao.clear();
        Authtoken testcase = aDao.find_token(bestAuthtoken.getAuthtoken());
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
        aDao.insert_token(bestAuthtoken);
        Authtoken compareTest = aDao.find_token(bestAuthtoken.getAuthtoken());
        assertNotNull(compareTest);

        aDao.clear();

        compareTest = aDao.find_token(bestAuthtoken.getAuthtoken());

        assertNull(compareTest);

    }






}
