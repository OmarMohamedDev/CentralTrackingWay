package com.omohamed.centraltrackingway.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.fragments.ResetPasswordFragment;
import com.omohamed.centraltrackingway.fragments.SigninFragment;
import com.omohamed.centraltrackingway.fragments.SignupFragment;

public class AuthActivity extends AppCompatActivity implements SigninFragment.OnFragmentInteractionListener,
                                                                SignupFragment.OnFragmentInteractionListener,
                                                                ResetPasswordFragment.OnFragmentInteractionListener{

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(AuthActivity.this, MainActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);

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

