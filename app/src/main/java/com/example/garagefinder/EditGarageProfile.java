package com.example.garagefinder;

//import libraries
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditGarageProfile extends AppCompatActivity {

    //Declaring variables
    TextView editGarageUsername;
    EditText editGarageEmail, editGarageMobilenumber, editGaragePassword;
    String garageUsername;
    Button  btnSaveChanges;
    ImageView showPassword;
    DatabaseReference garageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_garage_profile);

        //action bar hide
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //initialized variables
        showPassword = findViewById(R.id.showPassword);
        showPassword.setImageResource(R.drawable.baseline_visibility_off_24);
        editGaragePassword = findViewById(R.id.editPassword);

        //this is used for close and open eye icons in password field  to show password
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editGaragePassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editGaragePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.baseline_visibility_off_24);
                } else {
                    editGaragePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.baseline_visibility_24);
                }
            }
        });

        //initialized database table
        garageRef = FirebaseDatabase.getInstance().getReference().child("GARAGE");

        //initialized variables
        editGarageUsername = findViewById(R.id.editGarageUsername);
        editGarageEmail = findViewById(R.id.editEmail);
        editGarageMobilenumber = findViewById(R.id.editMobileNumber);
        editGaragePassword = findViewById(R.id.editPassword);

        //initialized button
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        //this function is used to get data from databased and set data to variables
        showData();

        //when click the save button validate the inputs data and it will send to the database
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationPassword() | !validationMobileNumber() | !validationEmail()){
                } else {
                    saveChanges(); //data send function
                }
            }
        });
    }


    //this function is used to get data from databased and set those data to variables.
    private void showData() {

        //get username from user login page
        garageUsername = GarageLogin.garagesUsernames;

        //database initialized
        Query checkUserDatabase = garageRef.orderByChild("username").equalTo(garageUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    //check the username are equals to the user entered username
                    String userNameFromDB = snapshot.child(garageUsername).child("username").getValue(String.class);
                    if (userNameFromDB.equals(garageUsername)){

                        //get data from database
                        String emailFromDB = snapshot.child(garageUsername).child("email").getValue(String.class);
                        String mobilenumberFromDB = snapshot.child(garageUsername).child("mobilenumber").getValue(String.class);
                        String passwordFromDB = snapshot.child(garageUsername).child("password").getValue(String.class);

                        //set data to variables
                        editGarageUsername.setText(userNameFromDB);
                        editGarageEmail.setText(emailFromDB);
                        editGarageMobilenumber.setText(mobilenumberFromDB);
                        editGaragePassword.setText(passwordFromDB);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }


    // This function is used to validates the email
    public Boolean validationEmail(){
        String val = editGarageEmail.getText().toString();
        if (val.isEmpty()){
            editGarageEmail.setError("Email cannot be empty...");
            editGarageEmail.requestFocus();
            return false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            editGarageEmail.setError("Please enter a valid email address...");
            editGarageEmail.requestFocus();
            return false;
        }else{
            editGarageEmail.setError(null);
            return true;
        }
    }

    // This function is used to validates the mobile number
    public Boolean validationMobileNumber(){
        String val =  editGarageMobilenumber.getText().toString();
        if (val.isEmpty()){
            editGarageMobilenumber.setError("Mobile number cannot be empty.");
            editGarageMobilenumber.requestFocus();
            return false;
        }else if(val.length() != 10) {
            editGarageMobilenumber.setError("Mobile number have only 10 characters.");
            editGarageMobilenumber.requestFocus();
            return false;
        }else{
            editGarageMobilenumber.setError(null);
            return true;
        }
    }

    // This function is used to validates the password
    public Boolean validationPassword(){
        String val = editGaragePassword.getText().toString();
        if (val.isEmpty()){
            editGaragePassword.setError("Password cannot be empty...");
            editGaragePassword.requestFocus();
            return false;
        }else if(val.length() < 8){
            editGaragePassword.setError("Password should have at least 8 characters.");
            editGaragePassword.requestFocus();
            return false;
        }else if(!val.matches(".*[a-z].*")){
            editGaragePassword.setError("Password should contain at least one lowercase letter.");
            editGaragePassword.requestFocus();
            return false;
        }else if(!val.matches(".*[A-Z].*")){
            editGaragePassword.setError("Password should contain at least one uppercase letter.");
            editGaragePassword.requestFocus();
            return false;
        }else if(!val.matches(".*[!@#$%^&*()].*")){
            editGaragePassword.setError("Password should contain at least one special character.");
            editGaragePassword.requestFocus();
            return false;
        }else{
            editGaragePassword.setError(null);
            return true;
        }
    }


    //if all inputs are valid the data send to the database
    private void saveChanges() {
        //get data and assign to variables
        String newEmail = editGarageEmail.getText().toString();
        String newMobilenumber = editGarageMobilenumber.getText().toString();
        String newPassword = editGaragePassword.getText().toString();

        //send data according to the username
        garageRef.child(garageUsername).child("email").setValue(newEmail);
        garageRef.child(garageUsername).child("mobilenumber").setValue(newMobilenumber);
        garageRef.child(garageUsername).child("password").setValue(newPassword);

        //toast message display
        Toast.makeText(EditGarageProfile.this, "Changes saved successfully !", Toast.LENGTH_SHORT).show();
    }
}
