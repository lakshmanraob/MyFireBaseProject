package com.fbauth.checAuth.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.fbauth.checAuth.models.DeviceModel;
import com.fbauth.checAuth.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by labattula on 24/10/16.
 */

public class AuthFragment extends BaseFragment {

    private AuthViews views;

    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener stateListener;

    private FirebaseDatabase mFireDatabaseInstance;
    private DatabaseReference mDatabaseReference;

    ArrayList<DeviceModel> deviceModelArrayList = null;

    SharedPreferences sharedPreferences = null;

    public AuthFragment() {
        //fillDeviceList();
    }

    /**
     * Enable only when we required data in the FB database
     */
    private void fillDeviceList() {
        mFireDatabaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFireDatabaseInstance.getReference("devices");
        deviceModelArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DeviceModel model = new DeviceModel();
            model.setModelString("Device" + i);
            model.setModelState("available");
            model.setModelImage("/sample/image-" + i);
            deviceModelArrayList.add(model);
            mDatabaseReference.child(model.getModelString()).setValue(model);
        }
        //mDatabaseReference.setValue(deviceModelArrayList);
    }

    public static AuthFragment getInstance() {
        return new AuthFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences("FBAccount", Context.MODE_PRIVATE);
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
                    if (getActivity() != null) {
                        views.loggedUserView.setText("Hi.." + user.getEmail() + "");
                        getActivity().setTitle("Hi.." + user.getEmail() + "");
                        updateLogin(true);
                        //getProfileDetails(user);
                        retrieveSampleList();
                    }
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

    /**
     * Getting the profile details from FB
     *
     * @param user
     */
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
                //launchSampleList();
                //fillSampleList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void retrieveSampleList() {
        mFireDatabaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFireDatabaseInstance.getReference("devices");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<DeviceModel> deviceModelList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("snapshot", "onDataChange: ");

                    DeviceModel model = snapshot.getValue(DeviceModel.class);
                    deviceModelList.add(model);
                }
                launchSampleList(deviceModelList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast("sample retrive failed");
            }
        });
    }


    private void launchSampleList(ArrayList<DeviceModel> deviceModelArrayList) {
        SampleRecyclerAdapter adapter = new SampleRecyclerAdapter();
        adapter.setSampleModelList(deviceModelArrayList, recyclerItemClickListener);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        views.sampleRecyclerView.setLayoutManager(mLayoutManager);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        views.sampleRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        views.sampleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        views.sampleRecyclerView.setAdapter(adapter);
        views.sampleRecyclerView.addItemDecoration(new SpaceItemDecor(16));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        views.sampleRecyclerView.setHasFixedSize(true);
    }

    class SpaceItemDecor extends RecyclerView.ItemDecoration {
        private final int space;

        public SpaceItemDecor(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //super.getItemOffsets(outRect, view, parent, state);
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = space;
        }
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
//        views = null;
        super.onDestroy();
    }

    private void signOut() {
        showToast("Bye");
        updateLogin(false);
        auth.signOut();
    }

    private void updateLogin(boolean isLoggedIn) {
        if (sharedPreferences == null) {
            sharedPreferences = getActivity().getSharedPreferences("FBAccount", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedIn", isLoggedIn);
        editor.apply();
    }

    private void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }


    recyclerItemClickListener recyclerItemClickListener = new recyclerItemClickListener() {
        @Override
        public void clickModel(DeviceModel model) {
            Toast.makeText(getContext(), model.getModelString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(getContext(), SampleDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("code", model.getModelString());
            bundle.putParcelable(DeviceModel.SELECTED_DEVICE, model);
            intent.putExtras(bundle);
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
        public void clickModel(DeviceModel model);
    }
}
