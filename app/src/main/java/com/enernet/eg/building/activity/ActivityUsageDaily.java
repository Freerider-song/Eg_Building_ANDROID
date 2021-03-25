package com.enernet.eg.building.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.enernet.eg.building.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ActivityUsageDaily extends BaseActivity {

    private LineChart chart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_daily);


        chart = findViewById(R.id.line_chart);

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));
        }

        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        // set data
        chart.setData(data);

        chart.setScrollContainer(true);
        chart.setScaleMinima((float) data.getDataSetCount() / 5f,1f);
        chart.animateY(2000);
    }
}