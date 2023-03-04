package com.example.familymap.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymap.DataContainers.FamilyMemberNode;
import com.example.familymap.DataContainers.FamilyTree;
import com.example.familymap.DataContainers.MainActivityViewModel;
import com.example.familymap.DataContainers.Settings;
import com.example.familymap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Person;

public class MapsFragment extends Fragment {

    private MapsFragment.Listener listener;

    private FamilyTree familyTree;
    private float [] colors = {BitmapDescriptorFactory.HUE_VIOLET,BitmapDescriptorFactory.HUE_GREEN,BitmapDescriptorFactory.HUE_CYAN,BitmapDescriptorFactory.HUE_AZURE,BitmapDescriptorFactory.HUE_MAGENTA,BitmapDescriptorFactory.HUE_BLUE,BitmapDescriptorFactory.HUE_ORANGE,BitmapDescriptorFactory.HUE_RED,BitmapDescriptorFactory.HUE_ROSE,BitmapDescriptorFactory.HUE_YELLOW};
    private Event selectedEvent;
    private Event activeEvent;
    private Event [] filteredEvents = new Event[0];
    private Settings settings = new Settings();
    private List<Polyline> polylines = new ArrayList<Polyline>();




    public interface Listener {

    }

    private MainActivityViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }


    public void registerListener(MapsFragment.Listener listener, FamilyTree familyTree) {
        this.listener = listener;

        this.familyTree = familyTree;
    }

    public void registerListener(MapsFragment.Listener listener, FamilyTree familyTree, Settings settings) {
        this.listener = listener;
        this.familyTree = familyTree;
        this.settings = settings;
    }
    public void registerListener(MapsFragment.Listener listener, FamilyTree familyTree, Settings settings, Event selected) {
        this.listener = listener;
        this.familyTree = familyTree;
        this.settings = settings;
        this.selectedEvent = selected;
    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            Iconify.with(new FontAwesomeModule());
            RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.mapRecycle);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            if (familyTree!=null){
                ArrayList<String> eventTypes = familyTree.getEventTypes();

                filteredEvents = familyTree.applySettings(settings);

                for(Event event : filteredEvents) {
                    int color_ind = eventTypes.indexOf(event.getEventType()) % colors.length;
                    // add coord to structure that checks for duplicate entries
                    LatLng coord = new LatLng(event.getLatitude(),event.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(coord).title(event.getEventType().toUpperCase()).icon(BitmapDescriptorFactory.defaultMarker(colors[color_ind])));

                }
                for (Polyline polyline:MapsFragment.this.polylines) {
                    polyline.remove();
                }


            }
            Event userBirth = familyTree.root.getEventsInOrder()[0];

            if(selectedEvent!= null) {
                LatLng first = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(first));
                ArrayList<Event> selected = new ArrayList<Event>();
                selected.add(selectedEvent);
                MapsFragment.EventsAdapter eventsAdapters = new MapsFragment.EventsAdapter(selected);
                recyclerView.setAdapter(eventsAdapters);

                ArrayList<Event> filteredEventsUpdated = new ArrayList<Event>();
                ArrayList<Event> sameSpot = new ArrayList<Event>();
                for (Event event:filteredEvents) {
                    filteredEventsUpdated.add(event);
                    if (event.getLatitude() == first.latitude && event.getLongitude() == first.longitude) {
                        sameSpot.add(event);
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    }
                }
                for (Polyline polyline:MapsFragment.this.polylines) {
                    polyline.remove();
                }
                generateLines(googleMap,filteredEventsUpdated,sameSpot);
            }
            else {
                LatLng first = new LatLng(userBirth.getLatitude(), userBirth.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(first));
                for (Polyline polyline:MapsFragment.this.polylines) {
                    polyline.remove();
                }
                //generateLines(googleMap,filtered_Events,selectedEvent);
            }
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    LatLng location = marker.getPosition();
                    ArrayList<Event> filteredEventsUpdated = new ArrayList<Event>();
                    ArrayList<Event> eventsOneSpot = new ArrayList<>();
                    for (Event event:MapsFragment.this.filteredEvents) {
                        filteredEventsUpdated.add(event);
                        if (event.getLatitude() == location.latitude && event.getLongitude() == location.longitude) {
                            eventsOneSpot.add(event);
                            selectedEvent = event;
                            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        }
                    }
                    for (Polyline polyline:MapsFragment.this.polylines) {
                        polyline.remove();
                    }
                    generateLines(googleMap,filteredEventsUpdated,eventsOneSpot);


                    MapsFragment.EventsAdapter eventsAdapter = new MapsFragment.EventsAdapter(eventsOneSpot);
                    recyclerView.setAdapter(eventsAdapter);

                    return false;
                }
            });
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    for (Polyline polyline:MapsFragment.this.polylines) {
                        polyline.remove();
                    }
                }
            });
        }
    };


    private class EventsAdapter extends RecyclerView.Adapter<MapsFragment.EventsViewHolder> {
        private ArrayList<Event> events;

        EventsAdapter(ArrayList<Event> events) {
            this.events = events;
        }


        @NonNull
        @Override
        public MapsFragment.EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            view = getLayoutInflater().inflate(R.layout.event_item,parent,false);
            return new MapsFragment.EventsViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull MapsFragment.EventsViewHolder holder, int position) {
            if (position < events.size()) {
                holder.bind(events.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return events.size();
        }
    }

    private class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView details;
        private TextView eventName;
        private ImageView icon;

        private final int viewType;
        private Event event;
        private Person person;

        EventsViewHolder(View view, int viewType) {
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
            this.person = MapsFragment.this.familyTree.get(event.getPersonID()).person;
            sb.append(person.getFirstName());
            sb.append(" ");
            sb.append(person.getLastName());
            eventName.setText(sb.toString());
            Drawable markerIcon = new IconDrawable(getContext(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.black).sizeDp(40);
            icon.setImageDrawable(markerIcon);

        }


        @Override
        public void onClick(View v) {
            if(listener.getClass() == MainActivity.class) {
                Gson gson = new Gson();
                String setCache = gson.toJson(settings);
                String treeCache = gson.toJson(familyTree);
                String authCache = gson.toJson(familyTree.authtoken);
                String userCache = gson.toJson(familyTree.root);
                Intent switchActivityIntent = new Intent(getContext(), PersonActivity.class);
                switchActivityIntent.putExtra("setCache",setCache);
                switchActivityIntent.putExtra("treeCache",treeCache);
                switchActivityIntent.putExtra("authCache",authCache);
                switchActivityIntent.putExtra("userCache",userCache);

                String selectedPersonID = this.event.getPersonID();
                switchActivityIntent.putExtra("selected",selectedPersonID);
                startActivity(switchActivityIntent);
            }
            else {

            }
        }
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        if(getActivity().getClass() == MainActivity.class) {
            setHasOptionsMenu(true);
        }
        else {

        }


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        if(getActivity().getClass() == MainActivity.class) {
            inflater.inflate(R.menu.map_menu, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings_button) {
            Gson gson = new Gson();
            Intent switchActivityIntent = new Intent(getActivity(), SettingsActivity.class);

            String treeCache = gson.toJson(familyTree);
            String authCache = gson.toJson(familyTree.authtoken);
            String userCache = gson.toJson(familyTree.root);
            String settingCache = gson.toJson(settings);
            String eventCache = gson.toJson(selectedEvent);
            switchActivityIntent.putExtra("treeCache",treeCache);
            switchActivityIntent.putExtra("authCache",authCache);
            switchActivityIntent.putExtra("userCache",userCache);
            switchActivityIntent.putExtra("setCache",settingCache);
            switchActivityIntent.putExtra("eventCache",eventCache);
            startActivity(switchActivityIntent);
            return true;
        }
        else if(id == R.id.search_button) {
            //Search activity stuff here
            Gson gson = new Gson();
            Intent switchActivityIntent = new Intent(getActivity(), SearchActivity.class);


            String treeCache = gson.toJson(familyTree);
            String authCache = gson.toJson(familyTree.authtoken);
            String userCache = gson.toJson(familyTree.root);
            String settingCache = gson.toJson(settings);
            String filteredEventsCache = gson.toJson(filteredEvents);
            String filteredPeople = gson.toJson(familyTree.getFilteredPeople(filteredEvents));
            switchActivityIntent.putExtra("treeCache",treeCache);
            switchActivityIntent.putExtra("authCache",authCache);
            switchActivityIntent.putExtra("userCache",userCache);
            switchActivityIntent.putExtra("setCache",settingCache);
            switchActivityIntent.putExtra("filteredCache",filteredEventsCache);
            switchActivityIntent.putExtra("filteredPeopleCache",filteredPeople);
            startActivity(switchActivityIntent);
            return true;
        }
        else if (id == android.R.id.home){
            Intent switchActivityIntent = new Intent(getActivity(), MainActivity.class);
            Gson gson = new Gson();

            String treeCache = gson.toJson(familyTree);
            String authCache = gson.toJson(familyTree.authtoken);
            String userCache = gson.toJson(familyTree.root);
            String settingCache = gson.toJson(settings);
            switchActivityIntent.putExtra("treeCache",treeCache);
            switchActivityIntent.putExtra("authCache",authCache);
            switchActivityIntent.putExtra("userCache",userCache);
            switchActivityIntent.putExtra("setCache",settingCache);
            startActivity(switchActivityIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    private void generateLines(GoogleMap googleMap,ArrayList<Event> filteredEvents,ArrayList<Event> oneSpot) {
        for (Polyline polyline:MapsFragment.this.polylines) {
            polyline.remove();
        }
        for (Event event:oneSpot) {
            FamilyMemberNode person = familyTree.get(event.getPersonID());
            if(MapsFragment.this.settings.FamilyTreeLines == true) {
                if(person.father!=null) {
                    float width = 10;
                    LatLng point1 = new LatLng(event.getLatitude(), event.getLongitude());
                    Event fatherFirst = person.father.getEventsInOrder()[0];
                    if(filteredEvents.contains(fatherFirst)) {
                        LatLng point2 = new LatLng(fatherFirst.getLatitude(), fatherFirst.getLongitude());
                        Polyline polyline = googleMap.addPolyline(new PolylineOptions().add(point1).add(point2));
                        polyline.setWidth(width);
                        polyline.setColor(R.color.teal_200);
                        polylines.add(polyline);
                        familyTreeLineHelper(googleMap,person.father,(float) (width-1.5));
                    }
                    Event motherFirst = person.mother.getEventsInOrder()[0];
                    if(filteredEvents.contains(motherFirst)) {
                        LatLng point2 = new LatLng(motherFirst.getLatitude(), motherFirst.getLongitude());
                        Polyline polyline = googleMap.addPolyline(new PolylineOptions().add(point1).add(point2));
                        polyline.setWidth(width);
                        polyline.setColor(R.color.teal_200);
                        polylines.add(polyline);
                        familyTreeLineHelper(googleMap,person.mother,(float) (width-1.5));
                    }
                }
            }
            if(MapsFragment.this.settings.LifeStoryLines == true) {
                Event[] events = person.getEventsInOrder();
                float width = 10;
                if (events != null) {
                    if(events.length > 0) {
                        for (int i = 0; i < events.length - 1; i++) {
                            LatLng point1 = new LatLng(events[i].getLatitude(), events[i].getLongitude());
                            LatLng point2 = new LatLng(events[i+1].getLatitude(), events[i+1].getLongitude());
                            Polyline polyline = googleMap.addPolyline(new PolylineOptions().add(point1).add(point2));
                            width = (float) (width - 1.5);
                            polyline.setWidth(width);
                            polyline.setColor(R.color.female_icon);
                            polylines.add(polyline);
                        }
                    }
                }
            }
            if(MapsFragment.this.settings.SpouseLines == true) {
                Event[] spouseEvents = new Event[0];
                if(person.person.getSpouseID()!=null) {
                    spouseEvents = familyTree.get(person.person.getSpouseID()).getEventsInOrder();


                    float width = 10;
                    if (spouseEvents != null) {
                        if(filteredEvents.contains(spouseEvents[0])) {
                            LatLng point1 = new LatLng(event.getLatitude(), event.getLongitude());
                            LatLng point2 = new LatLng(spouseEvents[0].getLatitude(), spouseEvents[0].getLongitude());
                            Polyline polyline = googleMap.addPolyline(new PolylineOptions().add(point1).add(point2));
                            width = (float) (width - 1);
                            polyline.setWidth(width);
                            polyline.setColor(R.color.black);
                            polylines.add(polyline);
                        }
                    }
                }
            }


        }
    }






    private void familyTreeLineHelper(GoogleMap googleMap,FamilyMemberNode parent, float thickness){
        if(parent.father!=null) {
            if(thickness < 1) {
                thickness = 1;
            }
            Event event = parent.getEventsInOrder()[0];
            LatLng point1 = new LatLng(event.getLatitude(), event.getLongitude());
            Event fatherFirst = parent.father.getEventsInOrder()[0];
            LatLng point2 = new LatLng(fatherFirst.getLatitude(),fatherFirst.getLongitude());
            Polyline polyline = googleMap.addPolyline(new PolylineOptions().add(point1).add(point2));
            polyline.setWidth(thickness);
            polyline.setColor(R.color.teal_200);
            polylines.add(polyline);
            Event motherFirst = parent.mother.getEventsInOrder()[0];
            point2 = new LatLng(motherFirst.getLatitude(),motherFirst.getLongitude());polyline = googleMap.addPolyline(new PolylineOptions().add(point1).add(point2));
            polyline.setWidth(thickness);
            polyline.setColor(R.color.teal_200);
            polylines.add(polyline);
            familyTreeLineHelper(googleMap,parent.father,(float) (thickness-1.5));
            familyTreeLineHelper(googleMap,parent.mother,(float) (thickness-1.5));
        }
    }



}