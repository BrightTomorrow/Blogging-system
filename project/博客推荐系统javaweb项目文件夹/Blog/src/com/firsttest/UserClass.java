package com.firsttest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserClass {
	
	Date date = new Date();//获得系统时间.
	SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
	String nowTime = sdf.format(date);
	private Timestamp user_registration_time =Timestamp.valueOf(nowTime);//把时间转换
	
	private String user_id;
	private String user_name;
	private String user_password;
	private String user_email;
	private String user_telephone_number;

	@Override
	public String toString() {
		return "UserClass [user_id=" + user_id + ", user_name=" + user_name + ", user_password=" + user_password + ", user_email="
				+ user_email + ", user_registration_time=" + user_registration_time + ", user_telephone_number=" + user_telephone_number + "]";
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUer_name() {
		return user_name;
	}
	public void setUer_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public Timestamp getUser_registration_time() {
		return user_registration_time;
	}

	public String getUser_telephone_number() {
		return user_telephone_number;
	}
	public void setUser_telephone_number(String user_telephone_number) {
		this.user_telephone_number = user_telephone_number;
	}
}
