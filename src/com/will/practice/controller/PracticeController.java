package com.will.practice.controller;

import java.util.List;
import java.util.Map;

import com.will.practice.entity.Child;
import com.will.practice.entity.Parent;
import com.will.practice.entity.User;
import com.will.practice.service.ChildService;
import com.will.practice.service.ParentService;
import com.will.practice.service.UserService;
import com.will.practice.utils.RenderUtils;

public class PracticeController {

	// 实际工作中使用spring注入进来
	private UserService userService = new UserService();
	private ParentService parentService = new ParentService();
	private ChildService childService = new ChildService();

	public Map<String, String> searchUser(String name) {
		List<User> userList = userService.search(name);
		return RenderUtils.renderListToMap(userList, new String[]{"userId", "userName", "gender"});
	}

	public Map<String, String> searchParent(String name) {
		List<Parent> userList = parentService.search(name);
		return RenderUtils.renderListToMap(userList, new String[]{"userId", "userName", "age"});
	}

	public Map<String, String> searchChild(String name) {
		List<Child> userList = childService.search(name);
		return RenderUtils.renderListToMap(userList, new String[]{"userId", "userName", "gender", "parent.userName"});
	}
}
