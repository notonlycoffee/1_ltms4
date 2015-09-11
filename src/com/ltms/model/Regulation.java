package com.ltms.model;

import java.util.Date;

public class Regulation implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private String content;
	private Date date;
	private String author;
	private int departmentID;
	private int laboratoryID;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	public int getLaboratoryID() {
		return laboratoryID;
	}
	public void setLaboratoryID(int laboratoryID) {
		this.laboratoryID = laboratoryID;
	}
}
