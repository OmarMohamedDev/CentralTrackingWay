package com.omohamed.centraltrackingway.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.fragments.ExpensesTrackingFragment;

/**
 * Fragment that is used as container for the fragments that manage the core functionality of the app
 *
 * @author omarmohamed
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Constant used in the app log
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Variable used to retrieve the Firebase authentication
     */
    private FirebaseAuth mAuth;

    /**
     * Listener used to capture Firebase authentication status updates
     */
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view
        setContentView(R.layout.activity_main);

        //Setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //If the user is not logged-in, the app bring him back to the authentication page
        //Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
            Log.d(MainActivity.class.getSimpleName(), "onAuthStateChanged:signed_in");
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

        //Setting up Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //

        // Add the fragment to the 'core_fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.core_fragment_container, ExpensesTrackingFragment.newInstance())
                .commit();
    }

    @Override
    public void onBackPressed() {
        //Managing navigation drawer behavior
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_expenses) {
            //TODO: Handle the expense action
        }
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
}
