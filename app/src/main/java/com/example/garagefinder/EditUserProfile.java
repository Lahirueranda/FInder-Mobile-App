package com.example.garagefinder;

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

public class EditUserProfile extends AppCompatActivity {

    //declare variables
    EditText  editEmail, editMobilenumber, editPassword;
    TextView editUsername;
    String usernameUser;
    ImageView showPassword;
    Button btnSave;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        //action bar hide
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //initialized the variables
        showPassword = findViewById(R.id.showPassword);
        showPassword.setImageResource(R.drawable.baseline_visibility_off_24);
        editPassword = findViewById(R.id.editPassword);

        //this is used for close and open eye icons in password field to show password
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.baseline_visibility_off_24);
                } else {
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.baseline_visibility_24);
                }
            }
        });

        //initialized database table
        userRef = FirebaseDatabase.getInstance().getReference().child("USER");

        //initialized variables
        editUsername = findViewById(R.id.editUserName);
        editEmail = findViewById(R.id.editEmail);
        editMobilenumber = findViewById(R.id.editMobileNumber);
        editPassword = findViewById(R.id.editPassword);

        //initialized button
        btnSave = findViewById(R.id.btnSave);

        //this function is used to get data from databased and set data to variables
        showData();

        //when click the save button validate the inputs data and it will send to the database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationPassword() | !validationMobilenumber() | !validationEmail()){
                } else {
                    saveChanges();
                }
            }
        });
    }


    //this function is used to get data from databased and set those data to variables.
    private void showData() {

        //get username from user login page
        usernameUser = UserLogin.userUsername;

        //database initialized
        Query CheckUserDatabase = userRef.orderByChild("username").equalTo(usernameUser);

        CheckUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    //check the username are equals to the user entered username
                    String UserNameFromDB = snapshot.child(usernameUser).child("username").getValue(String.class);
                    if (UserNameFromDB.equals(usernameUser)){

                        //get data from database
                        String emailFromDB = snapshot.child(usernameUser).child("email").getValue(String.class);
                        String mobilenumberFromDB = snapshot.child(usernameUser).child("mobilenumber").getValue(String.class);
                        String passwordFromDB = snapshot.child(usernameUser).child("password").getValue(String.class);

                        //set data to variables
                        editUsername.setText(UserNameFromDB);
                        editEmail.setText(emailFromDB);
                        editMobilenumber.setText(mobilenumberFromDB);
                        editPassword.setText(passwordFromDB);

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
        String val = editEmail.getText().toString();
        if (val.isEmpty()){
            editEmail.setError("Email cannot be empty...");
            editEmail.requestFocus();
            return false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            editEmail.setError("Please enter a valid email address...");
            editEmail.requestFocus();
            return false;
        }else{
            editEmail.setError(null);
            return true;
        }
    }

    // This function is used to validates the mobile number
    public Boolean validationMobilenumber(){
        String val =  editMobilenumber.getText().toString();
        if (val.isEmpty()){
            editMobilenumber.setError("Mobile number cannot be empty.");
            editMobilenumber.requestFocus();
            return false;
        }else if(val.length() != 10) {
            editMobilenumber.setError("Mobile number have only 10 characters.");
            editMobilenumber.requestFocus();
            return false;
        }else{
            editMobilenumber.setError(null);
            return true;
        }
    }

    // This function is used to validates the password
    public Boolean validationPassword(){
        String val = editPassword.getText().toString();
        if (val.isEmpty()){
            editPassword.setError("Password cannot be empty...");
            editPassword.requestFocus();
            return false;
        }else if(val.length() < 8){
            editPassword.setError("Password should have at least 8 characters.");
            editPassword.requestFocus();
            return false;
        }else if(!val.matches(".*[a-z].*")){
            editPassword.setError("Password should contain at least one lowercase letter.");
            editPassword.requestFocus();
            return false;
        }else if(!val.matches(".*[A-Z].*")){
            editPassword.setError("Password should contain at least one uppercase letter.");
            editPassword.requestFocus();
            return false;
        }else if(!val.matches(".*[!@#$%^&*()].*")){
            editPassword.setError("Password should contain at least one special character.");
            editPassword.requestFocus();
            return false;
        }else{
            editPassword.setError(null);
            return true;
        }
    }

    //if all inputs are valid the data send to the database
    private void saveChanges() {
        //get data and assign to variables
        String newEmail = editEmail.getText().toString();
        String newMobilenumber = editMobilenumber.getText().toString();
        String newPassword = editPassword.getText().toString();

        //send data according to the username
        userRef.child(usernameUser).child("email").setValue(newEmail);
        userRef.child(usernameUser).child("mobilenumber").setValue(newMobilenumber);
        userRef.child(usernameUser).child("password").setValue(newPassword);

        //toast message display
        Toast.makeText(EditUserProfile.this, "Changes saved successfully !", Toast.LENGTH_SHORT).show();
    }
}
