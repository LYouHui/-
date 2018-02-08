package com.example.schoolpet;

import cn.bmob.v3.BmobObject;

/**
 * Created by 林尤辉 on 2017/6/23.
 */

public class OldBookDatebase extends BmobObject{
    private String bookName;
    private String department;
    private String ownerPhoneNumber;
    private User owner;
    private boolean valid;

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public void setOwner(User owner){
        this.owner=owner;
    }

    public void setValid(boolean bool) { this.valid=bool; }

    public String getBookName()
    {
        return bookName;
    }

    public String getDepartment(){return  department;}

    public String getOwnerPhoneNumber(){ return ownerPhoneNumber; }

    public User getOwner(){ return owner; }

    public boolean getValid(){ return valid;}
}
