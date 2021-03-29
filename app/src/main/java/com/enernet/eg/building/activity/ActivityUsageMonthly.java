package com.enernet.eg.building.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.EgYearMonthPicker;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;

import java.util.Calendar;

public class ActivityUsageMonthly extends BaseActivity implements IaResultHandler {

    private EgYearMonthPicker m_dlgYearMonthPicker;
    public int Year;
    public int Month;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_monthly);

        prepareDrawer();

        Calendar calCurr= Calendar.getInstance();
        requestUsageMonthly(calCurr.get(Calendar.YEAR), calCurr.get(Calendar.MONTH)+1);

        /*
        CustomGauge m_GaugeChart = (CustomGauge) findViewById(R.id.gauge1);
        m_GaugeChart = (CustomGauge) findViewById(R.id.gauge1);
        m_GaugeChart.setStartValue(0);
        m_GaugeChart.setEndValue(500);
        m_GaugeChart.setValue(374);
        m_GaugeChart.setDividerDrawFirst(true);
        m_GaugeChart.setDividerColor(Color.RED);
        m_GaugeChart.setDividerStep(4);*/

    }


    public void requestUsageMonthly(int nYear, int nMonth) {
        Year = nYear;
        Month = nMonth;
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

                        m_dlgYearMonthPicker.dismiss();

                        int nYear=m_dlgYearMonthPicker.m_npYear.getValue();
                        int nMonth=m_dlgYearMonthPicker.m_npMonth.getValue();


                        Log.i("YearMonthDatePicker", "year="+nYear+", month="+nMonth);

                        Year = nYear;
                        Month = nMonth;


                    }
                };

                View.OnClickListener LsnConfirmNo=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ActivityCandidate", "No button clicked...");
                        m_dlgYearMonthPicker.dismiss();
                    }
                };

                m_dlgYearMonthPicker=new EgYearMonthPicker(this, "조회할 날짜를 선택하세요", LsnConfirmYes, LsnConfirmNo);
                m_dlgYearMonthPicker.show();
            }
            break;

            case R.id.btn_search: {
                //requestUsageMonthly(Year,Month);

            }
            break;

        }
    }

    @Override
    public void onResult(CaResult Result) {

    }
}