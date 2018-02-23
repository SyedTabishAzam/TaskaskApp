package com.example.tabish.taskask;

/**
 * Created by Tabish on 06-Jan-18.
 */

public class Tasks {
    private String tag_, user_,username_,fee_,urgency_,critical_,UrgencyColor_,CrticialColor_,time_,id_,desc_,accepteByName,acceptedByUser;
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

    public Tasks(String tag,String user,String fee,String urgency, String critical,String urgencycolor,String time,String id,String desc,String username)
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
        username_ = username;
    }

    public String getAccepteByName() {
        return accepteByName;
    }

    public void setAccepteByName(String accepteByName) {
        this.accepteByName = accepteByName;
    }

    public String getAcceptedByUser() {
        return acceptedByUser;
    }

    public void setAcceptedByUser(String acceptedByUser) {
        this.acceptedByUser = acceptedByUser;
    }

    public String getUsername() {
        return username_;
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
