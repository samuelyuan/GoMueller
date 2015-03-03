package com.example.rcos.gomueller;

import android.app.ListActivity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.SparseBooleanArray;
import java.util.ArrayList;


public class ExerciseDetailActivity extends ListActivity {

    private ArrayList<String> detailArray;
    private ArrayAdapter<String> adapter;
    private ListView detailListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        Bundle bundle = getIntent().getExtras();
        final String message = bundle.getString("message");

        final ExerciseCRUD crudDetail = new ExerciseCRUD(this);

        detailArray = crudDetail.getExerciseDetail(message);
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
                        crudDetail.delete(message, detailArray.get(i));
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        String selectedItem = (String) getListView().getItemAtPosition(position);

        //highlight the selected item
        view.setBackgroundColor(Color.YELLOW);

        adapter.notifyDataSetChanged();
    }
}
