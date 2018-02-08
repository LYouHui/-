package com.example.schoolpet;

import android.view.Gravity;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 尤辉 on 2017/3/23.
 */

public class Pet {
    private static int health;
    private static int mood;
    public static boolean isEvenHungry;
    public static boolean isEvenEating;

    Pet(Integer health,Integer mood){
        this.health=health;
        this.mood=mood;
    }

    public static Integer getHealth(){
        Integer number=new Integer(health);
        return number;
    }
    public static Integer getMood(){
        Integer number=new Integer(mood);
        return number;
    }
    public void setHealth(Integer number){
        health=number;
    }
    public void setMood(Integer number){  mood=number;  }
    public void setIsEvenHungry(Boolean number){ isEvenHungry=number; }
    public void setIsEvenEating(Boolean number){ isEvenEating=number; }
    public Boolean getIsEvenHungry(){ return isEvenHungry; };
    public Boolean getIsEvenEating(){ return isEvenEating; };

    public static boolean addHealth(Integer number){
        boolean result=false;
        health+=number;
        if(health>100)
            health=100;
        return result;
    }

    public static boolean reduceHealth(Integer number){
        boolean result=false;
        health-=number;
        if(health<0)
            health=0;
        return result;
    }

    public static boolean addMood(Integer number){
        boolean result=false;
        mood+=number;
        if(mood>100)
            mood=100;
        return result;
    }

    public static boolean reduceMood(Integer number){
        boolean result=false;
        mood-=number;
        if(mood<0)
            mood=0;
        return result;
    }
}
