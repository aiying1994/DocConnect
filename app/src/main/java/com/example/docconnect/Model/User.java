package com.example.docconnect.Model;

public class User {
    private String name, address, phoneNumber;

    public User() {
    }

    public User(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //    private String fbid, userPhone, name, address;

//    public User(String fbid, String userPhone, String name, String address) {
//        this.fbid = fbid;
//        this.userPhone = userPhone;
//        this.name = name;
//        this.address = address;
//    }
//
//    public String getFbid() {
//        return fbid;
//    }
//
//    public void setFbid(String fbid) {
//        this.fbid = fbid;
//    }
//
//    public String getUserPhone() {
//        return userPhone;
//    }
//
//    public void setUserPhone(String userPhone) {
//        this.userPhone = userPhone;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
}
