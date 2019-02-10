package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String userName;
    private String userAddress;
    private List<String> context = new ArrayList<>();

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userid) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String useraddress) {
        this.userAddress = userAddress;
    }

    public List<String> getContext() {
        return context;
    }

    public void setContext(List<String> context) {
        this.context = context;
    }
}
