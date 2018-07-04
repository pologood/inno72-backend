package com.inno72.model;

public class MessageBean<T> {

	private Integer eventType;
	private Integer subEventType;
	private T t;

	public enum EventType {
		// 查看状态
		CHECKSTATUS(1),
		// 更新App
		UPDATEAPP(2);

		private int v;

		private EventType(int v) {
			this.v = v;
		}

		public int v() {
			return this.v;
		}

		public static EventType get(int v) {
			for (EventType c : EventType.values()) {
				if (c.v == v) {
					return c;
				}
			}
			return null;
		}
	}

	public enum SubEventType {
		// 机器状态
		MACHINESTATUS(1),
		// app状态
		APPSTATUS(2);

		private int v;

		private SubEventType(int v) {
			this.v = v;
		}

		public int v() {
			return this.v;
		}

		public static SubEventType get(int v) {
			for (SubEventType c : SubEventType.values()) {
				if (c.v == v) {
					return c;
				}
			}
			return null;
		}
	}


	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public Integer getSubEventType() {
		return subEventType;
	}

	public void setSubEventType(Integer subEventType) {
		this.subEventType = subEventType;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

}
