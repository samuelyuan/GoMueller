package com.example.rcos.gomueller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class NewWeightActivity extends Activity {

    EditText weight_weight;
    EditText weight_date;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_weight);
        
        weight_weight = (EditText)findViewById(R.id.editWeight);
        weight_date = (EditText)findViewById(R.id.editDate);

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

    public void WeightOKButtonOnClick(View view) {
        ExerciseCRUD crud = new ExerciseCRUD(this);
        Weight wt = new Weight();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = getResources().getString(R.string.pref_units_default);
        String whichSystem = prefs.getString(getString(R.string.pref_units_key), defaultValue);

        wt.weight = Integer.parseInt(weight_weight.getText().toString());
        wt.date = weight_date.getText().toString();

        if (whichSystem.equals("imperial"))
            wt.weight = (int)((double)wt.weight * 0.453592);

        crud.insert(wt);

        finish();

    }
}
