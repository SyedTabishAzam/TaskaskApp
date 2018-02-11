package com.example.tabish.taskask;

/**
 * Created by Tabish on 06-Jan-18.
 */

public class myDict {
    private  String name_;
    private String value_;
    public  myDict(String name, String value)
    {
        name_ = name;
        value_ = value;
    }

    public String getName() {
        return name_;
    }

    public String getValue() {
        return value_;
    }
}
