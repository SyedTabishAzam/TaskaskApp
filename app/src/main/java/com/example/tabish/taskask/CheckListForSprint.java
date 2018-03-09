package com.example.tabish.taskask;

/**
 * Created by Tabish on 09-Mar-18.
 */

public class CheckListForSprint {
    private  String name_;
    private String value_,id_;
    private boolean _completed;
    public  CheckListForSprint(String name, String value,String id,boolean completed)
    {
        name_ = name;
        value_ = value;
        id_=id;
        _completed = completed;
    }

    public String getName() {
        return name_;
    }

    public String getValue() {
        return value_;
    }

    public String getId() {
        return id_;
    }

    public boolean isCompleted() {
        return _completed;
    }
}
