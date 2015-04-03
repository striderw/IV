package com.will.practice.service;

import java.util.ArrayList;
import java.util.List;

import com.will.practice.components.PageBean;
import com.will.practice.entity.Child;
import com.will.practice.entity.Parent;

public class ChildService extends GenericService<Child> {

	@Override
	protected List<Child> actualSearch(PageBean<Child> pb, String term) {
		// TODO Auto-generated method stub
		System.out.println("ChildService -> actualSearch");

		Parent parent = new Parent("P0001", "P1", "M", 40);
		List<Child> retList = new ArrayList<Child>();
		retList.add(new Child("C0001", "C1", "M", 18, parent));
		retList.add(new Child("C0002", "C2", "F", 16, parent));
		return retList;
	}

}
