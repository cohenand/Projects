package request;

import model.User;
import model.Person;
import model.Event;

/**
 * A class used as an argument for the LoadService class
 */
public class LoadRequest {

    User[] users;
    Person [] persons;
    Event [] events;

    /**
     * The constructor for the LoadRequest class that passes in arrays to be loaded into the database
     * @param users
     * @param persons
     * @param events
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }


    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }
}
