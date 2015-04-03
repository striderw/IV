package com.will.practice.service;

import java.util.List;

import com.will.practice.compent.PageBean;

abstract public class GenericService<T> {

	public List<T>search(String term) {
		PageBean<T> pb = new PageBean<T>();
		// 其他set方法，有可能是页面传过来的
		return actualSearch(pb, term);
	}

	abstract protected List<T>actualSearch(PageBean<T> pb, String term);
}
