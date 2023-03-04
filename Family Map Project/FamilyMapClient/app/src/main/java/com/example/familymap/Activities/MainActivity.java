package com.example.familymap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;


import com.example.familymap.DataContainers.FamilyMemberNode;
import com.example.familymap.DataContainers.FamilyTree;
import com.example.familymap.DataContainers.MainActivityViewModel;
import com.example.familymap.DataContainers.Settings;
import com.example.familymap.R;
import com.google.gson.Gson;

import model.Event;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener, MapsFragment.Listener {

    private LoginFragment loginFragment;
    private MapsFragment mapsFragment;
    private FamilyTree familyTree;
    private Settings settings = new Settings();
    private Event selectedEvent;
    Gson gson = new Gson();

    private MainActivityViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String treeString = gson.toJson(familyTree);
        String set = gson.toJson(settings);
        outState.putString("Saved tree",treeString);
        outState.putString("set",set);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        selectedEvent = gson.fromJson(getIntent().getStringExtra("eventCache"),Event.class);
        settings = gson.fromJson(getIntent().getStringExtra("setCache"),Settings.class);
        familyTree = gson.fromJson(getIntent().getStringExtra("treeCache"), FamilyTree.class);

        if(savedInstanceState!=null) {
            familyTree = gson.fromJson(savedInstanceState.getString("Saved tree"),FamilyTree.class);
            settings = gson.fromJson(savedInstanceState.getString("set"),Settings.class);
        }

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);

        if (familyTree != null) {


            familyTree.authtoken = gson.fromJson(getIntent().getStringExtra("authCache"), String.class);
            familyTree.root = gson.fromJson(getIntent().getStringExtra("userCache"), FamilyMemberNode.class);
        }


        if (fragment == null) {

            if(familyTree==null) {
                fragment = createFirstElement();
                fragmentManager.beginTransaction().add(R.id.fragmentFrameLayout, fragment).commit();
            }
            else {
                fragment = reload_map(familyTree,settings,selectedEvent);
                fragmentManager.beginTransaction().add(R.id.fragmentFrameLayout, fragment).commit();
            }
        }
        else {
            if(fragment instanceof LoginFragment) {
                ((LoginFragment) fragment).registerListener(this);
            }
            else if (fragment instanceof MapsFragment) {
                ((MapsFragment) fragment).registerListener(this,familyTree,settings);
            }
        }

    }

    private Fragment createFirstElement() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.registerListener(this);
        return loginFragment;
    }

    private Fragment reload_map(FamilyTree familyTree, Settings settings, Event selectedEvent){
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.registerListener(this,familyTree,settings, selectedEvent);
        return mapsFragment;
    }


    public void notifyLogin(FamilyTree familyTree) {
        this.familyTree = familyTree;
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        MapsFragment fragment = new MapsFragment();
        fragment.registerListener(this, this.familyTree);
        fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout, fragment).commit();
    }




}