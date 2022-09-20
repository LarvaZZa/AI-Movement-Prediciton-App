package com.smart.challenge3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView accelerometerText;
    private TextView gyroscopeText;
    private SensorManager manager;
    private SensorEventListener listener;

    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 3000; //intervals of 3 sec

    private ArrayList<float[]> accelerometerValues = new ArrayList<>();
    private ArrayList<float[]> gyroscopeValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometerText = findViewById(R.id.accelerometer);
        gyroscopeText = findViewById(R.id.gyroscope);

        manager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        
        listener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                Sensor sensor = event.sensor;
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    accelerometerValues.add(new float[]{event.values[0], event.values[1], event.values[2]});
                    accelerometerText.setText(String.format("Ax:%s\nAy:%s\nAz:%s", event.values[0], event.values[1], event.values[2]));
                }
                else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    gyroscopeValues.add(new float[]{event.values[0], event.values[1], event.values[2]});
                    gyroscopeText.setText(String.format("Gx:%s\nGy:%s\nGz:%s", event.values[0], event.values[1], event.values[2]));
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable = () -> {
            manager.unregisterListener(listener);
            predict();
            handler.postDelayed(runnable, delay);
            manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
            manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        }, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }

    private void predict(){
        System.out.println("Acc:"+accelerometerValues.size());
        System.out.println("Gyro:"+gyroscopeValues.size());
        accelerometerValues.clear();
        gyroscopeValues.clear();
    }
}