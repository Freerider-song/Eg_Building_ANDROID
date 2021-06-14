package com.enernet.eg.building.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaEngine;
import com.enernet.eg.building.CaInfo;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.ListViewInfinite;
import com.enernet.eg.building.R;
import com.enernet.eg.building.model.CaAct;
import com.enernet.eg.building.model.CaActHistory;
import com.enernet.eg.building.model.CaAlarm;
import com.enernet.eg.building.model.CaNotice;
import com.enernet.eg.building.model.CaPlan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityAlarmList extends BaseActivity implements IaResultHandler, ListViewInfinite.ListenerInfinite {

    private AlarmAdapter m_AlarmAdapter;

    private ListViewInfinite m_lvAlarm;

    SimpleDateFormat myyyyMMddFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat mHHFormat = new SimpleDateFormat("HH");

    Boolean reset = false;

    Calendar calToday = Calendar.getInstance();
    String m_dtToday = myyyyMMddFormat.format(calToday.getTime());


    private class AlarmViewHolder {
        public ConstraintLayout m_clAreaRoot;
        public TextView m_tvAlarmTitle;
        public TextView m_tvAlarmContent;
        public TextView m_tvAlarmTimeCreated;
        public TextView m_tvSavingStandard;
        public Button m_btnAlarmExecute;
        public ImageView m_ivNew;
        public ImageView m_ivAlarmDot;
    }

    private class AlarmAdapter extends BaseAdapter {

        public AlarmAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return CaApplication.m_Info.m_alAlarm.size();

        }

        @Override
        public Object getItem(int position) {

            return CaApplication.m_Info.m_alAlarm.get(position);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AlarmViewHolder holder;
            if (convertView == null) {
                holder = new AlarmViewHolder();

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_alarm, null);

                holder.m_clAreaRoot = convertView.findViewById(R.id.area_root);
                holder.m_tvAlarmTitle = convertView.findViewById(R.id.tv_alarm_title);
                holder.m_tvAlarmContent = convertView.findViewById(R.id.tv_alarm_content);
                holder.m_tvAlarmTimeCreated=convertView.findViewById(R.id.tv_alarm_time_created);

                holder.m_ivAlarmDot=convertView.findViewById(R.id.iv_alarm_dot);
                holder.m_ivAlarmDot.setImageDrawable(getDrawable(R.drawable.small_dot_pink));

                holder.m_ivNew=convertView.findViewById(R.id.iv_new);
                holder.m_ivNew.setImageDrawable(getDrawable(R.drawable.new_icon));

                holder.m_btnAlarmExecute=convertView.findViewById(R.id.btn_alarm_execute);


                convertView.setTag(holder);
            }
            else {
                holder = (AlarmViewHolder) convertView.getTag();
            }

            final CaAlarm alarm = CaApplication.m_Info.m_alAlarm.get(position);



            switch (alarm.m_nAlarmType) {
                case CaEngine.ALARM_PLAN_ELEM_BEGIN:
                case CaEngine.ALARM_PLAN_ELEM_END:
                case CaEngine.ALARM_SAVE_ACT_MISSED:

                holder.m_btnAlarmExecute.setVisibility(View.VISIBLE);

                for(int i=0; i<CaApplication.m_Info.m_alPlan.size(); i++){
                    CaPlan plan = CaApplication.m_Info.m_alPlan.get(i);
                    if(plan.m_nSeqPlanElem==alarm.m_nSeqSavePlanElem){

                        for (int j = 0; j < plan.m_alAct.size(); j++) {
                            CaAct act = plan.m_alAct.get(j);
                            boolean flag = false;

                            for(int k=0;k<act.m_alActHistory.size();k++){

                                CaActHistory actHistory = act.m_alActHistory.get(k);

                                if(m_dtToday.equals(myyyyMMddFormat.format(actHistory.m_dtBegin))){

                                    flag = true;
                                    break;
                                }
                            }
                            if(flag==false) {
                                act.m_bAllChecked=false;
                            }
                            if (!act.m_bAllChecked) {
                                plan.m_bAllChecked = false;

                                break;
                            }
                        }
                        Log.i("AlarmList", "현재시간 "+Integer.parseInt(mHHFormat.format(calToday.getTime())));
                        if (plan.m_bAllChecked) {

                            holder.m_btnAlarmExecute.setText("조치 완료");
                            holder.m_btnAlarmExecute.setEnabled(false);
                            holder.m_btnAlarmExecute.setBackground(getResources().getDrawable(R.drawable.shape_round_corner_notice_normal));
                                }

                        else if(plan.m_nHourTo<=Integer.parseInt(mHHFormat.format(calToday.getTime()))){
                            holder.m_btnAlarmExecute.setText("조치 미흡");
                            holder.m_btnAlarmExecute.setEnabled(false);
                            holder.m_btnAlarmExecute.setBackground(getResources().getDrawable(R.drawable.shape_round_corner_notice_normal));
                        }
                        else if(plan.m_nHourTo>Integer.parseInt(mHHFormat.format(calToday.getTime())) && plan.m_nHourFrom <= Integer.parseInt(mHHFormat.format(calToday.getTime()))){
                            Log.i("AlarmList", "시행안된 절감계획이름 :" + plan.m_strMeterDescr +" , 시작 날짜: " + plan.m_nHourFrom+" , 지금조치하기");
                            Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_animation);
                            holder.m_btnAlarmExecute.setText("지금조치하기");
                            //holder.m_btnAlarmExecute.startAnimation(startAnimation);
                            holder.m_btnAlarmExecute.setEnabled(true);
                            holder.m_btnAlarmExecute.setBackground(getResources().getDrawable(R.drawable.shape_round_corner_dark_yellow_filled));
                        }

                        else {
                            holder.m_btnAlarmExecute.setEnabled(false);
                        }
                        break;
                    }
                }
                break;

                default: {
                    holder.m_btnAlarmExecute.setVisibility(View.INVISIBLE);
                }
            }

            holder.m_tvAlarmTitle.setText(alarm.m_strTitle);
            holder.m_tvAlarmContent.setText(alarm.m_strContent);
            holder.m_tvAlarmTimeCreated.setText(alarm.getTimeCreated());

            if(!myyyyMMddFormat.format(alarm.m_dtCreated).equals(m_dtToday)) {
                holder.m_btnAlarmExecute.setVisibility(View.INVISIBLE);

            }


            if (alarm.m_bRead) holder.m_ivNew.setVisibility(View.INVISIBLE);
            else holder.m_ivNew.setVisibility(View.VISIBLE);


            holder.m_btnAlarmExecute.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    alarm.m_bRead=true;
                    alarm.m_bReadStateChanged=true;
                    alarm.m_dtRead= Calendar.getInstance().getTime();
                    m_AlarmAdapter.notifyDataSetChanged();

                    Intent it = new Intent(ActivityAlarmList.this, ActivityAlarm.class);
                    it.putExtra("seq_plan_elem", alarm.m_nSeqSavePlanElem);
                    startActivity(it);
                }
            });

            return convertView;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        CaApplication.m_Info.m_alAlarm.clear();

        prepareDrawer();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String getTime = sdf.format(date);

        //CaApplication.m_Engine.GetBldAlarmList(CaApplication.m_Info.m_nSeqAdmin, getTime,20, this,this);


        initListView();

    }

    //ActivityAlarm에서 절감조치하고 돌아올 시 onResume 호출 -> 새로고침 유도.
    @Override
    public void onResume(){
        super.onResume();

        CaApplication.m_Info.m_alAlarm.clear();
        setAlarmCount();
        int nSeqAdmin= PreferenceUtil.getPreferences(getApplicationContext(), "SeqAdmin");
        int nSeqSavePlanActive = PreferenceUtil.getPreferences(getApplicationContext(), "SeqSavePlanActive");

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String getTime = sdf.format(date);

        CaApplication.m_Engine.GetBldAlarmList(nSeqAdmin, getTime,20, this, this);
        CaApplication.m_Engine.GetSaveResultDaily(nSeqSavePlanActive, m_dtToday, this,this);

        m_AlarmAdapter.notifyDataSetChanged();

    }

    public void setAlarmReadStateToDb() {
        String strSeqAlarmList = CaApplication.m_Info.getAlarmReadListString();
        if(strSeqAlarmList.isEmpty()) {

        }
        else {
            CaApplication.m_Engine.SetBldAlarmListAsRead(CaApplication.m_Info.m_nSeqAdmin, strSeqAlarmList,this,this);
            Log.i("AlarmList", "setBldAlarmListAsRead called..");
        }
    }

    public void initListView() {

        m_lvAlarm = findViewById(R.id.lv_alarm_list);
        TextView tvEmpty = findViewById(R.id.tv_empty3);
        m_lvAlarm.setEmptyView(tvEmpty);

        final ActivityAlarmList This=this;

        m_lvAlarm.setLoadingView(R.layout.loading_layout);
        m_lvAlarm.setListener(This);

        // Save the ListView state (= includes scroll position) as a Parceble
        Parcelable state = m_lvAlarm.onSaveInstanceState();

        // e.g. set new items
        m_AlarmAdapter= new AlarmAdapter();
        m_lvAlarm.setAdapter(m_AlarmAdapter);

        // Restore previous state (including selected item index and scroll position)
        m_lvAlarm.onRestoreInstanceState(state);

        m_lvAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ListNotice", "Item clicked, pos="+position);


                CaAlarm alarm=CaApplication.m_Info.m_alAlarm.get(position);
                alarm.m_bRead=true;
                alarm.m_bReadStateChanged=true;
                alarm.m_dtRead= Calendar.getInstance().getTime();
                m_AlarmAdapter.notifyDataSetChanged();

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                setAlarmReadStateToDb();
                finish();
            }
            break;

            case R.id.btn_menu: {
                setAlarmReadStateToDb();
                m_Drawer.openDrawer();

            }
            break;


        }
    }


    @Override
    public void onBackPressed() {
        if (m_Drawer.isDrawerOpen()) {
            m_Drawer.closeDrawer();
        }
        else {
            setAlarmReadStateToDb();
            finish();
        }

    }

    public void setAlarmCount() {
        TextView tvAlarmCount = findViewById(R.id.tv_alarm_count);
        String strAlarmCount = "* 총 " + CaApplication.m_Info.m_alAlarm.size()+" 건";
        tvAlarmCount.setText(strAlarmCount);
    }

    @Override
    public void onResult(CaResult Result) {

        if (Result.object==null) {
            Toast.makeText(getApplicationContext(), "Check Network...", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.CB_GET_BLD_ALARM_LIST: {

                Log.i("Alarm", "Result of GetBldAlarmList received...");

                try {

                    JSONObject jo = Result.object;
                    JSONArray ja = jo.getJSONArray("list_alarm");

                    if(ja.length()==0) {
                        m_lvAlarm.m_bNoMoreData = true;
                    }
                    if(ja.length()!=0) CaApplication.m_Info.setAlarmList(ja);

                    setAlarmCount();

                    m_lvAlarm.onDataAppended();



                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            break;

            case CaEngine.CB_GET_SAVE_RESULT_DAILY: {

                Log.i("Alarm", "Result of GetBldAlarmList received...");

                Log.i("Home", "Result of GetSaveResultDaily received...");

                try {
                    JSONObject jo = Result.object;
                    JSONObject joSave = jo.getJSONObject("save_result_daily");
                    JSONArray jaPlan = joSave.getJSONArray("list_plan_elem");

                    CaApplication.m_Info.setPlanList(jaPlan);

                   // initListView();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            break;

            case CaEngine.CB_SET_BLD_ALARM_AS_READ: {

                CaApplication.m_Engine.GetUnreadBldAlarmCount(CaApplication.m_Info.m_nSeqAdmin, this,this);
            }

            case CaEngine.CB_GET_UNREAD_BLD_ALARM_COUNT: {

                try {
                    JSONObject jo = Result.object;
                    CaApplication.m_Info.m_nUnreadAlarmCount = jo.getInt("count_unread");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;


            default: {
                Log.i("Alarm", "Unknown type result received : " + Result.m_nCallback);
            }
            break;

        } // end of switch
    }

    @Override
    public void onNeedLoadData() {
        Log.i("NoticeList", "onNeedLoadData called...");

        String strTimeMax=CaApplication.m_Info.m_dfyyyyMMddhhmmss.format(CaApplication.m_Info.m_dtAlarmCreatedMaxForNextRequest);


        reset = false;
        CaApplication.m_Engine.GetBldAlarmList(CaApplication.m_Info.m_nSeqAdmin, strTimeMax, 20, this, this);
        Log.i("AlarmList", "Alarm Loaded more... and strTimeMax is " + strTimeMax);
    }



}