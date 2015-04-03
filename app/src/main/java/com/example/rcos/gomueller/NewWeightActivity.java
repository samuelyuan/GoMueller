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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class NewWeightActivity extends Activity {

    EditText weight_weight;
    EditText weight_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_weight);
        
        weight_weight = (EditText)findViewById(R.id.editWeight);
        weight_date = (EditText)findViewById(R.id.editDate);

        //autofill the current date
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        weight_date.setText(date);

        //set the weight units (kgs or lbs)
        TextView weightLabel = (TextView)findViewById(R.id.AddWeightUnit);
        weightLabel.setText(WeightUnit.getWhichLabel(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView weightLabel = (TextView)findViewById(R.id.AddWeightUnit);
        weightLabel.setText(WeightUnit.getWhichLabel(this));
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

        wt.weight = Integer.parseInt(weight_weight.getText().toString());
        wt.date = weight_date.getText().toString();

        if (WeightUnit.isImperial(this))
            wt.weight = (int)((double)wt.weight * WeightUnit.POUND_TO_KILOGRAM);

        crud.insert(wt);

        finish();
    }
}
