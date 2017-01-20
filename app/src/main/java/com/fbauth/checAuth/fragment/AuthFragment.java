package com.fbauth.checAuth.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fbauth.checAuth.R;
import com.fbauth.checAuth.activities.SampleDetailsActivity;
import com.fbauth.checAuth.adapters.SampleRecyclerAdapter;
import com.fbauth.checAuth.models.SampleModel;
import com.fbauth.checAuth.models.SampleModelDetails;
import com.fbauth.checAuth.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by labattula on 24/10/16.
 */

public class AuthFragment extends BaseFragment {

    private AuthViews views;

    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener stateListener;

    private FirebaseDatabase mFireDatabaseInstance;
    private DatabaseReference mDatabaseReference;

    ArrayList<SampleModel> sampleModelArrayList = null;

    public AuthFragment() {
        fillSampleList();
    }

    private void fillSampleList() {
        sampleModelArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SampleModel model = new SampleModel();
            model.setModelString("sampleMode.." + i);
            sampleModelArrayList.add(model);
        }
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
                launchSampleList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void launchSampleList() {
        SampleRecyclerAdapter adapter = new SampleRecyclerAdapter();
        adapter.setSampleModelList(sampleModelArrayList, recyclerItemClickListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        views.sampleRecyclerView.setLayoutManager(mLayoutManager);
        views.sampleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        views.sampleRecyclerView.setAdapter(adapter);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        views.sampleRecyclerView.setHasFixedSize(true);
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

    recyclerItemClickListener recyclerItemClickListener = new recyclerItemClickListener() {
        @Override
        public void clickModel(SampleModel model) {
            Toast.makeText(getContext(), model.getModelString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(getContext(), SampleDetailsActivity.class);
            startActivity(intent);
        }
    };

    static class AuthViews {
        final TextView loggedUserView;
        final Button signOutBtn;
        final RecyclerView sampleRecyclerView;

        public AuthViews(View view) {
            loggedUserView = (TextView) view.findViewById(R.id.hi_user);
            signOutBtn = (Button) view.findViewById(R.id.signOutBtn);
            sampleRecyclerView = (RecyclerView) view.findViewById(R.id.sampleList);
        }
    }

    public interface recyclerItemClickListener {
        public void clickModel(SampleModel model);
    }
}
