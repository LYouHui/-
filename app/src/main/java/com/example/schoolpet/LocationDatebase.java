package com.example.schoolpet;

import cn.bmob.v3.BmobObject;

/**
 * Created by 林尤辉 on 2017/4/25.
 * 为所有用户集中用户信息，调取位置进数据库，用户监听Location表，用户位置改变则改变Location表，进而通知所有好友。
 */

public class LocationDatebase extends BmobObject {
    private double Latitude;
    private double Longitude;
    private User user;

    public void setLocation(double latitude,double longitude){
        this.Latitude=latitude;
        this.Longitude=longitude;
    }
    public void setUser(User mUser){
        user=mUser;
    }
    public double getLatitude(){
        return Latitude;
    }
    public double getLongitude(){
        return Longitude;
    }
    public User getUser(){
        return user;
    }
}
