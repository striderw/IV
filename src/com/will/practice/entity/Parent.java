package com.will.practice.entity;

public class Parent extends User {

	private static final long serialVersionUID = -1735614953238477736L;

	// other properties

	public Parent() {}

	public Parent(String userId, String userName,
			String gender, Integer age) {
		this.userId = userId;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
	}
}
