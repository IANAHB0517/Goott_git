package com.miniproj.vodto;

public class LoginDTO {
private String userId;
private String userPwd;

public LoginDTO(String userId, String userPwd) {
	super();
	this.userId = userId;
	this.userPwd = userPwd;
}

public String getUserId() {
	return userId;
}

public String getUserPwd() {
	return userPwd;
}

@Override
public String toString() {
	return "LoginDTO [userId=" + userId + ", userPwd=" + userPwd + "]";
}

}
