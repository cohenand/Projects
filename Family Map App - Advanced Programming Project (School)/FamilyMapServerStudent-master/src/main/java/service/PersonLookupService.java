package service;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDao;
import model.Person;
import request.PersonLookupRequest;
import result.EventLookupResult;
import result.PersonLookupResult;

import java.sql.Connection;

/**
 * The class that performs the person lookup operation
 */

public class PersonLookupService {

    public PersonLookupResult person_lookup(PersonLookupRequest r) {

        Database db;
        PersonDao personDao;


        try {
            db = new Database();

            Connection conn = db.getConnection();
            personDao = new PersonDao(conn);
            Person person = personDao.findPerson(r.getPersonID());


            db.closeConnection(false);

            return new PersonLookupResult(person);
        }
        catch(DataAccessException d){
            return new PersonLookupResult(null);
        }
    }


}
