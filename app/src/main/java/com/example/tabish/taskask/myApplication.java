package com.example.tabish.taskask;

import android.app.Application;

/**
 * Created by Tabish on 12/6/2017.
 */

public class myApplication extends Application {

    private String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }
}