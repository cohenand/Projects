package com.example.familymap.DataContainers;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.Event;
import model.Person;


//// !!!!!! have this be the DataStorage object!!!!
public class FamilyTree extends TreeMap<String, FamilyMemberNode> {

    public FamilyMemberNode root = new FamilyMemberNode();
    public String authtoken = "";

    public Event[] get_FathersSide() {
        Event[] events = get_helper(root.father);
        events = combine_events(events,root.events);
        return events;
    }

    public Event[] get_MothersSide() {
        Event[] events = get_helper(root.mother);
        events = combine_events(events,root.events);
        return events;
    }

    public Event[] get_male_events(Event[] eventSet) {
        Event[] events = eventSet;
        Event[] male_events = new Event[0];
        for(Event event : events) {
            Event[] eventArr = new Event[1];
            eventArr[0] = event;
            if(this.get(event.getPersonID()).person.getGender().equals("m")) {
                male_events = combine_events(male_events,eventArr);
            }
        }
        return male_events;
    }

    public Event[] get_female_events(Event[] eventSet) {
        Event[] events = eventSet;
        Event[] female_events = new Event[0];
        for(Event event : events) {
            Event[] eventArr = new Event[1];
            eventArr[0] = event;
            if(this.get(event.getPersonID()).person.getGender().equals("f")) {
                female_events = combine_events(female_events,eventArr);
            }
        }
        return female_events;
    }

    public Event[] get_events() {
        Event[] events = new Event[0];
        Set<Entry<String,FamilyMemberNode>> values = this.entrySet();
        for(Entry<String,FamilyMemberNode> value : values) {
            events = combine_events(events,value.getValue().events);
        }
        return events;
    }



    private Event[] get_helper(FamilyMemberNode parent) {
        Event[] events = parent.events;

        if(parent.father!=null) {
            events = combine_events(events, get_helper(parent.getFather()));
            events = combine_events(events, get_helper(parent.getMother()));
        }

        return events;
    }



    public Event[] combine_events(Event[] eventArr1, Event[] eventArr2) {
        if(eventArr1 != null && eventArr2 != null) {
            Event[] newArr = new Event[eventArr1.length + eventArr2.length];
            if (newArr.length == eventArr1.length) {
                return eventArr1;
            }
            if (newArr.length == eventArr2.length) {
                return eventArr2;
            }


            for (int i = 0; i < eventArr1.length; i++) {
                newArr[i] = eventArr1[i];
            }
            for (int i = eventArr1.length; i < (eventArr1.length + eventArr2.length); i++) {
                newArr[i] = eventArr2[i-eventArr1.length];
            }
            return newArr;
        }
        else if (eventArr1 == null && eventArr2 != null) {
            return eventArr2;
        }
        else if (eventArr1 != null && eventArr2 == null) {
            return eventArr1;
        }
        return null;
    }

