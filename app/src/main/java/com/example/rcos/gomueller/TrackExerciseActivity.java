package com.example.rcos.gomueller;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


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
