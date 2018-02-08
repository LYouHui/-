package com.example.schoolpet;

/**
 * Created by 林尤辉 on 2017/7/7.
 */

class RankingList {
    private int number;
    private String name;
    private int studytime;
    private int sporttime;

    public RankingList(int number,String name,int studytime,int sporttime){
        this.number=number;
        this.name=name;
        this.studytime=studytime;
        this.sporttime=sporttime;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getSporttime() {
        return sporttime;
    }

    public int getStudytime() {
        return studytime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSporttime(int sporttime) {
        this.sporttime = sporttime;
    }

    public void setStudytime(int studytime) {
        this.studytime = studytime;
    }
}
