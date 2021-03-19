package com.enernet.eg.building.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.enernet.eg.building.EgYearMonthDatePicker;
import com.enernet.eg.building.R;

public class ActivitySaving extends AppCompatActivity {

    private Button btnSelectTime;
    private Button btnSelectTime2;

    private DatePickerDialog.OnDateSetListener callbackMethod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

        this.InitializeView();
        this.InitializeListener();
    }

    public void InitializeView()
    {
        btnSelectTime = (Button)findViewById(R.id.btn_select_time);
        btnSelectTime2 = (Button)findViewById(R.id.btn_select_time);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                btnSelectTime.setText(year + "-" + monthOfYear + "-" + dayOfMonth + "-");
                btnSelectTime2.setText(year + "=" + monthOfYear + "-" + dayOfMonth + "-");
            }
        };
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_select_time: {
                DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 3, 1);

                dialog.show();

            }

            case R.id.btn_select_time2: {
                DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 3, 19);

                dialog.show();
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}