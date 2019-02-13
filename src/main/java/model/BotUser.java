package model;

import java.util.ArrayList;
import java.util.List;

public class BotUser {



    private String name;

    public BotUser(String name) {
        this.name = name;
    }
    private Integer userId;
    private String userName;
    private String userAddress;
    private String phoneNum;
    private List<String> context = new ArrayList<>();
    private boolean notificationModeOn;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setNotificationModeOn(boolean notificationModeOn) {
        this.notificationModeOn = notificationModeOn;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public boolean isNotificationModeOn() {
        return notificationModeOn;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public List<String> getContext() {
        return context;
    }

    public void setContext(List<String> context) {
        this.context = context;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

}