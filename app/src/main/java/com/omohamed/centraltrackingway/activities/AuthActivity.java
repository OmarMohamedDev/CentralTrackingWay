package com.omohamed.centraltrackingway.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.fragments.ResetPasswordFragment;
import com.omohamed.centraltrackingway.fragments.SigninFragment;

public class AuthActivity extends AppCompatActivity implements SigninFragment.OnFragmentInteractionListener,
                                                                ResetPasswordFragment.OnFragmentInteractionListener{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If the user is logged-in, the app bring him directly to the core page view
        //Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (mAuth.getCurrentUser()!= null) {
            startActivity(new Intent(AuthActivity.this, MainActivity.class));
            finish();
            Log.d(AuthActivity.class.getSimpleName(), "onAuthStateChanged:signed_in:");
        } else {
            // User is signed out
            Log.d(AuthActivity.class.getSimpleName(), "onAuthStateChanged:signed_out");
        }
        //

        //Authentication listener used by the Firebase library
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    finish();
                    Log.d(AuthActivity.class.getSimpleName(), "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(AuthActivity.class.getSimpleName(), "onAuthStateChanged:signed_out");
                }
            }
        };
        //

        // set the view now
        setContentView(R.layout.activity_auth);

        //Setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.auth_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.auth_fragment_container, SigninFragment.newInstance("",""))
                    .commit();
        }
    }

    //TODO:Check if necessary
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

