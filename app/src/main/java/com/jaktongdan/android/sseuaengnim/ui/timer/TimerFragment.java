package com.jaktongdan.android.sseuaengnim.ui.timer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.jaktongdan.android.sseuaengnim.R;
import com.jaktongdan.android.sseuaengnim.adapter.TimerPagerAdapter;

import java.util.Timer;

public class TimerFragment extends Fragment {

    private TabLayout tlTimer;
    private ViewPager vpTimer;
    private TimerPagerAdapter timerPagerAdapter;
//    private FragmentActivity fragmentActivity;

//    @Override
//    public void onAttach(@NonNull Activity activity) {
//        fragmentActivity = (FragmentActivity) activity;
//        super.onAttach(activity);
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_timer, container, false);

        tlTimer = root.findViewById(R.id.tl_timer);
        vpTimer = (ViewPager) root.findViewById(R.id.vp_timer);
//        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

        tlTimer.addTab(tlTimer.newTab().setText("공부시간 타이머"));
        tlTimer.addTab(tlTimer.newTab().setText("시험 타이머"));

        timerPagerAdapter = new TimerPagerAdapter(getFragmentManager(), tlTimer.getTabCount());
        vpTimer.setAdapter(timerPagerAdapter);

        vpTimer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlTimer));

        tlTimer.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

        return root;
    }
}