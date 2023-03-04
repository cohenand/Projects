package service;

import dao.*;
import model.Event;
import request.EventLookupRequest;
import result.ClearResult;
import result.EventLookupResult;

import java.sql.Connection;

/**
 * The class that performs the event lookup operation
 */
public class EventLookupService {

    Database db;
    EventDao eventDao;


    /**
     * Uses data from a request class and checks against a database to find more information
     * @param r is a EventLookupRequest type
     * @return an EventLookupResult object
     */
    public EventLookupResult event_lookup(EventLookupRequest r) {
        try {
            db = new Database();

            Connection conn = db.getConnection();
            eventDao = new EventDao(conn);
            Event event = eventDao.findEvent(r.getEventID());


            db.closeConnection(false);

            return new EventLookupResult(event);
        }
        catch(DataAccessException d){
            return new EventLookupResult(null);
        }
    }


}
