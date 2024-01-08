package com.example.garagefinder;

//library imports
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class CompassFragment extends Fragment implements SensorEventListener {

    // request code for location permission
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;

    //initialize variables
    private ImageView compassImage;
    private TextView degreeText;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor magnetometerSensor;

    // data arrays to store sensor readings
    private float[] lastAccelerometerData = new float[3];
    private float[] lastMagnetometerData = new float[3];
    private boolean hasSensorData = false;

    // rotation matrix and orientation angles
    private float[] rotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);

        // initialize views
        compassImage = view.findViewById(R.id.compass);
        degreeText = view.findViewById(R.id.degreeTextView);

        // get the sensor service ACCELEROMETER sensor and MAGNETIC sensor.
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // request location permission if not granted
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // register sensor listeners for accelerometer and magnetometer
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister sensor listeners
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // update sensor data based on the sensor type
        if (event.sensor == accelerometerSensor) {
            System.arraycopy(event.values, 0, lastAccelerometerData, 0, event.values.length);
            hasSensorData = true;
        } else if (event.sensor == magnetometerSensor) {
            System.arraycopy(event.values, 0, lastMagnetometerData, 0, event.values.length);
            hasSensorData = true;
        }

        // if sensor data is available, calculate direction and update compass
        if (hasSensorData) {
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometerData, lastMagnetometerData);
            SensorManager.getOrientation(rotationMatrix, orientationAngles);

            float azimuth = (float) Math.toDegrees(orientationAngles[0]);
            azimuth = (azimuth + 360) % 360;
            compassImage.setRotation(-azimuth);
            String degree = String.format("%.2f", azimuth) + "°";
            degreeText.setText(degree);
        }
    }

    // nothing
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
