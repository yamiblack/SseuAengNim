package com.jaktongdan.android.sseuaengnim.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jaktongdan.android.sseuaengnim.ui.timer.StudyTimerFragment;
import com.jaktongdan.android.sseuaengnim.ui.timer.TestTimerFragment;

import java.util.ArrayList;
import java.util.List;

public class TimerPagerAdapter extends FragmentPagerAdapter {

    private int pageCount;

    public TimerPagerAdapter(@NonNull FragmentManager fm, int pageCount) {
        super(fm);
        this.pageCount = pageCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StudyTimerFragment studyTimerFragment = new StudyTimerFragment();
                return studyTimerFragment;

            case 1:
                TestTimerFragment testTimerFragment = new TestTimerFragment();
                return testTimerFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}
