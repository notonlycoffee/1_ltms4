package com.ltms.model;

public class _Class implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private int grade;
	private int studentNumber;
	private int departmentID;

	public int getDepartmentID() {
		return departmentID;
	}

	public int getGrade() {
		return grade;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getStudentNumber() {
		return studentNumber;
	}

	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}

}
