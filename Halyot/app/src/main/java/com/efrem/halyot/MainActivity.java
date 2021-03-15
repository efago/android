package com.efrem.halyot;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity, new CallFragment()).commitNow();
            getSupportActionBar().setTitle("News");
        }


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.bottom_nav_news:
                        fragment = new NewsFragment();
                        getSupportActionBar().setTitle("News");
                        break;
                    case R.id.bottom_nav_call:
                        fragment= new CallFragment();
                        getSupportActionBar().setTitle("Call");
                        break;
                    case R.id.bottom_nav_settings:
                        getSupportActionBar().setTitle("Settings");
                        fragment = new SettingsFragment();
                }
                return loadFragment(fragment);
            }
        });
        bottomNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.bottom_nav_news){}
                    //scroll to top
            }
        });
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, fragment).commit();
            return true;
        }
        return false;
    }
}

