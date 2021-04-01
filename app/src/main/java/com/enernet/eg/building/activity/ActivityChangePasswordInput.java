package com.enernet.eg.building.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.enernet.eg.building.CaApplication;
import com.enernet.eg.building.CaPref;
import com.enernet.eg.building.CaResult;
import com.enernet.eg.building.IaResultHandler;
import com.enernet.eg.building.R;

public class ActivityChangePasswordInput extends BaseActivity implements IaResultHandler {

    private EditText m_etUserId;
    private EditText m_etPasswordNew;
    private EditText m_etPasswordConfirm;
    private String m_strUserId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_input);

        m_strUserId=getIntent().getStringExtra("user_id");
        m_etUserId.setText(m_strUserId);

        CaPref pref = new CaPref(getApplicationContext());
        pref.setValue(CaPref.PREF_MEMBER_ID, m_strUserId);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_password: {
                Log.i("ChangePassword", "btn_change_password clicked");

                EditText etPasswordNew=findViewById(R.id.et_password_new);
                EditText etPasswordConfirm=findViewById(R.id.et_password_confirm);

                String strPasswordNew=etPasswordNew.getText().toString();
                String strPasswordConfirm=etPasswordConfirm.getText().toString();

                String strMessage="";

                if (strPasswordNew.isEmpty() || strPasswordConfirm.isEmpty()) {
                    strMessage="새 비밀번호와 확인 비밀번호를 모두 입력해 주세요";
                }
                else {
                    if (!strPasswordNew.equals(strPasswordConfirm)) {
                        strMessage="새 비밀번호와 확인 비밀번호가 서로 다릅니다. 다시 입력해 주세요";
                    }
                }

                if (strMessage.isEmpty()) {
                    //CaApplication.m_Engine.ChangeAdminPassword();
                }
                else {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChangePasswordInput.this);
                    dlg.setMessage(strMessage);
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();
                }

                Log.i("ChangePasswordInput", "PasswordNew=["+strPasswordNew+"], PasswordConfirm=["+strPasswordConfirm+"]");
            }
            break;

        }
    }

    @Override
    public void onResult(CaResult Result) {

    }
}