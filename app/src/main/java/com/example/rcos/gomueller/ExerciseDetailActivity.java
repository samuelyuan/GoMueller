package com.example.rcos.gomueller;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ExerciseDetailActivity extends ListActivity {

    private ArrayList<String> detailArray, displayArray;
    private boolean isItemSelected[] = new boolean[100];
    private ArrayAdapter<String> adapter;
    private ListView detailListView;
    private ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        displayData();
    }

    //update the records if something was added recently
    @Override
    protected void onResume() {
        super.onResume();

        displayData();
    }

    public void displayData()
    {
        Bundle bundle = getIntent().getExtras();
        final String dataType = getIntent().getStringExtra("type");
        final String exerciseName = bundle.getString("message");

        final ExerciseCRUD crudDetail = new ExerciseCRUD(this);
        if (dataType.equals("exercise")) {
            detailArray = crudDetail.getExerciseDetail(exerciseName);
        }
        else if (dataType.equals("weight")) {
            detailArray = crudDetail.getWeightDetail();
        }

        displayArray = formatForDisplay(detailArray);

        adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.listText, displayArray);
        setListAdapter(adapter);
    }

    public ArrayList<String> formatForDisplay(ArrayList<String> detailArray)
    {
        ArrayList<String> displayArray = new ArrayList<String>();
        for (String dataItem : detailArray)
        {
            String[] splitString = dataItem.split(" ");
            String dateStr = splitString[0];
            String weightStr = "", noteStr = "";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date tempDate = formatter.parse(dateStr);
                formatter = new SimpleDateFormat("MM/dd/yy");
                dateStr = formatter.format(tempDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < splitString.length - 1; j++)
            {
                if (splitString[j].equals("Weight:")) {
                    weightStr = String.valueOf(splitString[j + 1]);
                } else if (splitString[j].equals("Notes:")) {

                }
            }

            noteStr = dataItem.substring(dataItem.indexOf("Notes: ") + ("Notes: ").length());

            String itemToAdd = weightStr + " " + WeightUnit.getWhichLabel(this) + "        (" + noteStr + ")" + "\n";
            itemToAdd += dateStr;

            displayArray.add(itemToAdd);
        }

        return displayArray;
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
                String exerciseName = getIntent().getStringExtra("message");
                final ExerciseCRUD crudDetail = new ExerciseCRUD(ExerciseDetailActivity.this);

                addIntent.putExtra("exerciseName", exerciseName);
                addIntent.putExtra("attributeName", crudDetail.getAttributeName(exerciseName));
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

        boolean atLeastOneSelected = false;
        for (int i = 0; i < isItemSelected.length; i++)
        {
            if (isItemSelected[i])
                atLeastOneSelected = true;
        }

        if (atLeastOneSelected)
            mActionMode = ExerciseDetailActivity.this.startActionMode(new ActionBarCallBack());
        else if (mActionMode != null) //none selected
            mActionMode.finish();

        adapter.notifyDataSetChanged();
    }

    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();

            //delete items from list and from database
            if (id == R.id.deleteMenu) {
                deleteItem();
                return true;
            }

            return false;
        }

        public void deleteItem()  {
            Bundle bundle = getIntent().getExtras();
            final String exerciseName = bundle.getString("message");
            final String dataType = getIntent().getStringExtra("type");

            final ExerciseCRUD crudDetail = new ExerciseCRUD(ExerciseDetailActivity.this);

            //Get the checked items
            SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
            int itemCount = getListView().getCount();

            //Delete all the selected items
            for(int i = itemCount - 1; i >= 0; i--)
            {
                if(checkedItemPositions.get(i))
                {
                    View selectedItem = getListView().getChildAt(i);

                    if (dataType.equals("exercise")) {
                        crudDetail.deleteExercise(exerciseName, detailArray.get(i));
                    }
                    else if (dataType.equals("weight")) {
                        crudDetail.deleteWeight(detailArray.get(i));
                    }

                    adapter.remove(displayArray.get(i));

                    //deselect the selected item
                    isItemSelected[i] = !isItemSelected[i];
                    selectedItem.setBackgroundColor(0x00000000);
                }
            }
            checkedItemPositions.clear();
            adapter.notifyDataSetChanged();

            //don't show action bar after deleting
            mActionMode.finish();
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_delete_item, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            //List the number of items selected

            //Get the checked items
            SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
            int itemCount = getListView().getCount();
            int numSelected = 0;
            for(int i = itemCount - 1; i >= 0; i--) {
                if (checkedItemPositions.get(i)) {
                    numSelected++;
                }
            }
            mode.setTitle(numSelected + " Selected");
            return false;
        }
    }
}
