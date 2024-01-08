package com.example.garagefinder;

//import library
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetGaragePassword extends AppCompatActivity {

    //declare variables
    EditText garageUsername, editPassword, editRePassword;
    Button btnSave;
    ImageView showPassword, reShowPassword;
    DatabaseReference userRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_garage_password);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //initialized the variables
        garageUsername = findViewById(R.id.garageUsername);
        editPassword = findViewById(R.id.garagePassword);
        editRePassword = findViewById(R.id.reGaragePassword);
        btnSave = findViewById(R.id.btnSave);

        showPassword = findViewById(R.id.showPassword);
        showPassword.setImageResource(R.drawable.baseline_visibility_off_24);

        //set eye icon to show and close password
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

        //initialized the variables
        reShowPassword = findViewById(R.id.reShowPassword);
        reShowPassword.setImageResource(R.drawable.baseline_visibility_off_24);

        //set eye icon to show and close Re-password
        reShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editRePassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    reShowPassword.setImageResource(R.drawable.baseline_visibility_off_24);
                } else {
                    editRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    reShowPassword.setImageResource(R.drawable.baseline_visibility_24);
                }
            }
        });

        //initialized the database references
        userRef = FirebaseDatabase.getInstance().getReference().child("GARAGE");

        //check the user inputs are corrects
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validationRePassword() | !validationPassword() | !validationUsername()){
                }else {
                    saveChanges();//new data save to databased using this function
                }
            }
        });

    }

    // This function validates the username field
    public Boolean validationUsername() {
        String val = garageUsername.getText().toString();
        if (val.isEmpty()) {
            garageUsername.setError("Username cannot be empty...");
            garageUsername.requestFocus();
            return false;
        } else if (val.contains(" ")) {
            garageUsername.setError("Username cannot contain spaces...");
            garageUsername.requestFocus();
            return false;
        } else if (val.matches(".*[!@#$%^&*()].*")) {
            garageUsername.setError("Username cannot contain special characters...");
            garageUsername.requestFocus();
            return false;
        } else {
            garageUsername.setError(null);
            return true;
        }
    }

    // This function validates the password field
    public Boolean validationPassword() {
        String val = editPassword.getText().toString();
        if (val.isEmpty()) {
            editPassword.setError("Password cannot be empty...");
            editPassword.requestFocus();
            return false;
        } else if (val.length() < 8) {
            editPassword.setError("Password should have at least 8 characters.");
            editPassword.requestFocus();
            return false;
        } else if (!val.matches(".*[a-z].*")) {
            editPassword.setError("Password should contain at least one lowercase letter.");
            editPassword.requestFocus();
            return false;
        } else if (!val.matches(".*[A-Z].*")) {
            editPassword.setError("Password should contain at least one uppercase letter.");
            editPassword.requestFocus();
            return false;
        } else if (!val.matches(".*[!@#$%^&*()].*")) {
            editPassword.setError("Password should contain at least one special character.");
            editPassword.requestFocus();
            return false;
        } else {
            editPassword.setError(null);
            return true;
        }
    }

    // This function validates the Re enter password field
    public Boolean validationRePassword() {
        String val = editPassword.getText().toString();
        String passwordVal = editRePassword.getText().toString();
        if (val.isEmpty()) {
            editRePassword.setError("Re-enter password cannot be empty...");
            editRePassword.requestFocus();
            return false;
        } else if (!val.equals(passwordVal)) {
            editRePassword.setError("Passwords do not match.");
            editRePassword.requestFocus();
            return false;
        } else {
            editRePassword.setError(null);
            return true;
        }
    }

    //using this function if all inputs are correct, those new data updated in database
    private void saveChanges() {

        //get data and set data to variables
        String enteredUsername = garageUsername.getText().toString();
        String newPassword = editPassword.getText().toString();

        //check the current username equals database username
        userRef.orderByChild("username").equalTo(enteredUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        userRef.child(userId).child("password").setValue(newPassword);
                    }

                    //display message and redirect to the garage login page
                    Toast.makeText(ResetGaragePassword.this, "Password updated successfully...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetGaragePassword.this, GarageLogin.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(ResetGaragePassword.this, "Username not found !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //if some error occur, this message will display
                Toast.makeText(ResetGaragePassword.this, "Error updating password: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

