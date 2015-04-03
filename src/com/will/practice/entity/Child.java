package com.will.practice.entity;

public class Child extends User {

	private static final long serialVersionUID = -5362903060636122029L;

	// other properties
	private User parent;

	public Child() {

	}

	public Child(String userId, String userName,
			String gender, Integer age) {
		this.userId = userId;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
	}

	public Child(String userId, String userName,
			String gender, Integer age, User parent) {
		this.userId = userId;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
		this.parent = parent;
	}

	/**
	 * @return the parent
	 */
	public User getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(User parent) {
		this.parent = parent;
	}
}
