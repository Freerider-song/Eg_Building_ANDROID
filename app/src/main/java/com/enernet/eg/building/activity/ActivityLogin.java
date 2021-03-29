package com.enernet.eg.building;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.enernet.eg.building.activity.ActivityChangePasswordAuth;
import com.enernet.eg.building.activity.ActivityHome;
import com.enernet.eg.building.activity.ActivityHome_2;
import com.enernet.eg.building.activity.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class ActivityLogin extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getPushId();
    }

    public void getPushId() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log a
                        Log.e("newToken", token);
                        CaApplication.m_Info.m_strPushId=token;
                    }
                });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                Intent it = new Intent(this, ActivityHome.class);
                startActivity(it);
            }
            break;

            case R.id.btn_change_password: {
                Intent it = new Intent(this, ActivityChangePasswordAuth.class);
                startActivity(it);
            }


        }
    }
}