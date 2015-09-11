package com.ltms.model;

public class Laboratory implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String specialty;	//开课专业
	private String name;		//实验室名称
	private String address;		//实验室地址
	private String type;		//实验室类型
	private String admin;		//负责人
	private String course;		//开设课程
	private int departmentID;	//系别ID 
	private String equipment;	//实验室设备
	private String pic1;		//照片1
	private String pic2;		//照片2
	private String pic3;		//照片3
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public int getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public String getPic1() {
		return pic1;
	}
	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}
	public String getPic2() {
		return pic2;
	}
	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}
	public String getPic3() {
		return pic3;
	}
	public void setPic3(String pic3) {
		this.pic3 = pic3;
	}
	@Override
	public String toString() {
		return "Laboratory [address=" + address + ", admin=" + admin
				+ ", course=" + course + ", departmentID=" + departmentID
				+ ", equipment=" + equipment + ", id=" + id + ", name=" + name
				+ ", pic1=" + pic1 + ", pic2=" + pic2 + ", pic3=" + pic3
				+ ", specialty=" + specialty + ", type=" + type + "]";
	}
	
}
