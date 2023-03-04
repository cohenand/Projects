package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.*;
import model.Event;
import model.Person;
import request.FillRequest;
import result.FillResult;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.UUID;

import java.sql.Connection;


/**
 * The class that performs the fill operation
 */

public class FillService {

    Database db;

    JsonParser jsonParser;
    UserDao userDao;
    PersonDao personDao;
    EventDao eventDao;
    boolean commie = false;
    public FillResult fill(FillRequest r) {

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
            personDao = new PersonDao(conn);
            eventDao = new EventDao(conn);

            if (userDao.findUser(r.getUsername()).equals(null) || r.getGenerations() < 0) {
                db.closeConnection(false);
                return new FillResult(false,r.getGenerations());
            }
            Person user = personDao.findPerson(userDao.findUser(r.getUsername()).getPersonID());
            personDao.deletePersonByUser(r.getUsername());
            eventDao.deleteEventByUser(r.getUsername());
            user.setFatherID(null);
            user.setMotherID(null);
            personDao.insertPerson(user);


            //// Make random events
            String event_ID = UUID.randomUUID().toString();
            while (eventDao.findEvent(event_ID) != null) {
                event_ID = UUID.randomUUID().toString();
            }
            int rand = new Random().nextInt(loc.size());
            JsonObject location = loc.get(rand).getAsJsonObject();
            String country = location.get("country").getAsString();
            String city = location.get("city").getAsString();
            float latitude = location.get("latitude").getAsFloat();
            float longitude = location.get("longitude").getAsFloat();
            int year = 1998;
            Event birth = new Event(event_ID,user.getAssociatedUsername(),user.getPersonID(),latitude,longitude,country,city,"birth",year);
            eventDao.insertEvent(birth);





            int count = 0;
            if (r.getGenerations() > 0) {

                String father_ID = UUID.randomUUID().toString();
                String mother_ID = UUID.randomUUID().toString();
                while (personDao.findPerson(father_ID) != null || personDao.findPerson(mother_ID) != null) {
                    father_ID = UUID.randomUUID().toString();
                    mother_ID = UUID.randomUUID().toString();
                }
                personDao.setParents(userDao.findUser(r.getUsername()).getPersonID(),father_ID,mother_ID);

                /// Select random names from file
                rand = new Random().nextInt(male.size());
                String fatherFirst = male.get(rand).getAsString();
                rand = new Random().nextInt(female.size());
                String motherFirst = female.get(rand).getAsString();
                String fatherLast = user.getLastName();
                rand = new Random().nextInt(last.size());
                String motherLast = last.get(rand).getAsString();


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
                Event marriage_f = new Event(event_ID,user.getAssociatedUsername(),father_ID,latitude,longitude,country,city,"marriage",1997);
                event_ID = UUID.randomUUID().toString();
                while (eventDao.findEvent(event_ID) != null) {
                    event_ID = UUID.randomUUID().toString();
                }
                Event marriage_m = new Event(event_ID,user.getAssociatedUsername(),mother_ID,latitude,longitude,country,city,"marriage",1997);
                eventDao.insertEvent(marriage_f);
                eventDao.insertEvent(marriage_m);


                //// Make random events - father birth
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
                int yearb = year-25;
                Event birthf = new Event(event_ID,user.getAssociatedUsername(),father_ID,latitude,longitude,country,city,"birth",yearb);
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
                yearb = year-25;
                Event birthm = new Event(event_ID,user.getAssociatedUsername(),mother_ID,latitude,longitude,country,city,"birth",yearb);
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
                int yeard = year+65;
                Event deathf = new Event(event_ID,user.getAssociatedUsername(),father_ID,latitude,longitude,country,city,"death",yeard);
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
                yeard = year+65;
                Event deathm = new Event(event_ID,user.getAssociatedUsername(),mother_ID,latitude,longitude,country,city,"death",yeard);
                eventDao.insertEvent(deathm);





                Person father = new Person(father_ID,user.getAssociatedUsername(),fatherFirst,fatherLast,"m",mother_ID);
                Person mother = new Person(mother_ID,user.getAssociatedUsername(),motherFirst,motherLast,"f",father_ID);
                personDao.insertPerson(father);
                personDao.insertPerson(mother);
                count=1;
                if (r.getGenerations() > 1) {
                    filler(father,count,r.getGenerations(),year);
                    filler(mother,count,r.getGenerations(),year);
                }

            }



            commie = true;

            return new FillResult(true,r.getGenerations());
        }
        catch(DataAccessException | FileNotFoundException d){
            commie = false;
            return new FillResult(false,r.getGenerations());
        } finally {
            db.closeConnection(commie);
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
            int yearb = prev_b-40;
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
            yearb = prev_b-40;
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
