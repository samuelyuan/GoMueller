package com.example.rcos.gomueller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button enterButton, viewButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterButton = (Button) findViewById(R.id.enterButton);
        viewButton = (Button) findViewById(R.id.viewButton);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEnterButton(View view) {
        Intent intent = new Intent(this, NewExerciseActivity.class);
        startActivity(intent);
    }

    public void onViewButton(View view) {
        Intent intent = new Intent(this, TrackExerciseActivity.class);
        startActivity(intent);
    }
    
    public void onEnterWeightButton(View view) {
        Intent intent = new Intent(this, NewWeightActivity.class);
        startActivity(intent);
    }
}