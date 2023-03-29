package com.miniproj.vodto;

public class LoginDTO {
	private String userId;
	private String pwd;
	
	public LoginDTO(String userId, String pwd) {
		super();
		this.userId = userId;
		this.pwd = pwd;
	}

	public String getUserId() {
		return userId;
	}

	public String getPwd() {
		return pwd;
	}

	@Override
	public String toString() {
		return "LoginDTO [userId=" + userId + ", pwd=" + pwd + "]";
	}
	
	
}	
