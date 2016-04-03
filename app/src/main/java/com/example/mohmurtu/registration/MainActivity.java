package com.example.mohmurtu.registration;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

public class MainActivity extends AppCompatActivity{

    boolean productNotificationFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Distributor Desk");
        Bundle bundle = getIntent().getExtras();
        if((bundle != null) &&  bundle.getString("productNotification") != null){
            productNotificationFlag = true;
        }
        processScreens();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.send_feedback) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void processScreens(){
        boolean loginFlag = SharedPrefUtil.getBooleanPrefs(Constants.IS_LOGGED_IN);
        boolean rememberFlag = SharedPrefUtil.getBooleanPrefs(Constants.REMEMBER_ME);
        if(loginFlag == true && rememberFlag == true ){
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction transaction = manager.beginTransaction();
//            SplashFragment sf = new SplashFragment();
//            transaction.add(R.id.fragment_body,sf);
//            transaction.commit();
            Intent intent = new Intent(this,ActivityHome.class);
            if(productNotificationFlag == true){
                intent.putExtra("productNotification", true);
            }
            else{
                intent.putExtra("productNotification", false);
            }
            startActivity(intent);
            finish();
        }
        else{
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            LoginFragment registerHome = new LoginFragment();
            transaction.replace(R.id.fragment_body,registerHome);
            transaction.commit();
        }
    }
}
