package com.ltms.model;

public class Teacher implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private int unitID;
	private String password;
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
	public int getUnitID() {
		return unitID;
	}
	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
