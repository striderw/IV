package com.will.practice.service;

import java.util.ArrayList;
import java.util.List;

import com.will.practice.components.PageBean;
import com.will.practice.entity.User;

public class UserService extends GenericService<User> {

	@Override
	protected List<User> actualSearch(PageBean<User> pb, String term) {
		// TODO Auto-generated method stub
		System.out.println("UserService -> actualSearch");

		List<User> retList = new ArrayList<User>();
		retList.add(new User("U0001", "U1", "M", 10));
		retList.add(new User("U0002", "U2", "F", 15));
		return retList;
	}

}
