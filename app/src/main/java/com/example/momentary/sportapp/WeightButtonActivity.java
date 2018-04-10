package com.example.momentary.sportapp;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Map;

//extends FragmentActivity
public class WeightButtonActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    android.support.v4.app.FragmentManager frag = getSupportFragmentManager();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeActivity Home = new HomeActivity();
                    frag.beginTransaction()
                            .replace(R.id.frameLayout, Home,"Home")
                            .commit();
                    return true;
                case R.id.navigation_store:
                    //Toast.makeText(getApplicationContext(), "這是一個Toast......", Toast.LENGTH_LONG).show();
                    StoreActivity Store = new StoreActivity();
                    frag.beginTransaction()
                            .replace(R.id.frameLayout, Store,"Store")
                            .commit();
                    return true;
                case R.id.navigation_map:
                    MapsActivity Map = new MapsActivity();
                    frag.beginTransaction()
                            .replace(R.id.frameLayout, Map,"Map")
                            .commit();
                    return true;
                case R.id.navigation_collection:
                    CollectionActivity collection =  new CollectionActivity();
                    frag.beginTransaction()
                            .replace(R.id.frameLayout, collection, "collection")
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_weight_button);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        HomeActivity Home=new HomeActivity();
        frag.beginTransaction()
                .replace(R.id.frameLayout, Home,"Home")
                //.add(R.id.frameLayout, Store,"store")
                .commit();
    }
}
