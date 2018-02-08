package com.example.schoolpet;

import cn.bmob.v3.BmobObject;

/**
 * Created by 林尤辉 on 2017/4/25.
 */

/*
消息类型：-2 接受请求
          -1 拒绝请求
          0 好友请求
          1 借书请求
          2 约吃饭
          3 约自习
          4 约运动
          5 约游戏
          6 约外出
 */
public class MessageDatebase extends BmobObject {
    private Integer messageType;
    private User sender;
    private User getter;
    private OldBookDatebase oldBookDatebase;
    private String time;
    private String place;

    MessageDatebase(){}
    MessageDatebase(Integer messageType, User sender, User getter,OldBookDatebase other){
        this.messageType=messageType;
        this.sender=sender;
        this.getter=getter;
        this.oldBookDatebase=other;
    }
    MessageDatebase(Integer messageType, User sender, User getter){
        this.messageType=messageType;
        this.sender=sender;
        this.getter=getter;
    }

    MessageDatebase(Integer messageType, User sender, User getter,String time,String place){
        this.messageType=messageType;
        this.sender=sender;
        this.getter=getter;
        this.time=time;
        this.place=place;
    }

    public String getPlace() {
        return place;
    }

    public String getTime() {
        return time;
    }

    public Integer getMessageType(){
        return messageType;
    }
    public User getSender(){
        return sender;
    }
    public User getGetter(){
        return getter;
    }
    public OldBookDatebase getOldBookDatebase() { return oldBookDatebase; }
}
