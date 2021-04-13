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
                    dlg.setMessage("아이디와 휴대폰 번호가 입력되지 않았습니다.");
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
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

                CaApplication.m_Engine.CheckAuthCode(m_strUserPhone, m_strAuthCode, 50, this,this);
            }
            break;

            case R.id.btn_back2: {
                finish();
            }
        }
    }

    public void countDown() {

        long conversionTime = 180000;

        // 변환시간

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
        // 두번쨰 인자 : 주기( 1000 = 1초)
        new CountDownTimer(conversionTime, 1000) {

            // 특정 시간마다 뷰 변경
            public void onTick(long millisUntilFinished) {

                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000)) ;
                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지


                // 초가 한자리면 0을 붙인다
                if (second.length() == 1) {
                    second = "0" + second;
                }

                m_tvTimer.setText( min + ":" + second);
            }

            // 제한시간 종료시
            public void onFinish() {

                // 변경 후
                m_tvTimer.setText("인증시간이 초과되었습니다");

                // TODO : 타이머가 모두 종료될때 어떤 이벤트를 진행할지

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
                        dlg.setMessage("휴대폰으로 6자리 인증번호가 전송됩니다. 인증번호를 입력하신 후에 확인을 눌러주세요.");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                m_tvTimer.setVisibility(View.VISIBLE);
                                countDown();
                            }
                        });
                        dlg.show();
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                        dlg.setMessage("문자인증에 실패했습니다. 정확한 번호로 다시 요청해주세요.");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
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
                        dlg.setMessage("인증코드를 요청한 번호가 아닙니다. 처음부터 다시 진행해주세요.");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();
                    }
                    else if (nResultCode == 0) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                        dlg.setMessage("문자인증번호가 잘못되었습니다. 다시 입력해주세요.");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();
                    }
                    else if (nResultCode == 1) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                        dlg.setMessage("인증이 완료되었습니다. 다음 단계로 진행합니다.");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent it = new Intent(ActivityChangePasswordAuth.this, ActivityChangePasswordInput.class);
                                it.putExtra("user_id", m_strUserId);
                                startActivity(it);
                            }
                        });
                        dlg.show();
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordAuth.this);
                        dlg.setMessage("시간이 초과되었습니다. 다시 시도해주세요.");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
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