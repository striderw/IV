package com.will.practice.components;


public class PageBean<T> {

	/** 页码. */
	private int pageNumber = 1;

	/** 页面大小. */
	private int pageSize = 20;

	public PageBean(){

	}
	public PageBean(int pageNumber, int pageSize){
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}
	/**
	 * @return the pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
