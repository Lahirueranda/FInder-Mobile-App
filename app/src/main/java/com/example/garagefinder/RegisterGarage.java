package com.example.garagefinder;

//import libraries
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterGarage extends AppCompatActivity {

    //Declaring variables
    Button btnGarageSignUp;
    TextView terms;
    EditText garageUsername,garageEmail,garageMobilenumber,garagePassword,garageRePassword;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_garage);

        //database initialized
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("GARAGE");

        //hide actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //terms text initialized
        terms = findViewById(R.id.terms);

        //when click the terms it will direct to the PrivacyPolicy page
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterGarage.this,PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        //initialized the variables
        ImageView showPassword = findViewById(R.id.showPassword);
        showPassword.setImageResource(R.drawable.baseline_visibility_off_24);
        garagePassword = findViewById(R.id.garagePassword);

        //set eye icon to show and close password
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (garagePassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    garagePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.baseline_visibility_off_24);
                } else {
                    garagePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.baseline_visibility_24);
                }
            }
        });

        //initialized the variables
        ImageView showRePassword = findViewById(R.id.showRePassword);
        showRePassword.setImageResource(R.drawable.baseline_visibility_off_24);
        garageRePassword = findViewById(R.id.garageRePassword);

        //set eye icon to show and close Re-password
        showRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (garageRePassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    garageRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showRePassword.setImageResource(R.drawable.baseline_visibility_off_24);
                } else {
                    garageRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showRePassword.setImageResource(R.drawable.baseline_visibility_24);
                }
            }
        });

        //initialized the variables
        garageUsername = findViewById(R.id.garageUserName);
        garageEmail = findViewById(R.id.garageEmail);
        garageMobilenumber = findViewById(R.id.garageMobilenumber);
        garagePassword = findViewById(R.id.garagePassword);
        garageRePassword = findViewById(R.id.garageRePassword);
        btnGarageSignUp = findViewById(R.id.btnGarageSignUp);

        //check the user inputs are corrects
        btnGarageSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationRePassword() | !validationPassword() | !validationMobilenumber() | !validationEmail()  | !validationUsername()){
                } else {
                    dataSend(); //data send to databased using this function
                }
            }
        });
    }


    // This function validates the username field
    public Boolean validationUsername(){
        String val = garageUsername.getText().toString();
        if (val.isEmpty()){
            garageUsername.setError("Username cannot be empty...");
            garageUsername.requestFocus();
            return false;
        }else if(val.contains(" ")) {
            garageUsername.setError("Username cannot contain spaces...");
            garageUsername.requestFocus();
            return false;
        }else if(val.matches(".*[!@#$%^&*()].*")) {
            garageUsername.setError("Username cannot content the special character...");
            garageUsername.requestFocus();
            return false;
        }else{
            garageUsername.setError(null);
            return true;
        }
    }


    // This function validates the email field
    public Boolean validationEmail(){
        String val =  garageEmail.getText().toString();
        if (val.isEmpty()){
            garageEmail.setError("Email cannot be empty...");
            garageEmail.requestFocus();
            return false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            garageEmail.setError("Please enter a valid email address...");
            garageEmail.requestFocus();
            return false;
        }else{
            garageEmail.setError(null);
            return true;
        }
    }


    // This function validates the mobile number field.
    public Boolean validationMobilenumber(){
        String val = garageMobilenumber.getText().toString();
        if (val.isEmpty()){
            garageMobilenumber.setError("Mobile number cannot be empty.");
            garageMobilenumber.requestFocus();
            return false;
        }else if(val.length() != 10) {
            garageMobilenumber.setError("Mobile number have only 10 characters.");
            garageMobilenumber.requestFocus();
            return false;
        }else{
            garageMobilenumber.setError(null);
            return true;
        }
    }



    // This function validates the password field.
    public Boolean validationPassword(){
        String val = garagePassword.getText().toString();
        if (val.isEmpty()){
            garagePassword.setError("Password cannot be empty...");
            garagePassword.requestFocus();
            return false;
        }else if(val.length() < 8){
            garagePassword.setError("Password should have at least 8 characters.");
            garagePassword.requestFocus();
            return false;
        }else if(!val.matches(".*[a-z].*")){
            garagePassword.setError("Password should contain at least one lowercase letter.");
            garagePassword.requestFocus();
            return false;
        }else if(!val.matches(".*[A-Z].*")){
            garagePassword.setError("Password should contain at least one uppercase letter.");
            garagePassword.requestFocus();
            return false;
        }else if(!val.matches(".*[!@#$%^&*()].*")){
            garagePassword.setError("Password should contain at least one special character.");
            garagePassword.requestFocus();
            return false;
        }else{
            garagePassword.setError(null);
            return true;
        }
    }


    // This function validates the re-entered password field.
    public Boolean validationRePassword(){
        String val = garageRePassword.getText().toString();
        String passwordVal = garageRePassword.getText().toString();
        if (val.isEmpty()){
            garageRePassword.setError("Re-enter password cannot be empty...");
            garageRePassword.requestFocus();
            return false;
        }else if(!val.equals(passwordVal)){
            garageRePassword.setError("Passwords do not match.");
            garageRePassword.requestFocus();
            return false;
        }else{
            garageRePassword.setError(null);
            return true;
        }
    }


    //using this function if all inputs are correct those data send to the database
    public void dataSend() {

        //get data and set data to variables
        String setGarageUsername = garageUsername.getText().toString();
        String setGarageEmail = garageEmail.getText().toString();
        String setGarageMobilenumber = garageMobilenumber.getText().toString();
        String setGaragePassword = garagePassword.getText().toString();

        //send data to database using RegisterDataSend class
        HelperClass registerDataSends = new HelperClass(setGarageUsername,setGarageEmail,setGarageMobilenumber,setGaragePassword);
        reference.child(setGarageUsername).setValue(registerDataSends);

        btnGarageSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //initialized database
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("GARAGE");

                //display message and direct to the GarageLogin page
                Toast.makeText(RegisterGarage.this, "You have sign-up successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterGarage.this, GarageLogin.class);
                startActivity(intent);

            }
        });
    }
}