    public static FamilyTree load_Tree(Person user, Event[] events, Person[] persons, String authtoken) {
        FamilyTree familyTree = new FamilyTree();
        familyTree.authtoken = authtoken;
        for(Person person: persons) {
            FamilyMemberNode familyMemberNode = new FamilyMemberNode(person);
            if (user.getPersonID().equals(person.getPersonID())) {
                familyTree.root = familyMemberNode;
            }
            familyTree.put(person.getPersonID(),familyMemberNode);
        }

        for(Event event : events) {
            familyTree.get(event.getPersonID()).add_event(event);
        }

        Set<Entry<String,FamilyMemberNode>> values = familyTree.entrySet();
        for(Entry<String,FamilyMemberNode> value : values) {
            if(value.getValue().person.getSpouseID()!=null) {
                String spouse = value.getValue().person.getSpouseID();
                value.getValue().add_spouse(spouse);
            }
            if (value.getValue().person.getMotherID()!=null) {
                FamilyMemberNode mother = familyTree.get(value.getValue().person.getMotherID());
                FamilyMemberNode father = familyTree.get(value.getValue().person.getFatherID());
                mother.add_child(value.getKey());
                father.add_child(value.getKey());
                value.getValue().add_parents(father, mother);
            }
        }

        return familyTree;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public ArrayList<String> getEventTypes() {
        TreeSet<String> types = new TreeSet<String>();
        Event [] events = this.get_events();
        for (Event event:events) {
            types.add(event.getEventType());
        }
        ArrayList<String> type_array = new ArrayList<>(types);
        return type_array;
    }

    public ArrayList<Event> getEventArray() {
        TreeSet<Event> types = new TreeSet<Event>();
        Event [] events = this.get_events();
        for (Event event:events) {
            types.add(event);
        }
        ArrayList<Event> event_array = new ArrayList<Event>(types);
        return event_array;
    }


    public Person[] getFilteredPeople(Event [] filteredEvents) {
        TreeSet<String> output = new TreeSet<String>();
        Person[] person1 = new Person[0];
        Person[] person2 = new Person[1];
        for (Event event:filteredEvents) {
            output.add(event.getPersonID());
        }
        for(String ID : output) {
            person2[0] = this.get(ID).person;
            person1 = combine_people(person1,person2);
        }

        return person1;
    }

    public Person[] combine_people(Person[] personArr1, Person[] personArr2) {
        if(personArr1 != null && personArr2 != null) {
            Person[] newArr = new Person[personArr1.length + personArr2.length];
            if (newArr.length == personArr1.length) {
                return personArr1;
            }
            if (newArr.length == personArr2.length) {
                return personArr2;
            }


            for (int i = 0; i < personArr1.length; i++) {
                newArr[i] = personArr1[i];
            }
            for (int i = personArr1.length; i < (personArr1.length + personArr2.length); i++) {
                newArr[i] = personArr2[i-personArr1.length];
            }
            return newArr;
        }
        else if (personArr1 == null && personArr2 != null) {
            return personArr2;
        }
        else if (personArr1 != null && personArr2 == null) {
            return personArr1;
        }
        return null;
    }


    //USed to filter events based on settings
    public Event[] applySettings(Settings settings) {
        Event [] filteredEvents = new Event[0];
        if (settings.FathersSide && settings.MothersSide) {
            filteredEvents = this.get_events();
        }
        else if(settings.FathersSide) {
            if(this.root.person.getSpouseID()!=null) {
                filteredEvents = combine_events(this.get_FathersSide(),this.get(this.root.person.getSpouseID()).events);
            }
            else{
                filteredEvents = this.get_FathersSide();
            }
        }
        else if(settings.MothersSide) {
            if(this.root.person.getSpouseID()!=null) {
                filteredEvents = combine_events(this.get_MothersSide(),this.get(this.root.person.getSpouseID()).events);
            }
            else{
                filteredEvents = this.get_MothersSide();
            }
        }
        else {
            if(this.root.person.getSpouseID()!=null) {
                filteredEvents = this.combine_events(this.root.events,this.get(this.root.person.getSpouseID()).events);
            }
            else{
                filteredEvents = this.root.events;
            }

        }

        if(!settings.MaleEvents && filteredEvents.length > 0) {
            filteredEvents = this.get_female_events(filteredEvents);
        }
        if(!settings.FemaleEvents && filteredEvents.length > 0) {
            filteredEvents = this.get_male_events(filteredEvents);
        }
        return filteredEvents;
    }


    public ArrayList<Event> searchEventResults(String newText, Settings settings) {
        ArrayList<Event> filteredEventsUpdated = new ArrayList<Event>();
        Event[] filteredEvents = applySettings(settings);
        for(Event event:filteredEvents) {
            Person person = this.get(event.getPersonID()).person;
            String name = person.getFirstName() + " " + person.getLastName();
            if(name.toLowerCase().contains(newText)) {
                if(!filteredEventsUpdated.contains(event)) {
                    filteredEventsUpdated.add(event);
                }
            }
            else if(event.getEventType().toLowerCase().contains(newText) || Integer.toString(event.getYear()).toLowerCase().contains(newText) || event.getCountry().toLowerCase().contains(newText) || event.getCity().toLowerCase().contains(newText)) {
                if(!filteredEventsUpdated.contains(event)) {
                    filteredEventsUpdated.add(event);
                }
            }
        }
        return filteredEventsUpdated;
    }

    public ArrayList<Person> searchPersonResults(String newText, Settings settings) {
        ArrayList<Person> filteredPeopleUpdated = new ArrayList<Person>();
        Event[] filteredEvents = applySettings(settings);
        for(Event event:filteredEvents) {
            Person person = this.get(event.getPersonID()).person;
            String name = person.getFirstName() + " " + person.getLastName();
            if(name.toLowerCase().contains(newText)) {
                if(!filteredPeopleUpdated.contains(person)) {
                    filteredPeopleUpdated.add(person);
                }
            }
        }
        return filteredPeopleUpdated;
    }



}
