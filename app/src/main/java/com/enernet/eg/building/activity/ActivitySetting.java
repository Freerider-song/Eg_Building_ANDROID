package com.enernet.eg.building.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.enernet.eg.building.ActivityLogin;
import com.enernet.eg.building.model.CaApplication;
import com.enernet.eg.building.model.CaResult;
import com.enernet.eg.building.model.EgDialogLogout;
import com.enernet.eg.building.model.IaResultHandler;
import com.enernet.eg.building.R;

public class ActivitySetting extends BaseActivity implements IaResultHandler {

    private EgDialogLogout m_dlgLogout;

    private boolean m_bAlarmAll=CaApplication.m_Info.m_bNotiAll;
    private boolean m_bAlarmKwh=CaApplication.m_Info.m_bNotiKwh;
    private boolean m_bAlarmWon=CaApplication.m_Info.m_bNotiWon;
    private boolean m_bAlarmSavingStandard=CaApplication.m_Info.m_bNotiSavingStandard;
    private boolean m_bAlarmSavingGoal=CaApplication.m_Info.m_bNotiSavingGoal;
    private boolean m_bAlarmUsageAtTime= CaApplication.m_Info.m_bNotiUsageAtTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        prepareDrawer();

        Spinner m_SpAlarmUsageAtTime = (Spinner)findViewById(R.id.sp_alarm_usage_at_time);

        m_SpAlarmUsageAtTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long a =parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (m_Drawer.isDrawerOpen()) {
            m_Drawer.closeDrawer();
        }
        else {
            //setNoticeReadStateToDb();
            finish();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                //setNoticeReadStateToDb();
                finish();
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
                                        /*

                                        CaPref pref = new CaPref(Ctx);

                                        pref.setValue(CaPref.PREF_MEMBER_ID, "");
                                        pref.setValue(CaPref.PREF_PASSWORD, "");

                                         */

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

    @Override
    public void onResult(CaResult Result) {

    }
}