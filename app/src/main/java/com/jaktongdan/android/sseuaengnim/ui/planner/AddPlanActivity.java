package com.jaktongdan.android.sseuaengnim.ui.planner;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaktongdan.android.sseuaengnim.MainActivity;
import com.jaktongdan.android.sseuaengnim.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPlanActivity extends AppCompatActivity {
    private Context context = this;

    private EditText etPlanTitle;
    private TextView tvPlanDate;
    private Button btnSelectDate;
    private EditText etReviewDate;
    private TextView tvEndDate;
    private Button btnSelectEndDate;

    private Calendar calendar = Calendar.getInstance();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

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

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

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

        if(etPlanTitle.getText().toString().length() == 0 || tvPlanDate.getText().toString().length() == 0 || etReviewDate.getText().toString().length() == 0 || tvEndDate.getText().toString().length() == 0) {
            Toast.makeText(context, "양식을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("userId", auth.getCurrentUser().getUid());
            data.put("planTitle", etPlanTitle.getText().toString());
            data.put("planDate", tvPlanDate.getText().toString());
            data.put("reviewCycle", etReviewDate.getText().toString());
            data.put("reviewEndDate", tvEndDate.getText().toString());

            db.collection("PLANNER")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(context, "성공적으로 추가됐습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void updatePlanDateLabel() {
        String myFormat = "yyyy.MM.dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.KOREA);

        tvPlanDate = (TextView) findViewById(R.id.tv_planDate);
        tvPlanDate.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void updateEndDateLabel() {
        String myFormat = "yyyy.MM.dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.KOREA);

        tvEndDate = (TextView) findViewById(R.id.tv_endDate);
        tvEndDate.setText(simpleDateFormat.format(calendar.getTime()));
    }
}
