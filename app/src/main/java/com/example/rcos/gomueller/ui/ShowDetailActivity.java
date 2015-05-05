package com.example.rcos.gomueller.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rcos.gomueller.IntentParam;
import com.example.rcos.gomueller.ParseData;
import com.example.rcos.gomueller.R;
import com.example.rcos.gomueller.UnitDate;
import com.example.rcos.gomueller.WeightUnit;
import com.example.rcos.gomueller.database.ExerciseCRUD;

import java.util.ArrayList;


public class ShowDetailActivity extends ListActivity {

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
        final String exerciseName = IntentParam.getExerciseName(getIntent());

        final ExerciseCRUD crudDetail = new ExerciseCRUD(this);
        if (IntentParam.isTypeExercise(getIntent())) {
            detailArray = crudDetail.getExerciseDetail(exerciseName);
        }
        else if (IntentParam.isTypeWeight(getIntent())) {
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
            String weightStr = ParseData.getAttributeValue(dataItem);
            String dateStr = ParseData.getDate(dataItem);
            String noteStr = "(" + ParseData.getNotes(dataItem) + ")";

            if (ParseData.getNotes(dataItem).equals(""))
                noteStr = "";

            String itemToAdd = weightStr + " " + WeightUnit.getWhichLabel(this) + "        " + noteStr + "\n";
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
            if (IntentParam.isTypeExercise(getIntent()))
            {
                Intent addIntent = new Intent(this, NewExerciseActivity.class);
                String exerciseName = IntentParam.getExerciseName(getIntent());
                final ExerciseCRUD crudDetail = new ExerciseCRUD(ShowDetailActivity.this);
                IntentParam.setExerciseName(addIntent, exerciseName);
                IntentParam.setAttributeName(addIntent, crudDetail.getAttributeName(exerciseName));
                startActivity(addIntent);
            }
            else if (IntentParam.isTypeWeight(getIntent()))
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
            mActionMode = ShowDetailActivity.this.startActionMode(new ActionBarCallBack());
        else if (mActionMode != null) //none selected
            mActionMode.finish();

        adapter.notifyDataSetChanged();
    }

    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.editMenu) {
                editItem();
                return true;
            }
            //delete items from list and from database
            else if (id == R.id.deleteMenu) {
                deleteItem();
                return true;
            }
            else if (id == R.id.action_settings) {
                startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                return true;
            }

            return false;
        }

        public void editItem()
        {
            SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
            int itemCount = getListView().getCount();
            int indexEdit = 0;
            for (int i = 0; i < itemCount; i++) {
                if (checkedItemPositions.get(i)) {
                    indexEdit = i;
                    break;
                }
            }

            if (IntentParam.isTypeWeight(getIntent()))
            {
                Intent intent = new Intent(getBaseContext(), EditWeightActivity.class);
                String currentDetailStr = detailArray.get(indexEdit);
                String weightStr = ParseData.getAttributeValue(currentDetailStr);
                String dateStr = ParseData.getDate(currentDetailStr);

                //convert data if necessary since the data is in the metric system
                if (WeightUnit.settingsUseImperial(getBaseContext())) {
                    weightStr = WeightUnit.convertToMetric(weightStr);
                }

                IntentParam.setAttributeValue(intent, weightStr);
                IntentParam.setExerciseDate(intent, dateStr);

                //deselect the selected item
                isItemSelected[indexEdit] = !isItemSelected[indexEdit];
                getListView().getChildAt(indexEdit).setBackgroundColor(0x00000000);
                checkedItemPositions.clear();

                //hide action bar
                mActionMode.finish();

                //switch to the edit activity
                startActivity(intent);
            }
            else if (IntentParam.isTypeExercise(getIntent())) {
                //Prepare data for modification
                Intent addIntent = new Intent(getBaseContext(), EditExerciseActivity.class);
                final String exerciseName = IntentParam.getExerciseName(getIntent());
                final ExerciseCRUD crudDetail = new ExerciseCRUD(ShowDetailActivity.this);

                String currentDetailStr = detailArray.get(indexEdit);
                String weightStr = ParseData.getAttributeValue(currentDetailStr);
                String dateStr = ParseData.getDate(currentDetailStr);
                String noteStr = ParseData.getNotes(currentDetailStr);

                //convert data if necessary since the data is in the metric system
                if (WeightUnit.settingsUseImperial(getBaseContext())) {
                    weightStr = WeightUnit.convertToMetric(weightStr);
                }

                IntentParam.setExerciseName(addIntent, exerciseName);
                IntentParam.setAttributeName(addIntent, crudDetail.getAttributeName(exerciseName));
                IntentParam.setAttributeValue(addIntent, weightStr);
                IntentParam.setExerciseDate(addIntent, dateStr);
                IntentParam.setNotes(addIntent, noteStr);

                //deselect the selected item
                isItemSelected[indexEdit] = !isItemSelected[indexEdit];
                getListView().getChildAt(indexEdit).setBackgroundColor(0x00000000);
                checkedItemPositions.clear();

                //hide action bar
                mActionMode.finish();

                //switch to the edit activity
                startActivity(addIntent);
            }
        }

        public void deleteItem()  {
            final String exerciseName = IntentParam.getExerciseName(getIntent());
            final ExerciseCRUD crudDetail = new ExerciseCRUD(ShowDetailActivity.this);

            //Get the checked items
            SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
            int itemCount = getListView().getCount();

            //Delete all the selected items
            for(int i = itemCount - 1; i >= 0; i--)
            {
                if(checkedItemPositions.get(i))
                {
                    View selectedItem = getListView().getChildAt(i);

                    if (IntentParam.isTypeExercise(getIntent())) {
                        crudDetail.deleteExercise(exerciseName, detailArray.get(i));
                    }
                    else if (IntentParam.isTypeWeight(getIntent())) {
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
            mode.getMenuInflater().inflate(R.menu.menu_modify_item, menu);
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
