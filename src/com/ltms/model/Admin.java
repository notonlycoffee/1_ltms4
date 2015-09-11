package com.ltms.model;

public class Admin implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String password;
	private int departmentID;
	private int role; // 0系统管理员      1各系教务员          2教师          3系主任          4实验室主任          5实验室管理员
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
}
