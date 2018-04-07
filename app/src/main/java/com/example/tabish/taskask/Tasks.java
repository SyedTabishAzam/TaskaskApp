package com.example.tabish.taskask;

/**
 * Created by Tabish on 06-Jan-18.
 */

public class Tasks {
    private String tag_, user_,username_,fee_,urgency_,critical_,UrgencyColor_,CrticialColor_,time_,id_,desc_,accepteByName,acceptedByUser;
    private String postedByname,postedByUser,TaskStatus;
    public Tasks()
    {

    }
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

    public String getPostedByname() {
        return postedByname;
    }

    public void setPostedByname(String postedByname) {
        this.postedByname = postedByname;
    }

    public String getPostedByUser() {
        return this.postedByUser;
    }

    public String getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        TaskStatus = taskStatus;
    }

    public void setPostedByUser(String postedByUser) {
        this.postedByUser = postedByUser;
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

    public void setTag_(String tag_) {
        this.tag_ = tag_;
    }

    public void setUser_(String user_) {
        this.user_ = user_;
    }

    public void setUsername_(String username_) {
        this.username_ = username_;
    }

    public void setFee_(String fee_) {
        this.fee_ = fee_;
    }

    public void setUrgency_(String urgency_) {
        this.urgency_ = urgency_;
    }

    public void setCritical_(String critical_) {
        this.critical_ = critical_;
    }

    public void setUrgencyColor_(String urgencyColor_) {
        UrgencyColor_ = urgencyColor_;
    }

    public void setCrticialColor_(String crticialColor_) {
        CrticialColor_ = crticialColor_;
    }

    public void setTime_(String time_) {
        this.time_ = time_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public void setDesc_(String desc_) {
        this.desc_ = desc_;
    }
}
