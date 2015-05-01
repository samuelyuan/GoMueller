package com.example.rcos.gomueller;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rcos.gomueller.ui.GraphActivity;
import com.example.rcos.gomueller.ui.MainActivity;
import com.example.rcos.gomueller.ui.TrackExerciseActivity;

public class NavigationDrawer
{
    private Activity activity;

    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    public NavigationDrawer(final ActionBarActivity activity)
    {
        init(activity);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
    }

    public NavigationDrawer(ListActivity activity)
    {
        init(activity);

        activity.getActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getActionBar().setHomeButtonEnabled(true);
    }

    public void init(Activity activity)
    {
        this.activity = activity;

        mDrawerTitles = this.activity.getResources().getStringArray(R.array.drawer_array);
        mDrawerLayout = (DrawerLayout) this.activity.findViewById(R.id.main_layout);
        mDrawerList = (ListView) this.activity.findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this.activity, R.layout.drawer_list_item, mDrawerTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                NavigationDrawer.this.activity,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                NavigationDrawer.this.activity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                NavigationDrawer.this.activity.invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        else
            return false;
    }

    public void syncState()
    {
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);

            if(position == 0) {
                activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
            } else if (position == 1) {
                activity.startActivity(new Intent(activity.getApplicationContext(), TrackExerciseActivity.class));
            } else if (position == 2) {
                Intent intent = new Intent(activity.getApplicationContext(), GraphActivity.class);
                intent.putExtra("type", "weight");
                activity.startActivity(intent);
            }
        }
    }
}
