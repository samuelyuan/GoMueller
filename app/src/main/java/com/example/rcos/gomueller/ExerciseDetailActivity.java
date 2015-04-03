package com.example.rcos.gomueller;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ExerciseDetailActivity extends ListActivity {

    private ArrayList<String> detailArray;
    private boolean isItemSelected[] = new boolean[100];
    private ArrayAdapter<String> adapter;
    private ListView detailListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        Bundle bundle = getIntent().getExtras();
        final String exerciseName = bundle.getString("message");
        final String dataType = getIntent().getStringExtra("type");

        final ExerciseCRUD crudDetail = new ExerciseCRUD(this);
        if (dataType.equals("exercise"))
            detailArray = crudDetail.getExerciseDetail(exerciseName);
        else if (dataType.equals("weight"))
            detailArray = crudDetail.getWeightDetail();

        adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.listText, detailArray);

        Button btnDel = (Button) findViewById(R.id.btnDel);

        //When delete button is pressed...
        OnClickListener listenerDel = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the checked items
                SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
                int itemCount = getListView().getCount();

                //Delete all the selected items
                for(int i=itemCount-1; i >= 0; i--){

                    if(checkedItemPositions.get(i))
                    {
                        if (dataType.equals("exercise"))
                            crudDetail.deleteExercise(exerciseName, detailArray.get(i));
                        else if (dataType.equals("weight"))
                            crudDetail.deleteWeight(detailArray.get(i));

                        adapter.remove(detailArray.get(i));
                    }
                }
                checkedItemPositions.clear();
                adapter.notifyDataSetChanged();
            }
        };

        /** Setting the event listener for the delete button */
        btnDel.setOnClickListener(listenerDel);

        setListAdapter(adapter);
    }

    //update the records if something was added recently
    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getExtras();
        final String dataType = getIntent().getStringExtra("type");
        final String exerciseName = bundle.getString("message");

        final ExerciseCRUD crudDetail = new ExerciseCRUD(this);
        if (dataType.equals("exercise"))
            detailArray = crudDetail.getExerciseDetail(exerciseName);
        else if (dataType.equals("weight"))
            detailArray = crudDetail.getWeightDetail();

        adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.listText, detailArray);
        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_detail, menu);
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
        } else if (id == R.id.addMenu) {
            if (getIntent().getStringExtra("type").equals("exercise"))
            {
                Intent addIntent = new Intent(this, NewExerciseActivity.class);
                addIntent.putExtra("exerciseName", getIntent().getStringExtra("message"));
                startActivity(addIntent);
            }
            else if (getIntent().getStringExtra("type").equals("weight"))
            {
                Intent addIntent = new Intent(this, NewWeightActivity.class);
                startActivity(addIntent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        String selectedItem = (String) getListView().getItemAtPosition(position);

        //toggle selected item on/off
        isItemSelected[position] = !isItemSelected[position];
        //Check whether the item was selected
        if (isItemSelected[position]) {
            //highlight the selected item
            view.setBackgroundColor(Color.YELLOW);
        }
        else {
            //set it to transparent
            view.setBackgroundColor(0x00000000);
        }
        adapter.notifyDataSetChanged();
    }
}
