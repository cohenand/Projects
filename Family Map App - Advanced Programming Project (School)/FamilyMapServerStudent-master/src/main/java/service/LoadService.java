package service;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.ClearResult;
import result.LoadResult;

import java.sql.Connection;

/**
 * The class that performs the load operation
 */

public class LoadService {

    public LoadResult load(LoadRequest r) {

        EventDao eventDao;
        AuthtokenDao authtokenDao;
        UserDao userDao;
        PersonDao personDao;
        Database db;

        try {
            db = new Database();
            Connection conn = db.getConnection();
            eventDao = new EventDao(conn);
            personDao = new PersonDao(conn);
            userDao = new UserDao(conn);
            authtokenDao = new AuthtokenDao(conn);

            eventDao.clear();
            personDao.clear();
            userDao.clear();
            authtokenDao.clear();


            int ucount = 0;
            int pcount = 0;
            int ecount = 0;

            for (User user: r.getUsers()) {
                userDao.insertUser(user);
                ucount++;
            }
            for (Person person: r.getPersons()) {
                personDao.insertPerson(person);
                pcount++;
            }
            for (Event event: r.getEvents()) {
                eventDao.insertEvent(event);
                ecount++;
            }



            db.closeConnection(true);

            return new LoadResult(true, ucount, pcount, ecount);
        }
        catch(DataAccessException d){

            return new LoadResult(false,0,0,0);
        }




    }

}
