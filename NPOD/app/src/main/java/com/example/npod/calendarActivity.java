package com.example.npod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class calendarActivity extends AppCompatActivity {
    String calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ImageButton btnnext = findViewById(R.id.buttonnext);
        ImageButton btnprev = findViewById(R.id.btnprev);
        CalendarView cv = findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                 calendar = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(dayOfMonth);

            }
        }); //Set date from calendar
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(calendarActivity.this,MainActivity.class);
                intent.putExtra("data",calendar);
                startActivity(intent);
                finish();
            }
        });     //Move to next screen after date selection
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(calendarActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });     //Move to previous screen
    }
}