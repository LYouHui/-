package com.example.schoolpet;

import android.graphics.drawable.AnimationDrawable;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 尤辉 on 2017/3/23.
 */

public class PetState {
    PetPage activity=null;

    static boolean isSport;
    static boolean isStudy;
    static PetAction myState;
    static int before;
    static int time;

    public PetState(PetPage thread){
        this.activity=thread;
    }

    public String getSPetState(){
        return translateState(this.myState);
    }
    public String getChineseState(){ return translateToChinese(this.myState); }
    public PetAction getAPetState(){
        return myState;
    }
    public void setPetState(String petAction){
        myState=translateString(petAction);
    }


    //private PetAction currentState=PetAction.COMMON_STATE;
    public boolean updateState(UserLocation mlocation,PetAction healthState){
        getInformation();
        UserAction ma=null;
        myState=healthState;
        switch(mlocation){
            case CLASSROOM:
                ma=UserAction.STUDY_ACTION;
                break;
            case REFECTORY:
                ma=UserAction.EAT_ACTION;
                break;
            case PLAYGROUND:
                ma=UserAction.SPORT_ACTION;
                break;
            case DORMITORY:
                ma=UserAction.REST_ACTION;
                break;
            case EVERYWHERE:
                //ma=UserAction.EVETHING_ACTION;
                break;
            default:
                break;
        }
        boolean result=true;
        switch(healthState){
            case COMMON_STATE:{
                switch(ma){
                    case STUDY_ACTION:{
                        activity.mImageView_pet.setBackgroundResource(R.drawable.anim_study);
                        AnimationDrawable anim = (AnimationDrawable)activity.mImageView_pet.getBackground();
                        anim.start();

                        activity.chronometer.setVisibility(View.VISIBLE);
                        activity.chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
                        int chronometer_hour = (int) ((SystemClock.elapsedRealtime() - activity.chronometer.getBase()) / 1000 / 60);
                        activity.chronometer.setFormat("0"+String.valueOf(chronometer_hour)+":%s");
                        activity.chronometer.start();

                        Calendar cal=Calendar.getInstance();
                        int hour=cal.get(Calendar.HOUR_OF_DAY);
                        int minute=cal.get(Calendar.MINUTE);

                        before=hour*60+minute;
                        setIsStudy();
                        break;
                    }
                    case EAT_ACTION:{
                        activity.mImageView_pet.setBackgroundResource(R.drawable.anim_eating);
                        AnimationDrawable anim = (AnimationDrawable)activity.mImageView_pet.getBackground();
                        anim.start();

                        Calendar cal=Calendar.getInstance();
                        int hour=cal.get(Calendar.HOUR_OF_DAY);
                        int minute=cal.get(Calendar.MINUTE);
                        final int start1=6*60;
                        final int end1=9*60+45;//吃早餐
                        final int start2=10*60+45;
                        final int end2=13*60;//吃午饭
                        final int start3=16*60+45;
                        final int end3=19*60+30;//吃晚饭
                        int now=hour*60+minute;
                        if(now>=start1&&now<=end1){
                            if(Pet.isEvenHungry==true){
                                Pet.addHealth(4);
                                Pet.isEvenHungry=false;
                            }
                            //activity.mImageView_pet.setImageResource(R.mipmap.breakfast);
                            Pet.isEvenHungry=false;
                            Pet.isEvenEating=true;
                        }
                        if(now>=start2&&now<=end2){
                            if(Pet.isEvenHungry==true){
                                Pet.addHealth(2);
                                Pet.isEvenHungry=false;
                            }
                            //activity.mImageView_pet.setImageResource(R.mipmap.lunch);
                            Pet.isEvenHungry=false;
                            Pet.isEvenEating=true;
                        }
                        if(now>=start3&&now<=end3){
                            if(Pet.isEvenHungry==true){
                                Pet.addHealth(2);
                                Pet.isEvenHungry=false;
                            }
                            //activity.mImageView_pet.setImageResource(R.mipmap.dinner);
                            Pet.isEvenHungry=false;
                            Pet.isEvenEating=true;
                        }
                        if(isStudy==true){
                            time=hour*60+minute-before;
                            saveStudyTime(time,false);
                            before=0;
                        }
                        if(isSport==true){
                            time=hour*60+minute-before;
                            saveSportTime(time,false);
                            before=0;
                        }
                        break;
                    }

                    case SPORT_ACTION:{
                        activity.mImageView_pet.setBackgroundResource(R.drawable.anim_sport);
                        AnimationDrawable anim = (AnimationDrawable)activity.mImageView_pet.getBackground();
                        anim.start();

                        activity.chronometer.setVisibility(View.VISIBLE);
                        activity.chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
                        int chronometer_hour = (int) ((SystemClock.elapsedRealtime() - activity.chronometer.getBase()) / 1000 / 60);
                        activity.chronometer.setFormat("0"+String.valueOf(chronometer_hour)+":%s");
                        activity.chronometer.start();

                        Calendar cal=Calendar.getInstance();
                        int hour=cal.get(Calendar.HOUR_OF_DAY);
                        int minute=cal.get(Calendar.MINUTE);

                        if(isStudy==true){
                            time=hour*60+minute-before;
                            saveStudyTime(time,false);
                            before=0;
                        }

                        before=hour*60+minute;
                        setIsSport();
                        break;
                    }

                    case REST_ACTION:{
                        activity.mImageView_pet.setBackgroundResource(R.drawable.anim_sleep);
                        AnimationDrawable anim = (AnimationDrawable)activity.mImageView_pet.getBackground();
                        anim.start();
                        Pet.addMood(15);
                        Calendar cal=Calendar.getInstance();
                        int hour=cal.get(Calendar.HOUR_OF_DAY);
                        int minute=cal.get(Calendar.MINUTE);
                        if(isStudy==true){
                            time=hour*60+minute-before;
                            saveStudyTime(time,false);
                            before=0;
                        }
                        if(isSport==true){
                            time=hour*60+minute-before;
                            saveSportTime(time,false);
                            before=0;
                        }
                        break;
                    }

                    case EVETHING_ACTION:{
                        Calendar cal=Calendar.getInstance();
                        int hour=cal.get(Calendar.HOUR_OF_DAY);
                        int minute=cal.get(Calendar.MINUTE);
                        int after=hour*60+minute;
                        if(isStudy){
                            int StudyInterval=after-before;
                            if(StudyInterval<4)
                                Pet.addMood(StudyInterval*10);
                            else
                                Pet.addMood(StudyInterval*(-5));
                            saveStudyTime(StudyInterval,false);
                        }
                        if(isSport){
                            int SportInterval=after-before;
                            Pet.addMood(SportInterval*10);
                            Pet.addHealth(SportInterval*10);
                            SportInterval=0;
                            saveStudyTime(SportInterval,true);
                        }
                        before=0;
                    }
                    default:
                        break;
                }
                break;
            }
            case ILLNESS_STATE:{
                activity.mImageView_pet.setBackgroundResource(R.drawable.anim_illness);
                AnimationDrawable anim = (AnimationDrawable)activity.mImageView_pet.getBackground();
                anim.start();
                break;
            }
            case HUNGRY_STATE:{
                activity.mImageView_pet.setBackgroundResource(R.drawable.anim_illness);
                AnimationDrawable anim = (AnimationDrawable)activity.mImageView_pet.getBackground();
                anim.start();
                Pet.reduceHealth(5);
                Pet.reduceMood(5);
                Pet.isEvenHungry=true;
                break;
            }
            case SUB_HEALTH_STATE:{
                switch(ma){
                    case STUDY_ACTION: {
                        activity.mImageView_pet.setBackgroundResource(R.drawable.anim_study);
                        AnimationDrawable anim = (AnimationDrawable)activity.mImageView_pet.getBackground();
                        anim.start();

                        activity.chronometer.setVisibility(View.VISIBLE);
                        activity.chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
                        int chronometer_hour = (int) ((SystemClock.elapsedRealtime() - activity.chronometer.getBase()) / 1000 / 60);
                        activity.chronometer.setFormat("0"+String.valueOf(chronometer_hour)+":%s");
                        activity.chronometer.start();

                        Calendar cal=Calendar.getInstance();
                        int hour=cal.get(Calendar.HOUR_OF_DAY);
                        int minute=cal.get(Calendar.MINUTE);

                        if(isSport==true){
                            time=hour*60+minute-before;
                            saveSportTime(time,false);
                            before=0;
                        }

                        before=hour*60+minute;
                        setIsStudy();
                        break;
                    }
                    case EAT_ACTION:{
                        activity.mImageView_pet.setBackgroundResource(R.drawable.anim_eating);
                        AnimationDrawable anim = (AnimationDrawable)activity.mImageView_pet.getBackground();
                        anim.start();

                        Calendar cal=Calendar.getInstance();
                        int hour=cal.get(Calendar.HOUR_OF_DAY);
                        int minute=cal.get(Calendar.MINUTE);
                        final int start1=6*60;
                        final int end1=9*60+45;//吃早餐
                        final int start2=10*60+45;
                        final int end2=13*60;//吃午饭
                        final int start3=16*60+45;
                        final int end3=19*60+30;//吃晚饭
                        int now=hour*60+minute;
                        if(now>=start1&&now<=end1){
                            if(Pet.isEvenHungry==true){
                                Pet.addHealth(2);
                                Pet.isEvenHungry=false;
                            }
                            //activity.mImageView_pet.setImageResource(R.mipmap.breakfast);
                            Pet.isEvenHungry=false;
                            Pet.isEvenEating=true;
                        }
                        if(now>=start2&&now<=end2){
                            if(Pet.isEvenHungry==true){
                                Pet.addHealth(1);
                                Pet.isEvenHungry=false;
                            }
                           // activity.mImageView_pet.setImageResource(R.mipmap.lunch);
                            Pet.isEvenHungry=false;
                            Pet.isEvenEating=true;
                        }
                        if(now>=start3&&now<=end3){
                            if(Pet.isEvenHungry==true){
                                Pet.addHealth(1);
                                Pet.isEvenHungry=false;
                            }
                            //activity.mImageView_pet.setImageResource(R.mipmap.dinner);
                            Pet.isEvenHungry=false;
                            Pet.isEvenEating=true;
                        }
                        if(isStudy==true){
                            time=hour*60+minute-before;
                            saveStudyTime(time,false);
                            before=0;
                        }
                        if(isSport==true){
                            time=hour*60+minute-before;
                            saveSportTime(time,false);
                            before=0;
                        }
                        break;
                    }

                    case SPORT_ACTION:{
                        activity.mImageView_pet.setBackgroundResource(R.drawable.anim_sport);
                        AnimationDrawable anim = (AnimationDrawable)activity.mImageView_pet.getBackground();
                        anim.start();

                        activity.chronometer.setVisibility(View.VISIBLE);
                        activity.chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
                        int chronometer_hour = (int) ((SystemClock.elapsedRealtime() - activity.chronometer.getBase()) / 1000 / 60);
                        activity.chronometer.setFormat("0"+String.valueOf(chronometer_hour)+":%s");
                        activity.chronometer.start();

                        Calendar cal=Calendar.getInstance();
                        int hour=cal.get(Calendar.HOUR_OF_DAY);
                        int minute=cal.get(Calendar.MINUTE);

                        if(isStudy==true){
                            time=hour*60+minute-before;
                            saveStudyTime(time,false);
                            before=0;
                        }

                        before=hour*60+minute;
                        isSport=true;
                        break;
                    }

                    case REST_ACTION:{
                        activity.mImageView_pet.setBackgroundResource(R.drawable.anim_sleep);
                        AnimationDrawable anim = (AnimationDrawable)activity.mImageView_pet.getBackground();
                        anim.start();

                        Calendar cal=Calendar.getInstance();
                        int hour=cal.get(Calendar.HOUR_OF_DAY);
                        int minute=cal.get(Calendar.MINUTE);

                        if(isStudy==true){
                            time=hour*60+minute-before;
                            saveStudyTime(time,false);
                            before=0;
                        }
                        if(isSport==true){
                            time=hour*60+minute-before;
                            saveSportTime(time,false);
                            before=0;
                        }

                        Pet.addMood(15);
                        break;
                    }

                    case EVETHING_ACTION:{
                        Calendar cal=Calendar.getInstance();
                        int hour=cal.get(Calendar.HOUR_OF_DAY);
                        int minute=cal.get(Calendar.MINUTE);
                        int after=hour*60+minute;
                        if(isStudy){
                            int StudyInterval=after-before;
                            if(StudyInterval<4)
                                Pet.addMood(StudyInterval*10);
                            else
                                Pet.addMood(StudyInterval*(-5));
                            saveStudyTime(StudyInterval,false);
                        }
                        if(isSport){
                            int SportInterval=after-before;
                            Pet.addMood(SportInterval*10);
                            Pet.addHealth(SportInterval*10);
                            saveSportTime(SportInterval,true);
                        }
                        before=0;
                    }
                    default:
                        break;
                }
                break;
            }
            default:
                result=false;
        }
        return result;
    }

