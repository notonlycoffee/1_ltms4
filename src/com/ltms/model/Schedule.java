package com.ltms.model;

public class Schedule {

	private int id;
	private int experimentID;
	private String classID;
	private String teacherID;
	private int groupNum;
	private String purpose;  //实验目的要求
	private String material;  //使用教材及上学期已完成的章节及内容 	 text
	private String tGrade; //开设年级(一\二\三\四)  varchar(1)
	private String tTerm; //开设学期(1\2\3\4\5\6\7\8)  varchar(1)
	private String demand; //考试\考查    varchar(2)
	private int techWeek; //教学周数	int(2)	
	private int weekTime; //周学时数	int(2)
	private int totalTime; // 总学时数	int(2)
	private int theoTime; // 理论时数	int(2)
	private int exTime; // 实验时数	int(2)
	private String term; 
	private String pdfName;
	public String getClassID() {
		return classID;
	}
	public String getDemand() {
		return demand;
	}
	public int getExperimentID() {
		return experimentID;
	}
	public int getExTime() {
		return exTime;
	}
	public int getGroupNum() {
		return groupNum;
	}
	public int getId() {
		return id;
	}
	public String getMaterial() {
		return material;
	}
	public String getPurpose() {
		return purpose;
	}
	public int getTechWeek() {
		return techWeek;
	}
	public String getTerm() {
		return term;
	}
	public String gettGrade() {
		return tGrade;
	}
	public int getTheoTime() {
		return theoTime;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public String gettTerm() {
		return tTerm;
	}
	public int getWeekTime() {
		return weekTime;
	}
	public void setClassID(String classID) {
		this.classID = classID;
	}
	public void setDemand(String demand) {
		this.demand = demand;
	}
	public void setExperimentID(int experimentID) {
		this.experimentID = experimentID;
	}
	public void setExTime(int exTime) {
		this.exTime = exTime;
	}
	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getTeacherID() {
		return teacherID;
	}
	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}
	public void setTechWeek(int techWeek) {
		this.techWeek = techWeek;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public void settGrade(String tGrade) {
		this.tGrade = tGrade;
	}
	public void setTheoTime(int theoTime) {
		this.theoTime = theoTime;
	}
	public String getPdfName() {
		return pdfName;
	}
	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public void settTerm(String tTerm) {
		this.tTerm = tTerm;
	}
	public void setWeekTime(int weekTime) {
		this.weekTime = weekTime;
	} 
}
