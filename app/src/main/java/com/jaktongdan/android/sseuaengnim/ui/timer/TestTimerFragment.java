package com.jaktongdan.android.sseuaengnim.ui.timer;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaktongdan.android.sseuaengnim.R;
import com.jaktongdan.android.sseuaengnim.adapter.OnTestTimerItemClickListener;
import com.jaktongdan.android.sseuaengnim.adapter.TestTimerRecyclerViewAdapter;
import com.jaktongdan.android.sseuaengnim.model.TestTimerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestTimerFragment extends Fragment {

    private Timer timer;

    private TextView tvTestTime;
    private Button btnTestTimerPause;
    private Button btnTestTimerReset;

    private boolean isRunning = false;

    private String tempTime, leftTime;
    private long tempHour, tempMin;

    private Dialog addTimerDialog;
    private EditText etTimerTestName;
    private EditText etTimerTestTime;
    private Button btnAddNewTimer;

    private ArrayList<TestTimerData> arrayList;
    private RecyclerView recyclerView;
    private TestTimerRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_timer_test, container, false);
        setHasOptionsMenu(true);

        tvTestTime = (TextView) root.findViewById(R.id.tv_testTime);
        btnTestTimerPause = (Button) root.findViewById(R.id.btn_testTimerPause);
        btnTestTimerReset = (Button) root.findViewById(R.id.btn_testTimerReset);

        addTimerDialog = new Dialog(getActivity());
        addTimerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addTimerDialog.setContentView(R.layout.dialog_add_timer_test);
        etTimerTestName = (EditText) addTimerDialog.findViewById(R.id.et_timerTestName);
        etTimerTestTime = (EditText) addTimerDialog.findViewById(R.id.et_timerTestTime);
        btnAddNewTimer = (Button) addTimerDialog.findViewById(R.id.btn_addNewTimer);

        recyclerView = (RecyclerView) root.findViewById(R.id.rv_testTimer);

        auth = FirebaseAuth.getInstance();

        getData();

        adapter.setOnItemClickListener(new OnTestTimerItemClickListener() {
            @Override
            public void onItemClick(TestTimerRecyclerViewAdapter.ViewHolder holder, View view, int position) {
                btnTestTimerPause.setText("일시정지");
                TestTimerData item = adapter.getItem(position);
                Log.e("test", item.getTestTimerTime());

                if (item.getTestTimerTime().length() == 1) {
                    tempTime = "000" + item.getTestTimerTime() + "00";
                } else if (item.getTestTimerTime().length() == 2) {
                    tempTime = "00" + item.getTestTimerTime() + "00";
                } else if (item.getTestTimerTime().length() == 3) {
                    tempHour = Long.parseLong(item.getTestTimerTime()) / 60;
                    tempMin = Long.parseLong(item.getTestTimerTime()) - (tempHour * 60);

                    if (tempMin < 10) {
                        tempTime = "0" + Long.toString(tempHour) + "0" + Long.toString(tempMin) + "00";
                    } else {
                        tempTime = "0" + Long.toString(tempHour) + Long.toString(tempMin) + "00";
                    }
                }

                startTimer(tempTime);

            }
        });

        btnTestTimerPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvTestTime.getText().toString().equals("00:00:00")) {
                    Toast.makeText(getActivity(), "타이머를 시작해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (isRunning) {
                        leftTime = tvTestTime.getText().toString().substring(0, 2) + tvTestTime.getText().toString().substring(3, 5) + tvTestTime.getText().toString().substring(6, 8);
                        Log.e("left Time", leftTime);
                        timer.cancel();
                        isRunning = false;
                        btnTestTimerPause.setText("다시 시작");
                    } else {
                        startTimer(leftTime);
                        isRunning = true;
                        btnTestTimerPause.setText("일시정지");
                    }
                }

            }
        });

        btnTestTimerReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvTestTime.getText().toString().equals("00:00:00")) {
                    Toast.makeText(getActivity(), "타이머를 시작해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    timer.cancel();
                    tvTestTime.setText("00:00:00");
                }

            }
        });

        btnAddNewTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etTimerTestName.getText().toString().length() == 0 || etTimerTestTime.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "타이머명과 시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(etTimerTestTime.getText().toString()) == 0) {
                    Toast.makeText(getActivity(), "1분 이상의 타이머 시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> data = new HashMap<>();
                    data.put("userID", auth.getCurrentUser().getUid());
                    data.put("currentTime", System.currentTimeMillis());
                    data.put("TestTimerName", etTimerTestName.getText().toString());
                    data.put("TestTimerTime", etTimerTestTime.getText().toString());

                    db.collection("TIMER_TEST")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    addTimerDialog.dismiss();
                                    etTimerTestTime.setText("");
                                    etTimerTestName.setText("");
                                    Toast.makeText(getActivity(), "성공적으로 추가됐습니다.", Toast.LENGTH_SHORT).show();
                                    getData();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_timer_test, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTestTimer:
                addTimerDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData() {

        db = FirebaseFirestore.getInstance();

        arrayList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TestTimerRecyclerViewAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);

        db.collection("TIMER_TEST").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.get("userID").toString().equals(auth.getCurrentUser().getUid().toString())) {
//                            Log.e("writer : ", documentSnapshot.get("userID").toString());
//                            Log.e("current user : ", auth.getCurrentUser().toString());
                            TestTimerData testTimerData = documentSnapshot.toObject(TestTimerData.class);
                            arrayList.add(testTimerData);
                            Collections.sort(arrayList);
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void startTimer(String time) {
        long conversionTime = 0;

        String getHour = time.substring(0, 2);
        String getMin = time.substring(2, 4);
        String getSecond = time.substring(4, 6);

        if (getHour.substring(0, 1) == "0") {
            getHour = getHour.substring(1, 2);
        }

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2);
        }

        conversionTime = Long.valueOf(getHour) * 1000 * 3600 + Long.valueOf(getMin) * 60 * 1000 + Long.valueOf(getSecond) * 1000;

        if (isRunning) {
            timer.cancel();
            timer = new Timer(conversionTime, 1000);
            timer.start();
        } else {
            timer = new Timer(conversionTime, 1000);
            timer.start();
        }
    }

    class Timer extends CountDownTimer {

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            isRunning = true;
            String hour = String.valueOf(millisUntilFinished / (60 * 60 * 1000));

            long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));
            String min = String.valueOf(getMin / (60 * 1000) - Long.parseLong(hour) * 60);

            String second = String.valueOf((getMin % (60 * 1000)) / 1000);

            if (hour.length() == 1) {
                hour = "0" + hour;
            }

            if (min.length() == 1) {
                min = "0" + min;
            }

            if (second.length() == 1) {
                second = "0" + second;
            }

            tvTestTime.setText(hour + ":" + min + ":" + second);
        }

        @Override
        public void onFinish() {
            tvTestTime.setText("00:00:00");
            Toast.makeText(getActivity(), "타이머가 종료됐습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
