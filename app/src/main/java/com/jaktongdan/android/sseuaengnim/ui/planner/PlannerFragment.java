package com.jaktongdan.android.sseuaengnim.ui.planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jaktongdan.android.sseuaengnim.R;

public class PlannerFragment extends Fragment {

    private PlannerViewModel plannerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        plannerViewModel =
                new ViewModelProvider(this).get(PlannerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_planner, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        plannerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}