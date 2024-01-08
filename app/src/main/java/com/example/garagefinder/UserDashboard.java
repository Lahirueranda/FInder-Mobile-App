package com.example.garagefinder;

//import the libraries
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserDashboard extends AppCompatActivity {

    // Declaration of a BottomNavigationView variable
    BottomNavigationView bottomNavigationView;

    // Creation of a new Fragment instance
    MapsFragment MapsFragment = new MapsFragment();
    CompassFragment CompassFragment = new CompassFragment();
    UserSettingFragment UserSettingFragment = new UserSettingFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //initialized variable
        bottomNavigationView  = findViewById(R.id.bottom_navigation);

        //when login to the dashboard it is first loading
        getSupportFragmentManager().beginTransaction().replace(R.id.container,MapsFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    //redirect to the map screen
                    case R.id.map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,MapsFragment).commit();
                        return true;

                    //redirect to the compass screen
                    case R.id.compass:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,CompassFragment).commit();
                        return true;

                    //redirect to the setting screen
                    case R.id.setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,UserSettingFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}