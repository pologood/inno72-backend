package com.inno72.app.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Inno72MachineBatchDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(name = "batch_id")
	private String batchId;

	@Column(name = "row_no")
	private int rowNo;

	@Column(name = "count")
	private int count;

	@Column(name = "type")
	private int type;

	@Column(name = "volume_count")
	private int volumeCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getVolumeCount() {
		return volumeCount;
	}

	public void setVolumeCount(int volumeCount) {
		this.volumeCount = volumeCount;
	}
}
