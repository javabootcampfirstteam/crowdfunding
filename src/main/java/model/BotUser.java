package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BotUser implements Serializable {

	private Integer userId;
	private String userName;
	private String userAddress;
	private int phoneNumber;

	public int getPhoneNumber() { return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber;
	}

	private List<String> context = new ArrayList<>();
	private boolean notificationModeOn;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAddress() {
		return userAddress;
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

	public boolean isNotificationModeOn() {
		return notificationModeOn;
	}

	public void setNotificationModeOn(boolean notificationModeOn) {
		this.notificationModeOn = notificationModeOn;
	}
}
