package com.enernet.eg.building.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enernet.eg.building.ActivityLogin;
import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaEngine;
import com.enernet.eg.building.CaPref;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

public class ActivityChangePasswordAuth extends BaseActivity implements IaResultHandler {

    private EditText m_etUserId;
    private EditText m_etUserPhone;

    private EditText m_etAuthCode;

    private String m_strUserId;
    private String m_strUserPhone;
    private String m_strAuthCode;

    private Timer mTimer;
    private TextView m_tvTimer;


    private Context m_Context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_auth2);

        m_Context = getApplicationContext();

        m_etUserId = findViewById(R.id.et_member_id);
        m_etUserPhone = findViewById(R.id.et_member_phone);
        m_etAuthCode = findViewById(R.id.et_auth_code);

        m_tvTimer = findViewById(R.id.tv_timer);
        m_tvTimer.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        Log.i("Login", "onBackPressed called...");

        finish();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auth_request: {
                m_strUserId = m_etUserId.getText().toString();
                m_strUserPhone = m_etUserPhone.getText().toString();

                if (m_strUserId.isEmpty() || m_strUserPhone.isEmpty()){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                    dlg.setMessage("???????????? ????????? ????????? ???????????? ???????????????.");
                    dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();
                }

                CaApplication.m_Engine.RequestAuthCode(m_strUserId,m_strUserPhone,this,this);

            }
            break;

            case R.id.btn_auth_check: {
                m_strAuthCode = m_etAuthCode.getText().toString();

                CaApplication.m_Engine.CheckAuthCode(m_strUserPhone, m_strAuthCode, 180, this,this);
            }
            break;

            case R.id.btn_back2: {
                finish();
            }
        }
    }

    public void countDown() {

        long conversionTime = 180000;

        // ????????????

        // ????????? ?????? : ????????? ?????? (???????????? 30?????? 30 x 1000(??????))
        // ????????? ?????? : ??????( 1000 = 1???)
        new CountDownTimer(conversionTime, 1000) {

            // ?????? ???????????? ??? ??????
            public void onTick(long millisUntilFinished) {

                // ?????????
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000)) ;
                String min = String.valueOf(getMin / (60 * 1000)); // ???

                // ?????????
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // ?????????


                // ?????? ???????????? 0??? ?????????
                if (second.length() == 1) {
                    second = "0" + second;
                }

                m_tvTimer.setText( min + ":" + second);
            }

            // ???????????? ?????????
            public void onFinish() {

                // ?????? ???
                m_tvTimer.setText("??????????????? ?????????????????????");

            }
        }.start();

    }

    @Override
    public void onResult(CaResult Result) {
        if (Result.object == null) {
            Toast.makeText(m_Context, "Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.CB_REQUEST_AUTH_CODE: {

                try {
                    JSONObject jo = Result.object;
                    int nResultCode = jo.getInt("result_code");

                    if (nResultCode == 1) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                        dlg.setMessage("??????????????? 6?????? ??????????????? ???????????????. ??????????????? ???????????? ?????? ????????? ???????????????.");
                        dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                m_tvTimer.setVisibility(View.VISIBLE);
                                countDown();
                            }
                        });
                        dlg.show();
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                        dlg.setMessage("??????????????? ??????????????????. ????????? ????????? ?????? ??????????????????.");
                        dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            case CaEngine.CB_CHECK_AUTH_CODE: {
                try {
                    JSONObject jo = Result.object;
                    int nResultCode = jo.getInt("result_code");

                    if (nResultCode == -2) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                        dlg.setMessage("??????????????? ????????? ????????? ????????????. ???????????? ?????? ??????????????????.");
                        dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();
                    }
                    else if (nResultCode == 0) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                        dlg.setMessage("????????????????????? ?????????????????????. ?????? ??????????????????.");
                        dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();
                    }
                    else if (nResultCode == 1) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                        dlg.setMessage("????????? ?????????????????????. ?????? ????????? ???????????????.");
                        dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent it = new Intent(ActivityChangePasswordAuth.this, ActivityChangePasswordInput.class);
                                it.putExtra("user_id", m_strUserId);
                                Log.i("changepasswordAuth", "user id put extra is " + m_strUserId);

                                startActivity(it);
                            }
                        });
                        dlg.show();
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                        dlg.setMessage("????????? ?????????????????????. ?????? ??????????????????.");
                        dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            break;


            default: {
                Log.i("tag", "Unknown type result received");
            }
            break;

        }
    }
}