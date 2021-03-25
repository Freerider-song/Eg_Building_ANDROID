package com.enernet.eg.building.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.activity.BaseActivity;

public class ActivityUsageMonthly extends BaseActivity implements IaResultHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_monthly);
    }

    @Override
    public void onResult(CaResult Result) {

    }
}