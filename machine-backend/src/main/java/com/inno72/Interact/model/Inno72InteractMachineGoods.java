package com.inno72.Interact.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_interact_machine_goods")
public class Inno72InteractMachineGoods {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 活动ID
	 */
	@Column(name = "interact_machine_id")
	private String interactMachineId;

	/**
	 * 机器ID
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * 数量
	 */
	private Integer number;

	/**
	 * 排序
	 */
	private Integer seq;

	/**
	 * 开始时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "start_time")
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "end_time")
	private LocalDateTime endTime;

	/**
	 * 类型
	 */
	private Integer state;

	/**
	 * 类型
	 */
	private Integer type;

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
	 * 获取活动ID
	 *
	 * @return interact_machine_id - 活动ID
	 */
	public String getInteractMachineId() {
		return interactMachineId;
	}

	/**
	 * 设置活动ID
	 *
	 * @param interactMachineId
	 *            活动ID
	 */
	public void setInteractMachineId(String interactMachineId) {
		this.interactMachineId = interactMachineId;
	}

	/**
	 * 获取机器ID
	 *
	 * @return goods_id - 机器ID
	 */
	public String getGoodsId() {
		return goodsId;
	}

	/**
	 * 设置机器ID
	 *
	 * @param goodsId
	 *            机器ID
	 */
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	/**
	 * 获取数量
	 *
	 * @return number - 数量
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * 设置数量
	 *
	 * @param number
	 *            数量
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * 获取排序
	 *
	 * @return seq - 排序
	 */
	public Integer getSeq() {
		return seq;
	}

	/**
	 * 设置排序
	 *
	 * @param seq
	 *            排序
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	/**
	 * 获取开始时间
	 *
	 * @return start_time - 开始时间
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * 设置开始时间
	 *
	 * @param startTime
	 *            开始时间
	 */
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * 获取结束时间
	 *
	 * @return end_time - 结束时间
	 */
	public LocalDateTime getEndTime() {
		return endTime;
	}

	/**
	 * 设置结束时间
	 *
	 * @param endTime
	 *            结束时间
	 */
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}