package com.example.sensorexperimentapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sensorexperimentapp.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements android.hardware.SensorEventListener { // Implement SensorEventListener
    private SensorManager sensorManager;
    private TextView sensorListTextView;
    private Sensor accelerometer;
    private Sensor proximitySensor;
    private Sensor lightSensor;
    private TextView accelerometerData;
    private TextView proximityData;
    private TextView lightData;

    private Sensor rotationVectorSensor;

    private TextView orientationData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        accelerometerData = findViewById(R.id.accelerometerData);
        proximityData = findViewById(R.id.proximityData);
        lightData = findViewById(R.id.LightData);
        orientationData = findViewById(R.id.orientationData);

        // Register listeners to listen to the sensors
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Button detectSensorsButton = findViewById(R.id.detectSensorButton);
        sensorListTextView = findViewById(R.id.sensorListTextView);

        detectSensorsButton.setOnClickListener(v -> listAvailableSensors());

        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);



    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            accelerometerData.setText("Accelerometer Data: X=" + x + ", Y=" + y + ", Z=" + z);
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximityData.setText("Proximity Data: " + event.values[0]);
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightData.setText("Light Sensor Data: " + event.values[0]);
        }

        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float [] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix,event.values);

            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix,orientation);

            orientationData.setText("Orientation: " +
                    "\n Azimuth= " + Math.toDegrees(orientation[0]) +
                    ", \n Pitch= " + Math.toDegrees(orientation[1]) +
                    ", \n Roll= " + Math.toDegrees(orientation[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    private void listAvailableSensors() {
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorInfo = new StringBuilder("Available Sensors: \n");
        for (Sensor sensor : sensorList) {
            sensorInfo.append(sensor.getName()).append(" (").append(sensor.getType()).append(") \n");
        }
        sensorListTextView.setText(sensorInfo.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listeners when the activity is paused to conserve resources
        sensorManager.unregisterListener(this);
    }



}
