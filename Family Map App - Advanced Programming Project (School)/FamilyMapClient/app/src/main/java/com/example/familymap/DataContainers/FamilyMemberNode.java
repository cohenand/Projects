package com.example.familymap.DataContainers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import model.Event;
import model.Person;

public class FamilyMemberNode {

    public Person person;
    public Event[] events;
    public FamilyMemberNode father;
    public FamilyMemberNode mother;
    public String[] childrenIDs;
    public String spouse;

    public FamilyMemberNode() {
        this.person = null;
        this.events = null;
        this.father = null;
        this.mother = null;
        this.childrenIDs = null;
        this.spouse = null;
    }
    public FamilyMemberNode(Person user) {
        this.person = user;
        this.events = null;
        this.father = null;
        this.mother = null;
        this.childrenIDs = null;
        this.spouse = null;
    }



    public void add_event(Event event) {
        List<Event> newArr = new ArrayList<Event>();
        if (events != null) {
            for(int i = 0; i < events.length;i++) {
                newArr.add(events[i]);
            }
        }
        boolean duplicate = false;
        for(Event element :newArr) {
            if (element.getEventID().equals(event.getEventID())) {
                duplicate = true;
            }
        }
        if(duplicate == false) {
            newArr.add(event);
        }
        Event[] output = new Event[newArr.size()];
        for(int i = 0; i< newArr.size();i++) {
            output[i] = newArr.get(i);
        }
        events = output;
    }

    public void add_parents(FamilyMemberNode father, FamilyMemberNode mother) {
        this.father = father;
        this.mother = mother;
    }

    public void add_spouse(String spouse) {
        this.spouse = spouse;
    }

    public void add_child(String childID) {
        TreeSet<String> newSet = new TreeSet<>();
        if (childrenIDs != null) {
            for(int i = 0; i < childrenIDs.length;i++) {
                newSet.add(childrenIDs[i]);
            }
        }
        newSet.add(childID);
        String [] newArr = new String[newSet.size()];
        Iterator<String> itr = newSet.iterator();
        for (int i = 0; i< newArr.length;i++) {
            newArr[i] = itr.next();
        }
        childrenIDs = newArr;
    }

    public Event[] getEvents() {
        return events;
    }

    public FamilyMemberNode getMother() {
        return mother;
    }

    public FamilyMemberNode getFather() {
        return father;
    }

    public String getSpouseID(){
        return spouse;
    }

    public Event[] getEventsInOrder() {
        Event[] newEvents = new Event[events.length];
        TreeMap<String,Event> treeMap = new TreeMap<>();
        for (Event event : events) {
            String string = Integer.toString(event.getYear())+event.getEventType().toLowerCase()+event.getEventID();
            if (event.getEventType().toLowerCase().equals("birth")) {
                string="1111"+Integer.toString(event.getYear());
            }
            else if (event.getEventType().toLowerCase().equals("death")) {
                string=event.getEventType().toLowerCase();
            }
            treeMap.put(string,event);
        }
        Iterator<Event> itr = treeMap.values().iterator();
        for (int i = 0; i< events.length;i++) {
            newEvents[i] = itr.next();
        }

        return newEvents;
    }

}
