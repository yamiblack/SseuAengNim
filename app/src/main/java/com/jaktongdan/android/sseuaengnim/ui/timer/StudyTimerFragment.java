package com.jaktongdan.android.sseuaengnim.ui.timer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jaktongdan.android.sseuaengnim.AddPlanActivity;
import com.jaktongdan.android.sseuaengnim.R;
import com.jaktongdan.android.sseuaengnim.TimerHistoryActivity;

public class StudyTimerFragment extends Fragment {

    Button btnGoToHistory;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_timer_study, container, false);
        btnGoToHistory = (Button) root.findViewById(R.id.btn_goToHistory);

        btnGoToHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TimerHistoryActivity.class));

            }
        });

        return root;
    }
}
