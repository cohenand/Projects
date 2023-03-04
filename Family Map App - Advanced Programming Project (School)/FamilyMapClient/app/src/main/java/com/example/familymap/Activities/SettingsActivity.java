package com.example.familymap.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.familymap.DataContainers.Settings;
import com.example.familymap.R;
import com.google.gson.Gson;

public class SettingsActivity extends AppCompatActivity {

    boolean lifeStoryLines;
    boolean familyTreeLines;
    boolean spouseLines;
    boolean fathersFilter;
    boolean mothersFilter;
    boolean maleEvents;
    boolean femaleEvents;

    String dataCache;
    String treeCache;
    String authCache;
    String userCache;
    String setCache;
    Settings set;
    String eventCache;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Gson gson = new Gson();
        dataCache = getIntent().getStringExtra("dataCache");
        treeCache = getIntent().getStringExtra("treeCache");
        authCache = getIntent().getStringExtra("authCache");
        userCache = getIntent().getStringExtra("userCache");
        setCache = getIntent().getStringExtra("setCache");
        eventCache = getIntent().getStringExtra("eventCache");

        set = gson.fromJson(setCache,Settings.class);

        View.OnClickListener switchListener = new View.OnClickListener() {
            Switch lifeStorySwitch = (Switch) findViewById(R.id.lifeStorySwitch);
            Switch familyStorySwitch = (Switch) findViewById(R.id.familyTreeSwitch);
            Switch spouseSwitch = (Switch) findViewById(R.id.spouseSwitch);
            Switch fathersSwitch = (Switch) findViewById(R.id.fathersSwitch);
            Switch mothersSwitch = (Switch) findViewById(R.id.mothersSwitch);
            Switch maleSwitch = (Switch) findViewById(R.id.maleEventsSwitch);
            Switch femaleSwitch = (Switch) findViewById(R.id.femaleEventsSwitch);
            @Override
            public void onClick(View v) {
                lifeStoryLines = lifeStorySwitch.isChecked();
                familyTreeLines = familyStorySwitch.isChecked();
                spouseLines = spouseSwitch.isChecked();
                fathersFilter = fathersSwitch.isChecked();
                mothersFilter = mothersSwitch.isChecked();
                maleEvents = maleSwitch.isChecked();
                femaleEvents = femaleSwitch.isChecked();
            }
        };

        Switch lifeStorySwitch = (Switch) findViewById(R.id.lifeStorySwitch);
        lifeStorySwitch.setChecked(set.LifeStoryLines);
        lifeStorySwitch.setOnClickListener(switchListener);

        Switch familyStorySwitch = (Switch) findViewById(R.id.familyTreeSwitch);
        familyStorySwitch.setChecked(set.FamilyTreeLines);
        familyStorySwitch.setOnClickListener(switchListener);

        Switch spouseSwitch = (Switch) findViewById(R.id.spouseSwitch);
        spouseSwitch.setChecked(set.SpouseLines);
        spouseSwitch.setOnClickListener(switchListener);

        Switch fathersSwitch = (Switch) findViewById(R.id.fathersSwitch);
        fathersSwitch.setChecked(set.FathersSide);
        fathersSwitch.setOnClickListener(switchListener);

        Switch mothersSwitch = (Switch) findViewById(R.id.mothersSwitch);
        mothersSwitch.setChecked(set.MothersSide);
        mothersSwitch.setOnClickListener(switchListener);

        Switch maleSwitch = (Switch) findViewById(R.id.maleEventsSwitch);
        maleSwitch.setChecked(set.MaleEvents);
        maleSwitch.setOnClickListener(switchListener);

        Switch femaleSwitch = (Switch) findViewById(R.id.femaleEventsSwitch);
        femaleSwitch.setChecked(set.FemaleEvents);
        femaleSwitch.setOnClickListener(switchListener);

        lifeStoryLines = lifeStorySwitch.isChecked();
        familyTreeLines = familyStorySwitch.isChecked();
        spouseLines = spouseSwitch.isChecked();
        fathersFilter = fathersSwitch.isChecked();
        mothersFilter = mothersSwitch.isChecked();
        maleEvents = maleSwitch.isChecked();
        femaleEvents = femaleSwitch.isChecked();

        TextView logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(SettingsActivity.this, MainActivity.class);
                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(switchActivityIntent);
            }
        });




        if (savedInstanceState != null) {

        }
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
            Intent switchActivityIntent = new Intent(SettingsActivity.this, MainActivity.class);
            Settings settings = new Settings(lifeStoryLines,familyTreeLines,spouseLines,fathersFilter,mothersFilter,maleEvents,femaleEvents);
            Gson gson = new Gson();
            String settingString = gson.toJson(settings);
            switchActivityIntent.putExtra("setCache",settingString);
            switchActivityIntent.putExtra("dataCache",dataCache);
            switchActivityIntent.putExtra("treeCache",treeCache);
            switchActivityIntent.putExtra("authCache",authCache);
            switchActivityIntent.putExtra("userCache",userCache);
            switchActivityIntent.putExtra("eventCache",eventCache);
            startActivity(switchActivityIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}