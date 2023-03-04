package service;

import dao.*;
import model.Authtoken;
import model.Event;
import request.EventRequest;
import result.EventLookupResult;
import result.EventResult;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * The class that performs the event operation
 */

public class EventService {

    Database db;
    EventDao eventDao;
    PersonDao personDao;
    AuthtokenDao authtokenDao;

    public EventResult event(EventRequest r) {

        try {
            db = new Database();

            Connection conn = db.getConnection();
            eventDao = new EventDao(conn);
            personDao = new PersonDao(conn);
            authtokenDao = new AuthtokenDao(conn);


            Event[] events = eventDao.exportEvents(r.getAuthToken());



            return new EventResult(events,true);
        }
        catch(DataAccessException d){
            return new EventResult(null,false);
        }
        finally {
            db.closeConnection(false);
        }


    }

}
