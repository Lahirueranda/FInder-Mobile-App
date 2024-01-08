package com.example.garagefinder;

//import the libraries
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class GarageDashboard extends AppCompatActivity {

    // Declaration of a BottomNavigationView variable
    BottomNavigationView bottomNavigationView;

    // Creation of a new Fragment instance
    GarageMapFragment GarageMapFragment = new GarageMapFragment();
    BannerFragment BannerFragment = new BannerFragment();
    GarageSettingFragment GarageSettingFragment = new GarageSettingFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_dashboard);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //initialized variable
        bottomNavigationView  = findViewById(R.id.bottom_navigation);

        //when login to the dashboard it is first loading
        getSupportFragmentManager().beginTransaction().replace(R.id.container,GarageMapFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            //set fragment activity to bottom naviagtion
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    //redirect to the map screen
                    case R.id.garageMap:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,GarageMapFragment).commit();
                        return true;

                    //redirect to the banner screen
                    case R.id.banner:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,BannerFragment).commit();
                        return true;

                    //redirect to the setting screen
                    case R.id.setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,GarageSettingFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}