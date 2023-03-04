package com.example.familymap.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymap.DataContainers.FamilyMemberNode;
import com.example.familymap.DataContainers.FamilyTree;
import com.example.familymap.DataContainers.Settings;
import com.example.familymap.R;
import com.google.gson.Gson;

import model.Event;

public class EventActivity extends AppCompatActivity implements MapsFragment.Listener {
    Gson gson = new Gson();
    String dataCache;
    String treeCache;
    String authCache;
    String userCache;
    String setCache;
    String selectedEvent;

    FamilyTree familyTree;
    Settings settings;
    Event selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        dataCache = getIntent().getStringExtra("dataCache");
        treeCache = getIntent().getStringExtra("treeCache");
        authCache = getIntent().getStringExtra("authCache");
        userCache = getIntent().getStringExtra("userCache");
        setCache = getIntent().getStringExtra("setCache");
        selectedEvent = getIntent().getStringExtra("selected");


        familyTree = (FamilyTree) gson.fromJson(treeCache,FamilyTree.class);
        familyTree.authtoken = (String) gson.fromJson(authCache,String.class);
        familyTree.root = (FamilyMemberNode) gson.fromJson(userCache,FamilyMemberNode.class);
        settings = (Settings) gson.fromJson(setCache,Settings.class);
        selected = (Event) gson.fromJson(selectedEvent,Event.class);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.eventFrameLayout);
        fragment = reload_map(familyTree,settings,selected);
        fragmentManager.beginTransaction().add(R.id.eventFrameLayout, fragment).commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    // Google gave me help with this :)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent switchActivityIntent = new Intent(EventActivity.this, MainActivity.class);
            Gson gson = new Gson();
            String settingString = gson.toJson(settings);
            switchActivityIntent.putExtra("setCache",settingString);
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


    private Fragment reload_map(FamilyTree familyTree, Settings settings,Event selected){
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.registerListener(this,familyTree,settings,selected);
        return mapsFragment;
    }

}