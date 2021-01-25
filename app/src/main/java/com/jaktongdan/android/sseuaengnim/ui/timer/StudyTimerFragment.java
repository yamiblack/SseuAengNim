package com.jaktongdan.android.sseuaengnim.ui.timer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaktongdan.android.sseuaengnim.AddPlanActivity;
import com.jaktongdan.android.sseuaengnim.R;
import com.jaktongdan.android.sseuaengnim.TimerHistoryActivity;
import com.jaktongdan.android.sseuaengnim.adapter.StudyTimerRecyclerViewAdapter;
import com.jaktongdan.android.sseuaengnim.model.StudyTimerData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StudyTimerFragment extends Fragment {

    private TextView tvStudyTime;
    private Button btnStartStudyTime;
    private Button btnResetStudyTime;
    private Button btnRecordStudyTime;
    private Button btnGoToHistory;

    private String timeResult;
    private Thread timeThread = null;
    private Boolean isRunning = true;
    private int i = 0, buttonState = 0;
    private int hour, min, sec, mSec;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_timer_study, container, false);

        tvStudyTime = (TextView) root.findViewById(R.id.tv_studyTime);
        btnStartStudyTime = (Button) root.findViewById(R.id.btn_startStudyTime);
        btnResetStudyTime = (Button) root.findViewById(R.id.btn_resetStudyTime);
        btnRecordStudyTime = (Button) root.findViewById(R.id.btn_recordStudyTime);
        btnGoToHistory = (Button) root.findViewById(R.id.btn_goToHistory);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnStartStudyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonState == 0) {
                    isRunning = true;
                    timeThread = new Thread(new timeThread());
                    timeThread.start();
                    btnStartStudyTime.setText("일시정지");
                    buttonState++;
                } else {
                    isRunning = false;
                    btnStartStudyTime.setText("시작");
                    buttonState--;
                }
            }
        });

        btnResetStudyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 0;
                tvStudyTime.setText("00:00:00:00");
            }
        });

        btnRecordStudyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> data = new HashMap<>();
                data.put("userId", auth.getCurrentUser().getUid());
                data.put("todayDate", getToday());
                data.put("studyTime", tvStudyTime.getText().toString());
                data.put("currentTime", System.currentTimeMillis());

                db.collection("TIMER_STUDY")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                tvStudyTime.setText("00:00:00:00");
                                Toast.makeText(getActivity(), "성공적으로 기록됐습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        btnGoToHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TimerHistoryActivity.class));

            }
        });

        return root;
    }

    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            mSec = msg.arg1 % 100;
            sec = (msg.arg1 / 100) % 60;
            min = (msg.arg1 / 100) / 60;
            hour = (msg.arg1 / 100) / 360;

            timeResult = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);

            tvStudyTime.setText(timeResult);
        }
    };

    public class timeThread implements Runnable {
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.arg1 = i++;
                handler.sendMessage(msg);
            }
        }
    }

    public static String getToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return simpleDateFormat.format(new Date());
    }
}
