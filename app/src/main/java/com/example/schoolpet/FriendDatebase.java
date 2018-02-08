package com.example.schoolpet;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.datatype.a.I;

/**
 * Created by 林尤辉 on 2017/4/25.
 * 先为每位用户创建对应的本人账户，账户内包含一对多的好友关系，其关联的是用户。
 */

public class FriendDatebase extends BmobObject{
    private User myUser;
    private BmobRelation friend;

    FriendDatebase(){}
    FriendDatebase(User user,BmobRelation friend){
        this.myUser=user;
        this.friend=friend;
    }

    public void setMyUser(User myUser){
        this.myUser=myUser;
    }
    public void setFriend(BmobRelation friend){
        this.friend=friend;
    }
    public User getMyUser(){
        return myUser;
    }
    public BmobRelation getFriend(){
        return friend;
    }
}
