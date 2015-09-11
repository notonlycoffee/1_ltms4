package com.ltms.model;

public class Experiment implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//
	private String courseID;//
	private String classInfo;//
	private String teacherInfo;  // 0-0001,0-0002,1-0319          0鏈郴鏁欏笀, 1澶栫郴鏁欏笀
	private int departmentID;//
	private int laboratoryID;//
	private String timeInfo; // 涓婅鏃堕棿淇℃伅
	private String requirement;//
	private String type;//
	private String term;//   那一学年   那一学期
	private	int state;//
	private String symc;
	private int sylb;
	private int xs;
	private String syzzy;
	private int syzlb;
	private String ssxk;
	private String mxzy;
	
	public int getSylb() {
		return sylb;
	}
	public void setSylb(int sylb) {
		this.sylb = sylb;
	}
	public int getXs() {
		return xs;
	}
	public void setXs(int xs) {
		this.xs = xs;
	}
	public int getSyzlb() {
		return syzlb;
	}
	public void setSyzlb(int syzlb) {
		this.syzlb = syzlb;
	}
	public String getSymc() {
		return symc;
	}
	public void setSymc(String symc) {
		this.symc = symc;
	}
	public String getSyzzy() {
		return syzzy;
	}
	public void setSyzzy(String syzzy) {
		this.syzzy = syzzy;
	}
	public String getSsxk() {
		return ssxk;
	}
	public void setSsxk(String ssxk) {
		this.ssxk = ssxk;
	}
	public String getMxzy() {
		return mxzy;
	}
	public void setMxzy(String mxzy) {
		this.mxzy = mxzy;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCourseID() {
		return courseID;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public String getClassInfo() {
		return classInfo;
	}
	public void setClassInfo(String classInfo) {
		this.classInfo = classInfo;
	}
	public String getTeacherInfo() {
		return teacherInfo;
	}
	public void setTeacherInfo(String teacherInfo) {
		this.teacherInfo = teacherInfo;
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
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getTimeInfo() {
		return timeInfo;
	}
	public void setTimeInfo(String timeInfo) {
		this.timeInfo = timeInfo;
	}
}
