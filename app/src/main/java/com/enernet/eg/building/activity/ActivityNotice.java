package com.enernet.eg.building.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;
import com.enernet.eg.building.model.CaNotice;

public class ActivityNotice extends BaseActivity implements IaResultHandler {

    private Button m_BtnBack;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //PopUp의 Title을 제거
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notice);

        Intent it=getIntent();
        int position=it.getIntExtra("position",-1);

        if (position>=0) {
            CaNotice notice= CaApplication.m_Info.m_alNotice.get(position);

            TextView tvTitle=findViewById(R.id.tv_title);
            tvTitle.setText(notice.m_strTitle);

            TextView tvWriter=findViewById(R.id.tv_writer);
            ImageView ivNoticeType=findViewById(R.id.iv_noti_type);

            if (notice.m_nWriterType==3) {
                tvWriter.setText("에너넷");
                ivNoticeType.setImageDrawable(getDrawable(R.drawable.notice_eg));

            }
            else {
                tvWriter.setText("관리사무실");
                ivNoticeType.setImageDrawable(getDrawable(R.drawable.notice_site));
            }

            TextView tvTimeCreated=findViewById(R.id.tv_notice_time_created);
            tvTimeCreated.setText(notice.getTimeCreated());

            TextView tvContent=findViewById(R.id.tv_content);
            tvContent.setText(Html.fromHtml(notice.m_strContent));
            // a link click
            tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {

                finish();
            }
            break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onResult(CaResult Result) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}