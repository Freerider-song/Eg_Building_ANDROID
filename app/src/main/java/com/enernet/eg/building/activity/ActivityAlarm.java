package com.enernet.eg.building.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaEngine;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.model.CaAct;
import com.enernet.eg.building.model.CaActHistory;
import com.enernet.eg.building.model.CaPlan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityAlarm extends BaseActivity implements IaResultHandler {

    private Button m_BtnBack;
    private Button m_BtnSavingExecute;

    private View.OnClickListener m_ClickListenerBack;
    private View.OnClickListener m_ClickListenerExecute;

    private SavingCheckAdapter m_SavingCheckAdapter;

    private CaPlan plan;

    SimpleDateFormat myyyyMMddFormat = new SimpleDateFormat("yyyyMMdd");

    Calendar calToday = Calendar.getInstance();
    String m_dtToday = myyyyMMddFormat.format(calToday.getTime());


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


            boolean flag = false;

            for(int i=0;i<act.m_alActHistory.size();i++){
                CaActHistory actHistory = act.m_alActHistory.get(i);

                if(m_dtToday.equals(myyyyMMddFormat.format(actHistory.m_dtBegin))){
                    holder.m_CheckBox.setChecked(true);
                    flag = true;
                    break;
                }
            }

            if(flag==false) holder.m_CheckBox.setChecked(false);


            holder.m_CheckBox.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox)v).isChecked()) {
                        CaApplication.m_Engine.SetSaveActBegin(act.m_nSeqAct,CaApplication.m_Info.m_nSeqAdmin, m_dtToday, getApplicationContext(),ActivityAlarm.this);
                    } else {
                        // TODO : CheckBox is unchecked.
                    }
                }
            }) ;

            if(holder.m_CheckBox.isChecked()){
                holder.m_CheckBox.setClickable(false);
            }



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
                finish();

                AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityAlarm.this);
                dlg.setMessage("절감 조치가 실행되었습니다.");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.show();

            }
            break;


            default:
                //throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @Override
    public void onResult(CaResult Result) {
        if (Result.object==null) {
            Toast.makeText(getApplicationContext(), "Check Network...", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.CB_SET_SAVE_ACT_BEGIN: {

                Log.i("Alarm", "Result of SEtSaveActBegin received...");

                try {

                    JSONObject jo = Result.object;
                    int nResultCode = jo.getInt("result_code");
                    if (nResultCode == 0) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityAlarm.this);
                        dlg.setMessage("이미 실행된 절감 조치입니다.");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dlg.show();

                    } /*else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityAlarm.this);
                        dlg.setMessage("절감 조치가 실행되었습니다.");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();
                    }*/



                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            break;


            case CaEngine.CB_SET_SAVE_ACT_END: {
                Log.i("Alarm", "Result of SetSaveActEnd received...");

            }
            break;


            default: {
                Log.i("Alarm", "Unknown type result received : " + Result.m_nCallback);
            }
            break;

        } // end of switch

    }
}