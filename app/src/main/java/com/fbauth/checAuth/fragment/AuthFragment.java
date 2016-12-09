package com.fbauth.checAuth.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fbauth.checAuth.R;
import com.fbauth.checAuth.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by labattula on 24/10/16.
 */

public class AuthFragment extends BaseFragment {

    private AuthViews views;

    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener stateListener;

    private FirebaseDatabase mFireDatabaseInstance;
    private DatabaseReference mDatabaseReference;

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
                    getActivity().setTitle("Hi.." + user.getEmail() + "");
                    getProfileDetails(user);
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

    private void getProfileDetails(FirebaseUser user) {
        mFireDatabaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFireDatabaseInstance.getReference(user.getUid());

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * mDatabase = mFireBaseDatabase.getReference(authResult.getUser().getUid());
                 mDatabase.child(Constants.DB_USER).child(Constants.DB_PROFILE).setValue(profile);
                 */

                String key = dataSnapshot.getKey();
                User connectedUser = dataSnapshot.getValue(User.class);

                Toast.makeText(getActivity(), connectedUser.getProfile().getPhoneNumber(), Toast.LENGTH_SHORT).show();

                Log.i("check", "onDataChange: ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
