package com.enernet.eg.building.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaEngine;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.model.CaPlan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class ActivityHome extends BaseActivity implements IaResultHandler {

    private SavingAdapter m_SavingAdapter;
    private SavingCheckAdapter m_SavingCheckAdapter;

    private CustomGauge m_GaugeChart;
    private HalfGauge m_HalfGauge;

    private ListView m_lvSavingList;


    private class SavingViewHolder {
        public ConstraintLayout m_clAreaRoot;
        public TextView m_tvTitle;
        public TextView m_tvTime;
        public TextView m_tvUsageGoal;
        public TextView m_tvUsageToday;
        public TextView m_tvUsageRef;
        public ListView m_lvSavingCheckList;
        public TextView m_tvSavingResult;
    }

    private class SavingCheckViewHolder {
        public CheckBox m_CheckBox;
    }

    private class SavingCheckAdapter extends BaseAdapter {

        public SavingCheckAdapter() {
            super();
        }

        @Override
        public int getCount() {
            //return CaApplication.m_Info.m_alAlarm.size();
            return 3;
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
            SavingCheckViewHolder holder;
            if (convertView == null) {
                holder = new SavingCheckViewHolder();

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_saving_check, null);

                holder.m_CheckBox = convertView.findViewById(R.id.checkBox4);

                convertView.setTag(holder);
            }
            else {
                holder = (SavingCheckViewHolder) convertView.getTag();
            }


            return convertView;
        }
    }

    private class SavingAdapter extends BaseAdapter {

        public SavingAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return CaApplication.m_Info.m_alPlan.size();

        }

        @Override
        public Object getItem(int position) {
            return CaApplication.m_Info.m_alPlan.get(position);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           SavingViewHolder holder;
            if (convertView == null) {
                holder = new SavingViewHolder();

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_saving, null);

                holder.m_clAreaRoot = convertView.findViewById(R.id.area_root);
                holder.m_tvTitle = convertView.findViewById(R.id.tv_saving_title);
                holder.m_tvTime=convertView.findViewById(R.id.tv_time);
                holder.m_tvUsageGoal=convertView.findViewById(R.id.tv_usage_goal);
                holder.m_tvUsageToday=convertView.findViewById(R.id.tv_usage_today);
                holder.m_tvSavingResult=convertView.findViewById(R.id.tv_saving_result);
                holder.m_tvUsageRef=convertView.findViewById(R.id.tv_usage_ref);

                //list view in list view
                holder.m_lvSavingCheckList=convertView.findViewById(R.id.lv_saving_check_list);
                m_SavingCheckAdapter= new SavingCheckAdapter();
                holder.m_lvSavingCheckList.setAdapter(m_SavingCheckAdapter);
                //리스트 뷰 높이 설정
                if (m_SavingCheckAdapter == null) {
                    // pre-condition
                }
                int totalHeight = 0;
                for (int i = 0; i < m_SavingCheckAdapter.getCount(); i++) {
                    View listItem = m_SavingCheckAdapter.getView(i, null, holder.m_lvSavingCheckList);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = holder.m_lvSavingCheckList.getLayoutParams();
                params.height = totalHeight + (holder.m_lvSavingCheckList.getDividerHeight() * (m_SavingCheckAdapter.getCount() - 1));
                holder.m_lvSavingCheckList.setLayoutParams(params);
                holder.m_lvSavingCheckList.requestLayout();

                convertView.setTag(holder);
            }
            else {
                holder = (SavingViewHolder) convertView.getTag();
            }


            final CaPlan plan = CaApplication.m_Info.m_alPlan.get(position);



            holder.m_tvTitle.setText(plan.m_strMeterDescr);
            holder.m_tvUsageGoal.setText("목표 사용량 " + CaApplication.m_Info.m_dfKwh.format(plan.m_dKwhPlan)+"kWh");
            holder.m_tvUsageToday.setText("오늘 사용량 " + CaApplication.m_Info.m_dfKwh.format(plan.m_dKwhReal)+"kWh");
            holder.m_tvUsageRef.setText("기준 사용량 " + CaApplication.m_Info.m_dfKwh.format(plan.m_dKwhRef)+"kWh");
            holder.m_tvTime.setText(plan.m_nHourFrom+"시 ~ " +plan.m_nHourTo+"시");
            //Log.i("Alarm", "m_bRead="+alarm.m_bRead);

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            String getTime = sdf.format(date);
            if(plan.m_nHourTo > Integer.parseInt(getTime)){
                holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_gray_hollow));
            }
            else{
                if (plan.m_dKwhReal <= plan.m_dKwhPlan) {
                    holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_pastel_green_filled));
                }
                else if(plan.m_dKwhReal <= plan.m_dKwhRef){
                    holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_pastel_yellow_filled));
                }
                else {
                    holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_pastel_red_filled));
                }
            }

            return convertView;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        prepareDrawer();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String getTime = sdf.format(date);

        Log.i("get time is ", "is "+ getTime + "and saveplan is" + CaApplication.m_Info.m_nSeqSavePlanActive);

        CaApplication.m_Engine.GetSaveResultDaily(CaApplication.m_Info.m_nSeqSavePlanActive, getTime, this, this);

        m_lvSavingList = findViewById(R.id.lv_saving_list);

        initChart();

        ListView listView = (ListView) findViewById(R.id.lv_saving_list);
        //ListAdapter listAdapter = listView.getAdapter();
        m_SavingAdapter= new SavingAdapter();
        listView.setAdapter(m_SavingAdapter);

        Log.i("HOme", "Listview가 정상적으로 호출되었습니다.");

        //리스트 뷰 높이 설정

        if (m_SavingAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < m_SavingAdapter.getCount(); i++) {
            View listItem = m_SavingAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (m_SavingAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();


        //액티비티 전환시 리스트뷰 쪽으로 포커스가 맞춰지는 문제 해결

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView_home);
        //scrollView.fullScroll(ScrollView.FOCUS_UP);
        View targetView = findViewById(R.id.iv_small_dot_purple2);
        targetView.getParent().requestChildFocus(targetView,targetView);

    }

    public void initChart(){

        double kwhPlan=Double.parseDouble(CaApplication.m_Info.m_dfKwh.format(CaApplication.m_Info.m_dKwhPlanForAllMeter));
        double kwhRef=Double.parseDouble(CaApplication.m_Info.m_dfKwh.format(CaApplication.m_Info.m_dKwhRefForAllMeter));
        double kwhReal=Double.parseDouble(CaApplication.m_Info.m_dfKwh.format(CaApplication.m_Info.m_dKwhRealForAllMeter));

        TextView m_tvKwhRef = findViewById(R.id.tv_kwh_ref);
        TextView m_tvKwhPlan = findViewById(R.id.tv_kwh_plan);

        m_tvKwhPlan.setText("절감목표\n("+kwhPlan+")");
        m_tvKwhRef.setText("절감기준\n("+kwhRef+")");

        m_HalfGauge = findViewById(R.id.halfGauge);

        Range range = new Range();
        range.setColor(getResources().getColor(R.color.eg_pastel_green));
        range.setFrom(0.0);
        range.setTo(kwhPlan);

        Range range2 = new Range();
        range2.setColor(getResources().getColor(R.color.eg_pastel_yellow));
        range2.setFrom(kwhPlan);
        range2.setTo(kwhRef);

        Range range3 = new Range();
        range3.setColor(getResources().getColor(R.color.eg_pastel_red));
        if(kwhRef> kwhReal){
            range3.setTo(1.5*kwhRef);
            m_HalfGauge.setMaxValue(1.5*kwhRef);
        }
        else{
            range3.setTo(1.5*kwhReal);
            m_HalfGauge.setMaxValue(1.5*kwhReal);
        }

        range3.setFrom(kwhRef);


        //add color ranges to gauge
        m_HalfGauge.addRange(range);
        m_HalfGauge.addRange(range2);
        m_HalfGauge.addRange(range3);

        //set value to gauge

        m_HalfGauge.enableAnimation(true);
        m_HalfGauge.setMinValue(0.0);
        m_HalfGauge.setValue(kwhReal);

    }


    @Override
    public void onBackPressed() {
        if (m_Drawer.isDrawerOpen()) {
            m_Drawer.closeDrawer();
        }
        else {
            promptAppExit();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                promptAppExit();
            }
            break;

            case R.id.btn_menu: {
                m_Drawer.openDrawer();
            }
            break;


        }
    }

    public void promptAppExit() {
        // kill app
        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityHome.this);
        dlg.setTitle("경고"); //제목
        dlg.setMessage("앱을 종료하시겠습니까?"); // 메시지
        //dlg.setIcon(R.drawable.deum); // 아이콘 설정
