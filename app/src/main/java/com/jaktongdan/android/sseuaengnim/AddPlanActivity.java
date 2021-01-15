package com.jaktongdan.android.sseuaengnim;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPlanActivity extends AppCompatActivity {

    EditText etPlanTitle;
    TextView tvPlanDate;
    Button btnSelectDate;
    EditText etReviewDate;
    TextView tvEndDate;
    Button btnSelectEndDate;

    Calendar calendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener planDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updatePlanDateLabel();
        }
    };

    DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEndDateLabel();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_add);
        getSupportActionBar().setTitle("계획 추가");

        etPlanTitle = (EditText) findViewById(R.id.et_planTitle);
        tvPlanDate = (TextView) findViewById(R.id.tv_planDate);
        btnSelectDate = (Button) findViewById(R.id.btn_selectDate);
        etReviewDate = (EditText) findViewById(R.id.et_reviewDate);
        tvEndDate = (TextView) findViewById(R.id.tv_endDate);
        btnSelectEndDate = (Button) findViewById(R.id.btn_selectEndDate);

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddPlanActivity.this, planDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSelectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddPlanActivity.this, endDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_planner_add,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        return super.onOptionsItemSelected(item);
    }

    private void updatePlanDateLabel() {
        String myFormat = "yyyy.MM.dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.KOREA);

        tvPlanDate = (TextView) findViewById(R.id.tv_planDate);
        tvPlanDate.setText("계획 날짜 : " + simpleDateFormat.format(calendar.getTime()));
    }

    private void updateEndDateLabel() {
        String myFormat = "yyyy.MM.dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.KOREA);

        tvEndDate = (TextView) findViewById(R.id.tv_endDate);
        tvEndDate.setText("복습 종료 날짜 : " + simpleDateFormat.format(calendar.getTime()));
    }
}
