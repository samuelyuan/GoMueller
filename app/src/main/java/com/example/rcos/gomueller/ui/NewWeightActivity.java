package com.example.rcos.gomueller.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rcos.gomueller.R;
import com.example.rcos.gomueller.WeightUnit;
import com.example.rcos.gomueller.database.ExerciseCRUD;
import com.example.rcos.gomueller.model.Weight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
Create a new weight record, documenting your weight on a certain day
 */
public class NewWeightActivity extends Activity implements
        View.OnClickListener
{
    Button btnCalendar;

    EditText weight_weight;
    EditText weight_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_weight);

        btnCalendar = (Button) findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(this);

        weight_weight = (EditText)findViewById(R.id.editWeight);
        weight_date = (EditText)findViewById(R.id.editDate);

        //autofill the current date
        DateFormat df = new SimpleDateFormat("MM/dd/yy");
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

    @Override
    public void onClick(View v)
    {
        if (v == btnCalendar)
        {
            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            // Variable for storing current date and time
            int year, month, day;
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener()
                    {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                        {
                            // Display Selected date in textbox
                            weight_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        }
                    }, year, month, day);
            dpd.show();
        }
    }

    public void displayErrorPrompt(String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
                //return;
            }
        });
        alertDialog.show();
        return;
    }

    public void WeightOKButtonOnClick(View view) {
        ExerciseCRUD crud = new ExerciseCRUD(this);
        Weight wt = new Weight();

        if (weight_weight.getText().toString().equals("")) {
            displayErrorPrompt("Weight is Empty",
                    "You forgot to enter your weight. Please input it and then press Add");
            return;
        }
        wt.weight = Integer.parseInt(weight_weight.getText().toString());
        wt.date = weight_date.getText().toString();

        if (WeightUnit.settingsUseImperial(this))
            wt.weight = WeightUnit.convertToMetric(wt.weight);

        crud.insert(wt);

        finish();
    }
}