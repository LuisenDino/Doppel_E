package com.example.doppel_e;

import android.os.Bundle;

import com.example.doppel_e.Stars.Model.StarModel;
import com.example.doppel_e.Stars.Utils.StarsDataBaseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;


import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    PointsFragment pointsFragment = new PointsFragment();
    ToDoListFragment toDoListFragment = new ToDoListFragment();
    StarsDataBaseHandler starsDBHandler;
    List<StarModel> starList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        starsDBHandler = new StarsDataBaseHandler(this);
        starList = starsDBHandler.getAll();
        starsDBHandler.pullStars();

        pointsFragment.pushData(starList);
        pointsFragment.setDbHandler(starsDBHandler);

        bottomNavigationView = findViewById(R.id.nav_view);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, toDoListFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId== R.id.list) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, toDoListFragment).commit();
                    return true;
                }else if (itemId== R.id.points) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, pointsFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }
}