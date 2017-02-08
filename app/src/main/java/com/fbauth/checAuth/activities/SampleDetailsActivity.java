package com.fbauth.checAuth.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fbauth.checAuth.R;
import com.fbauth.checAuth.adapters.SDetailsHistoryAdapter;
import com.fbauth.checAuth.fragment.AuthFragment;
import com.fbauth.checAuth.models.DeviceModel;
import com.fbauth.checAuth.models.DeviceUsageHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by labattula on 19/01/17.
 */

public class SampleDetailsActivity extends AppCompatActivity {

    TextView detailsText;
    ImageView barcodeImg;
    DeviceModel model;

    TextView modelDetailsName;
    TextView modelDeatilsDate;
    Button requestDeviceBtn;
    RecyclerView historyList;
    TextView noHistory;

    FirebaseDatabase mFireDatabaseInstance;
    DatabaseReference mDatabaseReference;

    DeviceModel selecteddeviceModel;

    private static String currentUserProfileId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("sampleModelDetails");
        }

        mFireDatabaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFireDatabaseInstance.getReference("history");

        currentUserProfileId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Bundle arguments = getIntent().getExtras();
        initViews(arguments);
    }

    private void initViews(Bundle extras) {
        barcodeImg = (ImageView) findViewById(R.id.barCode);
        detailsText = (TextView) findViewById(R.id.detailsTxt);

        modelDetailsName = (TextView) findViewById(R.id.sdDetailsName);
        modelDeatilsDate = (TextView) findViewById(R.id.sdDetailsDate);
        historyList = (RecyclerView) findViewById(R.id.historyList);
        requestDeviceBtn = (Button) findViewById(R.id.request_device);
        noHistory = (TextView) findViewById(R.id.no_history);

        if (extras != null) {
            selecteddeviceModel = extras.getParcelable(DeviceModel.SELECTED_DEVICE);
            if (selecteddeviceModel != null) {
                //bindData(selecteddeviceModel, null, currentUserProfileId);
                String ref = "devices/" + selecteddeviceModel.getModelString();
                updateHistory(mFireDatabaseInstance.getReference(ref), false);
            }
        }

    }

    private void bindData(final DeviceModel model, ArrayList<DeviceUsageHistory> history, String userProfileId) {
        if (model != null) {
            if (model.getModelString() != null) {
                modelDetailsName.setText(model.getModelString());
            }

            if (model.getModelDate() != null) {
                modelDeatilsDate.setText(model.getModelDate());
            }

            if (isSelectedDeviceAvailable()) {
                requestDeviceBtn.setVisibility(View.VISIBLE);
                requestDeviceBtn.setText(R.string.request_device);
                requestDeviceBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(SampleDetailsActivity.this, "request", Toast.LENGTH_SHORT).show();
                        updateRequest(model.getModelString());
                    }
                });
            } else if (model.getModelState().equalsIgnoreCase(getString(R.string.engage))) {
                if (isCurrentUser(userProfileId)) {
                    requestDeviceBtn.setVisibility(View.VISIBLE);
                    requestDeviceBtn.setText(R.string.return_device);
                    requestDeviceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(SampleDetailsActivity.this, "return", Toast.LENGTH_SHORT).show();
                            updateRequest(model.getModelString());
                        }
                    });
                }
            } else {
                requestDeviceBtn.setVisibility(View.GONE);
            }
            if (history == null) {
                historyList.setVisibility(View.GONE);
                noHistory.setVisibility(View.VISIBLE);
            } else {
                noHistory.setVisibility(View.GONE);
                historyList.setVisibility(View.VISIBLE);

                Collections.sort(history);

                SDetailsHistoryAdapter adapter = new SDetailsHistoryAdapter();
                adapter.setHistoryArrayList(history);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                historyList.setLayoutManager(linearLayoutManager);

                historyList.setItemAnimator(new DefaultItemAnimator());
                historyList.setAdapter(adapter);
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                historyList.setHasFixedSize(true);
            }
        }
    }

    private void updateRequest(final String childName) {
        mDatabaseReference = mFireDatabaseInstance.getReference("devices");

        Query childQuery =
                mDatabaseReference.orderByChild(DeviceModel.SM_MODEL_STRING).equalTo(childName);

        childQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(SampleDetailsActivity.this, "dataSnapshot Value", Toast.LENGTH_SHORT).show();
                if (dataSnapshot != null) {
                    DeviceModel requestModel = getRequestedModel(dataSnapshot, childName);
                    if (requestModel.getModelState().equalsIgnoreCase(getString(R.string.available_str))) {
                        requestModel.setModelState(getString(R.string.engage));
                    } else {
                        requestModel.setModelState(getString(R.string.available_str));
                    }
                    //selecteddeviceModel = requestModel;
                    mDatabaseReference.child(childName).setValue(requestModel, updateCompletionLister);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private DatabaseReference.CompletionListener updateCompletionLister = new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            Toast.makeText(SampleDetailsActivity.this, "update completed", Toast.LENGTH_SHORT).show();
            updateHistory(databaseReference, true);
        }
    };

    private void updateHistory(final DatabaseReference reference, final boolean updateNeeded) {
        final DatabaseReference updateDBreference = reference.getDatabase().getReference("history");
        //reference.child(reference.getKey());

        final ArrayList<DeviceUsageHistory> history = new ArrayList<>();

        final Query childQuery =
                updateDBreference.orderByKey().equalTo(reference.getKey());

        childQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //This is for updating the device taken
                    ArrayList<DeviceUsageHistory> updatedHistory
                            = updateDeviceHistory(dataSnapshot, reference.getKey(), updateNeeded);
                    if (updateNeeded) {
                        childQuery.getRef().child(reference.getKey()).setValue(updatedHistory);
                    }
                    bindData(selecteddeviceModel, updatedHistory, currentUserProfileId);
                } else {
                    DeviceUsageHistory deviceUsageHistory = new DeviceUsageHistory();
                    deviceUsageHistory.setProfileId(currentUserProfileId);
                    if (isSelectedDeviceAvailable()) {
                        selecteddeviceModel.setModelState(getString(R.string.engage));
                        deviceUsageHistory.setStartTime(Calendar.getInstance().getTimeInMillis() + "");
                    } else {
                        selecteddeviceModel.setModelState(getString(R.string.available_str));
                        deviceUsageHistory.setEndTime(Calendar.getInstance().getTimeInMillis() + "");
                    }
                    history.add(deviceUsageHistory);
                    childQuery.getRef().child(reference.getKey()).setValue(history);
                    bindData(selecteddeviceModel, history, deviceUsageHistory.getProfileId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isSelectedDeviceAvailable() {
        if (selecteddeviceModel.getModelState().equalsIgnoreCase(getString(R.string.available_str))) {
            return true;
        }
        return false;
    }

    private boolean isCurrentUser(String profileId) {
        if (currentUserProfileId.equalsIgnoreCase(profileId)) {
            return true;
        }
        return false;
    }

    /**
     * Gets the device usage history
     *
     * @param dataSnapshot
     * @return
     */
    private ArrayList<DeviceUsageHistory> getDeviceUsageHistory(DataSnapshot dataSnapshot, String deviceName) {
        ArrayList<DeviceUsageHistory> deviceUsageHistory = new ArrayList<>();

        HashMap<String, Object> deviceHistoryMap = (HashMap<String, Object>) dataSnapshot.getValue();

        for (HashMap.Entry<String, Object> entry : deviceHistoryMap.entrySet()) {

            if (entry.getKey().equalsIgnoreCase(deviceName)) {
                ArrayList<HashMap<String, String>> arrayList = (ArrayList<HashMap<String, String>>) entry.getValue();

                for (HashMap<String, String> mapEntry : arrayList) {
                    DeviceUsageHistory history = new DeviceUsageHistory();

                    if (mapEntry.containsKey(DeviceUsageHistory.PROFILE_ID)) {
                        history.setProfileId(mapEntry.get(DeviceUsageHistory.PROFILE_ID));
                    }

                    if (mapEntry.containsKey(DeviceUsageHistory.START_TIME)) {
                        history.setStartTime(mapEntry.get(DeviceUsageHistory.START_TIME));
                    }

                    if (mapEntry.containsKey(DeviceUsageHistory.END_TIME)) {
                        history.setEndTime(mapEntry.get(DeviceUsageHistory.END_TIME));
                    }
                    deviceUsageHistory.add(history);
                }
            }

        }

        return deviceUsageHistory;
    }

    /**
     * Updates the Device History
     *
     * @param dataSnapshot
     * @param deviceName
     */
    private ArrayList<DeviceUsageHistory> updateDeviceHistory(DataSnapshot dataSnapshot,
                                                              String deviceName,
                                                              boolean updateNeeded) {
        ArrayList<DeviceUsageHistory> deviceUsageHistory = getDeviceUsageHistory(dataSnapshot, deviceName);
        if (updateNeeded) {
            DeviceUsageHistory updateHistory = getCurrentUsage(deviceUsageHistory);
            if (updateHistory == null) {
                updateHistory = new DeviceUsageHistory();
                updateHistory.setProfileId(currentUserProfileId);
                updateHistory.setStartTime(Calendar.getInstance().getTimeInMillis() + "");
                selecteddeviceModel.setModelState(getString(R.string.engage));
                deviceUsageHistory.add(updateHistory);
            } else {
                deviceUsageHistory.remove(updateHistory);
                updateHistory.setEndTime(Calendar.getInstance().getTimeInMillis() + "");
                selecteddeviceModel.setModelState(getString(R.string.available_str));
                deviceUsageHistory.add(updateHistory);
            }
        }
        return deviceUsageHistory;
    }

    private DeviceUsageHistory getCurrentUsage(ArrayList<DeviceUsageHistory> usageHistories) {
        DeviceUsageHistory currentUsage = null;
        for (DeviceUsageHistory history : usageHistories) {
            if (isCurrentUser(history.getProfileId())) {
                if (history.getEndTime() == null) {
                    currentUsage = history;
                }
            }
        }
        return currentUsage;
    }

    /**
     * Gets the Requested Model
     *
     * @param dataSnapshot
     * @param modelString
     * @return
     */
    private DeviceModel getRequestedModel(DataSnapshot dataSnapshot, String modelString) {

        DeviceModel matchedModel = new DeviceModel();

        HashMap<String, Object> selectedModelMap =
                (HashMap<String, Object>) dataSnapshot.getValue();


        for (HashMap.Entry<String, Object> entry : selectedModelMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(modelString)) {
                DeviceModel.Builder builder = new DeviceModel.Builder();

                HashMap<String, String> entryValueMap = (HashMap<String, String>) entry.getValue();

                if (entryValueMap.containsKey(DeviceModel.SM_MODEL_STRING)) {
                    builder.setModelString(entryValueMap.get(DeviceModel.SM_MODEL_STRING));
                }

                if (entryValueMap.containsKey(DeviceModel.SM_MODEL_STATUS)) {
                    builder.setModelState(entryValueMap.get(DeviceModel.SM_MODEL_STATUS));
                }

                if (entryValueMap.containsKey(DeviceModel.SM_MODEL_IMAGE)) {
                    builder.setModelImage(entryValueMap.get(DeviceModel.SM_MODEL_IMAGE));
                }
                matchedModel = builder.build();
            }
        }

        return matchedModel;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

}
