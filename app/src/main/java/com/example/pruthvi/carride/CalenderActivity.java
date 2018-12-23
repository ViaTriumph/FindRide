package com.example.pruthvi.carride;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalenderActivity extends AppCompatActivity {

    private CalendarView calender;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        calender=findViewById(R.id.calendarView);

        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                date=sdf.format(c);
                //Toast.makeText(CalenderActivity.this, "Selected :"+date, Toast.LENGTH_SHORT).show();
                Log.d("CalenderActivity", "onSelectedDayChange: "+date);
            }
        });
    }
}
