package com.enernet.eg.building.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.EgYearMonthDayPicker;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivitySaving extends BaseActivity implements IaResultHandler {

    private Button btnSelectTime;
    private Button btnSelectTime2;

    public int Year;
    public int Month;
    public int Day;

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    private EgYearMonthDayPicker m_dlgYearMonthDayPicker;
    BarChart savingStackedBarChart, usageTotalBarChart;
    HorizontalBarChart usageBarChart;
    int[] colorArray = new int[] {Color.CYAN, Color.LTGRAY};
    private SavingResultAdapter m_SavingResultAdapter;


    private class SavingResultViewHolder {
        public ConstraintLayout m_clAreaRoot;
        public TextView m_tvInstrument;
        public TextView m_tvInstrumentUsage;
        public TextView m_tvSavingGoal;
        public TextView m_tvSavingStandard;
    }

    private class SavingResultAdapter extends BaseAdapter {

        public SavingResultAdapter() {
            super();
        }

        @Override
        public int getCount() {
            //return CaApplication.m_Info.m_alAlarm.size();
            return 10;
        }

        @Override
        public Object getItem(int position) {
            //return CaApplication.m_Info.m_alAlarm.get(position);
            //return position;
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SavingResultViewHolder holder;
            if (convertView == null) {
                holder = new SavingResultViewHolder();

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.saving_result_list, null);

                holder.m_clAreaRoot = convertView.findViewById(R.id.area_root2);
                holder.m_tvInstrument = convertView.findViewById(R.id.tv_instrument);
                holder.m_tvInstrumentUsage=convertView.findViewById(R.id.tv_instrument_usage);
                holder.m_tvSavingGoal=convertView.findViewById(R.id.tv_saving_goal);
                holder.m_tvSavingStandard=convertView.findViewById(R.id.tv_saving_standard);


                convertView.setTag(holder);
            }
            else {
                holder = (SavingResultViewHolder) convertView.getTag();
            }
            /*

            final CaAlarm alarm = CaApplication.m_Info.m_alAlarm.get(position);

            switch (alarm.m_nAlarmType) {
                case CaEngine.ALARM_TYPE_NOTI_KWH:
                case CaEngine.ALARM_TYPE_NOTI_WON:
                case CaEngine.ALARM_TYPE_NOTI_PRICE_LEVEL:
                case CaEngine.ALARM_TYPE_NOTI_USAGE:
                    holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_filled_yellow_a));
                    break;

                case CaEngine.ALARM_TYPE_REQUEST_ACK_MEMBER:
                case CaEngine.ALARM_TYPE_RESPONSE_ACK_MEMBER_ACCEPTED:
                case CaEngine.ALARM_TYPE_RESPONSE_ACK_MEMBER_REJECTED:
                case CaEngine.ALARM_TYPE_RESPONSE_ACK_MEMBER_CANCELED:
                case CaEngine.ALARM_TYPE_NOTI_TRANS:
                    holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_filled_yellow_b));
                    break;
            }

            holder.m_tvTitle.setText(alarm.m_strTitle);
            holder.m_tvContent.setText(alarm.m_strContent);
            holder.m_tvTimeCreated.setText(alarm.getTimeCreated());

            if (alarm.m_bRead) holder.m_ivNew.setVisibility(View.INVISIBLE);
            else holder.m_ivNew.setVisibility(View.VISIBLE);

            //Log.i("Alarm", "m_bRead="+alarm.m_bRead);

            if (alarm.isRequestAck()) {
                switch (alarm.m_nResponse) {
                    case 0:
                        holder.m_tvAckResponse.setText("승인 대기중");
                        break;

                    case 1:
                        holder.m_tvAckResponse.setText("승인함");
                        break;

                    case 2:
                        holder.m_tvAckResponse.setText("거절함");
                        break;

                    default:
                        holder.m_tvAckResponse.setText("미정");
                        break;
                }
            }
            else {
                holder.m_tvAckResponse.setVisibility(View.INVISIBLE);
            }
            */

            return convertView;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

        prepareDrawer();

        Log.i("ActivitySaving", "onCreate called...");

        timeSetting();

        ListView listView = (ListView) findViewById(R.id.lv_saving_result);

        TextView tvEmpty = findViewById(R.id.tv_empty);
        listView.setEmptyView(tvEmpty);
        m_SavingResultAdapter= new SavingResultAdapter();

        listView.setAdapter(m_SavingResultAdapter);

        if (m_SavingResultAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < m_SavingResultAdapter.getCount(); i++) {
            View listItem = m_SavingResultAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (m_SavingResultAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

        initChart();


    }

    public void initChart()
    {
        savingStackedBarChart = findViewById(R.id.saving_stacked_bar_chart);
        usageBarChart = findViewById(R.id.usage_bar_chart);
        usageTotalBarChart = findViewById(R.id.usage_total_bar_chart);

        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        //절감행동 실행 차트 설정

        savingStackedBarChart.setTouchEnabled(false);
        savingStackedBarChart.setPinchZoom(false);
        savingStackedBarChart.setDrawGridBackground(false);

        XAxis xAxisSaving = savingStackedBarChart.getXAxis();
        xAxisSaving.setDrawLabels(false);
        xAxisSaving.setDrawAxisLine(true);
        xAxisSaving.setDrawGridLines(false);

        YAxis yAxisRightSaving = savingStackedBarChart.getAxisRight();
        yAxisRightSaving.setDrawLabels(false);
        yAxisRightSaving.setDrawAxisLine(false);
        yAxisRightSaving.setDrawGridLines(false);

        YAxis yAxisLeftSaving = savingStackedBarChart.getAxisLeft();
        yAxisLeftSaving.setTypeface(tf2);
        yAxisLeftSaving.setDrawGridLines(false);

        /*
        Description descriptionSaving = new Description();
        descriptionSaving.setText("절감행동 실행");
        descriptionSaving.setTextSize(15f);

        savingStackedBarChart.setDescription(descriptionSaving);*/
        savingStackedBarChart.setDescription(null);

        Legend legendSaving = savingStackedBarChart.getLegend();

        savingStackedBarChart.animateY(2500);

        //사용량 분석 차트 설정

        usageTotalBarChart.setTouchEnabled(false);
        usageTotalBarChart.setPinchZoom(false);
        usageTotalBarChart.setDrawGridBackground(false);

        XAxis xAxisTotal = usageTotalBarChart.getXAxis();
        xAxisTotal.setDrawLabels(false);
        xAxisTotal.setDrawAxisLine(true);
        xAxisTotal.setDrawGridLines(false);
        xAxisTotal.setDrawGridLinesBehindData(true);

        YAxis yAxisRightTotal = usageTotalBarChart.getAxisRight();
        yAxisRightTotal.setDrawLabels(false);
        yAxisRightTotal.setDrawAxisLine(false);
        yAxisRightTotal.setDrawGridLines(false);

        YAxis yAxisLeftTotal = usageTotalBarChart.getAxisLeft();
        yAxisLeftTotal.setTypeface(tf2);
        yAxisLeftTotal.setDrawGridLines(false);

        LimitLine llStandard = new LimitLine(150f, "기준");
        llStandard.setLineWidth(1f);
        //llStandard.enableDashedLine(10f,10f,0f);
        //llStandard.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        //llStandard.setTextSize(10f);
        //llStandard.setTypeface(tf2);

        LimitLine llGoal = new LimitLine(130f, "목표");
        llGoal.setLineWidth(1f);
        //llGoal.enableDashedLine(10f,10f,0f);
        //llGoal.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        //llGoal.setTextSize(10f);
        //llGoal.setTypeface(tf2);

        yAxisLeftTotal.removeAllLimitLines();
        yAxisLeftTotal.addLimitLine(llStandard);
        yAxisLeftTotal.addLimitLine(llGoal);

        usageTotalBarChart.setDescription(null);

        /*
        Description descriptionTotal = new Description();
        descriptionTotal.setText("사용량 분석");
        usageTotalBarChart.setDescription(descriptionTotal);
        */


        Legend legendTotal = savingStackedBarChart.getLegend();


        usageTotalBarChart.animateY(2500);



        //가로 바차트 설정

        usageBarChart.setDrawBarShadow(false);
        usageBarChart.setDrawValueAboveBar(true);
        usageBarChart.getDescription().setEnabled(false);
        usageBarChart.setTouchEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be drawn
        usageBarChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        usageBarChart.setPinchZoom(true);
        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);
        // mChart.setDrawXLabels(false);
        usageBarChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        usageBarChart.animateY(2500);



        XAxis xAxis = usageBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tf2);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(0.3f);

        YAxis yLeft = usageBarChart.getAxisLeft();
        yLeft.setTypeface(tf2);
        yLeft.setDrawAxisLine(true);
        yLeft.setDrawGridLines(true);
        yLeft.setGranularity(0.3f);

        Legend lgd = usageBarChart.getLegend();
        lgd.setDrawInside(false);
        lgd.setFormSize(8f);
        lgd.setXEntrySpace(4f);

        //barchart 데이터 입력
        BarDataSet barDataSetSaving = new BarDataSet(dataValueSaving(),"Bar Set");
        barDataSetSaving.setColors(colorArray);

        BarData barDataSaving = new BarData(barDataSetSaving);
        savingStackedBarChart.setData(barDataSaving);

        //사용량분석 차트 데이터 입력
        BarDataSet barDataSetTotal = new BarDataSet(dataValueUsageTotal(),"Bar Set");
        barDataSetTotal.setColor(Color.MAGENTA);
        BarData barDataTotal = new BarData(barDataSetTotal);

        usageTotalBarChart.setData(barDataTotal);
    }

    private ArrayList dataValueSaving(){
        ArrayList dataVals = new ArrayList<>();

        dataVals.add(new BarEntry(0, new float[]{2f,1f}));

        return dataVals;
    }

    private ArrayList dataValueUsageTotal(){
        ArrayList dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0, new float[]{156.7f}));

        return dataVals;
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

    @Override
    public void onResult(CaResult Result) {

    }
}