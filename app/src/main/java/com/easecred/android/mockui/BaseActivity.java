package com.easecred.android.mockui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



import java.util.List;

public  abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LoginManager.LoginListener {

    private LoginManager mLoginManager;
    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mLoginManager = new LoginManager(getApplicationContext());
        mLoginManager.setLoginListener(this);

        Log.d(TAG, "User Logged State:" + mLoginManager.isLogged());
        if(!mLoginManager.isLogged()) {
            startLoginActivity();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(getSelectedDrawerItem());
        createFragment();

    }

    protected void createFragment() {

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if(f == null) {
            f = getFragmentInstance();
            fm.beginTransaction().add(R.id.fragment_container,f).commit();
        }
    }

    protected abstract Fragment getFragmentInstance();

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mLoginManager.logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.message) {
            startMessageActivity();

        } else if (id == R.id.app_setting) {


        } else if (id == R.id.credit_apply) {
            startCreditAppActivity();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected abstract void startMessageActivity();

    protected abstract void startCreditAppActivity();

    protected abstract int getSelectedDrawerItem();


    public boolean isIntentRegistered(Intent intent) {
        final PackageManager packageManager = this.getPackageManager();
        final List<ResolveInfo> receiverList = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return receiverList.size() > 0;
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        mLoginManager.preserveMockUIAppState();
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public LoginManager getLoginManager() {
        return  mLoginManager;
    }


    @Override
    public void onLogin() {}

    @Override
    public void onLogout() {
       startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(intent.getFlags());
        startActivity(intent);
        finish();
    }
}
