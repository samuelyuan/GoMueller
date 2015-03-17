package com.example.rcos.gomueller;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


//public class TrackExerciseActivity extends ActionBarActivity {
public class TrackExerciseActivity extends ListActivity {

    private ListView exerciseList;
    private ArrayAdapter<String> adapter ;
    private ArrayList<String> exerciseArray;

    //exerciseArrayList
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_exercise);

        ExerciseCRUD crud = new ExerciseCRUD(this);

        exerciseArray = crud.getExerciseArray();

        adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.listText, exerciseArray);

        setListAdapter(adapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = prefs.getString(getString(R.string.pref_username_key), "");
        if (!username.equals("")){
            ((TextView)findViewById(R.id.track_headline)).setText(username +
                    ", below are the exercises you have finished.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = prefs.getString(getString(R.string.pref_username_key), "");
        if (!username.equals("")){
            ((TextView)findViewById(R.id.track_headline)).setText(username +
                    ", below are the exercises you have finished.");
        } else {
            ((TextView)findViewById(R.id.track_headline))
                    .setText("Hello! Below are the exercises you have finished.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_track_exercise, menu);
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
        } else if (id == R.id.add_exercise) {
            startActivity(new Intent(this, NewExerciseActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        String selectedItem = (String) getListView().getItemAtPosition(position);

        Intent intent = new Intent(this, ExerciseDetailActivity.class);
        intent.putExtra("message", selectedItem);
        startActivity(intent);
    }
}
