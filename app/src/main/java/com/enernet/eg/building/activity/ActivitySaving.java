package com.enernet.eg.building.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaEngine;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.EgYearMonthDayPicker;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.model.CaMeter;
import com.enernet.eg.building.model.CaMeterUsage;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Float.parseFloat;

public class ActivitySaving extends BaseActivity implements IaResultHandler {

    private Button btnSelectTime;
    private Button btnSelectTime2;

    public String m_strSelectedDate1;
    public String m_strSelectedDate2;


    public ArrayList<CaMeter> m_alMeterGross = new ArrayList<>();
    public ArrayList<CaMeterUsage> m_alUsageForAllMeter = new ArrayList<>();


    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat myyyyMMddFormat = new SimpleDateFormat("yyyyMMdd");

    private EgYearMonthDayPicker m_dlgYearMonthDayPicker;
    BarChart savingStackedBarChart, usageTotalBarChart;
    HorizontalBarChart usageBarChart;
    int[] colorArray = new int[] {Color.CYAN, Color.LTGRAY};
    String[] labelArray = new String[] {"시행 횟수", "미시행 횟수"};
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
            return m_alMeterGross.size();
            //return 10;
        }

        @Override
        public Object getItem(int position) {
            return m_alMeterGross.get(position);
            //return position;
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
                convertView = inflater.inflate(R.layout.list_item_saving_result, null);

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


            final CaMeter meter = m_alMeterGross.get(position);


            holder.m_tvInstrument.setText(meter.m_strDescr);
            holder.m_tvInstrumentUsage.setText(CaApplication.m_Info.m_dfKwh.format(meter.m_dKwhReal)+ " kWh");
            holder.m_tvSavingStandard.setText("절감 기준  "+CaApplication.m_Info.m_dfKwh.format(meter.m_dKwhRef));
            holder.m_tvSavingGoal.setText("절감 목표  " + CaApplication.m_Info.m_dfKwh.format(meter.m_dKwhPlan));

            if(meter.m_dKwhReal<meter.m_dKwhPlan) holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_pastel_green_filled));
            else if(meter.m_dKwhReal< meter.m_dKwhRef) holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_pastel_yellow_filled));
            else if(meter.m_dKwhReal>= meter.m_dKwhRef) holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_pastel_red_filled));


            return convertView;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

        prepareDrawer();

        Log.i("ActivitySaving", "onCreate called...");

        //time 세팅과 getsaveResult 호출
        timeSetting();

    }

    public void listSetting(){

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

        savingStackedBarChart.getLegend().setEnabled(false);



        savingStackedBarChart.animateY(2000);

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


        LimitLine llStandard = new LimitLine(parseFloat(CaApplication.m_Info.m_dfKwh.format(CaApplication.m_Info.m_dKwhRefForAllMeter)), "기준");
        llStandard.setLineWidth(1f);
        llStandard.setTypeface(tf2);
        //llStandard.setLineColor(getResources().getColor(R.color.ks_light_blue));
        llStandard.setTextColor(getResources().getColor(R.color.ks_gray));
        llStandard.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        //llStandard.enableDashedLine(10f,10f,0f);
        //llStandard.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        //llStandard.setTextSize(10f);
        //llStandard.setTypeface(tf2);

        LimitLine llGoal = new LimitLine(parseFloat(CaApplication.m_Info.m_dfKwh.format(CaApplication.m_Info.m_dKwhPlanForAllMeter)), "목표");
        llGoal.setLineWidth(1f);
        llGoal.setLineColor(getResources().getColor(R.color.ks_light_blue));
        llGoal.setTypeface(tf2);
        llGoal.setTextColor(getResources().getColor(R.color.ks_white_blue));
        llGoal.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

        //llGoal.enableDashedLine(10f,10f,0f);
        //llGoal.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        //llGoal.setTextSize(10f);
        //llGoal.setTypeface(tf2);

        yAxisLeftTotal.removeAllLimitLines();
        yAxisLeftTotal.addLimitLine(llStandard);
        yAxisLeftTotal.addLimitLine(llGoal);



        usageTotalBarChart.setDescription(null);
        Legend totalLgd=usageTotalBarChart.getLegend();
        totalLgd.setTypeface(tf2);


        /*
        Description descriptionTotal = new Description();
        descriptionTotal.setText("사용량 분석");
        usageTotalBarChart.setDescription(descriptionTotal);
        */

        usageTotalBarChart.animateY(2000);



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
        usageBarChart.animateY(2000);



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

        yLeft.addLimitLine(llStandard);
        yLeft.addLimitLine(llGoal);


        Legend lgd = usageBarChart.getLegend();
        lgd.setDrawInside(false);
        lgd.setFormSize(8f);
        lgd.setXEntrySpace(4f);


        //실천 횟수 데이터 입력
        ValueFormatter vfCount=new ValueFormatter() {

            @Override
            public String getFormattedValue(float v) {
                if (v==0) return "";
                else return CaApplication.m_Info.m_dfKwh.format(v)+ " 회";
            }
        };
        BarDataSet barDataSetSaving = new BarDataSet(dataValueSaving(),"Bar Set");
        barDataSetSaving.setColors(colorArray);
        barDataSetSaving.setValueFormatter(vfCount);
        BarData barDataSaving = new BarData(barDataSetSaving);
        barDataSaving.setBarWidth(0.5f);
        barDataSaving.setValueTextSize(13f);
        barDataSaving.setValueTypeface(tf2);


        savingStackedBarChart.setData(barDataSaving);


        //사용량분석 차트 데이터 입력
        ValueFormatter vfKwhWithUnit=new ValueFormatter() {

            @Override
            public String getFormattedValue(float v) {
                if (v==0) return "";
                else return CaApplication.m_Info.m_dfKwh.format(v)+ " kWh";
            }
        };

        BarDataSet barDataSetTotal = new BarDataSet(dataValueUsageTotal(),"평균 사용량");
        double maxYValue = 0;
        if(CaApplication.m_Info.m_dAvgKwhForAllMeter<CaApplication.m_Info.m_dKwhPlanForAllMeter) {
            barDataSetTotal.setColor(getResources().getColor(R.color.eg_pastel_green));
            maxYValue=CaApplication.m_Info.m_dKwhRefForAllMeter + 30f;
        }
        else if(CaApplication.m_Info.m_dAvgKwhForAllMeter<CaApplication.m_Info.m_dKwhRefForAllMeter) {
            barDataSetTotal.setColor(getResources().getColor(R.color.eg_yellow_dark));
            maxYValue=CaApplication.m_Info.m_dKwhRefForAllMeter + 30;
        }
        else if(CaApplication.m_Info.m_dAvgKwhForAllMeter>=CaApplication.m_Info.m_dKwhRefForAllMeter) {
            barDataSetTotal.setColor(getResources().getColor(R.color.eg_pastel_red));
            maxYValue=CaApplication.m_Info.m_dAvgKwhForAllMeter + 30;
        }

        barDataSetTotal.setValueFormatter(vfKwhWithUnit);

        BarData barDataTotal = new BarData(barDataSetTotal);
        barDataTotal.setBarWidth(0.5f);
        barDataTotal.setValueTextSize(13f);
        barDataTotal.setValueTypeface(tf2);

        usageTotalBarChart.setData(barDataTotal);
        yAxisLeftTotal.setAxisMaximum((float)maxYValue);

    }

    private ArrayList dataValueSaving(){
        ArrayList dataVals = new ArrayList<>();
        Log.i("Saving", "count is" + CaApplication.m_Info.m_nTotalSaveActWithHistoryCount);
        float m_fTotalSaveActCount= parseFloat(CaApplication.m_Info.m_dfKwh.format(CaApplication.m_Info.m_nTotalSaveActCount-CaApplication.m_Info.m_nTotalSaveActWithHistoryCount));
        float m_fTotalSaveActWithHistoryCount= Float.parseFloat(CaApplication.m_Info.m_dfKwh.format(CaApplication.m_Info.m_nTotalSaveActWithHistoryCount));

        dataVals.add(new BarEntry(0, new float[]{m_fTotalSaveActWithHistoryCount,m_fTotalSaveActCount}));


        return dataVals;
    }

    private ArrayList dataValueUsageTotal(){
        ArrayList dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0, new float[]{parseFloat(CaApplication.m_Info.m_dfKwh.format(CaApplication.m_Info.m_dAvgKwhForAllMeter))}));

        return dataVals;
    }

    public void setDataChart() {
        usageBarChart.clear();

        ArrayList<BarEntry> yValsKwh = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        float groupSpace = 0.2f;
        float barSpace = 0.10f;
        float barWidth = 0.30f;

        int nCountUsage=m_alUsageForAllMeter.size();
        for (int i=0; i<nCountUsage; i++) {
            CaMeterUsage Usage=m_alUsageForAllMeter.get(nCountUsage-1-i);
            //CaMeterUsage Usage=m_alUsageForAllMeter.get(i);
            yValsKwh.add(new BarEntry(i, (float)Usage.m_dKwh));
            if(Usage.m_dKwh<CaApplication.m_Info.m_dKwhPlanForAllMeter) colors.add(getResources().getColor(R.color.eg_pastel_green));
            else if(Usage.m_dKwh<CaApplication.m_Info.m_dKwhRefForAllMeter) colors.add(getResources().getColor(R.color.eg_yellow_dark));
            else if(Usage.m_dKwh>=CaApplication.m_Info.m_dKwhRefForAllMeter) colors.add(getResources().getColor(R.color.red));
            //Log.i("Saving", "each Usage is " + Usage.m_dKwh);

        }
        ValueFormatter vfKwhWithUnit=new ValueFormatter() {

            @Override
            public String getFormattedValue(float v) {
                if (v==0) return "";
                else return CaApplication.m_Info.m_dfKwh.format(v);
            }
        };

        ValueFormatter vfKwh=new ValueFormatter() {

            @Override
            public String getFormattedValue(float v) {
                return CaApplication.m_Info.m_dfKwh.format(v);
            }
        };

        YAxis yLeft = usageBarChart.getAxisLeft();
        yLeft.setValueFormatter(vfKwh);

        YAxis yRight = usageBarChart.getAxisRight();
        yRight.setValueFormatter(vfKwh);

        XAxis xAxis = usageBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getAreaCount()));
        xAxis.setLabelCount(m_alUsageForAllMeter.size());

        BarDataSet setKwhCurr=new BarDataSet(yValsKwh, "사용량");
        setKwhCurr.setColors(colors);
        setKwhCurr.setValueFormatter(vfKwhWithUnit);


        BarData dataKwh = new BarData(setKwhCurr);

        dataKwh.setValueTextSize(10f);
        dataKwh.setBarWidth(barWidth);
        dataKwh.setHighlightEnabled(false);

        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        dataKwh.setValueTypeface(tf2);
        dataKwh.setValueTextColor(getResources().getColor(R.color.eg_cyan_dark));
        usageBarChart.getXAxis().setAxisMinimum(0);
        usageBarChart.getXAxis().setAxisMaximum(nCountUsage);
        usageBarChart.setData(dataKwh);
        usageBarChart.getAxisLeft().setAxisMinimum(0f);
        usageBarChart.getAxisRight().setAxisMinimum(0f);
        usageBarChart.getLegend().setEnabled(false);
        //usageBarChart.groupBars(0f, groupSpace, barSpace);
    }

    public ArrayList<String> getAreaCount(){
        int nCountUsage=m_alUsageForAllMeter.size();
        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i <nCountUsage; i++) {
            CaMeterUsage Usage=m_alUsageForAllMeter.get(nCountUsage-1-i);

            label.add(Usage.m_nDay + " 일");
        };
        return label;
    }

    public void timeSetting() {
        btnSelectTime2 = (Button) findViewById(R.id.btn_select_time2);
        btnSelectTime = (Button) findViewById(R.id.btn_select_time);


        Calendar calYesterday = Calendar.getInstance();
        calYesterday.add(Calendar.DATE, -1);
        String m_dtYesterday = mFormat.format(calYesterday.getTime());
        btnSelectTime2.setText(m_dtYesterday);
        m_strSelectedDate2 = myyyyMMddFormat.format(calYesterday.getTime());

        String m_dtSavePlanCreated = mFormat.format(CaApplication.m_Info.m_dtSavePlanCreated);
        btnSelectTime.setText(m_dtSavePlanCreated);
        m_strSelectedDate1 = myyyyMMddFormat.format(CaApplication.m_Info.m_dtSavePlanCreated);

        CaApplication.m_Engine.GetSaveResult(CaApplication.m_Info.m_nSeqSavePlanActive, m_strSelectedDate1, m_strSelectedDate2,this,this);

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
                        int nMonth = m_dlgYearMonthDayPicker.m_DatePicker.getMonth() + 1;
                        int nDay = m_dlgYearMonthDayPicker.m_DatePicker.getDayOfMonth();

                        Log.i("YearMonthDayPicker", "year="+nYear+", month="+nMonth+", day="+nDay);

                        //명령어
                        String strYear = Integer.toString(nYear);
                        String strMonth =Integer.toString(nMonth);
                        if(nMonth <=9) strMonth="0"+strMonth;
                        String strDay = Integer.toString(nDay);
                        if(nDay <=9) strDay="0"+strDay;

                        String chosenDate = strYear+"-"+strMonth+"-"+strDay;
                        btnSelectTime = (Button) findViewById(R.id.btn_select_time);
                        btnSelectTime.setText(chosenDate);
                        m_strSelectedDate1 = strYear+strMonth+strDay;
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
                        if(nMonth <=9) strMonth="0"+strMonth;
                        String strDay = Integer.toString(nDay);
                        if(nDay <=9) strDay="0"+strDay;

                        String chosenDate = strYear+"-"+strMonth+"-"+strDay;
                        btnSelectTime2 = (Button) findViewById(R.id.btn_select_time2);
                        btnSelectTime2.setText(chosenDate);
                        m_strSelectedDate2 = strYear+strMonth+strDay;
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

            case R.id.btn_search: {
                int date1 = Integer.parseInt(m_strSelectedDate1);
                int date2 = Integer.parseInt(m_strSelectedDate2);
                int dtSavePlan=Integer.parseInt(myyyyMMddFormat.format(CaApplication.m_Info.m_dtSavePlanCreated));

                if(date1>=date2){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivitySaving.this);
                    dlg.setMessage("날짜입력이 잘못되었습니다.");
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();
                }
                /*
                else if(date2-date1>=40){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivitySaving.this);
                    dlg.setMessage("40일 이내의 데이터만 조회하실 수 있습니다.");
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();
                }

                 */
                else if(date1<dtSavePlan){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivitySaving.this);
                    dlg.setMessage("절감계획 이전의 데이터는 불러올 수 없습니다.");
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();
                }
                else{
                    CaApplication.m_Engine.GetSaveResult(CaApplication.m_Info.m_nSeqSavePlanActive, m_strSelectedDate1, m_strSelectedDate2, this, this);
                }

            }
            break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }




    public void prepareChartData(JSONArray ja) {
        Log.i("Saving", "prepareChartData is activated...");

        m_alUsageForAllMeter.clear();
        try{
            for(int i=0;i<ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                CaMeterUsage usage = new CaMeterUsage();

                usage.m_nYear=jo.getInt("year");
                usage.m_nMonth=jo.getInt("month");
                usage.m_nDay=jo.getInt("day");
                usage.m_bHoliday=jo.getBoolean("is_holiday");
                usage.m_dKwh=jo.getDouble("kwh");
                usage.m_dWon=jo.getDouble("won");


                m_alUsageForAllMeter.add(usage);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void prepareListData(JSONArray ja) {
        Log.i("Saving", "prepareListData is activated...");

        m_alMeterGross.clear();
        try{
            for(int i=0;i<ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                CaMeter meter = new CaMeter();

                meter.m_nSeqMeter=jo.getInt("seq_meter");
                meter.m_strMid=jo.getString("mid");
                meter.m_strDescr=jo.getString("descr");
                meter.m_dKwhRef=jo.getDouble("kwh_ref");
                meter.m_dWonRef=jo.getDouble("won_ref");
                meter.m_dKwhPlan=jo.getDouble("kwh_plan");
                meter.m_dWonPlan=jo.getDouble("won_plan");
                meter.m_dKwhReal=jo.getDouble("kwh_real");
                meter.m_dWonReal=jo.getDouble("won_real");


                m_alMeterGross.add(meter);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(CaResult Result) {

        if (Result.object==null) {
            Toast.makeText(getApplicationContext(),"Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.CB_GET_SAVE_RESULT: {
                Log.i("Saving", "Result of GetSaveResult received...");

                try {
                    JSONObject jo = Result.object;
                    CaApplication.m_Info.m_nTotalSaveActCount=jo.getInt("total_save_act_count");
                    CaApplication.m_Info.m_nTotalSaveActWithHistoryCount=jo.getInt("total_save_act_with_history_count");
                    CaApplication.m_Info.m_dAvgKwhForAllMeter=jo.getDouble("avg_kwh_for_all_meter");
                    CaApplication.m_Info.m_dAvgWonForAllMeter=jo.getDouble("avg_won_for_all_meter");
                    CaApplication.m_Info.m_dKwhRefForAllMeter=jo.getDouble("kwh_ref_for_all_meter");
                    CaApplication.m_Info.m_dKwhPlanForAllMeter=jo.getDouble("kwh_plan_for_all_meter");
                    JSONArray jaMeter = jo.getJSONArray("list_meter");
                    JSONArray jaUsageForAllMeter = jo.getJSONArray("list_usage_for_all_meter");
                    JSONArray jaMeterGross = jo.getJSONArray("list_meter_gross");
                    CaApplication.m_Info.setMeterList(jaMeter);
                    prepareChartData(jaUsageForAllMeter);
                    prepareListData(jaMeterGross);

                    initChart();
                    setDataChart();
                    listSetting();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            default: {
                Log.i("Saving", "Unknown type result received : " + Result.m_nCallback);
            }
            break;

        } // end of switch

    }
}