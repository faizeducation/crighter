package com.crighter.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crighter.Preferences.SPref;
import com.crighter.R;
import com.google.android.material.navigation.NavigationView;

public class BaseActvitvityForDrawer extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    MenuItem navLoginRegister;
    MenuItem navUsername;
    View view;
    RelativeLayout rl_alert_icon;
    TextView tv_privacy_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_actvitvity_for_drawer);
        view = new View(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;
        mNavigationView.setItemIconTintList(null);

       Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/office_square.otf");
       // custom_font2 = Typeface.createFromAsset(getAssets(),  "fonts/century_gothic.ttf");

        tv_privacy_policy = (TextView) findViewById(R.id.tv_privacy_policy);
        tv_privacy_policy.setTypeface(custom_font);

        tv_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.crighter.com/privacy.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        // get menu from navigationView
        Menu menu = mNavigationView.getMenu();

        // find MenuItem you want to change
        navUsername = menu.findItem(R.id.username);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nave_item_dashboard) {
                    //home activity
                    //Toast.makeText(getApplicationContext(), "DashBoard", Toast.LENGTH_SHORT).show();

                }

                if (menuItem.getItemId() == R.id.username) {

                    Intent i = new Intent(BaseActvitvityForDrawer.this, UpdateProfileActivity.class);
                    startActivity(i);


                }
                if (menuItem.getItemId() == R.id.nave_danger_zone){


                    Intent i = new Intent(BaseActvitvityForDrawer.this, DangerMapsActivity.class);
                    startActivity(i);

                }
               /* if (menuItem.getItemId() == R.id.nav_item_about){

                    Intent i = new Intent(BaseActvitvityForDrawer.this, AboutUs.class);
                    startActivity(i);

                }*/
                if (menuItem.getItemId() == R.id.nav_item_contact_us){

                    Intent i = new Intent(BaseActvitvityForDrawer.this, ContactUs.class);
                    startActivity(i);

                }

                if (menuItem.getItemId() == R.id.aboutus){

                    Intent i = new Intent(BaseActvitvityForDrawer.this, UserHistory.class);
                    startActivity(i);
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        //end on Create
        rl_alert_icon = (RelativeLayout) menu.findItem(R.id.username).getActionView().findViewById(R.id.rl_alert_icon);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
        String fullName = SPref.getStringPref(sharedPreferences, SPref.USER_FULLNAME);
        navUsername.setTitle(fullName);


        String vStatus = SPref.getStringPref(sharedPreferences, SPref.EVERIFY_STATUS);
        if (vStatus.equals("1")) {
            rl_alert_icon.setBackground(getResources().getDrawable(R.drawable.green_tick));
        }

    }
}