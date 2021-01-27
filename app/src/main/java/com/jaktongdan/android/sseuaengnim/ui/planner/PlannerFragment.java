package com.jaktongdan.android.sseuaengnim.ui.planner;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaktongdan.android.sseuaengnim.AddPlanActivity;
import com.jaktongdan.android.sseuaengnim.R;
import com.jaktongdan.android.sseuaengnim.adapter.PlannerRecyclerViewAdapter;
import com.jaktongdan.android.sseuaengnim.adapter.StudyTimerRecyclerViewAdapter;
import com.jaktongdan.android.sseuaengnim.model.PlannerData;
import com.jaktongdan.android.sseuaengnim.model.StudyTimerData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class PlannerFragment extends Fragment {

    private Dialog dDayDialog;
    private CalendarView cvDDay;
    private Button btnAddDDay;

    private CalendarView cvPlannerCalendar;
    private TextView tvTodayDate;
    private TextView tvDDay;
    private TextView tvStudyTime;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<StudyTimerData> studyTimeList;
    private StudyTimerRecyclerViewAdapter studyTimeAdapter;

    private ArrayList<PlannerData> plannerList;
    private PlannerRecyclerViewAdapter plannerAdapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String date, dDay, defaultDDay = "1900.01.01";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_planner, container, false);

        setHasOptionsMenu(true);

        dDayDialog = new Dialog(getActivity());
        dDayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dDayDialog.setContentView(R.layout.dialog_add_dday);
        cvDDay = dDayDialog.findViewById(R.id.cv_dDay);
        btnAddDDay = dDayDialog.findViewById(R.id.btn_addDDay);

        cvPlannerCalendar = root.findViewById(R.id.cv_plannerCalendar);
        tvTodayDate = root.findViewById(R.id.tv_todayDate);
        tvDDay = root.findViewById(R.id.tv_dDay);
        tvStudyTime = root.findViewById(R.id.tv_studyTime);

        recyclerView = root.findViewById(R.id.rv_plannerList);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();

        tvTodayDate.setText(calculateToday());

        //초반 D-Day 설정
        dDay = sharedPreferences.getString("dDay", defaultDDay);

        try {
            if (calculateDDay(calculateToday(), dDay) < 0) {
                tvDDay.setText(" ");
            } else if (calculateDDay(calculateToday(), dDay) == 0) {
                tvDDay.setText("(D-Day)");
            } else {
                tvDDay.setText("(D - " + String.valueOf(calculateDDay(calculateToday(), dDay)) + ")");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //초반 할 일 list
        studyTimeList = new ArrayList<>();
        studyTimeAdapter = new StudyTimerRecyclerViewAdapter(getActivity(), studyTimeList);

        db.collection("TIMER_STUDY").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.get("userId").toString().equals(auth.getCurrentUser().getUid()) &&
                                documentSnapshot.get("todayDate").toString().equals(calculateToday())) {
                            StudyTimerData studyTimerData = documentSnapshot.toObject(StudyTimerData.class);
                            studyTimeList.add(studyTimerData);
                            tvStudyTime.setText("공부 시간 : " + studyTimerData.getStudyTime());
                        }

                    }
                    studyTimeAdapter.notifyDataSetChanged();
                }
            }
        });

        plannerList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        plannerAdapter = new PlannerRecyclerViewAdapter(getActivity(), plannerList);
        recyclerView.setAdapter(plannerAdapter);

        db.collection("PLANNER").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.get("userId").toString().equals(auth.getCurrentUser().getUid().toString()) &&
                                documentSnapshot.get("planDate").equals(calculateToday())) {
                            PlannerData plannerData = documentSnapshot.toObject(PlannerData.class);
                            plannerList.add(plannerData);
                        }
                    }
                    plannerAdapter.notifyDataSetChanged();
                }
            }
        });


        cvPlannerCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month,
                                            int day) {
                if (month + 1 < 10) {
                    if (day < 10) {
                        date = year + ".0" + (month + 1) + ".0" + day;
                    } else {
                        date = year + ".0" + (month + 1) + "." + day;
                    }
                } else {
                    if (day < 10) {
                        date = year + "." + (month + 1) + ".0" + day;
                    } else {
                        date = year + "." + (month + 1) + "." + day;
                    }
                }

                tvTodayDate.setText(date);

                dDay = sharedPreferences.getString("dDay", defaultDDay);

                try {
                    if (calculateDDay(date, dDay) < 0) {
                        tvDDay.setText(" ");
                    } else if (calculateDDay(date, dDay) == 0) {
                        tvDDay.setText("(D-Day)");
                    } else {
                        tvDDay.setText("(D - " + String.valueOf(calculateDDay(date, dDay)) + ")");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                db.collection("TIMER_STUDY").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if (documentSnapshot.get("userId").toString().equals(auth.getCurrentUser().getUid().toString()) &&
                                        documentSnapshot.get("todayDate").toString().equals(date)) {
                                    StudyTimerData studyTimerData = documentSnapshot.toObject(StudyTimerData.class);
                                    studyTimeList.add(studyTimerData);
                                    tvStudyTime.setText("공부 시간 : " + studyTimerData.getStudyTime());
                                } else {
                                    tvStudyTime.setText("기록된 공부 시간이 없습니다.");
                                }

                            }
                            studyTimeAdapter.notifyDataSetChanged();

                        }
                    }
                });

                plannerList = new ArrayList<>();

                db.collection("PLANNER").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if (documentSnapshot.get("userId").toString().equals(auth.getCurrentUser().getUid().toString()) &&
                                        documentSnapshot.get("planDate").equals(date)) {
                                    PlannerData plannerData = documentSnapshot.toObject(PlannerData.class);
                                    plannerList.add(plannerData);
                                } else {
                                    plannerList.clear();
                                }
                            }
                            plannerAdapter.notifyDataSetChanged();
                        }
                    }
                });


            }
        });

        btnAddDDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvDDay.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                        dDay = year + ".0" + (month + 1) + "." + day;
                        editor.putString("dDay", dDay);
                        editor.apply();
                    }
                });
                dDay = sharedPreferences.getString("dDay", defaultDDay);
                if (dDay.equals("1900.01.01")) {
                    Toast.makeText(getActivity(), "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        tvDDay.setText("(D - " + String.valueOf(calculateDDay(date, dDay)) + ")");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dDayDialog.dismiss();

            }
        });


        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_planner, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPlan:
                startActivity(new Intent(getActivity(), AddPlanActivity.class));
                break;
            case R.id.setupPlan:
                dDayDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public Long calculateDDay(String startDate, String endDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date start, end;

        start = simpleDateFormat.parse(String.valueOf(startDate));
        end = simpleDateFormat.parse(String.valueOf(endDate));

        return (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);
    }

    public String calculateToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Calendar time = Calendar.getInstance();
        Date today = null;

        try {
            today = simpleDateFormat.parse(simpleDateFormat.format(time.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String todayDate = simpleDateFormat.format(today);

        return todayDate;
    }

}