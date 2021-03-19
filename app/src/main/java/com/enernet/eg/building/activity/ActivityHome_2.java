package com.enernet.eg.building.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.enernet.eg.building.R;

public class ActivityHome_2 extends BaseActivity {

    @Override
    public
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_2);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_plus: {
                Intent it = new Intent(this, ActivitySaving.class);
                startActivity(it);
            }


        }
    }
}