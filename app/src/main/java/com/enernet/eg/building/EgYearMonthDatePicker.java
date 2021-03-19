package com.enernet.eg.building;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.enernet.eg.building.R;
import com.enernet.eg.building.activity.BaseActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EgYearMonthDatePicker extends BaseActivity {

    private int mYear = 0, mMonth = 0, mDay = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eg_year_month_date_picker);

        Calendar calendar = new GregorianCalendar();

        mYear = calendar.get(Calendar.YEAR);

        mMonth = calendar.get(Calendar.MONTH);

        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        DatePicker datePicker = findViewById(R.id.vDatePicker);

        DatePicker.OnDateChangedListener mOnDateChangedListener = new DatePicker.OnDateChangedListener() {

            @Override

            public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {

                mYear = yy;

                mMonth = mm;

                mDay = dd;

            }

        };

        datePicker.init(mYear, mMonth, mDay, mOnDateChangedListener);
    }

    public void OnClick(View v) {

        Intent intent = new Intent();

        intent.putExtra("mYear", mYear);

        intent.putExtra("mMonth", mMonth);

        intent.putExtra("mDay", mDay);

        setResult(RESULT_OK, intent);

        finish();

    }
}