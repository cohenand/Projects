package service;

import dao.AuthtokenDao;
import dao.DataAccessException;
import dao.Database;
import dao.PersonDao;
import model.Person;
import request.PersonRequest;
import result.EventResult;
import result.PersonResult;

import java.sql.Connection;

/**
 * The class that performs the Person operation
 */

public class PersonService {

    Database db;
    PersonDao personDao;
    AuthtokenDao authtokenDao;

    public PersonResult person(PersonRequest r) {
        try {
            db = new Database();
            Connection conn = db.getConnection();
            personDao = new PersonDao(conn);
            authtokenDao = new AuthtokenDao(conn);

            Person[] persons = personDao.exportPersons(r.getAuthToken());



            return new PersonResult(persons, true);
        }
        catch(DataAccessException d){
            return new PersonResult(null, false);
        }
        finally {
            db.closeConnection(false);
        }
    }

}
