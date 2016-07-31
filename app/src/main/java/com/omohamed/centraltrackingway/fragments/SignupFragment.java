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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.activities.MainActivity;
import com.omohamed.centraltrackingway.utils.Constants;
import com.omohamed.centraltrackingway.utils.Utilities;

import java.util.Calendar;

import static android.R.anim.fade_in;
import static android.R.anim.fade_out;

/**
 * Fragment used to permit to the user to signup a new account
 */
public class SignupFragment extends Fragment {
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private FirebaseAuth auth;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method that create a new instance of
     * this fragment
     *
     * @return A new instance of fragment SignupFragment.
     */
    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) view.findViewById(R.id.sign_in_button);
        btnSignUp = (Button) view.findViewById(R.id.sign_up_button);
        inputEmail = (EditText) view.findViewById(R.id.email);
        inputPassword = (EditText) view.findViewById(R.id.password);
        btnResetPassword = (Button) view.findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Replace current fragment with Fragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(fade_in, fade_out)
                        .replace(R.id.auth_fragment_container, ResetPasswordFragment.newInstance("",""))
                        .commit();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Note: We are not using popbackstack in order to mantain the fade out animation
                //To obtain the same result, we replace and avoid to add to the backstack
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(fade_in, fade_out)
                        .replace(R.id.auth_fragment_container, SigninFragment.newInstance("",""))
                        .commit();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getActivity(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(getActivity(), getString(R.string.user_with_email_created) + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getActivity(), getString(R.string.authentication_failed) + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //Saving creation date in the database (we are NOT saving also the
                                    //other user data as required by the requirements, as username/email
                                    //or password just because firebase is already storing them in the Firebase user
                                    //object, so are available in any moment
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference();
                                    // myRef.child(Constants.DBNodes.USERS).setValue(auth.getCurrentUser().getUid());
                                    //Adding creationg date
                                    myRef.child(Constants.DBNodes.USERS).child(auth.getCurrentUser().getUid())
                                            .child(Constants.DBNodes.CREATION_DATE)
                                            .setValue(Utilities.formatDate(Calendar.getInstance().getTime()));

                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                }
                            }
                        });

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
