package com.inno72.check.vo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

public class Inno72CheckFaultRemarkVo {

	private String answer;

	private String answerType;
	private String answerName;

	private List<Map<String, Object>> imgList;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "answerTime")
	private LocalDateTime answerTime;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

	public String getAnswerName() {
		return answerName;
	}

	public void setAnswerName(String answerName) {
		this.answerName = answerName;
	}

	public LocalDateTime getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(LocalDateTime answerTime) {
		this.answerTime = answerTime;
	}

	public List<Map<String, Object>> getImgList() {
		return imgList;
	}

	public void setImgList(List<Map<String, Object>> imgList) {
		this.imgList = imgList;
	}

}