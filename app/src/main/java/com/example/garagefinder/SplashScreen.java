package com.example.garagefinder;

//import library
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.LocationServices;

public class SplashScreen extends AppCompatActivity {

    //initialized the variables
    private static final int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, check location services
            checkLocationServices();
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, check location services
                checkLocationServices();
            } else {
                //nothing
            }
        }
    }


    //this function is used to check the location service
    private void checkLocationServices() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        LocationServices.getSettingsClient(this).checkLocationSettings(builder.build()).addOnSuccessListener(this, locationSettingsResponse -> {

                    //Location services are enabled, proceed to the next screen
                    goToNextScreen();
                })

                .addOnFailureListener(this, e -> {
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        int statusCode = apiException.getStatusCode();
                        if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            // Location services are disabled, showing a system dialog
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) apiException;
                                resolvableApiException.startResolutionForResult(SplashScreen.this, REQUEST_CODE);
                            } catch (Exception ex) {
                                Log.e("SplashScreen", "Error starting resolution for location settings.", ex);
                            }
                        } else {
                            //nothing
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Location services are enabled, go to the next screen
                goToNextScreen();
            } else {
                // Location services are disabled, handle it accordingly
            }
        }
    }

    //if the location are enable go to next screen and also check the who are login
    private void goToNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MySharedPreferences sharedPreferences = new MySharedPreferences(getApplicationContext());
                if(sharedPreferences.isLoggedIn()){

                    //check the user login, in shared presences
                    if( "user".equals(sharedPreferences.getUserType())) {
                        UserLogin.setUsersUsername(sharedPreferences.getUser());
                        Intent intent = new Intent(SplashScreen.this, UserDashboard.class);
                        startActivity(intent);
                        finish();
                    }

                    //check the garage login, in shared presences
                    if("garage".equals(sharedPreferences.getUserType())){
                        GarageLogin.setGarageUsername(sharedPreferences.getUser());
                        Intent intent = new Intent(SplashScreen.this, GarageDashboard.class);
                        startActivity(intent);
                        finish();
                    }

                } else{
                    //parsing the SelectUserRollWhenLogin screen
                    Intent intent = new Intent(SplashScreen.this, SelectUserRollWhenLogin.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000); //delay time 2000 ms
    }
}
