package com.fbauth.checAuth.fragment;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by labattula on 24/10/16.
 */

public class LoginFragment extends BaseFragment {

    private LoginViews views;
    FirebaseAuth auth;

    private static final String TAG = LoginFragment.class.getSimpleName();

    public LoginFragment() {

    }

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        views = new LoginViews(view);
        auth = FirebaseAuth.getInstance();

        views.mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLogin();
            }
        });

        views.mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignUp();
            }
        });
    }

    private void launchSignUp() {
        ((AuthenticationActivity) getActivity()).launchFragment(SignupFragment.newInstance(), "signupFragment");
    }

    private void submitLogin() {
        String email = views.loginEmail.getText().toString().trim();
        String password = views.loginPass.getText().toString().trim();

        if (!checkEmail()) {
            return;
        }
        if (!checkPassword()) {
            return;
        }
        views.emailLayout.setErrorEnabled(false);
        views.passLayout.setErrorEnabled(false);

        views.mProgressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "SignIn:onComplete:" + task.isSuccessful());
                views.mProgressBar.setVisibility(View.GONE);
                // If sign in fails, Log the message to the LogCat. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.d(TAG, "SignIn failed." + task.getException());
                    showSnackBar(getView(), getErrorDescription(task.getException()));
                } else {
                    Toast.makeText(getActivity(), "You are successfully LoggedIn !!", Toast.LENGTH_SHORT).show();
                    ((AuthenticationActivity) getActivity()).launchFragment(AuthFragment.getInstance(), "authFragment");
                }
            }
        });
    }

    private String getErrorDescription(Exception ex){
        return ex.toString().split(":")[1];
    }

    private boolean checkEmail() {
        String email = views.loginEmail.getText().toString().trim();
        if (email.isEmpty() || !isEmailValid(email)) {

            views.emailLayout.setErrorEnabled(true);
            views.emailLayout.setError(getString(R.string.err_msg_email));
            views.loginEmail.setError(getString(R.string.err_msg_required));
            requestFocus(views.loginEmail);
            return false;
        }
        views.emailLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {

        String password = views.loginPass.getText().toString().trim();
        if (password.isEmpty() || !isPasswordValid(password)) {

            views.passLayout.setError(getString(R.string.err_msg_password));
            views.loginPass.setError(getString(R.string.err_msg_required));
            requestFocus(views.loginPass);
            return false;
        }
        views.passLayout.setErrorEnabled(false);
        return true;
    }

    private static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isPasswordValid(String password) {
        return (password.length() >= 6);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(getString(R.string.title_login));
    }

    @Override
    public void onResume() {
        super.onResume();
        views.mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        views = null;
        super.onDestroy();
    }

    static class LoginViews {
        final TextInputLayout emailLayout;
        final TextInputLayout passLayout;
        final ProgressBar mProgressBar;

        final EditText loginEmail;
        final EditText loginPass;

        final Button mLoginBtn;
        final Button mSignUpBtn;

        public LoginViews(View view) {
            emailLayout = (TextInputLayout) view.findViewById(R.id.login_input_layout_email);
            passLayout = (TextInputLayout) view.findViewById(R.id.login_input_layout_password);

            loginEmail = (EditText) view.findViewById(R.id.login_input_email);
            loginPass = (EditText) view.findViewById(R.id.login_input_password);

            mLoginBtn = (Button) view.findViewById(R.id.btn_login);
            mSignUpBtn = (Button) view.findViewById(R.id.btn_link_signup);

            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }
}
