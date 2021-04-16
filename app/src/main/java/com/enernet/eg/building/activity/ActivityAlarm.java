package com.enernet.eg.building.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.model.CaAct;
import com.enernet.eg.building.model.CaPlan;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ActivityAlarm extends BaseActivity implements IaResultHandler {

    private Button m_BtnBack;
    private Button m_BtnSavingExecute;

    private View.OnClickListener m_ClickListenerBack;
    private View.OnClickListener m_ClickListenerExecute;

    private SavingCheckAdapter m_SavingCheckAdapter;

    private CaPlan plan;


    private class SavingCheckViewHolder {
        public CheckBox m_CheckBox;
    }

    private class SavingCheckAdapter extends BaseAdapter {

        //ArrayList<CaAct> m_lvAct;

        public SavingCheckAdapter() {
            super();
        }

        @Override
        public int getCount() {
            //return CaApplication.m_Info.m_alAlarm.size();
            return plan.m_alAct.size();
        }

        @Override
        public Object getItem(int position) {
            return plan.m_alAct.get(position);
            //return position;
            //return position;

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

            final CaAct act = plan.m_alAct.get(position);
            holder.m_CheckBox.setText(act.m_strActContent);

            if(holder.m_CheckBox.isChecked()){
                holder.m_CheckBox.setClickable(false);
            }

            if (act.m_alActHistory.isEmpty()){
                holder.m_CheckBox.setChecked(false);
            }
            else{holder.m_CheckBox.setChecked(true);}


            return convertView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //PopUp의 Title을 제거
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alarm);

        Intent it = getIntent();
        //int nSeqMeter = it.getIntExtra("seq_meter", -1);
        int nSeqPlanElem = it.getIntExtra("seq_plan_elem", -1);
        if (nSeqPlanElem>0){
            for(int i=0; i<CaApplication.m_Info.m_alPlan.size(); i++){

                if(CaApplication.m_Info.m_alPlan.get(i).m_nSeqPlanElem==nSeqPlanElem){
                    plan = CaApplication.m_Info.m_alPlan.get(i);

                    TextView m_tvInstrument = findViewById(R.id.tv_instrument);
                    TextView m_tvSavingTime = findViewById(R.id.tv_saving_time);
                    TextView m_tvSavingGoal = findViewById(R.id.tv_saving_goal);

                    m_tvInstrument.setText(plan.m_strMeterDescr);
                    m_tvSavingTime.setText("절감 시간  " + plan.m_nHourFrom +"시 ~ " +plan.m_nHourTo +"시");
                    m_tvSavingGoal.setText("절감 목표  "+plan.m_nPercentToSave+ " %  ( " + CaApplication.m_Info.m_dfKwh.format(plan.m_dKwhPlan) + " kWh )");

                    initListView();

                    break;
                }
            }
        }


    }

    public void initListView() {


        ListView listView = (ListView) findViewById(R.id.lv_saving_check);

        TextView tvEmpty = findViewById(R.id.tv_empty);
        listView.setEmptyView(tvEmpty);
        m_SavingCheckAdapter= new SavingCheckAdapter();

        listView.setAdapter(m_SavingCheckAdapter);

        if (m_SavingCheckAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < m_SavingCheckAdapter.getCount(); i++) {
            View listItem = m_SavingCheckAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (m_SavingCheckAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onBackPressed() {
            finish();

    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
            }
            break;

            case R.id.btn_saving_execute: {
                //m_Drawer.openDrawer();
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