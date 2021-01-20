package com.jaktongdan.android.sseuaengnim.ui.timer;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jaktongdan.android.sseuaengnim.AddPlanActivity;
import com.jaktongdan.android.sseuaengnim.R;
import com.jaktongdan.android.sseuaengnim.TimerHistoryActivity;

public class TestTimerFragment extends Fragment {

    Button btnAddTestTimer;

    Dialog addTimerDialog;
    Button btnAddNewTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_timer_test, container, false);
        setHasOptionsMenu(true);

        btnAddTestTimer = (Button) root.findViewById(R.id.btn_addTestTimer);

        addTimerDialog = new Dialog(getActivity());
        addTimerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addTimerDialog.setContentView(R.layout.dialog_add_timer_test);
        btnAddNewTimer = (Button) addTimerDialog.findViewById(R.id.btn_addNewTimer);

        btnAddTestTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTimerDialog.show();
            }
        });

        btnAddNewTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "타이머 성공", Toast.LENGTH_SHORT).show();
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
//                startActivity(new Intent(getActivity(), AddPlanActivity.class));
                Toast.makeText(getActivity(), "성공", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
