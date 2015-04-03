package com.will.practice.entity;



/**
 * 用户信息
 *
 * @version 01-00
 */
public class User implements java.io.Serializable {


	/** 序列化. */
	private static final long serialVersionUID = -4861759244229421811L;
	/** 用户ID. */
	protected String userId;
	/** 用户姓名. */
	protected String userName;
	/** 用户性别. */
	protected String gender;
	/** 年龄。 */
	protected Integer age;

	public User() {

	}

	public User(String userId, String userName,
			String gender, Integer age) {
		this.userId = userId;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
}
