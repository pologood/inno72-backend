package com.inno72.store.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_storekeeper")
public class Inno72Storekeeper {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 用户姓名
	 */
	private String name;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 所属公司
	 */
	private String company;

	/**
	 * 状态：0正常，1停用
	 */
	private Integer status;

	/**
	 * 密码
	 */
	private String pwd;

	/**
	 * 状态：0正常，1停止
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

	/**
	 * 备注描述
	 */
	private String remark;

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
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 更新时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_time")
	private LocalDateTime updateTime;


	@Transient
	private String storeName;

	@Transient
	private List<Inno72StoreFunction> storeFunctionList;

	@Transient
	private List<Inno72Store> storeList;

	@Transient
	private String[] storeIds;

	@Transient
	private String[] functionIds;

	@Transient
	private Integer loginType;

	@Transient
	private String smsCode;
	/**
	 * 获取ID
	 *
	 * @return id - ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置ID
	 *
	 * @param id
	 *            ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取用户姓名
	 *
	 * @return name - 用户姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置用户姓名
	 *
	 * @param name
	 *            用户姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取手机号
	 *
	 * @return mobile - 手机号
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置手机号
	 *
	 * @param mobile
	 *            手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取所属公司
	 *
	 * @return company - 所属公司
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * 设置所属公司
	 *
	 * @param company
	 *            所属公司
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * 获取状态：0正常，1停用
	 *
	 * @return status - 状态：0正常，1停用
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置状态：0正常，1停用
	 *
	 * @param status
	 *            状态：0正常，1停用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取密码
	 *
	 * @return pwd - 密码
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * 设置密码
	 *
	 * @param pwd
	 *            密码
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * 获取状态：0正常，1停止
	 *
	 * @return is_delete - 状态：0正常，1停止
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置状态：0正常，1停止
	 *
	 * @param isDelete
	 *            状态：0正常，1停止
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * 获取备注描述
	 *
	 * @return remark - 备注描述
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注描述
	 *
	 * @param remark
	 *            备注描述
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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

	/**
	 * 获取更新人
	 *
	 * @return update_id - 更新人
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * 设置更新人
	 *
	 * @param updateId
	 *            更新人
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * 获取更新时间
	 *
	 * @return update_time - 更新时间
	 */
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateTime
	 *            更新时间
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public List<Inno72StoreFunction> getStoreFunctionList() {
		return storeFunctionList;
	}

	public void setStoreFunctionList(List<Inno72StoreFunction> storeFunctionList) {
		this.storeFunctionList = storeFunctionList;
	}

	public String[] getStoreIds() {
		return storeIds;
	}

	public void setStoreIds(String[] storeIds) {
		this.storeIds = storeIds;
	}

	public String[] getFunctionIds() {
		return functionIds;
	}

	public void setFunctionIds(String[] functionIds) {
		this.functionIds = functionIds;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public List<Inno72Store> getStoreList() {
		return storeList;
	}

	public void setStoreList(List<Inno72Store> storeList) {
		this.storeList = storeList;
	}
}