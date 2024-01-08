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

public class UserRegister extends AppCompatActivity {

    //Declaring variables
    EditText username,email,mobilenumber,password,rePassword;
    TextView terms;
    Button btnSignUp;
    FirebaseDatabase database;
    DatabaseReference reference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //database initialized
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("USER");

        //hide actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //show and hide password section
        ImageView showPassword = findViewById(R.id.showPassword);
        showPassword.setImageResource(R.drawable.baseline_visibility_off_24);
        password = findViewById(R.id.password);

        //set eye icon to show and close password
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.baseline_visibility_off_24);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.baseline_visibility_24);
                }
            }
        });

        //show and hide Re-password section
        ImageView showRePassword = findViewById(R.id.reShowPassword);
        showRePassword.setImageResource(R.drawable.baseline_visibility_off_24);
        rePassword = findViewById(R.id.rePassword);

        //set eye icon to show and close Re-password
        showRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rePassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    rePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showRePassword.setImageResource(R.drawable.baseline_visibility_off_24);
                } else {
                    rePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showRePassword.setImageResource(R.drawable.baseline_visibility_24);
                }
            }
        });

        //initialized the variables
        terms = findViewById(R.id.terms);

        // link to privacy policy page
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserRegister.this,PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        //initialized the variables
        username = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        mobilenumber = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.rePassword);

        btnSignUp = findViewById(R.id.btnSignUp);

        //check the user inputs are corrects
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationRePassword() | !validationPassword()  | !validationMobilenumber() | !validationEmail() | !validationUsername()){
                } else {
                    dataSend();//data send to databased using this function
                }
            }
        });
    }

    // This function validates the username field
    public Boolean validationUsername(){
        String val = username.getText().toString();
        if (val.isEmpty()){
            username.setError("username cannot be empty...");
            username.requestFocus();
            return false;
        }else if(val.contains(" ")) {
            username.setError("username cannot contain spaces...");
            username.requestFocus();
            return false;
        }else if(val.matches(".*[!@#$%^&*()].*")) {
            username.setError("username cannot content the special character...");
            username.requestFocus();
            return false;
        }else{
            username.setError(null);
            return true;
        }
    }

    // This function validates the email field
    public Boolean validationEmail(){
        String val = email.getText().toString();
        if (val.isEmpty()){
            email.setError("email cannot be empty...");
            email.requestFocus();
            return false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            email.setError("Please enter a valid email address...");
            email.requestFocus();
            return false;
        }else{
            email.setError(null);
            return true;
        }
    }

    // This function validates the mobile number field.
    public Boolean validationMobilenumber(){
        String val = mobilenumber.getText().toString();
        if (val.isEmpty()){
            mobilenumber.setError("Mobile number cannot be empty.");
            mobilenumber.requestFocus();
            return false;
        }else if(val.length() != 10) {
            mobilenumber.setError("Mobile number have only 10 characters.");
            mobilenumber.requestFocus();
            return false;
        }else{
            mobilenumber.setError(null);
            return true;
        }
    }

    // This function validates the password field.
    public Boolean validationPassword(){
        String val = password.getText().toString();
        if (val.isEmpty()){
            password.setError("password cannot be empty...");
            password.requestFocus();
            return false;
        }else if(val.length() < 8){
            password.setError("password should have at least 8 characters.");
            password.requestFocus();
            return false;
        }else if(!val.matches(".*[a-z].*")){
            password.setError("password should contain at least one lowercase letter.");
            password.requestFocus();
            return false;
        }else if(!val.matches(".*[A-Z].*")){
            password.setError("password should contain at least one uppercase letter.");
            password.requestFocus();
            return false;
        }else if(!val.matches(".*[!@#$%^&*()].*")){
            password.setError("password should contain at least one special character.");
            password.requestFocus();
            return false;
        }else{
            password.setError(null);
            return true;
        }
    }

    // This function validates the re-entered password field.
    public Boolean validationRePassword(){
        String val = rePassword.getText().toString();
        String passwordVal = password.getText().toString();
        if (val.isEmpty()){
            rePassword.setError("Re-enter password cannot be empty...");
            rePassword.requestFocus();
            return false;
        }else if(!val.equals(passwordVal)){
            rePassword.setError("Passwords do not match.");
            rePassword.requestFocus();
            return false;
        }else{
            rePassword.setError(null);
            return true;
        }
    }


    //using this function if all inputs are correct those data send to the database
    public void dataSend() {

        //get data and set data to variables
        String setUsername = username.getText().toString();
        String setEmail =  email.getText().toString();
        String setMobilenumber = mobilenumber.getText().toString();
        String setPassword = password.getText().toString();

        //send data to database using RegisterDataSend class
        HelperClass registerDataSend = new HelperClass(setUsername,setEmail,setMobilenumber,setPassword);
        reference.child(setUsername).setValue(registerDataSend);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //initialized database
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("USER");

                //display message and direct to the GarageLogin page
                Toast.makeText(UserRegister.this, "You have sign-up successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserRegister.this, UserLogin.class);
                startActivity(intent);

            }
        });

    }

}