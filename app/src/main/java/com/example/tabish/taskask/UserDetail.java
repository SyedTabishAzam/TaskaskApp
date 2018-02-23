package com.example.tabish.taskask;

import android.util.Log;

/**
 * Created by Tabish on 11-Feb-18.
 */

public class UserDetail {
    private String username;
    private String fullname;
    private String number;
    private float cRating;
    private float eRating;
    private int debt;
    private String address;
    private String cnic;
    private String vehicle;
    private String city;
    private String gender;
    private int postedTasks;
    private int completedTasks;
    private Double latitude;
    private Double longitude;
    private String status;

    public UserDetail(String fullname,String username, String number,String status, float cRating, float eRating, String address, String cnic,int postedTasks,int completedTasks,Double latitude, Double longitude) {
        this.username = username;
        this.fullname = fullname;
        this.number = number;
        this.cRating = cRating;
        this.eRating = eRating;
        this.address = address;
        this.cnic = cnic;
        this.postedTasks = postedTasks;
        this.completedTasks = completedTasks;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status=status;

    }

    public UserDetail() {

    }

    public UserDetail(String username, String fullname, String number, String status, int postedTasks,int completedTasks, String vehicle, float cRating, float eRating,int debt) {
        this.fullname = fullname;
        this.username = username;
        this.number = number;
        this.status = status;
        this.vehicle = vehicle;
        this.cRating = cRating;
        this.eRating = eRating;

        this.gender = gender;
        this.completedTasks = completedTasks;
        this.postedTasks = postedTasks;
        this.debt = debt;
    }

    public String getStatus() {
        return status;
    }

    public int getDebt() {
        return debt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getCity() {
        return city;
    }

    public String getGender() {
        return gender;
    }

    public int getPostedTasks() {
        return postedTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public String getFullname() {
        Log.e("User","Full name requested");
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public float getcRating() {
        return cRating;
    }

    public void setcRating(float cRating) {
        this.cRating = cRating;
    }

    public float geteRating() {
        return eRating;
    }

    public void seteRating(float eRating) {
        this.eRating = eRating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
