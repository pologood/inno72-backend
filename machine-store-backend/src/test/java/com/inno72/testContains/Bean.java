package com.inno72.testContains;

import com.inno72.common.StringUtil;

public class Bean {
	private String id;
	private String code;
	private Integer status;

	@Override
	public boolean equals(Object obj) {
		if (!StringUtil.isEmpty(code) && status != null) {
			Bean other = (Bean) obj;
			if (code.equals(other.code) && status == other.status) {
				return true;
			}
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
