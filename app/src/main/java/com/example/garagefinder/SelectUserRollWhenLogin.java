package com.example.garagefinder;

//import class
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectUserRollWhenLogin extends AppCompatActivity {

    //declare variables
    Button btnUser, btnGarage;
    TextView guidelines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_roll_when_login);

        //set name to the action bar
        getSupportActionBar().setTitle("Select Account Type");

        //initialized the variables
        btnUser = findViewById(R.id.btnUser);
        btnGarage = findViewById(R.id.btnGarage);
        guidelines = findViewById(R.id.guidelines);


        //When user click user button it will link to the User Login page
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectUserRollWhenLogin.this,UserLogin.class);
                startActivity(intent);

            }
        });

        //When user click garage button it will link to the garage Login page
        btnGarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectUserRollWhenLogin.this,GarageLogin.class);
                startActivity(intent);
            }
        });

        //when user click the guidelines text it will redirect to the About us page
        guidelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectUserRollWhenLogin.this,AboutUs.class);
                startActivity(intent);
            }
        });

    }
}