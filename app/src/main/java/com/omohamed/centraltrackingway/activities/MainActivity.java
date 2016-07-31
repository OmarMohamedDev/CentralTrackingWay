package com.omohamed.centraltrackingway.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.fragments.ExpensesTrackingFragment;
import com.omohamed.centraltrackingway.fragments.ManipulateExpenseFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   ExpensesTrackingFragment.OnFragmentInteractionListener,
                   ManipulateExpenseFragment.OnFragmentInteractionListener{

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //If the user is not logged-in, the app bring him back to the authentication page
        //Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
            Log.d(MainActivity.class.getSimpleName(), "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
            // User is signed out
            Log.d(MainActivity.class.getSimpleName(), "onAuthStateChanged:signed_out");
        }
        //

        //Authentication listener used by Firebase Library
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, AuthActivity.class));
                    finish();
                    Log.d(MainActivity.class.getSimpleName(), "onAuthStateChanged:signed_in:");
                } else {
                    // User is signed out
                    Log.d(MainActivity.class.getSimpleName(), "onAuthStateChanged:signed_out");
                }
            }
        };
        //

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Add the fragment to the 'core_fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.core_fragment_container, ExpensesTrackingFragment.newInstance("",""))
                .commit();
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
        //TODO: Low Priority - Add useful settings
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //TODO: Check if the parent/back button connections are properly setted
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_expenses) {
            // Handle the expense action
        }
        /*
        else if (id == R.id.nav_change_email) {
            //TODO: Low Priority

        } else if (id == R.id.nav_change_password) {
            //TODO: Low Priority

        } else if (id == R.id.nav_remove_user) {
            //TODO: Low Priority

        }
        */
        else if (id == R.id.nav_sign_out) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //sign out method
    private void signOut() {
        mAuth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //TODO: Check if necessary
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
