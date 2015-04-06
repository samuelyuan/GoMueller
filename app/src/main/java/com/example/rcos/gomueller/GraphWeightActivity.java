package com.example.rcos.gomueller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class GraphWeightActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_weight);

        drawGraph();
    }

    @Override
    protected void onResume() {
        super.onResume();

        drawGraph();
    }

    public void drawGraph()
    {
        //Draw graph
        final ExerciseCRUD crudDetail = new ExerciseCRUD(this);
        ArrayList<String> detailArray = crudDetail.getWeightDetail();
        DataPoint[] dataPoints = new DataPoint[detailArray.size()];

        //fetch data
        for (int i = 0; i < detailArray.size(); i++)
        {
            //parse the string
            String weightStr = "0";
            String[] splitString = detailArray.get(i).split(" ");
            String dateStr = splitString[0];
            for (int j = 0; j < splitString.length - 1; j++)
            {
                if (splitString[j].equals("Weight:")) {
                    weightStr = String.valueOf(splitString[j + 1]);
                    break;
                }
            }

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            try {
                Date date = formatter.parse(dateStr);
                dataPoints[i] = new DataPoint(date, Integer.parseInt(weightStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);

        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(dataPoints[0].getX());
        graph.getViewport().setMaxX(dataPoints[dataPoints.length - 1].getX());
        graph.getViewport().setXAxisBoundsManual(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph_weight, menu);
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
        } else if (id == R.id.addMenu) {
            Intent addIntent = new Intent(this, NewWeightActivity.class);
            startActivity(addIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onViewWeightButton(View view) {
        Intent intent = new Intent(this, ExerciseDetailActivity.class);
        intent.putExtra("type", "weight");
        startActivity(intent);
    }
}
