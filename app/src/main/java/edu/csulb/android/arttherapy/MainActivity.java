package edu.csulb.android.arttherapy;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    ImageButton new_canvas, save, brush, eraser;
    CanvasView canvasView;
    SensorManager snsrManager;
    private long timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new_canvas = (ImageButton) findViewById(R.id.new_button);
        brush = (ImageButton) findViewById(R.id.brush_button);
        eraser = (ImageButton) findViewById(R.id.eraser_button);
        canvasView = (CanvasView) findViewById(R.id.canvas);
        snsrManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        snsrManager.registerListener(this, snsrManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onToolButtonClick(View view){
        int button = view.getId();
        switch(button){
            case R.id.eraser_button:
                eraser.setClickable(false);
                brush.setClickable(true);
                canvasView.setErasing(true);
                break;
            case R.id.brush_button:
                brush.setClickable(false);
                eraser.setClickable(true);
                canvasView.setErasing(false);
                break;
            case R.id.new_button:
                canvasView.reset(false);
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gravityX = x / SensorManager.GRAVITY_EARTH;
            float gravityY = y / SensorManager.GRAVITY_EARTH;
            float gravityZ = z / SensorManager.GRAVITY_EARTH;

            double gForce = Math.sqrt(gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ);
            if(gForce >=  2.5D){
                final long now = System.currentTimeMillis();
                if(timeStamp + 1000 >= now){
                    canvasView.reset(true);
                }
                timeStamp = now;
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        snsrManager.unregisterListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        snsrManager.registerListener(this, snsrManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        canvasView.releaseMediaPlayer();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
