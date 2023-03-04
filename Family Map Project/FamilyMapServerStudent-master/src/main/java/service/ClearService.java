package service;

import dao.*;
import model.Authtoken;
import result.ClearResult;

import java.sql.Connection;

/**
 * The class that performs the clear operation
 */
public class ClearService {

    EventDao eventDao;
    AuthtokenDao authtokenDao;
    UserDao userDao;
    PersonDao personDao;
    Database db;




    public ClearResult clear() {

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
            db.closeConnection(true);

            return new ClearResult(true);
        }
        catch(DataAccessException d){
            return new ClearResult(false);
        }

    }

}
