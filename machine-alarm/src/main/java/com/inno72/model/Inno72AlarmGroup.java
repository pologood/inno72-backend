package com.inno72.model;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "Inno72_alarm_group")
public class Inno72AlarmGroup {

	@Id
	private String id;

	private String groupId1;

	private String areaCode;

	private String groupId2;

	private int level;

	private String areaName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId1() {
		return groupId1;
	}

	public void setGroupId1(String groupId1) {
		this.groupId1 = groupId1;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getGroupId2() {
		return groupId2;
	}

	public void setGroupId2(String groupId2) {
		this.groupId2 = groupId2;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
