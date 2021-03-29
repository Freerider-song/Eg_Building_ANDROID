package com.enernet.eg.building.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.EgYearPicker;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;

import java.util.Calendar;

public class ActivityUsageYearly extends BaseActivity implements IaResultHandler {

    private EgYearPicker m_dlgYearPicker;
    public int Year;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_yearly);

        prepareDrawer();

        Calendar calCurr= Calendar.getInstance();
        requestUsageYearly(calCurr.get(Calendar.YEAR));
    }


    public void requestUsageYearly(int nYear) {
        Year = nYear;

        /*CaApplication.m_Engine.GetUsageOfOneDay(CaApplication.m_Info.m_nSeqSite, CaApplication.m_Info.m_nSeqMeter,
                nYear, nMonth, nDay, this, this);*/
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
            }
            break;


            case R.id.btn_menu: {
                m_Drawer.openDrawer();
            }
            break;

            case R.id.btn_select_time: {

                View.OnClickListener LsnConfirmYes=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        m_dlgYearPicker.dismiss();

                        int nYear=m_dlgYearPicker.m_npYear.getValue();


                        Log.i("YearPicker", "year="+nYear);

                        Year = nYear;



                    }
                };

                View.OnClickListener LsnConfirmNo=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ActivityCandidate", "No button clicked...");
                        m_dlgYearPicker.dismiss();
                    }
                };

                m_dlgYearPicker=new EgYearPicker(this, "조회할 날짜를 선택하세요", LsnConfirmYes, LsnConfirmNo);
                m_dlgYearPicker.show();
            }
            break;

            case R.id.btn_search: {
                requestUsageYearly(Year);

            }
            break;

        }
    }

    @Override
    public void onResult(CaResult Result) {

    }
}