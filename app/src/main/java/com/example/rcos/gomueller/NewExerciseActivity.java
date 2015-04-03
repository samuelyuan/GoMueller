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

public class NewExerciseActivity extends Activity {

    EditText exercise_name;
    EditText exercise_weight;
    EditText exercise_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        exercise_name = (EditText)findViewById(R.id.nameEditText);
        exercise_weight = (EditText)findViewById(R.id.weightEditText);
        exercise_number = (EditText)findViewById(R.id.numberEditText);

        //autofill exercise name if possible
        exercise_name.setText(getIntent().getStringExtra("exerciseName"));

        //Set the weight's units depending on user's preferences
        TextView weightLabel = (TextView)findViewById(R.id.WeightUnit);
        weightLabel.setText(WeightUnit.getWhichLabel(this));
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
        Exercise ex = new Exercise();

        ex.activityName = exercise_name.getText().toString();

        //weight used can be optional for non-weightlifting exercises
        if (!exercise_weight.getText().toString().equals(""))
            ex.weight = Integer.parseInt(exercise_weight.getText().toString());
        else
            ex.weight = 0;

        //time spent can be optional for weightlifting
        if (!exercise_number.getText().toString().equals(""))
            ex.number = Integer.parseInt(exercise_number.getText().toString());
        else
            ex.number = 0;

        if (WeightUnit.isImperial(this))
            ex.weight = (int)((double)ex.weight * WeightUnit.POUND_TO_KILOGRAM);

        crud.insert(ex);

        finish();

    }
}
