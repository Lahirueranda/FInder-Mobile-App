package com.example.garagefinder;

//import libraries
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class GarageSettingFragment extends Fragment {

    //declare variables
    TextView userName,privacyPolicy,aboutUs,editProfile,logOut,deleteAccount;
    String garageUsername;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_garage_setting, container, false);

        //initialized the variables
        privacyPolicy = v.findViewById(R.id.privacyPolicy);
        aboutUs = v.findViewById(R.id.aboutUs);
        editProfile = v.findViewById(R.id.editProfile);
        deleteAccount = v.findViewById(R.id.deleteAccount);
        userName = v.findViewById(R.id.userName);
        logOut = v.findViewById(R.id.logOut);

        //get garage username from garage login page
        userName.setText(GarageLogin.garagesUsernames);

        //when clicked the privacy policy text it will direct to the privacy policy page
        privacyPolicy .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        //when clicked the about text it will direct to the about us page
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutUs.class);
                startActivity(intent);
            }
        });

        //when clicked the edit password text it will direct to the edit password page
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditGarageProfile.class);
                startActivity(intent);
            }
        });

        //when click the logout text popup window display
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialogInLogout();
            }
        });

        //when click the delete account text popup window display
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialogInDelete();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }


    //logout confirmation dialog popup
    private void showConfirmationDialogInLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm logout from account !");
        builder.setMessage("Are you sure you want to logout from your account ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //shared preference clear
                MySharedPreferences sharedPreferences = new MySharedPreferences(getContext());
                sharedPreferences.clear();

                //display message
                showToast(getContext(), "Logout Successfully...");

                //direct to the  SelectUserRollWhenLogin screen
                Intent intent = new Intent(getContext(), SelectUserRollWhenLogin.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //Delete confirmation dialog popup
    private void showConfirmationDialogInDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Delete Your Account !");
        builder.setMessage("Are you sure, you want to permanently delete your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //get values from garage-login page
                garageUsername = GarageLogin.garagesUsernames;
                deleteRecord(garageUsername, getContext());
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //if the user gives the Yes button data will delete from databases
    public void deleteRecord(String garageUsername, Context context){

        //initialized the database reference garage and garage details tables
        DatabaseReference dbRefOne = FirebaseDatabase.getInstance().getReference("GARAGE").child(garageUsername);
        DatabaseReference dbRefTwo = FirebaseDatabase.getInstance().getReference("GARAGE_DETAILS").child(garageUsername);

        Task<Void> removeTask1 = dbRefOne.removeValue();
        Task<Void> removeTask2 = dbRefTwo.removeValue();

        Task<Void> combinedTask = Tasks.whenAll(removeTask1, removeTask2);

        combinedTask.addOnSuccessListener(new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void aVoid) {

                //shared preference clear
                MySharedPreferences sharedPreferences = new MySharedPreferences(getContext());
                sharedPreferences.clear();

                //message displayed
                showToast(context, "Account Deleted Successfully...");

                //redirect to the SelectUserRollWhenLogin page
                Intent intent = new Intent(getContext(), SelectUserRollWhenLogin.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NotNull Exception e) {
                showToast(context, "Error deleting Account...");
            }
        });
    }


    //toast message function
    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}