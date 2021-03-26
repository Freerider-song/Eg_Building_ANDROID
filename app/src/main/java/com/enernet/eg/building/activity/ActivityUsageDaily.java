package com.enernet.eg.building.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.enernet.eg.building.model.CaResult;
import com.enernet.eg.building.model.EgYearMonthDayPicker;
import com.enernet.eg.building.model.IaResultHandler;
import com.enernet.eg.building.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityUsageDaily extends BaseActivity implements IaResultHandler {

    private LineChart chart;

    public int Year;
    public int Month;
    public int Day;

    private EgYearMonthDayPicker m_dlgYearMonthDayPicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_daily);

        prepareDrawer();

        Calendar calCurr= Calendar.getInstance();
        requestUsageDaily(calCurr.get(Calendar.YEAR), calCurr.get(Calendar.MONTH)+1, calCurr.get(Calendar.DATE));

        chart = findViewById(R.id.line_chart);

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));
        }

        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        // set data
        chart.setData(data);

        chart.setScrollContainer(true);
        chart.setScaleMinima((float) data.getDataSetCount() / 5f,1f);
        chart.animateY(2000);
    }


    public void requestUsageDaily(int nYear, int nMonth, int nDay) {
        Year = nYear;
        Month = nMonth;
        Day = nDay;
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

                        m_dlgYearMonthDayPicker.dismiss();

                        int nYear=m_dlgYearMonthDayPicker.m_DatePicker.getYear();
                        int nMonth=m_dlgYearMonthDayPicker.m_DatePicker.getMonth()+1;
                        int nDay=m_dlgYearMonthDayPicker.m_DatePicker.getDayOfMonth();

                        Log.i("YearMonthDatePicker", "year="+nYear+", month="+nMonth+", day="+nDay);

                        Year = nYear;
                        Month = nMonth;
                        Day = nDay;
                        //requestUsageDaily(nYear, nMonth, nDay);

                    }
                };

                View.OnClickListener LsnConfirmNo=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ActivityCandidate", "No button clicked...");
                        m_dlgYearMonthDayPicker.dismiss();
                    }
                };

                m_dlgYearMonthDayPicker=new EgYearMonthDayPicker(this, "조회할 날짜를 선택하세요", LsnConfirmYes, LsnConfirmNo);
                m_dlgYearMonthDayPicker.show();
            }
            break;

            case R.id.btn_search: {
                //requestUsageDaily(Year,Month,Day);

            }
            break;

        }
    }


    @Override
    public void onResult(CaResult Result) {

    }
}