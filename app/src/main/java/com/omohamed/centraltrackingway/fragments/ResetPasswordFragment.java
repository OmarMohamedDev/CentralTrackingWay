package com.omohamed.centraltrackingway.fragments;

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
import com.google.firebase.auth.FirebaseAuth;
import com.omohamed.centraltrackingway.R;

import static android.R.anim.fade_in;
import static android.R.anim.fade_out;

/**
 * Fragment that permit to the user to reset his current password and set a new one
 * @author omarmohamed
 */
public class ResetPasswordFragment extends Fragment {

    /**
     * Constant used in the app log
     */
    private static final String TAG = ResetPasswordFragment.class.getSimpleName();

    /**
     * User mail address
     */
    private EditText inputEmail;

    /**
     * Button used to reset the password
     */
    private Button btnReset;

    /**
     * Button used to go back to the authentication page
     */
    private Button btnBack;

    /**
     * Variable used to retrieve the Firebase authentication
     */
    private FirebaseAuth auth;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method that create a new instance of
     * this fragment

     * @return A new instance of fragment ResetPasswordFragment.
     */
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        inputEmail = (EditText) view.findViewById(R.id.email);
        btnReset = (Button) view.findViewById(R.id.btn_reset_password);
        btnBack = (Button) view.findViewById(R.id.btn_back);
        auth = FirebaseAuth.getInstance();

        //Going back to signin fragment
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Note: We are not using popbackstack in order to mantain the fade out animation
                //To obtain the same result, we replace and avoid to add to the backstack
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(fade_in, fade_out)
                        .replace(R.id.auth_fragment_container, SigninFragment.newInstance())
                        .commit();
            }
        });

        //Resetting password
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), getString(R.string.enter_registered_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), getString(R.string.instrunctions_reset_password_sent), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.reset_password_failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return view;
    }
}
