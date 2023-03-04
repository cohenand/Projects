package com.example.familymap.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymap.DataContainers.FamilyMemberNode;
import com.example.familymap.DataContainers.FamilyTree;
import com.example.familymap.DataContainers.Settings;
import com.example.familymap.R;
import com.google.gson.Gson;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    private String treeCache;
    private String dataCache;
    private String authCache;
    private String userCache;
    private String setCache;
    private String selectedPersonID;

    private FamilyTree familyTree;

    private Settings settings;

    Gson gson = new Gson();

    // maybe staticize this??
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        treeCache = getIntent().getStringExtra("treeCache");
        dataCache = getIntent().getStringExtra("dataCache");
        authCache = getIntent().getStringExtra("authCache");
        userCache = getIntent().getStringExtra("userCache");
        setCache = getIntent().getStringExtra("setCache");
        selectedPersonID = getIntent().getStringExtra("selected");



        familyTree = gson.fromJson(treeCache,FamilyTree.class);

        familyTree.authtoken = gson.fromJson(authCache,String.class);
        familyTree.root = gson.fromJson(userCache, FamilyMemberNode.class);
        settings = gson.fromJson(setCache,Settings.class);

        FamilyMemberNode familyMemberNode = familyTree.get(selectedPersonID);

        TextView firstname = findViewById(R.id.PersonFirstName);
        firstname.setText(familyMemberNode.person.getFirstName());

        TextView lastname = findViewById(R.id.PersonLastName);
        lastname.setText(familyMemberNode.person.getLastName());

        TextView gender = findViewById(R.id.PersonGender);
        if(familyMemberNode.person.getGender().equals("m")) {
            gender.setText(R.string.male);
        }
        else{
            gender.setText(R.string.female);
        }

        Event[] eventsInOrder = familyMemberNode.getEventsInOrder();
        Event[] filtered_Events;


        if (settings.FathersSide && settings.MothersSide) {
            filtered_Events = familyTree.get_events();
        }
        else if(settings.FathersSide) {
            filtered_Events = familyTree.get_FathersSide();
        }
        else if(settings.MothersSide) {
            filtered_Events = familyTree.get_MothersSide();
        }
        else {
            filtered_Events = familyTree.root.events;
        }

        if(!settings.MaleEvents && filtered_Events.length > 0) {
            filtered_Events = familyTree.get_female_events(filtered_Events);
        }
        if(!settings.FemaleEvents && filtered_Events.length > 0) {
            filtered_Events = familyTree.get_male_events(filtered_Events);
        }
        boolean good = false;
        for(Event event: filtered_Events) {
            if (event.getEventID().equals(eventsInOrder[0].getEventID())) {
                good = true;
            }
        }

        if(good == false) {
            eventsInOrder = new Event[0];
        }



        ArrayList<Person> people = new ArrayList<>();
        if(familyMemberNode.father != null) {
            people.add(familyMemberNode.father.person);
            people.add(familyMemberNode.mother.person);
        }
        if(familyMemberNode.person.getSpouseID() != null) {
            people.add(familyTree.get(familyMemberNode.person.getSpouseID()).person);
        }
        if(familyMemberNode.childrenIDs != null) {
            for (String id : familyMemberNode.childrenIDs) {
                people.add(familyTree.get(id).person);
            }
        }




        ExpandableListView expandableListView = findViewById(R.id.PersonExpandable);
        expandableListView.setAdapter(new ExpandableListAdapter(eventsInOrder, people));


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Event[] events;
        private ArrayList<Person> people;

        ExpandableListAdapter(Event[] events, ArrayList<Person> people) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case 0:
                    return events.length;
                case 1:
                    return people.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case 0:
                    return getString(R.string.relatedEvents);
                case 1:
                    return getString(R.string.relatedPeople);
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case 0:
                    return events[childPosition];
                case 1:
                    return people.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.expandable_list_headings,parent,false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch(groupPosition) {
                case 0:
                    titleView.setText(R.string.relatedEvents);
                    break;
                case 1:
                    titleView.setText(R.string.relatedPeople);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: "+groupPosition);

            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;
            switch(groupPosition) {
                case 0:
                    itemView = getLayoutInflater().inflate(R.layout.event_item,parent, false);
                    initializeEventView(itemView,childPosition);
                    break;
                case 1:
                    itemView = getLayoutInflater().inflate(R.layout.event_item,parent, false);
                    initializePersonView(itemView,childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: "+groupPosition);

            }
            return itemView;
        }

        private void initializePersonView(View personView, final int childPosition) {
            TextView details;
            TextView eventName;
            ImageView icon;
            icon = personView.findViewById(R.id.symbol);
            details = personView.findViewById(R.id.eventDetails);
            eventName = personView.findViewById(R.id.personName);
            StringBuilder sb = new StringBuilder();
            Person person = people.get(childPosition);
            sb.append(person.getFirstName() + " " + person.getLastName());
            details.setText(sb.toString());
            FamilyMemberNode guy = familyTree.get(events[0].getPersonID());
            int relation = determineRelation(guy,person);
            eventName.setText(relation);
            if (person.getGender().equals("m")){
                Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(40);
                icon.setImageDrawable(genderIcon);
            }
            else {
                Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(40);
                icon.setImageDrawable(genderIcon);
            }

            personView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent switchActivityIntent = new Intent(PersonActivity.this, PersonActivity.class);
                    switchActivityIntent.putExtra("setCache",setCache);
                    switchActivityIntent.putExtra("dataCache",dataCache);
                    switchActivityIntent.putExtra("treeCache",treeCache);
                    switchActivityIntent.putExtra("authCache",authCache);
                    switchActivityIntent.putExtra("userCache",userCache);
                    Gson gson = new Gson();
                    String selectedPerson = person.getPersonID();
                    switchActivityIntent.putExtra("selected",selectedPerson);
                    startActivity(switchActivityIntent);
                }
            });

        }
        private void initializeEventView(View eventView, final int childPosition) {
            TextView details;
            TextView eventName;
            ImageView icon;
            icon = eventView.findViewById(R.id.symbol);
            details = eventView.findViewById(R.id.eventDetails);
            eventName = eventView.findViewById(R.id.personName);
            StringBuilder sb = new StringBuilder();
            sb.append(events[childPosition].getEventType().toUpperCase());
            sb.append(": ");
            sb.append(events[childPosition].getCity());
            sb.append(", "+events[childPosition].getCountry());
            sb.append(" ("+events[childPosition].getYear()+")");
            details.setText(sb.toString());
            sb = new StringBuilder();
            Person person = familyTree.get(events[childPosition].getPersonID()).person;
            sb.append(person.getFirstName());
            sb.append(" ");
            sb.append(person.getLastName());
            eventName.setText(sb.toString());
            Drawable markerIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.black).sizeDp(40);
            icon.setImageDrawable(markerIcon);

            eventView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent switchActivityIntent = new Intent(PersonActivity.this, EventActivity.class);
                    switchActivityIntent.putExtra("setCache",setCache);
                    switchActivityIntent.putExtra("dataCache",dataCache);
                    switchActivityIntent.putExtra("treeCache",treeCache);
                    switchActivityIntent.putExtra("authCache",authCache);
                    switchActivityIntent.putExtra("userCache",userCache);
                    Gson gson = new Gson();
                    String selectedEvent = gson.toJson(events[childPosition]);
                    switchActivityIntent.putExtra("selected",selectedEvent);
                    startActivity(switchActivityIntent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent switchActivityIntent = new Intent(PersonActivity.this, MainActivity.class);
            switchActivityIntent.putExtra("setCache",setCache);
            switchActivityIntent.putExtra("dataCache",dataCache);
            switchActivityIntent.putExtra("treeCache",treeCache);
            switchActivityIntent.putExtra("authCache",authCache);
            switchActivityIntent.putExtra("userCache",userCache);
            switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(switchActivityIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int determineRelation(FamilyMemberNode familyMemberNode, Person person) {
        if(familyMemberNode.childrenIDs != null) {
            for (String id : familyMemberNode.childrenIDs) {
                if (id.equals(person.getPersonID())) {
                    return R.string.child;
                }
            }
        }
        if(familyMemberNode.person.getPersonID() != null) {
            if (familyMemberNode.person.getSpouseID().equals(person.getPersonID())) {
                return R.string.spouse;
            }
        }
        if(familyMemberNode.father != null) {
            if (familyMemberNode.father.person.getPersonID().equals(person.getPersonID())) {
                return R.string.father;
            }
            if (familyMemberNode.mother.person.getPersonID().equals(person.getPersonID())) {
                return R.string.mother;
            }
        }
        return R.string.unknown;
    }
}