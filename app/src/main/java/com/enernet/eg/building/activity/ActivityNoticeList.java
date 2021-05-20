package com.enernet.eg.building.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaEngine;
import com.enernet.eg.building.CaInfo;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.ListViewInfinite;
import com.enernet.eg.building.model.CaNotice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityNoticeList extends BaseActivity implements IaResultHandler, ListViewInfinite.ListenerInfinite {

    private ListViewInfinite m_lvNotice;

    private NoticeAdapter m_NoticeAdapter;

    private ActivityNotice m_ActivityNotice;

    private class NoticeViewHolder {
        public ConstraintLayout m_clAreaRoot;
        public TextView m_tvNoticeTitle;
        public TextView m_tvTimeCreated;
        public TextView m_tvNoticeType;
        public ImageView m_ivNoticeType;
        public ImageView m_ivRightArrow;
        public ImageView m_ivNew;
    }

    private class NoticeAdapter extends BaseAdapter {

        public NoticeAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return CaApplication.m_Info.m_alNotice.size();
            //return 15;
        }

        @Override
        public Object getItem(int position) {

            return CaApplication.m_Info.m_alNotice.get(position);
            //return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NoticeViewHolder holder;
            if (convertView == null) {
                holder = new NoticeViewHolder();

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_notice, null);

                holder.m_clAreaRoot = convertView.findViewById(R.id.area_root);
                holder.m_tvNoticeTitle = convertView.findViewById(R.id.tv_notice_title);
                holder.m_tvNoticeType = convertView.findViewById(R.id.tv_notice_type);
                holder.m_tvTimeCreated=convertView.findViewById(R.id.tv_notice_time_created);
                holder.m_ivNoticeType=convertView.findViewById(R.id.iv_notice_type);
                holder.m_ivRightArrow=convertView.findViewById(R.id.iv_right_arrow);
                holder.m_ivRightArrow.setImageDrawable(getDrawable(R.drawable.arrow_right_hollow));
                holder.m_ivNew=convertView.findViewById(R.id.iv_new);
                holder.m_ivNew.setImageDrawable(getDrawable(R.drawable.new_icon));

                convertView.setTag(holder);
            }
            else {
                holder = (NoticeViewHolder) convertView.getTag();
            }


            final CaNotice notice = CaApplication.m_Info.m_alNotice.get(position);

            if (notice.m_bTop) {
                holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_notice_top));
            }
            else {
                holder.m_clAreaRoot.setBackground(getDrawable(R.drawable.shape_round_corner_notice_normal));
            }

            if (notice.m_nWriterType==1) {
                holder.m_ivNoticeType.setImageDrawable(getDrawable(R.drawable.notice_site));
                holder.m_tvNoticeType.setText("아파트관리실");
            }
            else if (notice.m_nWriterType==2){
                holder.m_ivNoticeType.setImageDrawable(getDrawable(R.drawable.notice_eg));
                holder.m_tvNoticeType.setText("에너넷");

            }
            else {
                holder.m_ivNoticeType.setImageDrawable(getDrawable(R.drawable.notice_site));
                holder.m_tvNoticeType.setText("구청관리자");

            }


            holder.m_tvNoticeTitle.setText(notice.m_strTitle);
            holder.m_tvTimeCreated.setText(notice.getTimeCreated());

            if (notice.m_bRead) holder.m_ivNew.setVisibility(View.INVISIBLE);
            else holder.m_ivNew.setVisibility(View.VISIBLE);



            return convertView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        prepareDrawer();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String getTime = sdf.format(date);


        CaApplication.m_Engine.GetBldNoticeList(CaApplication.m_Info.m_nSeqAdmin, getTime, 10, this , this);

        m_lvNotice = findViewById(R.id.lv_notice_list);
        TextView tvEmpty = findViewById(R.id.tv_empty2);
        m_lvNotice.setEmptyView(tvEmpty);

        final ActivityNoticeList This=this;

        m_lvNotice.setLoadingView(R.layout.loading_layout);
        m_lvNotice.setListener(this);

        m_NoticeAdapter=new NoticeAdapter();
        m_lvNotice.setAdapter(m_NoticeAdapter);

        m_lvNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ListNotice", "Item clicked, pos="+position);


                CaNotice notice=CaApplication.m_Info.m_alNotice.get(position);
                notice.m_bRead=true;
                notice.m_bReadStateChanged=true;
                notice.m_dtRead= Calendar.getInstance().getTime();
                m_NoticeAdapter.notifyDataSetChanged();

                Intent it = new Intent(This, ActivityNotice.class);
                it.putExtra("position", position);

                startActivity(it);
            }
        });
    }

    public void setNoticeReadStateToDb() {
        String strSeqNoticeList = CaApplication.m_Info.getNoticeReadListString();
        if (strSeqNoticeList.isEmpty()) {
            finish();
        }
        else {
            CaApplication.m_Engine.SetBldNoticeListAsRead(CaApplication.m_Info.m_nSeqAdmin, strSeqNoticeList, this, this);
            Log.i("NoticeList", "setBldNoticeListAsRead 실행");
        }
    }

    @Override
    public void onBackPressed() {
        if (m_Drawer.isDrawerOpen()) {
            m_Drawer.closeDrawer();
        }
        else {
            setNoticeReadStateToDb();
            finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        setNoticeCount();

        m_NoticeAdapter.notifyDataSetChanged();
    }

    public void setNoticeCount() {
        TextView tvNoticeCount=findViewById(R.id.tv_notice_count);
        String strNoticeCount="* 총 "+CaApplication.m_Info.m_alNotice.size()+" 건";
        //String strNoticeCount="* 총 5건";
        tvNoticeCount.setText(strNoticeCount);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                setNoticeReadStateToDb();
                finish();
            }
            break;

            case R.id.btn_menu: {
                m_Drawer.openDrawer();
                setNoticeReadStateToDb();
            }
            break;

        }
    }


    @Override
    public void onResult(CaResult Result) {

        if (Result.object==null) {
            Toast.makeText(getApplicationContext(),"Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.CB_GET_BLD_NOTICE_LIST: {
                Log.i("NoticeList", "Result of GetNoticeList received...");

                try {
                    JSONObject jo = Result.object;
                    JSONArray jaTop = jo.getJSONArray("notice_top_list");
                    JSONArray jaNormal = jo.getJSONArray("notice_list");

                    Log.i("NoticeList", "jaNormal length="+jaNormal.length());

                    if (jaNormal.length()==0) {
                        m_lvNotice.m_bNoMoreData=true;
                    }

                    CaApplication.m_Info.setNoticeList(jaTop, jaNormal);
                    setNoticeCount();

                    m_lvNotice.onDataAppended();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            case CaEngine.CB_SET_BLD_NOTICE_AS_READ: {


                CaApplication.m_Engine.GetUnreadBldNoticeCount(CaApplication.m_Info.m_nSeqAdmin, this,this);
            }

            case CaEngine.CB_GET_UNREAD_BLD_NOTICE_COUNT: {

                try {
                    JSONObject jo = Result.object;
                    CaApplication.m_Info.m_nUnreadNoticeCount = jo.getInt("count_unread");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            default: {
                Log.i("NoticeList", "Unknown type result received : " + Result.m_nCallback);
            }
            break;

        } // end of switch

    }

    @Override
    public void onNeedLoadData() {
        Log.i("NoticeList", "onNeedLoadData called...");

        String strTimeMax=CaApplication.m_Info.m_dfyyyyMMddhhmmss.format(CaApplication.m_Info.m_dtNoticeCreatedMaxForNextRequest);

        CaApplication.m_Engine.GetBldNoticeList(CaApplication.m_Info.m_nSeqAdmin, strTimeMax, CaInfo.DEFAULT_REQUEST_NOTICE_COUNT, this, this);
    }
}