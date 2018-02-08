package com.example.schoolpet;

import cn.bmob.v3.BmobObject;

/**
 * Created by 林尤辉 on 2017/7/6.
 */

public class AchievementDatebase extends BmobObject {
    private double studyTime;
    private double sportTime;
    private User user;
    private boolean isSport;
    private boolean isStudy;
    private int before;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public double getSportTime() {
        return sportTime;
    }

    public double getStudyTime() {
        return studyTime;
    }

    public void setSportTime(double sportTime) {
        this.sportTime = sportTime;
    }

    public void setStudyTime(double studyTime) {
        this.studyTime = studyTime;
    }

    public boolean isSport() {
        return isSport;
    }

    public boolean isStudy() {
        return isStudy;
    }

    public void setIsSport(boolean sport) {
        isSport = sport;
    }

    public void setIsStudy(boolean study) {
        isStudy = study;
    }

    public int getBefore() {
        return before;
    }

    public void setBefore(int before) {
        this.before = before;
    }
}
