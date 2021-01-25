package com.jaktongdan.android.sseuaengnim.model;

public class StudyTimerData implements Comparable<StudyTimerData> {

    private String userId;
    private String todayDate;
    private String studyTime;
    private long currentTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public int compareTo(StudyTimerData studyTimerData) {
        if (this.currentTime < studyTimerData.getCurrentTime()) {
            return -1;
        } else if (this.currentTime > studyTimerData.getCurrentTime()) {
            return 1;
        }
        return 0;
    }
}
