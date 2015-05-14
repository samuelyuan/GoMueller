package com.example.rcos.gomueller.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.rcos.gomueller.IntentParam;
import com.example.rcos.gomueller.NavigationDrawer;
import com.example.rcos.gomueller.R;

/*
Main Menu:
-View Exercises (exercises are categorized by exercise name)
-View Weight History (just display a graph,since there's no categorizing necessary)

For each exercise, a graph of the data will be shown first. Visualizing data is good enough in most
cases. However, if the user wants to view specific numbers and/or modify the data, then they can
click the edit button
 */
public class MainActivity extends ActionBarActivity {

    private NavigationDrawer navigationDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawer = new NavigationDrawer(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if (id == R.id.add_exercise) {
            startActivity(new Intent(this, NewExerciseActivity.class));
            return true;
        } else if (id == R.id.add_weight) {
            startActivity(new Intent(this, NewWeightActivity.class));
            return true;
        }

        if (navigationDrawer.onOptionsItemSelected(item)) {
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

    public void onViewExerciseButton(View view) {
        Intent intent = new Intent(this, TrackExerciseActivity.class);
        startActivity(intent);
    }

    public void onViewWeightButton(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        IntentParam.setTypeWeight(intent);
        startActivity(intent);
    }
}