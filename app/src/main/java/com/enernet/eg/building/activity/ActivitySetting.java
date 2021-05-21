package com.enernet.eg.building.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.enernet.eg.building.ActivityLogin;
import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaEngine;
import com.enernet.eg.building.CaPref;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.EgDialogLogout;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.StringUtil;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivitySetting extends BaseActivity implements IaResultHandler {

    private EgDialogLogout m_dlgLogout;

    private  boolean m_bFinishWhenChangeSaved=false;

    private boolean m_bAlarmAll=CaApplication.m_Info.m_bNotiAll;
    private boolean m_bAlarmKwh=CaApplication.m_Info.m_bNotiKwh;
    private boolean m_bAlarmWon=CaApplication.m_Info.m_bNotiWon;
    private boolean m_bAlarmSavingStandard=CaApplication.m_Info.m_bNotiSavingStandard;
    private boolean m_bAlarmSavingGoal=CaApplication.m_Info.m_bNotiSavingGoal;
    private boolean m_bAlarmUsageAtTime= CaApplication.m_Info.m_bNotiUsageAtTime;

    private double m_dThresholdKwh=CaApplication.m_Info.m_dThresholdThisMonthKwh;
    private double m_dThresholdWon=CaApplication.m_Info.m_dThresholdThisMonthWon;
    private int m_nUsageNotiHour=CaApplication.m_Info.m_nHourNotiThisMonthUsage-1;

    private Spinner m_SpAlarmUsageAtTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        prepareDrawer();

        m_SpAlarmUsageAtTime = findViewById(R.id.sp_alarm_usage_at_time);

        m_SpAlarmUsageAtTime.setSelection(m_nUsageNotiHour+1);
        m_SpAlarmUsageAtTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long a =parent.getItemIdAtPosition(position);
                //사용량 보고 시간을 포지션으로 설정
                m_nUsageNotiHour=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView tvSiteName = findViewById(R.id.tv_site_name);
        TextView tvCompletionDay = findViewById(R.id.tv_completion_day);
        TextView tvSiteFloor = findViewById(R.id.tv_site_floor);
        TextView tvSiteAddress = findViewById(R.id.tv_site_address);
        TextView tvHomepage = findViewById(R.id.tv_homepage);
        TextView tvId = findViewById(R.id.tv_1_id);
        TextView tvPasswordChange = findViewById(R.id.tv_1_date_password_change);
        TextView tvName = findViewById(R.id.tv_1_name);
        TextView tvPhone = findViewById(R.id.tv_1_phone);
        EditText etThresholdKwh =findViewById(R.id.et_threshold_kwh);
        EditText etThresholdWon =findViewById(R.id.et_threshold_won);

        tvSiteAddress.setText(CaApplication.m_Info.m_strSiteAddress);
        tvSiteName.setText(CaApplication.m_Info.m_strSiteName);
        tvCompletionDay.setText(CaApplication.m_Info.m_nBuiltYear+" - "+CaApplication.m_Info.m_nBuiltMonth);
        tvSiteFloor.setText(CaApplication.m_Info.m_strFloorInfo);
        tvHomepage.setText(CaApplication.m_Info.m_strHomePage);
        tvId.setText(CaApplication.m_Info.m_strAdminId);
        tvPasswordChange.setText(CaApplication.m_Info.m_dfyyyyMMddhhmmStd.format(CaApplication.m_Info.m_dtChangePassword));
        tvName.setText(CaApplication.m_Info.m_strAdminName);
        tvPhone.setText(CaApplication.m_Info.m_strAdminPhone);
        //etThresholdKwh.setText(CaApplication.m_Info.m_dfKwh.format(CaApplication.m_Info.m_dThresholdThisMonthKwh));
        //etThresholdWon.setText(CaApplication.m_Info.m_dfWon.format(CaApplication.m_Info.m_dThresholdThisMonthWon));

        etThresholdKwh.setText(String.valueOf((int) CaApplication.m_Info.m_dThresholdThisMonthKwh));
        etThresholdWon.setText(String.valueOf((int) CaApplication.m_Info.m_dThresholdThisMonthWon));
        setAlarmInfo();
    }

    @Override
    public void onBackPressed() {
        if (m_Drawer.isDrawerOpen()) {
            m_Drawer.closeDrawer();
        }
        else {
            if (isSettingChanged()) {
                m_bFinishWhenChangeSaved=true;
                processSettingChange();
            }
            else {
                finish();
            }
        }

    }


    public void scrollToView(View v) {

        ScrollView sv=findViewById(R.id.setting_scroll_view);
        int y=sv.getScrollY();
        int[] locScroll={0, 0};
        sv.getLocationInWindow(locScroll);

        int[] locView={0, 0};
        v.getLocationOnScreen(locView);

        sv.scrollTo(0, y+locView[1]-locScroll[1]);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                if (isSettingChanged()) {
                    m_bFinishWhenChangeSaved=true;
                    processSettingChange();
                }
                else {
                    finish();
                }
            }
            break;

            case R.id.btn_menu: {
                m_Drawer.openDrawer();
            }
            break;

            case R.id.ib_alarm_all: {
                m_bAlarmAll=!m_bAlarmAll;
                setAlarmInfo();
            }
            break;

            case R.id.ib_alarm_kwh: {
                m_bAlarmKwh=!m_bAlarmKwh;
                setAlarmInfo();
            }
            break;

            case R.id.ib_alarm_won: {
                m_bAlarmWon=!m_bAlarmWon;
                setAlarmInfo();
            }
            break;

            case R.id.ib_alarm_saving_standard: {
                m_bAlarmSavingStandard=!m_bAlarmSavingStandard;
                setAlarmInfo();
            }
            break;

            case R.id.ib_alarm_saving_goal: {
                m_bAlarmSavingGoal=!m_bAlarmSavingGoal;
                setAlarmInfo();
            }
            break;

            case R.id.ib_alarm_usage_at_time: {
                m_bAlarmUsageAtTime=!m_bAlarmUsageAtTime;
                setAlarmInfo();
            }
            break;

            case R.id.btn_logout: {

                final Context Ctx=getApplicationContext();

                View.OnClickListener LsnConfirmYes=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ActivitySetting", "Yes button clicked...");
                        m_dlgLogout.dismiss();

                                        CaPref pref = new CaPref(Ctx);

                                        pref.setValue(CaPref.PREF_MEMBER_ID, "");
                                        pref.setValue(CaPref.PREF_PASSWORD, "");

                        final Class Clazz= ActivityLogin.class;

                        Intent nextIntent = new Intent(Ctx, Clazz);
                        startActivity(nextIntent);
                    }
                };

                View.OnClickListener LsnConfirmNo=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ActivitySetting", "No button clicked...");
                        m_dlgLogout.dismiss();
                    }
                };

                m_dlgLogout=new EgDialogLogout(this, LsnConfirmYes, LsnConfirmNo);
                m_dlgLogout.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode==KeyEvent.KEYCODE_BACK) {
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

                m_dlgLogout.show();
            }
            break;

            case R.id.btn_change_password_setting: {
                Intent it = new Intent(this, ActivityChangePasswordAuth.class);
                startActivity(it);
            }

        }
    }

    private void setAlarmInfo() {
        ImageButton ibAlarmAll=findViewById(R.id.ib_alarm_all);
        ImageButton ibAlarmKwh=findViewById(R.id.ib_alarm_kwh);
        ImageButton ibAlarmWon=findViewById(R.id.ib_alarm_won);
        ImageButton ibAlarmSavingStandard=findViewById(R.id.ib_alarm_saving_standard);
        ImageButton ibAlarmSavingGoal=findViewById(R.id.ib_alarm_saving_goal);
        ImageButton ibAlarmUsageAtTime=findViewById(R.id.ib_alarm_usage_at_time);

        if (m_bAlarmAll) {

            ibAlarmAll.setImageResource(R.drawable.check_yes);

            if (m_bAlarmKwh) ibAlarmKwh.setImageResource(R.drawable.check_yes);
            else ibAlarmKwh.setImageResource(R.drawable.check_no);

            if (m_bAlarmWon) ibAlarmWon.setImageResource(R.drawable.check_yes);
            else ibAlarmWon.setImageResource(R.drawable.check_no);

            if (m_bAlarmSavingStandard) ibAlarmSavingStandard.setImageResource(R.drawable.check_yes);
            else ibAlarmSavingStandard.setImageResource(R.drawable.check_no);

            if (m_bAlarmSavingGoal) ibAlarmSavingGoal.setImageResource(R.drawable.check_yes);
            else ibAlarmSavingGoal.setImageResource(R.drawable.check_no);

            if (m_bAlarmUsageAtTime) ibAlarmUsageAtTime.setImageResource(R.drawable.check_yes);
            else ibAlarmUsageAtTime.setImageResource(R.drawable.check_no);
        }
        else {
            ibAlarmAll.setImageResource(R.drawable.check_no);

            if (m_bAlarmKwh) ibAlarmKwh.setImageResource(R.drawable.check_disabled_yes);
            else ibAlarmKwh.setImageResource(R.drawable.check_disabled_no);

            if (m_bAlarmWon) ibAlarmWon.setImageResource(R.drawable.check_disabled_yes);
            else ibAlarmWon.setImageResource(R.drawable.check_disabled_no);

            if (m_bAlarmSavingStandard) ibAlarmSavingStandard.setImageResource(R.drawable.check_disabled_yes);
            else ibAlarmSavingStandard.setImageResource(R.drawable.check_disabled_no);

            if (m_bAlarmSavingGoal) ibAlarmSavingGoal.setImageResource(R.drawable.check_disabled_yes);
            else ibAlarmSavingGoal.setImageResource(R.drawable.check_disabled_no);

            if (m_bAlarmUsageAtTime) ibAlarmUsageAtTime.setImageResource(R.drawable.check_disabled_yes);
            else ibAlarmUsageAtTime.setImageResource(R.drawable.check_disabled_no);
        }
    }

    private boolean isSettingChanged() {
        if (m_bAlarmAll != CaApplication.m_Info.m_bNotiAll) return true;
        if (m_bAlarmKwh != CaApplication.m_Info.m_bNotiKwh) return true;
        if (m_bAlarmWon != CaApplication.m_Info.m_bNotiWon) return true;
        if (m_bAlarmSavingGoal != CaApplication.m_Info.m_bNotiSavingGoal) return true;
        if (m_bAlarmSavingStandard != CaApplication.m_Info.m_bNotiSavingStandard) return true;
        if (m_bAlarmUsageAtTime != CaApplication.m_Info.m_bNotiUsageAtTime) return true;


        EditText etThresholdKwh=findViewById(R.id.et_threshold_kwh);
        String strThresholdKwh=etThresholdKwh.getText().toString();
        m_dThresholdKwh=Double.parseDouble(strThresholdKwh);

        EditText etThresholdWon=findViewById(R.id.et_threshold_won);
        String strThresholdWon=etThresholdWon.getText().toString();
        m_dThresholdWon=Double.parseDouble((strThresholdWon));


        if (Math.abs(m_dThresholdKwh - CaApplication.m_Info.m_dThresholdThisMonthKwh)>=1) return true;
        if (Math.abs(m_dThresholdWon - CaApplication.m_Info.m_dThresholdThisMonthWon)>=1) return true;


        if (m_nUsageNotiHour != CaApplication.m_Info.m_nHourNotiThisMonthUsage) return true;

        return false;
    }


    public void processSettingChange() {

        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivitySetting.this);
        dlg.setTitle("확인"); //제목
        dlg.setMessage("변경한 설정값을 저장하시겠습니까?"); // 메시지

        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                requestChangeAdminBldSettings();
            }
        });

        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (m_bFinishWhenChangeSaved) finish();
            }
        });

        dlg.show();
    }

    public void requestChangeAdminBldSettings(){
        CaApplication.m_Engine.ChangeAdminBldSettings(CaApplication.m_Info.m_nSeqAdmin,
                m_bAlarmAll, m_bAlarmKwh, m_bAlarmWon, m_bAlarmUsageAtTime,
                m_bAlarmSavingStandard, m_bAlarmSavingGoal,
                m_dThresholdKwh, m_dThresholdWon, m_nUsageNotiHour, this, this);
    }


    @Override
    public void onResult(CaResult Result) {
        if (Result.object == null) {
            Toast.makeText(getApplicationContext(), "네트워크 에러...", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.CB_CHANGE_ADMIN_BLD_SETTINGS: {
                Log.i("Setting", "Result of ChangeBldSettings received...");

                try {
                    JSONObject jo = Result.object;
                    int nSeqSettings = jo.getInt("seq_settings_found");

                    if (nSeqSettings == 0) {

                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivitySetting.this);
                        dlg.setTitle("확인"); //제목
                        dlg.setMessage("설정값 저장에 실패하였습니다."); // 메시지

                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();

                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivitySetting.this);
                        dlg.setTitle("확인"); //제목
                        dlg.setMessage("설정값 저장에 성공했습니다"); // 메시지

                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                CaApplication.m_Info.m_bNotiAll = m_bAlarmAll;
                                CaApplication.m_Info.m_bNotiKwh = m_bAlarmKwh;
                                CaApplication.m_Info.m_bNotiWon = m_bAlarmWon;
                                CaApplication.m_Info.m_bNotiSavingGoal = m_bAlarmSavingGoal;
                                CaApplication.m_Info.m_bNotiSavingStandard = m_bAlarmSavingStandard;
                                CaApplication.m_Info.m_bNotiUsageAtTime = m_bAlarmUsageAtTime;
                                CaApplication.m_Info.m_dThresholdThisMonthKwh = m_dThresholdKwh;
                                CaApplication.m_Info.m_dThresholdThisMonthWon = m_dThresholdWon;

                                CaApplication.m_Info.m_nHourNotiThisMonthUsage = m_nUsageNotiHour + 1;

                                if (m_bFinishWhenChangeSaved) finish();
                            }
                        });

                        dlg.show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            break;


        }
    }
}