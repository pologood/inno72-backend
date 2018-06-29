package com.inno72.test;

import java.util.List;

public class City {
	private String name;
	private List<Country> counties;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Country> getCounties() {
		return counties;
	}

	public void setCounties(List<Country> counties) {
		this.counties = counties;
	}

}
