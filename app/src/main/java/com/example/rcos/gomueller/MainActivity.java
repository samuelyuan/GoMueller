package com.example.rcos.gomueller;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
}