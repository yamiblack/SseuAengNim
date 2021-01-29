package com.jaktongdan.android.sseuaengnim.ui.timer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jaktongdan.android.sseuaengnim.R;
import com.jaktongdan.android.sseuaengnim.adapter.TimerPagerAdapter;

public class TimerFragment extends Fragment {

    private TabLayout tlTimer;
    private ViewPager vpTimer;
    private TimerPagerAdapter timerPagerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_timer, container, false);

        tlTimer = root.findViewById(R.id.tl_timer);
        vpTimer = (ViewPager) root.findViewById(R.id.vp_timer);

        tlTimer.addTab(tlTimer.newTab().setText("공부시간 타이머"));
        tlTimer.addTab(tlTimer.newTab().setText("시험 타이머"));


        timerPagerAdapter = new TimerPagerAdapter(getChildFragmentManager(), tlTimer.getTabCount());
        vpTimer.setAdapter(timerPagerAdapter);

        vpTimer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlTimer));

        tlTimer.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpTimer.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                vpTimer.setCurrentItem(tab.getPosition());
            }

        });

        return root;
    }
}