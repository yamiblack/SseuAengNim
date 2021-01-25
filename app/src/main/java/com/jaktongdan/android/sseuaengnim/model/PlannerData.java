package com.jaktongdan.android.sseuaengnim.model;

public class PlannerData {

    private String userId;
    private String TodayDate;
    private String planTitle;
    private String planDate;
    private String reviewCycle;
    private String reviewEndDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTodayDate() {
        return TodayDate;
    }

    public void setTodayDate(String todayDate) {
        TodayDate = todayDate;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getReviewCycle() {
        return reviewCycle;
    }

    public void setReviewCycle(String reviewCycle) {
        this.reviewCycle = reviewCycle;
    }

    public String getReviewEndDate() {
        return reviewEndDate;
    }

    public void setReviewEndDate(String reviewEndDate) {
        this.reviewEndDate = reviewEndDate;
    }
}
