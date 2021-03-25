package com.enernet.eg.building.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.enernet.eg.building.R;

public class ActivityHome extends BaseActivity {

    private SavingAdapter m_SavingAdapter;

    private class SavingViewHolder {
        public ConstraintLayout m_clAreaRoot;
        public TextView m_tvTitle;
        public TextView m_tvTime;
        public TextView m_tvUsageGoal;
        public TextView m_tvUsageToday;
        public ListView m_lvSavingCheckList;
        public TextView m_tvSavingResult;
    }

    private class SavingAdapter extends BaseAdapter {

        public SavingAdapter() {
            super();
        }

        @Override
        public int getCount() {
            //return CaApplication.m_Info.m_alAlarm.size();
            return 1;
        }

        @Override
        public Object getItem(int position) {
            //return CaApplication.m_Info.m_alAlarm.get(position);
            return position;
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
                holder.m_lvSavingCheckList=convertView.findViewById(R.id.lv_saving_check_list);
                holder.m_tvSavingResult=convertView.findViewById(R.id.tv_saving_result);

                convertView.setTag(holder);
            }
            else {
                holder = (SavingViewHolder) convertView.getTag();
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
        setContentView(R.layout.activity_home);

        ListView listView = (ListView) findViewById(R.id.lv_saving_list);
        //ListAdapter listAdapter = listView.getAdapter();
        m_SavingAdapter= new SavingAdapter();
        listView.setAdapter(m_SavingAdapter);

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


        prepareDrawer();


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
        finishAffinity();
        System.runFinalization();
        System.exit(0);

    }





}


