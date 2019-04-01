package com.shi.entity;

import java.io.Serializable;

public class Messages implements Serializable{

	/**
	 * @Field @serialVersionUID : (这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = 1L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
