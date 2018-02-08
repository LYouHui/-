package com.example.schoolpet;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.socketio.callback.StringCallback;

/**
 * Created by 林尤辉 on 2017/4/13.
 */

public class User extends BmobUser {
    private String trueName;
    private String petName;
    private String hometown;
    private String dormitory;
    private String roomNumber;
    private String department;
    private Integer health;
    private Integer mood;
    private Boolean isEvenHungry;
    private Boolean isEvenEating;
    private Integer imageId;
    private String location;
    private String objectId1;

    User(String name,String department){
        trueName=name;
        this.department=department;
        this.imageId=R.mipmap.head;
    }
    User(String name,String department,String objectId){
        trueName=name;
        this.department=department;
        this.imageId=R.mipmap.head;
        this.objectId1=objectId;
    }
    User(){

    }

    public String gettureName() {
        return this.trueName;
    }

    public void settureName(String tureName) {
        this.trueName = tureName;
    }

    public String getpetName() {
        return petName;
    }

    public void setpetName(String petName) {
        this.petName = petName;
    }

    public void sethometown(String hometown) {
        this.hometown = hometown;
    }

    public String gethometown() {
        return this.hometown;
    }

    public String getdormitory() {
        return this.dormitory;
    }

    public void setdormitory(String dormitory) {
        this.dormitory = dormitory;
    }

    public String getroomNumber() {
        return this.roomNumber;
    }

    public void setroomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getdepartment() {
        return this.department;
    }

    public void setdepartment(String department) {
        this.department = department;
    }

    public Integer getHealth(){
        return health;
    }

    public void setHealth(Integer number){
        health=number;
    }

    public void setMood(Integer number){
        mood=number;
    }

    public Integer getMood(){
        return mood;
    }

    public void setIsEvenHungry(Boolean number){
        this.isEvenHungry=number;
    }

    public Boolean getIsEvenHungry(){
        return isEvenHungry;
    }

    public void setIsEvenEating(Boolean number){
        this.isEvenEating=number;
    }

    public Boolean getIsEvenEating(){
        return isEvenEating;
    }

    public void setImageId(Integer imageId){
        this.imageId=imageId;
    }

    public int getImageId(){
        return imageId;
    }

    public void setLocation(String location){ this.location=location; }

    public String getLocation(){ return location; }

    public  String getObjectId1(){ return objectId1;}
}
