package com.enernet.eg.building;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.enernet.eg.building.activity.ActivityHome;
import com.enernet.eg.building.activity.ActivityHome_2;
import com.enernet.eg.building.activity.BaseActivity;

public class ActivityLogin extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                Intent it = new Intent(this, ActivityHome.class);
                startActivity(it);
            }


        }
    }
}