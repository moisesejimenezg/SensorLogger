package com.siemens.sensorlogger.activities;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.siemens.sensorlogger.R;
import com.siemens.sensorlogger.model.Constants;
import com.siemens.sensorlogger.model.ParcelableInteger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ListView listViewSensors;
    private Button buttonStartSensing;
    private List<Sensor> sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<String> sensorNames = new ArrayList<>();
        for (Sensor sensor : sensors) {
            sensorNames.add(sensor.getName());
        }

        listViewSensors = (ListView) findViewById(R.id.listViewSensors);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, sensorNames);
        listViewSensors.setAdapter(arrayAdapter);
        listViewSensors.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        buttonStartSensing = (Button) findViewById(R.id.buttonStartSensing);
        buttonStartSensing.setOnClickListener(buttonOnClickListenerStartSensing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private View.OnClickListener buttonOnClickListenerStartSensing = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            SparseBooleanArray sparseBooleanArray = listViewSensors.getCheckedItemPositions();
            ArrayList<ParcelableInteger> sensorTypesChecked = new ArrayList<>();
            for(int i = 0; i < sensors.size(); i++){
                if(sparseBooleanArray.get(i)){
                    sensorTypesChecked.add(new ParcelableInteger(sensors.get(i).getType()));
                }
            }
            Intent intent = new Intent(MainActivity.this,SensingActivity.class);
            intent.putParcelableArrayListExtra(Constants.extraSelectedSensors,sensorTypesChecked);
            startActivity(intent);
        }
    };
}