    public String translateState(PetAction myPetAction){
        switch(myPetAction){
            case STUDY_STATE:
                return   "STUDY_STATE";
            case EAT_STATE:
                return "EAT_STATE";
            case SPORT_STATE:
                return "SPORT_STATE";
            case REST_STATE:
                return "REST_STATE";
            case COMMON_STATE:
                return "COMMON_STATE";
            case ILLNESS_STATE:
                return "ILLNESS_STATE";
            case HUNGRY_STATE:
                return "HUNGRY_STATE";
            case SUB_HEALTH_STATE:
                return "SUB_HEALTH_STATE";
        }
        return null;
    }

    public PetAction translateString(String mystring){
        if(mystring.equals("STUDY_STATE"))
            return PetAction.STUDY_STATE;
        if(mystring.equals("EAT_STATE"))
            return PetAction.EAT_STATE;
        if(mystring.equals("SPORT_STATE"))
            return PetAction.SPORT_STATE;
        if(mystring.equals("REST_STATE"))
            return PetAction.REST_STATE;
        if(mystring.equals("COMMON_STATE"))
            return PetAction.COMMON_STATE;
        if(mystring.equals("ILLNESS_STATE"))
            return PetAction.ILLNESS_STATE;
        if(mystring.equals("HUNGRY_STATE"))
            return PetAction.HUNGRY_STATE;
        if(mystring.equals("SUB_HEALTH_STATE"))
            return PetAction.SUB_HEALTH_STATE;
        return null;
    }

