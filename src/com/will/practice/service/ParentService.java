package com.will.practice.service;

import java.util.ArrayList;
import java.util.List;

import com.will.practice.compent.PageBean;
import com.will.practice.entity.Parent;

public class ParentService extends GenericService<Parent> {

	@Override
	protected List<Parent> actualSearch(PageBean<Parent> pb, String term) {
		// TODO Auto-generated method stub
		System.out.println("ParentService -> actualSearch");
		List<Parent> retList = new ArrayList<Parent>();
		retList.add(new Parent("P0001", "P1", "M", 30));
		retList.add(new Parent("P0002", "P2", "F", 35));
		return retList;
	}

}
