package com.jaktongdan.android.sseuaengnim.ui.planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jaktongdan.android.sseuaengnim.AddDDayPopupActivity;
import com.jaktongdan.android.sseuaengnim.AddPlanActivity;
import com.jaktongdan.android.sseuaengnim.R;

public class PlannerFragment extends Fragment {

    private PlannerViewModel plannerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        plannerViewModel =
                new ViewModelProvider(this).get(PlannerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_planner, container, false);
        plannerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        setHasOptionsMenu(true);
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
                startActivity(new Intent(getActivity(), AddDDayPopupActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}