package com.omohamed.centraltrackingway.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.activities.MainActivity;

import static android.R.anim.fade_in;
import static android.R.anim.fade_out;

//"Transaction between fragments" animations

/**
 * Fragment that permit to the user to signin in the application
 * providing email and password created in the Signup page
 * @author omarmohamed
 */
public class SigninFragment extends Fragment {

    /**
     * Constant used in the app log
     */
    private static final String TAG = SigninFragment.class.getSimpleName();

    /**
     * User email editext
     */
    private EditText mInputEmail;

    /**
     * User password edittext
     */
    private EditText mInputPassword;

    /**
     * Variable used to retrieve the Firebase authentication
     */
    private FirebaseAuth mAuth;

    /**
     * Button that permit to go to the signup fragment
     */
    private Button mButtonSignup;

    /**
     * Button that trigger the signin process
     */
    private Button mButtonSignin;

    /**
     * Button that permit to go to the reset password fragment
     */
    private Button mButtonResetPassword;

    public SigninFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method that reate a new instance of
     * this fragment
     *
     * @return A new instance of fragment SigninFragment.
     */
    public static SigninFragment newInstance() {
        SigninFragment fragment = new SigninFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        mInputEmail = (EditText) view.findViewById(R.id.email);
        mInputPassword = (EditText) view.findViewById(R.id.password);
        mButtonSignup = (Button) view.findViewById(R.id.btn_signup);
        mButtonSignin = (Button) view.findViewById(R.id.btn_login);
        mButtonResetPassword = (Button) view.findViewById(R.id.btn_reset_password);

        //Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();

        //Setting up the transition to the signup fragment
        mButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Replace current fragment with SignupFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(fade_in, fade_out)
                        .replace(R.id.auth_fragment_container, SignupFragment.newInstance())
                        .commit();
            }
        });

        //Setting up the transition to the reset password fragment
        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Replace current fragment with ResetPasswordFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(fade_in, fade_out)
                        .replace(R.id.auth_fragment_container, ResetPasswordFragment.newInstance("",""))
                        .commit();
            }
        });

        //Triggering the signin process
        mButtonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mInputEmail.getText().toString();
                final String password = mInputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the mAuth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        mInputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    //Signin successful, redirecting to the core activity
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        });
            }
        });

        return view;
    }
}
