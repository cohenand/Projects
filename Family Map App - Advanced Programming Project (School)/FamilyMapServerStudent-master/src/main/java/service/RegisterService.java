package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.*;
import model.Authtoken;
import model.Event;
import model.Person;
import model.User;
import request.FillRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.FillResult;
import result.LoginResult;
import result.RegisterResult;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;


/**
 * The class that performs the register operation
 */

public class RegisterService {

    PersonDao personDao;
    Database db;
    JsonParser jsonParser;
    EventDao eventDao;

    public RegisterResult register(RegisterRequest r) {

        AuthtokenDao authtokenDao;
        UserDao userDao;


        try {

            jsonParser = new JsonParser();
            JsonObject fnames = (JsonObject) jsonParser.parse(new FileReader("json/fnames.json"));
            JsonObject mnames = (JsonObject) jsonParser.parse(new FileReader("json/mnames.json"));
            JsonObject snames = (JsonObject) jsonParser.parse(new FileReader("json/snames.json"));
            JsonObject locations = (JsonObject) jsonParser.parse(new FileReader("json/locations.json"));
            JsonArray female = (JsonArray) fnames.get("data");
            JsonArray male = (JsonArray) mnames.get("data");
            JsonArray last = (JsonArray) snames.get("data");
            JsonArray loc = (JsonArray) locations.get("data");
            db = new Database();

            Connection conn = db.getConnection();
            userDao = new UserDao(conn);
            authtokenDao = new AuthtokenDao(conn);
            personDao = new PersonDao(conn);
            eventDao = new EventDao(conn);

            if(userDao.findUser(r.getUsername())!=null) {
                db.closeConnection(false);
                return new RegisterResult(null,r.getUsername(),null);
            }

            String personID = UUID.randomUUID().toString();
            while(personDao.findPerson(personID)!= null) {
                personID = UUID.randomUUID().toString();
            }


            User user = new User(r.getUsername(),r.getPassword(),r.getEmail(),r.getFirstName(),r.getLastName(),r.getGender(),personID);
            Person person = new Person(personID,r.getUsername(),r.getFirstName(),r.getLastName(),r.getGender());

            userDao.insertUser(user);
            personDao.insertPerson(person);

            db.closeConnection(true);
            FillRequest fillRequest = new FillRequest(user.getUsername());
            FillService fillService = new FillService();
            FillResult fillResult = fillService.fill(fillRequest);
            conn = db.getConnection();
            userDao = new UserDao(conn);
            authtokenDao = new AuthtokenDao(conn);
            personDao = new PersonDao(conn);
            eventDao = new EventDao(conn);


            if (fillResult.isSuccess()) {
                String tokenID;
                tokenID = UUID.randomUUID().toString();
                while (authtokenDao.find_token(tokenID) != null) {
                    tokenID = UUID.randomUUID().toString();
                }
                Authtoken authtoken = new Authtoken(tokenID, r.getUsername());
                authtokenDao.insert_token(authtoken);
                db.closeConnection(true);
                return new RegisterResult(authtoken.getAuthtoken(),r.getUsername(),personID);
            }




            return new RegisterResult(null,r.getUsername(),personID);
        }
        catch(DataAccessException | FileNotFoundException d){
            return new RegisterResult(null,r.getUsername(),null);
        }


    }


