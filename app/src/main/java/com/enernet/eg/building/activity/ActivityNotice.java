package com.enernet.eg.building.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;

public class ActivityNotice extends Dialog implements IaResultHandler {

    private Button m_BtnBack;

    private View.OnClickListener m_ClickListenerBack;

    public ActivityNotice(Context context, View.OnClickListener ClickListenerBack) {
        super(context);

        m_ClickListenerBack = ClickListenerBack;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setCancelable(false);
        getWindow().setGravity(Gravity.CENTER);

        setContentView(R.layout.activity_notice);

        m_BtnBack = findViewById(R.id.btn_back);

        if (m_ClickListenerBack != null){
            m_BtnBack.setOnClickListener(m_ClickListenerBack);
        }
    }

    @Override
    public void onResult(CaResult Result) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}