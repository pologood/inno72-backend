package com.inno72.check.vo;

import java.util.List;


import com.inno72.check.model.Inno72CheckFault;

public class Inno72CheckFaultVo extends Inno72CheckFault{
	
	private List<String> imgList;
	
	private List<String> answerList;

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

	public List<String> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<String> answerList) {
		this.answerList = answerList;
	}
	
    
}