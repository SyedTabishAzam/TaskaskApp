package com.example.tabish.taskask;

/**
 * Created by Tabish on 06-Jan-18.
 */

public class Tasks {
    private String tag_, user_,fee_,urgency_,critical_,UrgencyColor_,CrticialColor_,time_,id_,desc_;
    public Tasks(String tag,String user,String fee,String urgency, String critical,String urgencycolor,String time,String id)
    {
        id_=id;
        tag_=tag;
        user_=user;
        fee_=fee;
        urgency_=urgency;
        critical_=critical;
        UrgencyColor_ = urgencycolor;
        CrticialColor_ = "Black";
        time_ = time;
    }

    public Tasks(String tag,String user,String fee,String urgency, String critical,String urgencycolor,String time,String id,String desc)
    {
        id_=id;
        tag_=tag;
        user_=user;
        fee_=fee;
        urgency_=urgency;
        critical_=critical;
        UrgencyColor_ = urgencycolor;
        CrticialColor_ = "Black";
        time_ = time;
        desc_=desc;
    }

    public String getDesc() {
        return desc_;
    }

    public String getCritical() {
        return critical_;
    }

    public String getTag() {
        return tag_;
    }

    public String getFee() {
        return fee_;
    }

    public String getUrgency() {
        return urgency_;
    }

    public String getUser() {
        return user_;
    }

    public String getUrgencyColor() {
        return UrgencyColor_;
    }

    public String getID() { return id_;};

    public String getCriticalColor()
    {
        return CrticialColor_;
    }

    public String getTime() {
        return time_;
    }
}
