package com.inno72.project.vo;

import com.inno72.project.model.Inno72Activity;

public class Inno72ActivityVo extends Inno72Activity{
	
    private String state;
    
    private String prizeType;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}

    
    
}