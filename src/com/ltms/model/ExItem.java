package com.ltms.model;

public class ExItem implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int scheduleID;   // 教学进度表ID int(8)
	private String week;  //周次 varchar(2)
	private String itemName;   //实验项目名称 varchar(50)
	private String comment;    //备注  varchar(50)
	private int laboratoryID;
	public int getLaboratoryID() {
		return laboratoryID;
	}
	public void setLaboratoryID(int laboratoryID) {
		this.laboratoryID = laboratoryID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
