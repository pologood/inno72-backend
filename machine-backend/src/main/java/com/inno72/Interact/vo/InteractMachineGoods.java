package com.inno72.Interact.vo;

import java.util.List;

public class InteractMachineGoods {

	private String interactId;

	private List<String> machines;

	private List<Inno72InteractMachineGoodsVo> goods;

	public String getInteractId() {
		return interactId;
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
	}

	public List<String> getMachines() {
		return machines;
	}

	public void setMachines(List<String> machines) {
		this.machines = machines;
	}

	public List<Inno72InteractMachineGoodsVo> getGoods() {
		return goods;
	}

	public void setGoods(List<Inno72InteractMachineGoodsVo> goods) {
		this.goods = goods;
	}

}