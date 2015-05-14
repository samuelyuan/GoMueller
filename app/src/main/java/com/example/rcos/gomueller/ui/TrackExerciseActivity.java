package com.example.rcos.gomueller.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rcos.gomueller.IntentParam;
import com.example.rcos.gomueller.NavigationDrawer;
import com.example.rcos.gomueller.R;
import com.example.rcos.gomueller.database.ExerciseCRUD;

import java.util.ArrayList;


/*
Takes all your recorded exercises and puts them into categories by name

By clicking on the name of the exercise, you will get a graph of data for that one exercise.

For example, you have:
BenchPress
Deadlift
...

When you click on BenchPress, you get all the data just for bench press
 */
//public class TrackExerciseActivity extends ActionBarActivity {
public class TrackExerciseActivity extends ListActivity {

    private ListView exerciseList;
    private ArrayAdapter<String> adapter ;
    private ArrayList<String> exerciseArray;

    private NavigationDrawer navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_exercise);

        navigationDrawer = new NavigationDrawer(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }

    public void loadData()
    {
        ExerciseCRUD crud = new ExerciseCRUD(this);
        exerciseArray = crud.getExerciseArray();
        adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.listText, exerciseArray);
        setListAdapter(adapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = prefs.getString(getString(R.string.pref_username_key), "");
        if (!username.equals("")){
            ((TextView)findViewById(R.id.track_headline)).setText(username +
                    "'s Exercise History");
        } else {
            ((TextView)findViewById(R.id.track_headline))
                    .setText("Your Exercise History.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_track_exercise, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (navigationDrawer.onOptionsItemSelected(item)) {
            return true;
        }
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navigationDrawer.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navigationDrawer.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        String selectedItem = (String) getListView().getItemAtPosition(position);

        Intent intent = new Intent(this, GraphActivity.class);
        IntentParam.setExerciseName(intent, selectedItem);
        IntentParam.setTypeExercise(intent);
        startActivity(intent);
    }
}
