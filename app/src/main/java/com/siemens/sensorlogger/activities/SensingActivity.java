package com.siemens.sensorlogger.activities;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.siemens.sensorlogger.R;
import com.siemens.sensorlogger.model.Constants;
import com.siemens.sensorlogger.model.ParcelableInteger;

import java.util.ArrayList;

public class SensingActivity extends Activity implements SensorEventListener {
    private ArrayList<ParcelableInteger> sensorTypes;
    private ArrayList<Sensor> sensors = new ArrayList<>();
    private SensorManager sensorManager;
    private ListView listViewSensorNames, listViewSensorValues;
    private int stepsDetected = 0, initialStepsCounted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensing);
        Intent intent = getIntent();
        sensorTypes = intent.getParcelableArrayListExtra(Constants.extraSelectedSensors);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        for (ParcelableInteger parcelableInteger : sensorTypes) {
            sensors.add(sensorManager.getDefaultSensor(parcelableInteger.getInteger()));
        }
        ArrayList<String> sensorNames = new ArrayList<>();
        ArrayList<String> nullEvents = new ArrayList<>();
        for (Sensor sensor : sensors) {
            sensorNames.add(sensor.getName());
            nullEvents.add("No measurement yet.");
        }
        listViewSensorNames = (ListView) findViewById(R.id.listViewSensorNames);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sensorNames);
        listViewSensorNames.setAdapter(arrayAdapter);
        listViewSensorValues = (ListView) findViewById(R.id.listViewSensorValues);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nullEvents);
        listViewSensorValues.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (Sensor sensor : sensors) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int index = sensors.indexOf(event.sensor);
        TextView displayText = (TextView) listViewSensorValues.getChildAt(index);
        String text = "";
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepsDetected += event.values[0];
            text = "" + stepsDetected;
        } else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (initialStepsCounted == 0) {
                initialStepsCounted = (int) event.values[0];
            } else {
                text = "" + (event.values[0] - initialStepsCounted);
            }
        } else {
            for (float value : event.values) {
                text += value + ",";
            }

        }
        if (displayText != null)
            displayText.setText(text);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
