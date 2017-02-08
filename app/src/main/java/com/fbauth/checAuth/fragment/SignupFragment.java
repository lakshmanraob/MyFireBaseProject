package com.fbauth.checAuth.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fbauth.checAuth.AuthenticationActivity;
import com.fbauth.checAuth.R;
import com.fbauth.checAuth.models.Profile;
import com.fbauth.checAuth.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by labattula on 24/10/16.
 */

public class SignupFragment extends BaseFragment {

    private SignUpViews mSignupViews;
    private final String TAG = SignupFragment.class.getSimpleName();

    private FirebaseAuth auth;

    private DatabaseReference mDatabase;
    private FirebaseDatabase mFireBaseDatabase;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSignupViews = new SignUpViews(view);

        auth = FirebaseAuth.getInstance();
        mFireBaseDatabase = FirebaseDatabase.getInstance();


        //signup
        mSignupViews.mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        //login
        mSignupViews.mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLogin();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(getString(R.string.btn_sign_up));
    }

    @Override
    public void onResume() {
        super.onResume();
        mSignupViews.mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        mSignupViews = null;
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void submitForm() {
        String email = mSignupViews.signupEmail.getText().toString().trim();
        String password = mSignupViews.signupPass.getText().toString().trim();

        if (!checkEmail()) {
            return;
        }
        if (!checkPassword()) {
            return;
        }
        if (!checkPhone()) {
            return;
        }

        mSignupViews.emailLayout.setErrorEnabled(false);
        mSignupViews.passLayout.setErrorEnabled(false);

        mSignupViews.mProgressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        mSignupViews.mProgressBar.setVisibility(View.GONE);
                        // If sign in fails, Log the message to the LogCat. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "Authentication failed." + task.getException());
                        } else {
                            Toast.makeText(getActivity(), "You are successfully Registered !!", Toast.LENGTH_SHORT).show();

                            createProfile(task.getResult());

                            ((AuthenticationActivity) getActivity()).launchFragment(AuthFragment.getInstance(), "authFragment");
                        }
                    }
                });
    }

    /**
     * Creates the profile with the authResult
     *
     * @param authResult
     */
    private void createProfile(AuthResult authResult) {
        String phone = mSignupViews.signupPhone.getText().toString().trim();
        Profile profile = new Profile();
        profile.setUserEmail(authResult.getUser().getEmail());
        profile.setPhoneNumber(phone);
        profile.setUserName(authResult.getUser().getEmail());
        profile.setUserUDID(authResult.getUser().getUid());
        User user = new User();
        user.setProfile(profile);

        mDatabase = mFireBaseDatabase.getReference(authResult.getUser().getUid());
        mDatabase.setValue(user);
    }

    private void launchLogin() {
        ((AuthenticationActivity) getActivity()).launchFragment(LoginFragment.getInstance(), "loginFragment");
    }

    private boolean checkEmail() {
        String email = mSignupViews.signupEmail.getText().toString().trim();
        if (email.isEmpty() || !isEmailValid(email)) {

            mSignupViews.emailLayout.setErrorEnabled(true);
            mSignupViews.emailLayout.setError(getString(R.string.err_msg_email));
            mSignupViews.signupEmail.setError(getString(R.string.err_msg_required));
            requestFocus(mSignupViews.signupEmail);
            return false;
        }
        mSignupViews.emailLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {

        String password = mSignupViews.signupPass.getText().toString().trim();
        if (password.isEmpty() || !isPasswordValid(password)) {

            mSignupViews.passLayout.setError(getString(R.string.err_msg_password));
            mSignupViews.signupPass.setError(getString(R.string.err_msg_required));
            requestFocus(mSignupViews.signupPass);
            return false;
        }
        mSignupViews.passLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkPhone() {

        String phone = mSignupViews.signupPhone.getText().toString().trim();
        if (phone.isEmpty() || !isPhoneValid(phone)) {

            mSignupViews.phoneLayout.setError(getString(R.string.err_msg_password));
            mSignupViews.signupPhone.setError(getString(R.string.err_msg_required));
            requestFocus(mSignupViews.signupPhone);
            return false;
        }
        mSignupViews.phoneLayout.setErrorEnabled(false);
        return true;
    }


    private static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isPasswordValid(String password) {
        return (password.length() >= 6);
    }

    private static boolean isPhoneValid(String phone) {
        return (phone.length() >= 6);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    static class SignUpViews {
        final TextInputLayout emailLayout;
        final TextInputLayout passLayout;
        final TextInputLayout phoneLayout;
        final ProgressBar mProgressBar;

        final EditText signupEmail;
        final EditText signupPass;
        final EditText signupPhone;

        final Button mSignUpBtn;
        final Button mLoginBtn;

        public SignUpViews(View view) {
            emailLayout = (TextInputLayout) view.findViewById(R.id.signup_input_layout_email);
            passLayout = (TextInputLayout) view.findViewById(R.id.signup_input_layout_password);
            phoneLayout = (TextInputLayout) view.findViewById(R.id.signup_input_layout_phone);

            signupEmail = (EditText) view.findViewById(R.id.signup_input_email);
            signupPass = (EditText) view.findViewById(R.id.signup_input_password);
            signupPhone = (EditText) view.findViewById(R.id.signup_input_phone);

            mSignUpBtn = (Button) view.findViewById(R.id.btn_signup);
            mLoginBtn = (Button) view.findViewById(R.id.btn_link_login);

            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }
}
