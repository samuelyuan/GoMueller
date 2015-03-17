package com.example.rcos.gomueller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class NewWeightActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_weight);

        TextView weightLabel = (TextView)findViewById(R.id.AddWeightUnit);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = getResources().getString(R.string.pref_units_default);
        String whichSystem = prefs.getString(getString(R.string.pref_units_key), defaultValue);
        if (whichSystem.equals("metric"))
            weightLabel.setText("kgs");
        else if (whichSystem.equals("imperial"))
            weightLabel.setText("lbs");
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView weightLabel = (TextView)findViewById(R.id.AddWeightUnit);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = getResources().getString(R.string.pref_units_default);
        String whichSystem = prefs.getString(getString(R.string.pref_units_key), defaultValue);
        if (whichSystem.equals("metric"))
            weightLabel.setText("kgs");
        else if (whichSystem.equals("imperial"))
            weightLabel.setText("lbs");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_exercise, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
