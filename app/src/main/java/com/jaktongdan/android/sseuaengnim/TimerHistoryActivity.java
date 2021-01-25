package com.jaktongdan.android.sseuaengnim;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaktongdan.android.sseuaengnim.adapter.StudyTimerRecyclerViewAdapter;
import com.jaktongdan.android.sseuaengnim.adapter.TestTimerRecyclerViewAdapter;
import com.jaktongdan.android.sseuaengnim.model.StudyTimerData;
import com.jaktongdan.android.sseuaengnim.model.TestTimerData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class TimerHistoryActivity extends AppCompatActivity {

    private Context context = this;

    private ArrayList<StudyTimerData> arrayList;
    private StudyTimerRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_history);
        getSupportActionBar().setTitle("공부시간 기록");

        recyclerView = (RecyclerView) findViewById(R.id.rv_timerHistory);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        arrayList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudyTimerRecyclerViewAdapter(context, arrayList);
        recyclerView.setAdapter(adapter);

        db.collection("TIMER_STUDY").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.get("userId").toString().equals(auth.getCurrentUser().getUid().toString())) {
                            TestTimerData testTimerData = documentSnapshot.toObject(TestTimerData.class);
                            StudyTimerData studyTimerData = documentSnapshot.toObject(StudyTimerData.class);
                            arrayList.add(studyTimerData);
                            Collections.sort(arrayList);
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

}
