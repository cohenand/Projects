package com.example.familymap.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.familymap.DataContainers.FamilyMemberNode;
import com.example.familymap.DataContainers.FamilyTree;
import com.example.familymap.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


import com.google.gson.Gson;

import java.util.ArrayList;

import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private String dataCache;
    private String treeCache;
    private String authCache;
    private String userCache;
    private String setCache;
    private String filteredSet;
    private String filtered;


    private FamilyTree familyTree;
    ArrayList<Event> filteredEvents = new ArrayList<>();
    ArrayList<Person> filteredPeople = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Iconify.with(new FontAwesomeModule());

        Gson gson = new Gson();
        dataCache = getIntent().getStringExtra("dataCache");
        treeCache = getIntent().getStringExtra("treeCache");
        authCache = getIntent().getStringExtra("authCache");
        userCache = getIntent().getStringExtra("userCache");
        setCache = getIntent().getStringExtra("setCache");
        filteredSet = getIntent().getStringExtra("filteredCache");
        filtered = getIntent().getStringExtra("filteredPeopleCache");

        familyTree = (FamilyTree) gson.fromJson(treeCache, FamilyTree.class);
        familyTree.authtoken = (String) gson.fromJson(authCache, String.class);
        familyTree.root = (FamilyMemberNode) gson.fromJson(userCache, FamilyMemberNode.class);


        Event[] filteredEventArray = (Event []) gson.fromJson(filteredSet,Event[].class);
        for(Event event : filteredEventArray) {
            filteredEvents.add(event);
        }
        Person[] filteredPeopleArray = (Person []) gson.fromJson(filtered, Person[].class);
        for (Person person : filteredPeopleArray) {
            filteredPeople.add(person);
        }


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.searchRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));


        SearchView searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();


                ArrayList<Event> filteredEventsUpdated = new ArrayList<Event>();
                ArrayList<Person> filteredPeopleUpdated = new ArrayList<Person>();

                for(Event event:filteredEvents) {
                    Person person = SearchActivity.this.familyTree.get(event.getPersonID()).person;
                    String name = person.getFirstName() + " " + person.getLastName();
                    if(name.toLowerCase().contains(newText)) {
                        if(!filteredEventsUpdated.contains(event)) {
                            filteredEventsUpdated.add(event);
                        }
                        if(!filteredPeopleUpdated.contains(person)) {
                            filteredPeopleUpdated.add(person);
                        }
                    }
                    else if(event.getEventType().toLowerCase().contains(newText) || Integer.toString(event.getYear()).toLowerCase().contains(newText) || event.getCountry().toLowerCase().contains(newText) || event.getCity().toLowerCase().contains(newText)) {
                        if(!filteredEventsUpdated.contains(event)) {
                            filteredEventsUpdated.add(event);
                        }
                    }
                }
                if(!newText.equals("")) {
                    SearchAdapter searchAdapter = new SearchAdapter(filteredEventsUpdated, filteredPeopleUpdated);
                    recyclerView.setAdapter(searchAdapter);
                }
                else {
                    filteredEventsUpdated = new ArrayList<Event>();
                    filteredPeopleUpdated = new ArrayList<Person>();
                    SearchAdapter searchAdapter = new SearchAdapter(filteredEventsUpdated, filteredPeopleUpdated);
                    recyclerView.setAdapter(searchAdapter);
                }

                return false;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent switchActivityIntent = new Intent(SearchActivity.this, MainActivity.class);
            switchActivityIntent.putExtra("setCache",setCache);
            switchActivityIntent.putExtra("dataCache",dataCache);
            switchActivityIntent.putExtra("treeCache",treeCache);
            switchActivityIntent.putExtra("authCache",authCache);
            switchActivityIntent.putExtra("userCache",userCache);
            startActivity(switchActivityIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private ArrayList<Event> events;
        private ArrayList<Person> people;

        SearchAdapter(ArrayList<Event> events, ArrayList<Person> people) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? 0:1;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;


            view = getLayoutInflater().inflate(R.layout.event_item,parent,false);


            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < people.size()) {
                holder.bind(people.get(position));
            }
            else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return events.size() + people.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView details;
        private TextView eventName;
        private ImageView icon;

        private final int viewType;
        private Event event;
        private Person person;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == 0) {
                icon = itemView.findViewById(R.id.symbol);
                details = itemView.findViewById(R.id.eventDetails);
                eventName = itemView.findViewById(R.id.personName);
            }
            else {
                icon = itemView.findViewById(R.id.symbol);
                details = itemView.findViewById(R.id.eventDetails);
                eventName = itemView.findViewById(R.id.personName);
            }
        }
        private void bind(Event event) {
            this.event = event;
            StringBuilder sb = new StringBuilder();
            sb.append(event.getEventType().toUpperCase());
            sb.append(": ");
            sb.append(event.getCity());
            sb.append(", "+event.getCountry());
            sb.append(" ("+event.getYear()+")");
            details.setText(sb.toString());
            sb = new StringBuilder();
            this.person = SearchActivity.this.familyTree.get(event.getPersonID()).person;
            sb.append(person.getFirstName());
            sb.append(" ");
            sb.append(person.getLastName());
            eventName.setText(sb.toString());
            Drawable markerIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.black).sizeDp(40);
            icon.setImageDrawable(markerIcon);

        }

        private void bind(Person person) {
            this.person = person;
            StringBuilder sb = new StringBuilder();
            sb.append(person.getFirstName() + " " + person.getLastName());
            details.setText(sb.toString());
            if (person.getGender().equals("m")){
                Drawable genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(40);
                icon.setImageDrawable(genderIcon);
            }
            else {
                Drawable genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(40);
                icon.setImageDrawable(genderIcon);
            }
        }

        @Override
        public void onClick(View v) {
            if(viewType == 0) {
                Intent switchActivityIntent = new Intent(SearchActivity.this, PersonActivity.class);
                switchActivityIntent.putExtra("setCache",setCache);
                switchActivityIntent.putExtra("dataCache",dataCache);
                switchActivityIntent.putExtra("treeCache",treeCache);
                switchActivityIntent.putExtra("authCache",authCache);
                switchActivityIntent.putExtra("userCache",userCache);
                String selectedPerson = this.person.getPersonID();
                switchActivityIntent.putExtra("selected",selectedPerson);
                startActivity(switchActivityIntent);
            }
            else {
                Intent switchActivityIntent = new Intent(SearchActivity.this, EventActivity.class);
                switchActivityIntent.putExtra("setCache",setCache);
                switchActivityIntent.putExtra("dataCache",dataCache);
                switchActivityIntent.putExtra("treeCache",treeCache);
                switchActivityIntent.putExtra("authCache",authCache);
                switchActivityIntent.putExtra("userCache",userCache);
                Gson gson = new Gson();
                String selectedEvent = gson.toJson(this.event);
                switchActivityIntent.putExtra("selected",selectedEvent);
                startActivity(switchActivityIntent);
            }
        }
    }
}