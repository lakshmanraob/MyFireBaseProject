package com.fbauth.checAuth.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fbauth.checAuth.R;
import com.fbauth.checAuth.models.SampleModel;

/**
 * Created by labattula on 24/01/17.
 */

public class ScannerDetailsActivity extends AppCompatActivity {

    Button availableBtn;
    Button historyBtn;
    TextView detailsModelName;

    SampleModel launchedModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_details);

        detailsModelName = (TextView) findViewById(R.id.details_model_name);
        availableBtn = (Button) findViewById(R.id.available_btn);
        availableBtn.setOnClickListener(btnClickListener);
        historyBtn = (Button) findViewById(R.id.history_btn);
        historyBtn.setOnClickListener(btnClickListener);

    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.available_btn:
                    break;
                case R.id.history_btn:
                    break;
            }
        }
    };

    private void getLaunchedModel() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String modelName = bundle.getString("modelname");
                if (modelName != null) {
                    detailsModelName.setText(modelName);
                }
            }
        }
    }
}
