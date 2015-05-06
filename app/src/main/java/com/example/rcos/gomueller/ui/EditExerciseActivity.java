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

import com.example.rcos.gomueller.IntentParam;
import com.example.rcos.gomueller.R;
import com.example.rcos.gomueller.WeightUnit;
import com.example.rcos.gomueller.database.ExerciseCRUD;
import com.example.rcos.gomueller.model.Exercise;

import java.util.Calendar;

/*
First autofill all the fields in the new exercise screen with the ones from an existing exercise

User can modify those fields (except for exercise name and attribute name, those are fixed)

Once the user presses enter, the database is updated.
 */
public class EditExerciseActivity extends Activity implements
        View.OnClickListener {
    Exercise oldExercise;

    EditText exercise_date;
    Button btnCalendar;

    EditText exercise_name;
    EditText exercise_attribute_name;
    EditText exercise_weight;

    EditText exercise_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        exercise_date = (EditText)findViewById(R.id.dateEditText);
        btnCalendar = (Button) findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(this);
        exercise_name = (EditText)findViewById(R.id.nameEditText);
        exercise_attribute_name = (EditText)findViewById(R.id.editWeightNameText);
        exercise_weight = (EditText)findViewById(R.id.weightEditText);
        exercise_notes = (EditText)findViewById(R.id.textOptionalNotes);
        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setText("Edit");

        //Autofill
        exercise_date.setText(IntentParam.getExerciseDate(getIntent()));
        exercise_weight.setText(IntentParam.getAttributeValue(getIntent()));
        exercise_notes.setText(IntentParam.getNotes(getIntent()));

        //disable editing
        exercise_name.setText(IntentParam.getExerciseName(getIntent()));
        exercise_name.setFocusable(false);
        exercise_name.setEnabled(false);

        exercise_attribute_name.setText(IntentParam.getAttributeName(getIntent()));
        exercise_attribute_name.setFocusable(false);
        exercise_attribute_name.setEnabled(false);

        //Set the weight's units depending on user's preferences
        TextView weightLabel = (TextView)findViewById(R.id.WeightUnit);
        weightLabel.setText(WeightUnit.getWhichLabel(this));

        oldExercise = new Exercise();
        oldExercise.activityName = exercise_name.getText().toString();
        oldExercise.attributeName = exercise_attribute_name.getText().toString();
        oldExercise.weight = Integer.parseInt(exercise_weight.getText().toString());
        oldExercise.notes = exercise_notes.getText().toString();
        oldExercise.date = exercise_date.getText().toString();

        int weightConverted = oldExercise.weight;
        if (WeightUnit.settingsUseImperial(this))
            weightConverted = WeightUnit.convertToImperial(weightConverted);

        exercise_weight.setText(String.valueOf(weightConverted));
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView weightLabel = (TextView)findViewById(R.id.WeightUnit);
        weightLabel.setText(WeightUnit.getWhichLabel(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
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

    public void okButtonOnClick(View view) {
        ExerciseCRUD crud = new ExerciseCRUD(this);
        Exercise updatedExercise = new Exercise();

        //error checking input
        if (exercise_weight.getText().toString().equals("")) {
            displayErrorPrompt("Attribute Value is Empty!",
                    "You need to enter a number.");
            return;
        }

        updatedExercise.activityName = exercise_name.getText().toString();
        updatedExercise.attributeName = exercise_attribute_name.getText().toString();
        updatedExercise.weight = Integer.parseInt(exercise_weight.getText().toString());
        updatedExercise.date = exercise_date.getText().toString();
        updatedExercise.notes = exercise_notes.getText().toString();

        if (WeightUnit.settingsUseImperial(this))
            updatedExercise.weight = WeightUnit.convertToMetric(updatedExercise.weight);

        crud.edit(updatedExercise, oldExercise);

        finish();
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

    @Override
    public void onClick(View v) {
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
                            exercise_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        }
                    }, year, month, day);
            dpd.show();
        }
    }
}
