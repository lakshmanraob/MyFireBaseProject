package com.fbauth.checAuth.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fbauth.checAuth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by labattula on 24/10/16.
 */

public class AuthFragment extends BaseFragment {

    private AuthViews views;

    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener stateListener;

    public AuthFragment() {

    }

    public static AuthFragment getInstance() {
        return new AuthFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        views = new AuthViews(view);
        auth = FirebaseAuth.getInstance();

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    showToast("Sorry we are unable to get the info of yours");
                    getActivity().finish();
                } else {
                    views.loggedUserView.setText("Hi.." + user.getEmail() + "");
                }
            }
        };

        views.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(getString(R.string.logged_in_str));
        auth.addAuthStateListener(stateListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (stateListener != null) {
            auth.removeAuthStateListener(stateListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        views = null;
        super.onDestroy();
    }

    private void signOut() {
        showToast("Bye");
        auth.signOut();
    }

    private void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    static class AuthViews {
        final TextView loggedUserView;
        final Button signOutBtn;

        public AuthViews(View view) {
            loggedUserView = (TextView) view.findViewById(R.id.hi_user);
            signOutBtn = (Button) view.findViewById(R.id.signOutBtn);
        }
    }
}
