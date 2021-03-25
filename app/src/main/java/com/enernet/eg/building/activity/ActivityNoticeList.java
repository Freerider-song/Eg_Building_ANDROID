package com.enernet.eg.building.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
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

import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.model.ListViewInfinite;

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
        setContentView(R.layout.activity_notice_list);

        prepareDrawer();

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
                /*

                CaNotice notice=CaApplication.m_Info.m_alNotice.get(position);
                notice.m_bRead=true;
                notice.m_bReadStateChanged=true;
                notice.m_dtRead= Calendar.getInstance().getTime();
                m_NoticeAdapter.notifyDataSetChanged();*/

                View.OnClickListener LsnBack=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("BaseActivity", "Yes button clicked...");
                        m_ActivityNotice.dismiss();
                    }
                };


                m_ActivityNotice=new ActivityNotice(This, LsnBack);
                m_ActivityNotice.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode==KeyEvent.KEYCODE_BACK) {
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

                m_ActivityNotice.show();

            }
        });
    }
/*
    public void setNoticeReadStateToDb() {
        String strSeqNoticeList = CaApplication.m_Info.getNoticeReadListString();
        if (strSeqNoticeList.isEmpty()) {
            finish();
        }
        else {
            CaApplication.m_Engine.SetNoticeListAsRead(CaApplication.m_Info.m_nSeqMember, strSeqNoticeList, this, this);
        }
    }*/

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
    public void onResume() {
        super.onResume();

        setNoticeCount();

        m_NoticeAdapter.notifyDataSetChanged();
    }

    public void setNoticeCount() {
        TextView tvNoticeCount=findViewById(R.id.tv_notice_count);
        //String strNoticeCount="* 총 "+CaApplication.m_Info.m_alNotice.size()+" 건";
        String strNoticeCount="* 총 5건";
        tvNoticeCount.setText(strNoticeCount);
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
    public void onResult(CaResult Result) {

    }

    @Override
    public void onNeedLoadData() {
        Log.i("NoticeList", "onNeedLoadData called...");

        //String strTimeMax=CaApplication.m_Info.m_dfyyyyMMddhhmmss.format(CaApplication.m_Info.m_dtNoticeCreatedMaxForNextRequest);

        //CaApplication.m_Engine.GetNoticeList(CaApplication.m_Info.m_nSeqMember, strTimeMax, CaInfo.DEFAULT_REQUEST_NOTICE_COUNT, this, this);
    }
}