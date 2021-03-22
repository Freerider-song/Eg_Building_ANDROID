package com.enernet.eg.building.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.enernet.eg.building.EgYearMonthDayPicker;
import com.enernet.eg.building.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivitySaving extends BaseActivity {

    private Button btnSelectTime;
    private Button btnSelectTime2;

    public int Year;
    public int Month;
    public int Day;

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    private EgYearMonthDayPicker m_dlgYearMonthDayPicker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

        prepareDrawer();

        Log.i("ActivitySaving", "onCreate called...");

        timeSetting();

        ListView listView = (ListView) findViewById(R.id.lv_saving_result);
        ListAdapter listAdapter = listView.getAdapter();
        listView.setAdapter(listAdapter);
        setListViewHeightBasedOnChildren(listView);

    }

    public void timeSetting() {
        btnSelectTime2 = (Button) findViewById(R.id.btn_select_time2);
        btnSelectTime = (Button) findViewById(R.id.btn_select_time);

        Date today = new Date();
        Calendar calCurr = Calendar.getInstance();
        calCurr.setTime(today);
        String timeCurr = mFormat.format(calCurr.getTime());
        btnSelectTime2.setText(timeCurr);

        Calendar calMonthAgo = Calendar.getInstance();
        calMonthAgo.add(Calendar.MONTH, -1);
        String beforeMonth = mFormat.format(calMonthAgo.getTime());
        btnSelectTime.setText(beforeMonth);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onBackPressed() {
        if (m_Drawer.isDrawerOpen()) {
            m_Drawer.closeDrawer();
        }
        else {
            finish();
        }
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

                View.OnClickListener LsConfirmYes=new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        m_dlgYearMonthDayPicker.dismiss();

                        int nYear = m_dlgYearMonthDayPicker.m_DatePicker.getYear();
                        int nMonth = m_dlgYearMonthDayPicker.m_DatePicker.getMonth();
                        int nDay = m_dlgYearMonthDayPicker.m_DatePicker.getDayOfMonth();

                        Log.i("YearMonthDayPicker", "year="+nYear+", month="+nMonth+", day="+nDay);

                        //명령어
                        String strYear = Integer.toString(nYear);
                        String strMonth = Integer.toString(nMonth);
                        String strDay = Integer.toString(nDay);

                        String chosenDate = strYear+"-"+strMonth+"-"+strDay;
                        btnSelectTime = (Button) findViewById(R.id.btn_select_time);
                        btnSelectTime.setText(chosenDate);
                    }

                };
                View.OnClickListener LsConfirmNo = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Log.i("ActivityCandidate", "No button clicked...");
                        m_dlgYearMonthDayPicker.dismiss();
                    }
                };
                m_dlgYearMonthDayPicker=new EgYearMonthDayPicker(this, "조회할 날짜를 선택하세요",LsConfirmYes,LsConfirmNo);
                m_dlgYearMonthDayPicker.show();
            }
            break;

            case R.id.btn_select_time2: {

                View.OnClickListener LsConfirmYes=new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        m_dlgYearMonthDayPicker.dismiss();

                        int nYear = m_dlgYearMonthDayPicker.m_DatePicker.getYear();
                        int nMonth = m_dlgYearMonthDayPicker.m_DatePicker.getMonth()+1;
                        int nDay = m_dlgYearMonthDayPicker.m_DatePicker.getDayOfMonth();

                        Log.i("YearMonthDayPicker", "year="+nYear+", month="+nMonth+", day="+nDay);

                        //명령어
                        String strYear = Integer.toString(nYear);
                        String strMonth =Integer.toString(nMonth);
                        String strDay = Integer.toString(nDay);

                        String chosenDate = strYear+"-"+strMonth+"-"+strDay;
                        btnSelectTime2 = (Button) findViewById(R.id.btn_select_time2);
                        btnSelectTime2.setText(chosenDate);
                    }

                };
                View.OnClickListener LsConfirmNo = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Log.i("ActivityCandidate", "No button clicked...");
                        m_dlgYearMonthDayPicker.dismiss();
                    }
                };
                m_dlgYearMonthDayPicker=new EgYearMonthDayPicker(this, "조회할 날짜를 선택하세요",LsConfirmYes,LsConfirmNo);
                m_dlgYearMonthDayPicker.show();
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}