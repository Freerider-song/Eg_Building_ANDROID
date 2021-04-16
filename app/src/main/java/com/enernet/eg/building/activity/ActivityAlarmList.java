package com.enernet.eg.building.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaEngine;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.model.CaAlarm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityAlarmList extends BaseActivity implements IaResultHandler {

    private AlarmAdapter m_AlarmAdapter;

    private ListView m_lvAlarm;

    private ArrayList<CaAlarm> m_alAlarm = new ArrayList<>();

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
            return m_alAlarm.size();

        }

        @Override
        public Object getItem(int position) {

            return m_alAlarm.get(position);
            //return position;
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

            final CaAlarm alarm = m_alAlarm.get(position);

            switch (alarm.m_nAlarmType) {
                case CaEngine.ALARM_PLAN_ELEM_BEGIN:
                case CaEngine.ALARM_PLAN_ELEM_END:
                case CaEngine.ALARM_SAVE_ACT_MISSED:

                    holder.m_btnAlarmExecute.setVisibility(View.VISIBLE);
                    break;

                default: {
                    holder.m_btnAlarmExecute.setVisibility(View.INVISIBLE);
                }
            }

            holder.m_tvAlarmTitle.setText(alarm.m_strTitle);
            holder.m_tvAlarmContent.setText(alarm.m_strContent);
            holder.m_tvAlarmTimeCreated.setText(alarm.getTimeCreated());

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

        prepareDrawer();
        CaApplication.m_Engine.GetBldAlarmList(CaApplication.m_Info.m_nSeqAdmin, 30, this,this);

    }

    public void initListView() {

        m_lvAlarm = findViewById(R.id.lv_alarm_list);
        TextView tvEmpty = findViewById(R.id.tv_empty3);
        m_lvAlarm.setEmptyView(tvEmpty);

        final ActivityAlarmList This=this;

        ListView listView = (ListView) findViewById(R.id.lv_alarm_list);

        m_AlarmAdapter= new AlarmAdapter();

        listView.setAdapter(m_AlarmAdapter);
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


        }
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
                    m_alAlarm.clear();
                    JSONObject jo = Result.object;
                    JSONArray ja = jo.getJSONArray("list_alarm");

                    for (int i=0; i<ja.length(); i++) {
                        JSONObject joAlarm=ja.getJSONObject(i);

                        CaAlarm alarm=new CaAlarm();
                        alarm.m_nSeqAlarm=joAlarm.getInt("seq_alarm");
                        alarm.m_nAlarmType=joAlarm.getInt("alarm_type");
                        alarm.m_nSeqSavePlanElem=joAlarm.getInt("seq_save_plan_elem");
                        alarm.m_strTitle=joAlarm.getString("title");
                        alarm.m_strContent=joAlarm.getString("content");
                        alarm.m_bRead=(joAlarm.getInt("is_read")==1);
                        alarm.m_dtCreated=parseDate(joAlarm.getString("time_created"));

                        if (alarm.m_bRead) {
                            alarm.m_dtRead=parseDate(joAlarm.getString("time_read"));
                        }

                        m_alAlarm.add(alarm);
                    }
                    initListView();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            break;

            /*
            case CaEngine.CB_SET_ALARM_LIST_AS_READ: {
                Log.i("Alarm", "Result of SetAlarmListAsRead received...");
                finish();
            }
            break;
             */

            default: {
                Log.i("Alarm", "Unknown type result received : " + Result.m_nCallback);
            }
            break;

        } // end of switch
    }



}