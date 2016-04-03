package com.example.mohmurtu.registration;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

public class ActivityHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView companyName ;
    ProgressDialog pd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Seller Dashboard");
        setSupportActionBar(toolbar);

        if(SharedPrefUtil.getBooleanPrefs(Constants.IS_GCM_ID_UPLOADED_IN_SERVER) == false){
            Intent intent = new Intent(this,RegisterGCMSenderID.class);
            startService(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        companyName = (TextView) findViewById(R.id.company_name_home);
//        companyName.setText("Fabjewel Enterprises");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Bundle bundle = getIntent().getExtras();
        boolean notificationFlag = bundle.getBoolean("productNotification");
        Fragment f ;
        if(notificationFlag == true){
            f = new MyProducts();
        }
        else{
            f = new DashboardFragment();
        }
        if(f != null){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.login_fragment,f);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.send_feedback1) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null ;

        if (id == R.id.dashboard) {
            fragment = new DashboardFragment();

        } else if (id == R.id.my_products) {
            fragment = new MyProducts();

        } else if (id == R.id.new_products) {
            fragment = new CategoryOneList();

        } else if (id == R.id.my_orders) {
            fragment = new MyOrders();

        } else if (id == R.id.account) {
            fragment = new AccountFragment();

        } else if (id == R.id.logout) {
            this.finish();
            this.getSupportFragmentManager().popBackStack();
            SharedPrefUtil.setBooleanPrefs(Constants.IS_LOGGED_IN, false);
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if(fragment != null){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.login_fragment,fragment);
            transaction.commit();
        }
        return true;
    }
}
