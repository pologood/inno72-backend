package com.inno72.project.vo;

import java.util.List;
import java.util.Map;

import com.inno72.project.model.Inno72ActivityIndex;
import com.inno72.project.model.Inno72ActivityInfoDesc;

import lombok.Data;

@Data
public class Inno72ActivityIndexVo extends Inno72ActivityIndex{
	private List<Inno72ActivityIndex> indexList;
	private List<Inno72ActivityInfoDesc> infoList;

	public Inno72ActivityIndexVo(List<Inno72ActivityIndex> indexList, List<Inno72ActivityInfoDesc> infoList) {
		this.indexList = indexList;
		this.infoList = infoList;
	}

	public Inno72ActivityIndexVo() {
	}
}