//                버튼 클릭시 동작

        dlg.setNegativeButton("취소",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                // kill app
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });

        dlg.show();


    }


    @Override
    public void onResult(CaResult Result) {
        if (Result.object==null) {
            Toast.makeText(getApplicationContext(),"Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.CB_GET_SAVE_RESULT_DAILY: {
                Log.i("Home", "Result of GetSaveResultDaily received...");

                try {
                    JSONObject jo = Result.object;
                    JSONObject joSave = jo.getJSONObject("save_result_daily");
                    JSONArray jaPlan = joSave.getJSONArray("list_plan_elem");

                    CaApplication.m_Info.m_nSeqSaveRef = joSave.getInt("seq_save_ref");
                    CaApplication.m_Info.m_nSeqSite = joSave.getInt("seq_site");
                    CaApplication.m_Info.m_strSavePlanName = joSave.getString("save_plan_name");
                    CaApplication.m_Info.m_strSaveRefName = joSave.getString("save_ref_name");
                    CaApplication.m_Info.m_dSaveKwhTotalFromElem = joSave.getDouble("save_kwh_total_from_elem");
                    CaApplication.m_Info.m_dSaveWonTotalFromElem = joSave.getDouble("save_won_total_from_elem");
                    CaApplication.m_Info.m_dSaveKwhTotalFromMeter = joSave.getDouble("save_kwh_total_from_meter");
                    CaApplication.m_Info.m_dSaveWonTotalFromMeter = joSave.getDouble("save_kwh_total_from_meter");
                    CaApplication.m_Info.m_dKwhPlanForAllMeter = joSave.getDouble("kwh_plan_for_all_meter");
                    CaApplication.m_Info.m_dKwhRealForAllMeter = joSave.getDouble("kwh_real_for_all_meter");
                    CaApplication.m_Info.m_dKwhRefForAllMeter = joSave.getDouble("kwh_ref_for_all_meter");
                    CaApplication.m_Info.m_dWonPlanForAllMeter = joSave.getDouble("won_plan_for_all_meter");
                    CaApplication.m_Info.m_dWonRealForAllMeter = joSave.getDouble("won_real_for_all_meter");
                    CaApplication.m_Info.m_dWonRefForAllMeter = joSave.getDouble("won_ref_for_all_meter");
                    CaApplication.m_Info.m_dtSavePlanEnded = parseDate(joSave.getString("time_ended"));
                    CaApplication.m_Info.m_dtSavePlanCreated = parseDate(joSave.getString("time_created"));
                    CaApplication.m_Info.m_nActCount = joSave.getInt("act_count");
                    CaApplication.m_Info.m_nActCountWithHistory = joSave.getInt("act_count_with_history");


                    CaApplication.m_Info.setPlanList(jaPlan);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            default: {
                Log.i("Home", "Unknown type result received : " + Result.m_nCallback);
            }
            break;

        } // end of switch

    }


}


