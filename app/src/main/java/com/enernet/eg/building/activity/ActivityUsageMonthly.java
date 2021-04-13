package com.enernet.eg.building.activity;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaEngine;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.EgYearMonthDayPicker;
import com.enernet.eg.building.EgYearMonthPicker;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.StringUtil;
import com.enernet.eg.building.model.CaMeter;
import com.enernet.eg.building.model.CaMeterUsage;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityUsageMonthly extends BaseActivity implements IaResultHandler {

    private HorizontalBarChart m_Chart;

    private EgYearMonthPicker m_dlgYearMonthPicker;
    public int Year;
    public int Month;

    public int m_nMeter=0;
    public ArrayList<CaMeter> m_alMeter = new ArrayList<>();
    public CaMeter m_AllMeter = new CaMeter();

    private EgYearMonthDayPicker m_dlgYearMonthDayPicker;

    private Spinner m_spMeter;

    private Button btnSelectTime;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_monthly);

        prepareDrawer();

        timeSetting();

        Calendar calCurr= Calendar.getInstance();
        requestUsageMonthly(calCurr.get(Calendar.YEAR), calCurr.get(Calendar.MONTH)+1);



    }

    public void timeSetting() {

        btnSelectTime = (Button) findViewById(R.id.btn_select_time);


        Calendar calToday = Calendar.getInstance();
        String m_dtToday = mFormat.format(calToday.getTime());
        btnSelectTime.setText(m_dtToday);

    }

    public void initSpinner(){
        m_spMeter = findViewById(R.id.sp_meter);

        final List<String> alMeter = new ArrayList<>();

        for (int i=0; i<m_alMeter.size(); i++) {
            CaMeter ds=m_alMeter.get(i);
            alMeter.add(ds.m_strDescr);
        }

        ArrayAdapter<String> AdapterMeter = new ArrayAdapter<String>(this, R.layout.eg_spinner_style, alMeter) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), getString(R.string.font_open_sans_regular));
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(17.0f);
                ((TextView) v).setTextColor(getResources().getColor(R.color.eg_cyan_dark));
                return v;

            }

        };
        m_spMeter.setEnabled(true);


        m_spMeter.setAdapter(AdapterMeter);
        AdapterMeter.setDropDownViewResource(R.layout.eg_spinner_item_style);

        m_spMeter.setSelection(m_nMeter);


        m_spMeter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_nMeter=position;
                Log.i("UsageMonthly", "Selected="+alMeter.get(position)+", position="+position+", id="+id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void requestUsageMonthly(int nYear, int nMonth) {
        Year = nYear;
        Month = nMonth;
        CaApplication.m_Engine.GetUsageForAllMeterMonth(CaApplication.m_Info.m_nSeqSite,
                nYear, nMonth, this, this);
    }

    public void initChartDaily()
    {
        m_Chart = findViewById(R.id.usage_chart);

        m_Chart.setDrawBarShadow(false);
        m_Chart.setDrawValueAboveBar(true);
        m_Chart.getDescription().setEnabled(false);
        m_Chart.setTouchEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be drawn
        m_Chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        m_Chart.setPinchZoom(true);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);
        // mChart.setDrawXLabels(false);
        m_Chart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        m_Chart.animateY(2500);

        Typeface tf2 = Typeface.createFromAsset(getAssets(), StringUtil.getString(this, R.string.font_open_sans_regular));

        XAxis xAxis = m_Chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tf2);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(0.3f);

        YAxis yLeft = m_Chart.getAxisLeft();
        yLeft.setTypeface(tf2);
        yLeft.setDrawAxisLine(true);
        yLeft.setDrawGridLines(true);
        yLeft.setGranularity(0.3f);

        Legend lgd = m_Chart.getLegend();
        lgd.setDrawInside(false);
        lgd.setFormSize(8f);
        lgd.setXEntrySpace(6f);
        lgd.setYEntrySpace(2f);

        m_Chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                String s = Float.toString(x);

                float index_f = e.getY();
                int index = Math.round(index_f);

            }

            @Override
            public void onNothingSelected() {

            }

        });
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

                View.OnClickListener LsnConfirmYes=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        m_dlgYearMonthPicker.dismiss();

                        int nYear=m_dlgYearMonthPicker.m_npYear.getValue();
                        int nMonth=m_dlgYearMonthPicker.m_npMonth.getValue();


                        Log.i("YearMonthDatePicker", "year="+nYear+", month="+nMonth);

                        Year = nYear;
                        Month = nMonth;

                        String strYear = Integer.toString(nYear);
                        String strMonth =Integer.toString(nMonth);
                        if(nMonth <=9) strMonth="0"+strMonth;

                        String chosenDate = strYear+"-"+strMonth;
                        btnSelectTime = (Button) findViewById(R.id.btn_select_time);
                        btnSelectTime.setText(chosenDate);


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
                requestUsageMonthly(Year,Month);

            }
            break;

        }
    }

    @Override
    public void onResult(CaResult Result) {

        if (Result.object==null) {
            Toast.makeText(getApplicationContext(), "Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.CB_GET_USAGE_FOR_ALL_METER_MONTH: {
                Log.i("ActivityAck", "Result of GetUsageOfOneMonth received...");

                try {
                    JSONObject jo = Result.object;
                    JSONArray jaMeter = jo.getJSONArray("list_meter");
                    JSONArray jaAllMeter = jo.getJSONArray("list_usage_for_all_meter");
                    m_AllMeter.m_alMeterUsage = new ArrayList<>();
                    for(int j=0; j<jaAllMeter.length();j++){
                        JSONObject joUsage = jaAllMeter.getJSONObject(j);
                        CaMeterUsage usage = new CaMeterUsage();
                        usage.m_nUnit=joUsage.getInt("unit");
                        if(joUsage.isNull("kwh")){
                            usage.m_dKwh=0.0;
                        }
                        else{
                            usage.m_dKwh=joUsage.getDouble("kwh");
                        }
                        m_AllMeter.m_alMeterUsage.add(usage);
                    }
                    initChartDaily();
                    prepareChartData(jaMeter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            break;

            default: {
                Log.i("ActivityAck", "Unknown type result received : " + Result.m_nCallback);
            }
            break;
        }

    }

    public void prepareChartData(JSONArray ja) {
        m_alMeter.clear();
        try{
            for(int i=0;i<ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                JSONArray jaUsage = jo.getJSONArray("list_usage");
                CaMeter meter = new CaMeter();

                meter.m_nSeqMeter=jo.getInt("seq_meter");

                meter.m_strMid=jo.getString("mid");
                meter.m_strDescr=jo.getString("descr");

                meter.m_alMeterUsage = new ArrayList<>();
                for(int j=0; j<jaUsage.length();j++){
                    JSONObject joUsage = jaUsage.getJSONObject(j);
                    CaMeterUsage usage = new CaMeterUsage();
                    usage.m_nUnit=joUsage.getInt("unit");
                    if(joUsage.isNull("kwh")){
                        usage.m_dKwh=0.0;
                    }
                    else{
                        usage.m_dKwh=joUsage.getDouble("kwh");
                    }

                    meter.m_alMeterUsage.add(usage);
                }
                m_alMeter.add(meter);
            }
            setDataChart();
            initSpinner();

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAreaCount(){
        int nCountUsage=m_alMeter.get(0).m_alMeterUsage.size(); //24

        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i <nCountUsage; i++) {
            CaMeterUsage Usage=m_alMeter.get(0).m_alMeterUsage.get(nCountUsage-1-i);

            label.add(Usage.m_nUnit + " 일");
        };
        return label;
    }

    public void setDataChart() {

        m_Chart.clear();

        ArrayList<BarEntry> yValsKwhAll = new ArrayList<>();
        ArrayList<BarEntry> yValsKwhMeter = new ArrayList<>();


        float groupSpace = 0.2f;
        float barSpace = 0.10f;
        float barWidth = 0.30f;

        int nCountUsage=m_alMeter.get(0).m_alMeterUsage.size();
        for (int i=0; i<nCountUsage; i++) {
            CaMeterUsage UsageAll=m_AllMeter.m_alMeterUsage.get(nCountUsage-1-i);
            CaMeterUsage UsageMeter=m_alMeter.get(m_nMeter).m_alMeterUsage.get(nCountUsage-1-i);

            yValsKwhAll.add(new BarEntry(UsageAll.m_nUnit, (float)UsageAll.m_dKwh));
            yValsKwhMeter.add(new BarEntry(UsageMeter.m_nUnit, (float)UsageMeter.m_dKwh));
            //  yValsKwhAvg.add(new BarEntry((float)Usage.m_dKwhAvg, i));


            //  yValsWonAvg.add(new BarEntry((float)Usage.m_dWonAvg, i));

        }



        ValueFormatter vfKwhWithUnit=new ValueFormatter() {

            @Override
            public String getFormattedValue(float v) {
                if (v==0) return "";
                    //else return CaApplication.m_Info.m_dfKwh.format(v)+" kWh";
                else return CaApplication.m_Info.m_dfKwh.format(v);
            }
        };

        ValueFormatter vfKwh=new ValueFormatter() {

            @Override
            public String getFormattedValue(float v) {
                return CaApplication.m_Info.m_dfKwh.format(v);
            }
        };

        YAxis yLeft = m_Chart.getAxisLeft();
        yLeft.setValueFormatter(vfKwh);

        YAxis yRight = m_Chart.getAxisRight();
        yRight.setValueFormatter(vfKwh);

        XAxis xAxis = m_Chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getAreaCount()));
        xAxis.setLabelCount(nCountUsage);

        BarDataSet setKwhAll=new BarDataSet(yValsKwhAll, "전체 사용량");
        setKwhAll.setColor(getResources().getColor(R.color.eg_cyan_light));
        setKwhAll.setValueFormatter(vfKwhWithUnit);

        BarDataSet setKwhMeter=new BarDataSet(yValsKwhMeter, m_alMeter.get(m_nMeter).m_strDescr);
        setKwhMeter.setColor(getResources().getColor(R.color.eg_yellow_dark));
        setKwhMeter.setValueFormatter(vfKwhWithUnit);

        //m_Chart.getLegend().setEnabled(false);

        BarData dataKwh = new BarData(setKwhMeter, setKwhAll);

        dataKwh.setValueTextSize(10f);
        dataKwh.setBarWidth(barWidth);
        dataKwh.setHighlightEnabled(false);
        //dataKwh.setValueTypeface(tf2);
        m_Chart.getXAxis().setAxisMinimum(0);
        m_Chart.getXAxis().setAxisMaximum(nCountUsage);
        m_Chart.setData(dataKwh);
        m_Chart.getAxisLeft().setAxisMinimum(0f);
        m_Chart.getAxisRight().setAxisMinimum(0f);
        m_Chart.groupBars(0f, groupSpace, barSpace);



    }

}