package com.example.garagefinder;


//library imports
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class AboutUs extends AppCompatActivity {

    //Declaring variables
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //actionbar hide
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //assign id to variable
        videoView = findViewById(R.id.video_view);

        //video path
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.aboutusvideo;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        //create media controller
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

    }
}