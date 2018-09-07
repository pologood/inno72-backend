package com.inno72.system.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;

@Table(name = "inno72_user_function_data")
public class Inno72UserFunctionData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private String userId;

	/**
	 * 权限ID
	 */
	@Column(name = "function_id")
	private String functionId;

	/**
	 * 对象包名
	 */
	@Column(name = "vo_name")
	private String voName;

	/**
	 * 不显示列
	 */
	@Column(name = "vo_column")
	private String voColumn;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private String createId;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 权限描述
	 */
	private String functionDepict;

	/**
	 * 权限描述
	 */
	private Integer functionLevel;

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取用户ID
	 *
	 * @return user_id - 用户ID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 *
	 * @param userId
	 *            用户ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	/**
	 * 获取对象包名
	 *
	 * @return vo_name - 对象包名
	 */
	public String getVoName() {
		return voName;
	}

	/**
	 * 设置对象包名
	 *
	 * @param voName
	 *            对象包名
	 */
	public void setVoName(String voName) {
		this.voName = voName;
	}

	/**
	 * 获取不显示列
	 *
	 * @return vo_column - 不显示列
	 */
	public String getVoColumn() {
		return voColumn;
	}

	/**
	 * 设置不显示列
	 *
	 * @param voColumn
	 *            不显示列
	 */
	public void setVoColumn(String voColumn) {
		this.voColumn = voColumn;
	}

	/**
	 * 获取创建人
	 *
	 * @return create_id - 创建人
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * 设置创建人
	 *
	 * @param createId
	 *            创建人
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 * 获取创建时间
	 *
	 * @return create_time - 创建时间
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getFunctionDepict() {
		return functionDepict;
	}

	public void setFunctionDepict(String functionDepict) {
		this.functionDepict = functionDepict;
	}

	public Integer getFunctionLevel() {
		return functionLevel;
	}

	public void setFunctionLevel(Integer functionLevel) {
		this.functionLevel = functionLevel;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Inno72UserFunctionData) {
			Inno72UserFunctionData result = (Inno72UserFunctionData) obj;
			if (null != result && result.getFunctionId().equals(this.getFunctionId())) {
				return true;
			}
		}
		return false;
	}

}