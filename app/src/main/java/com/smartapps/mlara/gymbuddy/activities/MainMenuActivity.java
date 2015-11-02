package com.smartapps.mlara.gymbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseUser;
import com.smartapps.mlara.gymbuddy.R;

import java.util.List;

import util.Constants;

/**
 * Created by mlara on 9/30/2015.
 */
public class MainMenuActivity extends ActionBarActivity {
    private List<String> menuItems;
    private ListView mDrawerList;
    private DrawerLayout mainDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        menuItems= Constants.leftMenuItems;
        mainDrawerLayout=(DrawerLayout) findViewById(R.id.mainMenu_drawer_layout);
        mDrawerList=(ListView) findViewById(R.id.mainMenu_drawer_navList);
        mActivityTitle=getTitle().toString();
        //Set the List click listeners

        setupDrawer();

        //set the adapter for the list
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item_1, menuItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



    }



    private void setupDrawer() {

        mDrawerToggle= new ActionBarDrawerToggle(this,mainDrawerLayout,R.string.drawer_open,R.string.drawer_close){
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.menu);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mainDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       // routeActionAfterClickDrawerItem(id);
        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            routeActionAfterClickDrawerItem(position);
        }
    }

    public void routeActionAfterClickDrawerItem(int position){
        mDrawerList.setItemChecked(position, true);
        Log.v("CHECK", "Position outside is "+position);

        switch (position){
            case 0:
                //Edit Profile Option
                Log.v("CHECK", "Position inside is "+position);
                MainMenuActivity.this.startActivity(new Intent(MainMenuActivity.this, EditProfileActivity.class));
                break;
            case 1:
                //Log out
                Log.v("CHECK", "CAME IN 2");
                ParseUser.logOut();
                //redirecting to log in screen
                MainMenuActivity.this.startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));
                break;

        }
       /* if(position==0){
            //Profile option
            Log.v("CHECK", "CAME IN 1");
        }else if(position==1){
            //Log out
            Log.v("CHECK", "CAME IN 2");
            ParseUser.logOut();
            //redirecting to log in screen
            MainMenuActivity.this.startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));



        } *///here add more cases depending on the drawer options
    }
}