    public String translateToChinese(PetAction myPetAction){
        switch(myPetAction){
            case STUDY_STATE:
                return   "学习";
            case EAT_STATE:
                return "吃饭";
            case SPORT_STATE:
                return "运动";
            case REST_STATE:
                return "休息";
            case COMMON_STATE:
                return "健康";
            case ILLNESS_STATE:
                return "生病";
            case HUNGRY_STATE:
                return "饥饿";
            case SUB_HEALTH_STATE:
                return "亚健康";
        }
        return null;
    }

    public void saveStudyTime(final double time, final boolean isStudy){
        final User bmobUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<AchievementDatebase> query = new BmobQuery<AchievementDatebase>();
        query.addWhereEqualTo("user", bmobUser);
        query.findObjects(new FindListener<AchievementDatebase>() {
            @Override
            public void done(List<AchievementDatebase> object, BmobException e) {
                if(e==null){
                    for (AchievementDatebase gameScore : object) {
                        AchievementDatebase gameScore1 = new AchievementDatebase();
                        gameScore1.setStudyTime(time+gameScore.getStudyTime());
                        gameScore1.setIsStudy(isStudy);
                        gameScore1.update(gameScore.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){

                                }else{
                                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                }else{
                    AchievementDatebase gameScore = new AchievementDatebase();
                    gameScore.setUser(bmobUser);
                    gameScore.setStudyTime(time);
                    gameScore.setSportTime(0);
                    gameScore.setIsStudy(false);
                    gameScore.setIsSport(false);
                    gameScore.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if(e==null){

                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }

    public void saveSportTime(final double time, final boolean isSport){
        final User bmobUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<AchievementDatebase> query = new BmobQuery<AchievementDatebase>();
        query.addWhereEqualTo("user", bmobUser);
        query.findObjects(new FindListener<AchievementDatebase>() {
            @Override
            public void done(List<AchievementDatebase> object, BmobException e) {
                if(e==null){
                    for (AchievementDatebase gameScore : object) {
                        AchievementDatebase gameScore1 = new AchievementDatebase();
                        gameScore1.setSportTime(time+gameScore.getSportTime());
                        gameScore1.setIsSport(isSport);
                        gameScore1.update(gameScore.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){

                                }else{
                                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                }else{
                    AchievementDatebase gameScore = new AchievementDatebase();
                    gameScore.setUser(bmobUser);
                    gameScore.setStudyTime(0);
                    gameScore.setSportTime(time);
                    gameScore.setIsSport(false);
                    gameScore.setIsStudy(false);
                    gameScore.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if(e==null){

                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }

    public void setIsStudy(){
        final User bmobUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<AchievementDatebase> query = new BmobQuery<AchievementDatebase>();
        query.addWhereEqualTo("user", bmobUser);
        query.findObjects(new FindListener<AchievementDatebase>() {
            @Override
            public void done(List<AchievementDatebase> object, BmobException e) {
                if(e==null){
                    for (AchievementDatebase gameScore : object) {
                        AchievementDatebase gameScore1 = new AchievementDatebase();
                        gameScore1.setIsStudy(true);
                        gameScore1.update(gameScore.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){

                                }else{
                                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                }else{
                    AchievementDatebase gameScore = new AchievementDatebase();
                    gameScore.setUser(bmobUser);
                    gameScore.setStudyTime(0);
                    gameScore.setSportTime(0);
                    gameScore.setIsSport(false);
                    gameScore.setIsStudy(true);
                    gameScore.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if(e==null){

                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }

    public void setIsSport(){
        final User bmobUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<AchievementDatebase> query = new BmobQuery<AchievementDatebase>();
        query.addWhereEqualTo("user", bmobUser);
        query.findObjects(new FindListener<AchievementDatebase>() {
            @Override
            public void done(List<AchievementDatebase> object, BmobException e) {
                if(e==null){
                    for (AchievementDatebase gameScore : object) {
                        AchievementDatebase gameScore1 = new AchievementDatebase();
                        gameScore1.setIsSport(true);
                        gameScore1.update(gameScore.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){

                                }else{
                                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                }else{
                    AchievementDatebase gameScore = new AchievementDatebase();
                    gameScore.setUser(bmobUser);
                    gameScore.setStudyTime(0);
                    gameScore.setSportTime(0);
                    gameScore.setIsSport(true);
                    gameScore.setIsStudy(false);
                    gameScore.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if(e==null){

                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }

    public void getInformation(){
        final User bmobUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<AchievementDatebase> query = new BmobQuery<AchievementDatebase>();
        query.addWhereEqualTo("user", bmobUser);
        query.findObjects(new FindListener<AchievementDatebase>() {
            @Override
            public void done(List<AchievementDatebase> object, BmobException e) {
                if(e==null){
                    before=object.get(0).getBefore();
                }else{
                    before=0;
                    isSport=false;
                    isStudy=false;
                }
            }
        });
    }
}
