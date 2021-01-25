package com.jaktongdan.android.sseuaengnim.model;

public class TestTimerData implements Comparable<TestTimerData> {
    private String userId;
    private long currentTime;
    private String TestTimerName;
    private String TestTimerTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getTestTimerName() {
        return TestTimerName;
    }

    public void setTestTimerName(String testTimerName) {
        TestTimerName = testTimerName;
    }

    public String getTestTimerTime() {
        return TestTimerTime;
    }

    public void setTestTimerTime(String testTimerTime) {
        TestTimerTime = testTimerTime;
    }

    @Override
    public int compareTo(TestTimerData testTimerData) {
        if (this.currentTime < testTimerData.getCurrentTime()) {
            return -1;
        } else if (this.currentTime > testTimerData.getCurrentTime()) {
            return 1;
        }
        return 0;
    }
}
