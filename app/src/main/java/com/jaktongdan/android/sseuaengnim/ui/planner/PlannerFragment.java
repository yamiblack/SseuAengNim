package com.jaktongdan.android.sseuaengnim.ui.planner;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jaktongdan.android.sseuaengnim.AddPlanActivity;
import com.jaktongdan.android.sseuaengnim.R;

public class PlannerFragment extends Fragment {

    private PlannerViewModel plannerViewModel;
    private Dialog dDayDialog;

    Button btnAddDDay;

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

        dDayDialog = new Dialog(getActivity());
        dDayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dDayDialog.setContentView(R.layout.dialog_add_dday);
        btnAddDDay = (Button) dDayDialog.findViewById(R.id.btn_addDDay);

        btnAddDDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "성공", Toast.LENGTH_SHORT).show();
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
//                startActivity(new Intent(getActivity(), AddDDayPopupActivity.class));
//                break;
                dDayDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}