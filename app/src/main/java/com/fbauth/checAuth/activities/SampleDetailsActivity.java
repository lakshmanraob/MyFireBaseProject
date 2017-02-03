package com.fbauth.checAuth.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fbauth.checAuth.R;
import com.fbauth.checAuth.adapters.SDetailsHistoryAdapter;
import com.fbauth.checAuth.models.SampleModel;
import com.fbauth.checAuth.models.SampleModelHistory;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by labattula on 19/01/17.
 */

public class SampleDetailsActivity extends AppCompatActivity {


    TextView detailsText;
    ImageView barcodeImg;
    SampleModel model;

    TextView modelDetailsName;
    TextView modelDeatilsDate;
    Button requestDeviceBtn;
    RecyclerView historyList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("sampleModelDetails");
        }

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

        if (extras != null) {
            final String str = extras.getString("code");
            detailsText.setText(str);

            FirebaseDatabase mFireDatabaseInstance = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseReference = mFireDatabaseInstance.getReference("sampleList");

            Query queryRef = mDatabaseReference.orderByChild("history");

            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    log(s);
                    model = getSampleModel(dataSnapshot, str);
                    bindData(model);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void bindData(SampleModel model) {
        if (model != null) {
            if (model.getModelString() != null) {
                modelDetailsName.setText(model.getModelString());
            }

            if (model.getModelDate() != null) {
                modelDeatilsDate.setText(model.getModelDate());
            }

            if (model.getModelState().equalsIgnoreCase(getString(R.string.available_str))) {
                requestDeviceBtn.setVisibility(View.VISIBLE);
                requestDeviceBtn.setText(R.string.request_device);
                requestDeviceBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(SampleDetailsActivity.this, "request", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                requestDeviceBtn.setVisibility(View.GONE);
            }

            if (model.getModelHistory() != null && model.getModelHistory().size() > 0) {
                SDetailsHistoryAdapter adapter = new SDetailsHistoryAdapter();
                adapter.setHistoryArrayList(model.getModelHistory());

                historyList.setLayoutManager(new LinearLayoutManager(this));
                historyList.setItemAnimator(new DefaultItemAnimator());
                historyList.setAdapter(adapter);
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                historyList.setHasFixedSize(true);
            }
        }
    }

    private void log(String str) {
        Log.i("Somevalues", "log: " + str);
    }

    public SampleModel getSampleModel(DataSnapshot dataSnapshot, String matchString) {

//        (ArrayList) (((HashMap) dataSnapshot.getValue()).get("history"));
        HashMap<String, Object> mp = (HashMap) dataSnapshot.getValue();
        if (((String) mp.get(SampleModel.SM_MODEL_STRING)).equalsIgnoreCase(matchString)) {
            SampleModel model = new SampleModel();
            model.setModelString((String) mp.get(SampleModel.SM_MODEL_STRING));
            model.setModelImage((String) mp.get(SampleModel.SM_MODEL_IMAGE));
            model.setModelState((String) mp.get(SampleModel.SM_MODEL_STATUS));
            List sampleList = (ArrayList) (((HashMap) dataSnapshot.getValue()).get(SampleModel.SM_MODEL_HISTORY));
            ArrayList<SampleModelHistory> historyList = new ArrayList<>();
            for (Object sample : sampleList) {
                SampleModelHistory history = new SampleModelHistory();
                HashMap<String, String> sampleHashmap = (HashMap) sample;
                if (sampleHashmap.get(SampleModel.SM_HT_USER) != null) {
                    history.setUser(sampleHashmap.get(SampleModel.SM_HT_USER));
                }
                if (sampleHashmap.get(SampleModel.SM_HT_START_TIME) != null) {
                    history.setStarttime(sampleHashmap.get(SampleModel.SM_HT_START_TIME));
                }
                if (sampleHashmap.get(SampleModel.SM_HT_END_TIME) != null) {
                    history.setEndtime(sampleHashmap.get(SampleModel.SM_HT_END_TIME));
                }
                historyList.add(history);
            }
            model.setModelHistory(historyList);
            return model;
        }
        return null;
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