    private void filler(Person current, int gen, int max_gen, int prev_b) throws DataAccessException, FileNotFoundException {
        if (gen < max_gen) {
            jsonParser = new JsonParser();
            JsonObject fnames = (JsonObject) jsonParser.parse(new FileReader("json/fnames.json"));
            JsonObject mnames = (JsonObject) jsonParser.parse(new FileReader("json/mnames.json"));
            JsonObject snames = (JsonObject) jsonParser.parse(new FileReader("json/snames.json"));
            JsonObject locations = (JsonObject) jsonParser.parse(new FileReader("json/locations.json"));
            JsonArray female = (JsonArray) fnames.get("data");
            JsonArray male = (JsonArray) mnames.get("data");
            JsonArray last = (JsonArray) snames.get("data");
            JsonArray loc = (JsonArray) locations.get("data");
            int num = gen + 1;
            String father_ID = UUID.randomUUID().toString();
            String mother_ID = UUID.randomUUID().toString();
            while (personDao.findPerson(father_ID) != null || personDao.findPerson(mother_ID) != null) {
                father_ID = UUID.randomUUID().toString();
                mother_ID = UUID.randomUUID().toString();
            }
            personDao.setParents(current.getPersonID(),father_ID,mother_ID);

            /// Select random names from file
            int rand = new Random().nextInt(male.size());
            String fatherFirst = male.get(rand).getAsString();
            rand = new Random().nextInt(female.size());
            String motherFirst = female.get(rand).getAsString();
            String fatherLast = current.getLastName();
            rand = new Random().nextInt(last.size());
            String motherLast = last.get(rand).getAsString();

            Person father = new Person(father_ID,current.getAssociatedUsername(),fatherFirst,fatherLast,"m",mother_ID);
            Person mother = new Person(mother_ID,current.getAssociatedUsername(),motherFirst,motherLast,"f",father_ID);
            personDao.insertPerson(father);
            personDao.insertPerson(mother);


            //// Make random events - father birth
            String event_ID = UUID.randomUUID().toString();
            while (eventDao.findEvent(event_ID) != null) {
                event_ID = UUID.randomUUID().toString();
            }
            rand = new Random().nextInt(loc.size());
            JsonObject location = loc.get(rand).getAsJsonObject();
            String country = location.get("country").getAsString();
            String city = location.get("city").getAsString();
            float latitude = location.get("latitude").getAsFloat();
            float longitude = location.get("longitude").getAsFloat();
            int yearb = prev_b-50;
            Event birthf = new Event(event_ID,current.getAssociatedUsername(),father_ID,latitude,longitude,country,city,"birth",yearb);
            eventDao.insertEvent(birthf);

            //// Make random events - mother birth
            event_ID = UUID.randomUUID().toString();
            while (eventDao.findEvent(event_ID) != null) {
                event_ID = UUID.randomUUID().toString();
            }
            rand = new Random().nextInt(loc.size());
            location = loc.get(rand).getAsJsonObject();
            country = location.get("country").getAsString();
            city = location.get("city").getAsString();
            latitude = location.get("latitude").getAsFloat();
            longitude = location.get("longitude").getAsFloat();
            yearb = prev_b-50;
            Event birthm = new Event(event_ID,current.getAssociatedUsername(),mother_ID,latitude,longitude,country,city,"birth",yearb);
            eventDao.insertEvent(birthm);



            //// Make random events - death
            event_ID = UUID.randomUUID().toString();
            while (eventDao.findEvent(event_ID) != null) {
                event_ID = UUID.randomUUID().toString();
            }
            rand = new Random().nextInt(loc.size());
            location = loc.get(rand).getAsJsonObject();
            country = location.get("country").getAsString();
            city = location.get("city").getAsString();
            latitude = location.get("latitude").getAsFloat();
            longitude = location.get("longitude").getAsFloat();
            int yeard = prev_b+65;
            Event deathf = new Event(event_ID,current.getAssociatedUsername(),father_ID,latitude,longitude,country,city,"death",yeard);
            eventDao.insertEvent(deathf);

            //// Make random events - death
            event_ID = UUID.randomUUID().toString();
            while (eventDao.findEvent(event_ID) != null) {
                event_ID = UUID.randomUUID().toString();
            }
            rand = new Random().nextInt(loc.size());
            location = loc.get(rand).getAsJsonObject();
            country = location.get("country").getAsString();
            city = location.get("city").getAsString();
            latitude = location.get("latitude").getAsFloat();
            longitude = location.get("longitude").getAsFloat();
            yeard = prev_b+65;
            Event deathm = new Event(event_ID,current.getAssociatedUsername(),mother_ID,latitude,longitude,country,city,"death",yeard);
            eventDao.insertEvent(deathm);



            //// Make random events - marriage
            event_ID = UUID.randomUUID().toString();
            while (eventDao.findEvent(event_ID) != null) {
                event_ID = UUID.randomUUID().toString();
            }
            rand = new Random().nextInt(loc.size());
            location = loc.get(rand).getAsJsonObject();
            country = location.get("country").getAsString();
            city = location.get("city").getAsString();
            latitude = location.get("latitude").getAsFloat();
            longitude = location.get("longitude").getAsFloat();
            int year = prev_b - 2;
            Event marriage_f = new Event(event_ID,current.getAssociatedUsername(),father_ID,latitude,longitude,country,city,"marriage",year);
            event_ID = UUID.randomUUID().toString();
            while (eventDao.findEvent(event_ID) != null) {
                event_ID = UUID.randomUUID().toString();
            }
            Event marriage_m = new Event(event_ID,current.getAssociatedUsername(),mother_ID,latitude,longitude,country,city,"marriage",year);
            eventDao.insertEvent(marriage_f);
            eventDao.insertEvent(marriage_m);






            filler(personDao.findPerson(father_ID),num,max_gen,yearb);
            filler(personDao.findPerson(mother_ID),num,max_gen,yearb);
        }

    }


}
