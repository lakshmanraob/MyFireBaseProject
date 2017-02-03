package com.fbauth.checAuth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fbauth.checAuth.R;
import com.fbauth.checAuth.models.SampleModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by labattula on 23/01/17.
 */

public class ScannerActivity extends AppCompatActivity {

    View sanDetailsContent;
    Button scannerBtn, availBtn, enageBtn;
    TextView sampleName, sampleDate;
    ImageView sampleImg;

    private static final int NAME = 0;
    private static final int IMAGE = 1;
    private static final int DATE = 2;
    private static final int STATE = 3;
    private static final String SPLIT_DELIMITER = "$";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanner);

        scannerBtn = (Button) findViewById(R.id.scanBtn);

        sanDetailsContent = findViewById(R.id.sanDetailsContent);
        sanDetailsContent.setVisibility(View.GONE);
        availBtn = (Button) findViewById(R.id.available_btn);
        enageBtn = (Button) findViewById(R.id.engage_btn);
        sampleName = (TextView) findViewById(R.id.scanSampleName);
        sampleDate = (TextView) findViewById(R.id.scanSamplDate);
        sampleImg = (ImageView) findViewById(R.id.scanSampleImage);

        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchZxingScanner(view);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                SampleModel model = getSampleModel(result.getContents());
                if (model != null) {
                    bindView(model);
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void launchZxingScanner(View view) {
        Toast.makeText(ScannerActivity.this, "button", Toast.LENGTH_SHORT).show();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }


    private SampleModel getSampleModel(String str) {
        if (str == null)
            return null;
        else {
            String[] sampleList = str.split("_");
            SampleModel model = new SampleModel(sampleList[NAME], sampleList[IMAGE], sampleList[DATE], sampleList[STATE]);
            return model;
        }
    }

    /**
     * Binding the View
     *
     * @param model
     */
    private void bindView(SampleModel model) {
        scannerBtn.setVisibility(View.GONE);
        sanDetailsContent.setVisibility(View.VISIBLE);
        sampleImg.setImageResource(R.mipmap.ic_launcher);
        if (model.getModelString() != null && model.getModelString().length() > 0) {
            sampleName.setText(model.getModelString());
        }
        if (model.getModelDate() != null && model.getModelDate().length() > 0) {
            sampleDate.setText(model.getModelDate());
        }
        if (model.getModelState() != null && model.getModelState().length() > 0) {
            if (model.getModelState().equalsIgnoreCase("available")) {
                availBtn.setEnabled(true);
                availBtn.setText(model.getModelState());
            }

            if (model.getModelState().equalsIgnoreCase("engaged")) {
                enageBtn.setEnabled(true);
                enageBtn.setText(model.getModelState());
            }
        }
    }

}
