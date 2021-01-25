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
import androidx.recyclerview.widget.RecyclerView;

import com.jaktongdan.android.sseuaengnim.AddPlanActivity;
import com.jaktongdan.android.sseuaengnim.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    private RecyclerView recyclerView;

    private ArrayList<>

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

        sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();

        tvTodayDate.setText(calculateToday());

        dDay = sharedPreferences.getString("dDay", defaultDDay);

        try {
            if (calculateDDay(calculateToday(), dDay) <= 0) {
                tvDDay.setText(" ");
            } else {
                tvDDay.setText("(D - " + String.valueOf(calculateDDay(calculateToday(), dDay)) + ")");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


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
                    if (calculateDDay(date, dDay) <= 0) {
                        tvDDay.setText(" ");
                    } else {
                        tvDDay.setText("(D - " + String.valueOf(calculateDDay(date, dDay)) + ")");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


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
                if(dDay.equals("1900.01.01")) {
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