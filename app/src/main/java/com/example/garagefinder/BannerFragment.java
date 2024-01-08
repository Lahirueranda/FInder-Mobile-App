package com.example.garagefinder;

//library imports
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.garagefinder.GarageHelperClass;
import com.example.garagefinder.GarageLogin;
import com.example.garagefinder.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BannerFragment extends Fragment {

    //Declaring variables
    double ourLatitude;
    double ourLongitude;
    TextView garageUsernameInput;
    EditText garageNameInput, garageContactInput, garageDateInput, garageServiceInput,longitude, latitude;
    Button  btnGet,btnAddToDetails, btnEdit, btnDelete;
    FirebaseDatabase database;
    DatabaseReference reference;

    // Initialize variables
    private final static int REQUEST_CODE = 100;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_banner, container, false);

        // Initialize firebase database 
        database = FirebaseDatabase.getInstance();

        // Get database reference
        reference = FirebaseDatabase.getInstance().getReference("GARAGE_DETAILS");

        // Initialize EditText views and button
        garageUsernameInput = v.findViewById(R.id.garageUserNameInput);
        garageNameInput = v.findViewById(R.id.garageNameInput);
        garageContactInput = v.findViewById(R.id.garageContactInput);
        garageDateInput = v.findViewById(R.id.garageDateInput);
        garageServiceInput = v.findViewById(R.id.garageServiceInput);
        longitude = v.findViewById(R.id.longitude);
        latitude = v.findViewById(R.id.latitude);

        //set garage username to new variable garage username come from garage login page
        garageUsernameInput.setText(GarageLogin.garagesUsernames);


        //get button initialize
        btnGet = v.findViewById(R.id.btnGet);

        //when we click the get button uts get the Longitude and Latitude and set for variables
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call get location function and get longitude and latitude
                getLastLocation();
                longitude.setText((Double.toString(ourLongitude)));
                latitude.setText((Double.toString(ourLatitude)));

            }
        });

        //Add to details button Initialize
        btnAddToDetails = v.findViewById(R.id.btnAddToDetails);

        //when we click the Add to details button after the validation those data pass to the database
        btnAddToDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationLongitude() | !validationLatitude() | !validationServiceInput() | !validationDateInput() | !validationContactNumber() | !validationName()) {
                }else{
                    bannerPublishConfirmationDialog();
                }
            }
        });


        //Initialize edit button
        btnEdit = v.findViewById(R.id.btnEdit);

        //when we click te edit button after the validation part its will update from database
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationLongitude() | !validationLatitude() | !validationServiceInput() | !validationDateInput() | !validationContactNumber() | !validationName()){
                } else {
                    bannerEditConfirmationDialog(); //call banner edit function
                }
            }
        });

        //delete button initialized
        btnDelete = v.findViewById(R.id.btnDelete);

        //when click the delete button banner data delete from database.
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirmationDialog();
            }
        });


        //get gps data and set data
        getDataFromDatabase();
        getLastLocation();
        
        return v;
    }




    // validation garage name
    public Boolean validationName() {
        String val = garageNameInput.getText().toString();
        if (val.isEmpty()) {
            garageNameInput.setError("Garage name cannot be empty...");
            garageNameInput.requestFocus();
            return false;
        } else {
            garageNameInput.setError(null);
            return true;
        }
    }


    //validation garage contact number
    public Boolean validationContactNumber(){
        String val = garageContactInput.getText().toString();
        if (val.isEmpty()){
            garageContactInput.setError("Mobile number cannot be empty.");
            garageContactInput.requestFocus();
            return false;
        }else if(val.length() != 10) {
            garageContactInput.setError("Mobile number have only 10 characters.");
            garageContactInput.requestFocus();
            return false;
        }else{
            garageContactInput.setError(null);
            return true;
        }
    }


    //validation date and time inputs
    public Boolean validationDateInput() {
        String val = garageDateInput.getText().toString();
        if (val.isEmpty()) {
            garageDateInput.setError("Date and time cannot be empty...");
            garageDateInput.requestFocus();
            return false;
        }else{
            garageDateInput.setError(null);
            return true;
        }
    }


    //validation service inputs
    public Boolean validationServiceInput() {
        String val = garageServiceInput.getText().toString();
        if (val.isEmpty()) {
            garageServiceInput.setError("Garage services cannot be empty...");
            garageServiceInput.requestFocus();
            return false;
        } else {
            garageServiceInput.setError(null);
            return true;
        }
    }


    //validation latitude
    public Boolean validationLatitude() {
        String val = latitude.getText().toString();

        if (val.isEmpty()) {
            latitude.setError("Latitude cannot be empty...");
            latitude.requestFocus();
            return false;
        } else {
            double vall = Double.parseDouble(val);
            if (vall == 0) {
                latitude.setError("Latitude cannot be '0' please please get your current location or enter valid Latitude...");
                latitude.requestFocus();
                return false;
            } else {
                latitude.setError(null);
                return true;
            }
        }
    }

    //validation longitude
    public Boolean validationLongitude() {
        String val = longitude.getText().toString();
        if (val.isEmpty()) {
            longitude.setError("Longitude cannot be empty...");
            longitude.requestFocus();
            return false;
        } else {
            double vall = Double.parseDouble(val);
            if (vall == 0) {
                longitude.setError("Longitude cannot be '0' please please get your current location or enter valid Longitude...");
                longitude.requestFocus();
                return false;
            }else {
                longitude.setError(null);
                return true;
            }
        }
    }



    // using this function data get from database and set to the input fields
    public void getDataFromDatabase(){

        //get garage user name and its assign to garage username variable
        String SetGarageUsername = garageUsernameInput.getText().toString();
        Query CheckUserDatabase = reference.orderByChild("garageUserName").equalTo(SetGarageUsername);

        //check the garage username same as the database's username
        CheckUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    garageUsernameInput.setError(null);

                    //get garage username from database
                    String GarageUserNameFromDB = snapshot.child(SetGarageUsername).child("garageUserName").getValue(String.class);
                    if (GarageUserNameFromDB.equals(SetGarageUsername)){
                        garageUsernameInput.setError(null);

                        //get values from database and its assign to variables
                        String garageContactNumber = snapshot.child(SetGarageUsername).child("garageContactNumber").getValue(String.class);
                        String garageOpenDateTime = snapshot.child(SetGarageUsername).child("garageOpenDateTime").getValue(String.class);
                        String garageServices = snapshot.child(SetGarageUsername).child("garageServices").getValue(String.class);
                        String garageName = snapshot.child(SetGarageUsername).child("garagename").getValue(String.class);
                        String getLatitude = snapshot.child(SetGarageUsername).child("latitude").getValue(String.class);
                        String getLongitude = snapshot.child(SetGarageUsername).child("longitude").getValue(String.class);

                        //set values to variable
                        garageNameInput.setText(garageName);
                        garageContactInput.setText(garageContactNumber);
                        garageDateInput.setText(garageOpenDateTime);
                        garageServiceInput.setText(garageServices);
                        latitude.setText(getLatitude);
                        longitude.setText(getLongitude);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }



    //this function is used to identify our current location and get longitude and latitude
    private void getLastLocation() {
        FusedLocationProviderClient fusedLocationProviderClient;

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            ourLatitude = addresses.get(0).getLatitude();
                            ourLongitude = addresses.get(0).getLongitude();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            askPermission(); //if device location is off asked the devices location to on
        }
    }

    //location on function
    private void askPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_CODE);
    }



    //when click the data insert button, after pop up the confirmation dialog to insert data
    private void bannerPublishConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Publish Your Banner !");
        builder.setMessage("Are you sure you want to publish your banner ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dataInsert(); //call data insert function
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    //in the confirmation dialog if the user gives the Yes button data send to the database through this function
    public void dataInsert() {

        //get data and those data assign to the variables
        String setGarageUsername = garageUsernameInput.getText().toString();
        String setGarageNameInput = garageNameInput.getText().toString();
        String setGarageContactInput = garageContactInput.getText().toString();
        String setGarageDateInput = garageDateInput.getText().toString();
        String setGarageServiceInput = garageServiceInput.getText().toString();
        String setLongitude = longitude.getText().toString();
        String setLatitude = latitude.getText().toString();

        //send data to database using DetailsDataSend class
        GarageHelperClass garageHelperClass = new GarageHelperClass(setGarageUsername, setGarageNameInput, setGarageContactInput, setGarageDateInput, setGarageServiceInput, setLongitude, setLatitude);
        reference.child(setGarageUsername).setValue(garageHelperClass);

        //successfully message display
        Toast.makeText(getContext(), "Data Added successfully !", Toast.LENGTH_SHORT).show();
    }




    //when click the edit button it will popup the confirmation dialog
    private void bannerEditConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Edit Your Banner !");
        builder.setMessage("Are you sure you want to Edit your banner ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveChanges(); //call save changers function
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    //if user gives the Yes button in confirmation dialog updated data changed from the database
    private void saveChanges() {

        //get data and those data assign to the variables
        String garageUsername = garageUsernameInput.getText().toString();
        String newGarageName = garageNameInput.getText().toString();
        String newContactNumber = garageContactInput.getText().toString();
        String newDateInput = garageDateInput.getText().toString();
        String newServiceInput = garageServiceInput.getText().toString();
        String newLongitude = longitude.getText().toString();
        String newLatitude =  latitude.getText().toString();

        //new data updated from database according to the garage username
        reference.child(garageUsername).child("garagename").setValue(newGarageName);
        reference.child(garageUsername).child("garageContactNumber").setValue(newContactNumber);
        reference.child(garageUsername).child("garageOpenDateTime").setValue(newDateInput);
        reference.child(garageUsername).child("garageServices").setValue(newServiceInput);
        reference.child(garageUsername).child("longitude").setValue(newLongitude);
        reference.child(garageUsername).child("latitude").setValue(newLatitude);

        //successfully message displayed
        Toast.makeText(getContext(), "Changers saved successfully !", Toast.LENGTH_SHORT).show();
    }



    //when user click the delete button it will pop up the confirmation dialog
    private void deleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Delete Your Banner !");
        builder.setMessage("Are you sure you want to permanently delete your banner ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteBanner(); //call to delete vanner function
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    //if user gives the Yes button banner details delete from database
    private void deleteBanner() {
        String SetGarageUsername = garageUsernameInput.getText().toString();
        reference.child(SetGarageUsername).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Banner deleted successfully !", Toast.LENGTH_SHORT).show();
                clearFields(); //call the clear fields function
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to delete banner. Please try again...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //after deleting data the inputs fields are clear
    private void clearFields() {
        garageNameInput.setText("");
        garageContactInput.setText("");
        garageDateInput.setText("");
        garageServiceInput.setText("");
        longitude.setText("");
        latitude.setText("");
    }

}
