package com.ltms.model;

import java.sql.Timestamp;

public class Log implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int id ;                  // ID
	private String operator;          // 操作者
	private String event;             // 事件
	private Timestamp Dadatetime;     // 时间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Timestamp getDadatetime() {
		return Dadatetime;
	}
	public void setDadatetime(Timestamp dadatetime) {
		Dadatetime = dadatetime;
	}
}
