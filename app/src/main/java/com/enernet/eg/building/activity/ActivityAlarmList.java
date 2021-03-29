package com.enernet.eg.building.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;

public class ActivityAlarmList extends BaseActivity implements IaResultHandler {

    private AlarmAdapter m_AlarmAdapter;

    private ListView m_lvAlarm;

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
            //return CaApplication.m_Info.m_alNotice.size();
            return 15;
        }

        @Override
        public Object getItem(int position) {

            //return CaApplication.m_Info.m_alNotice.get(position);
            return position;
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
                holder.m_btnAlarmExecute.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                            Intent it = new Intent(ActivityAlarmList.this, ActivityAlarm.class);
                            startActivity(it);
                    }
                });


                convertView.setTag(holder);
            }
            else {
                holder = (AlarmViewHolder) convertView.getTag();
            }
            /*

            final CaNotice notice = CaApplication.m_Info.m_alNotice.get(position);

            if (notice.m_bTop) {
                holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_notice_top));
            }
            else {
                holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_notice_normal));
            }

            if (notice.m_nNoticeType==1) {
                holder.m_ivNoticeType.setImageDrawable(getDrawable(R.drawable.notice_site));
            }
            else {
                holder.m_ivNoticeType.setImageDrawable(getDrawable(R.drawable.notice_eg));
            }

            holder.m_tvTitle.setText(notice.m_strTitle);
            holder.m_tvTimeCreated.setText(notice.getTimeCreated());

            if (notice.m_bRead) holder.m_ivNew.setVisibility(View.INVISIBLE);
            else holder.m_ivNew.setVisibility(View.VISIBLE);

             */

            return convertView;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        prepareDrawer();

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
    public void onResult(CaResult mdlResult) {

    }
}