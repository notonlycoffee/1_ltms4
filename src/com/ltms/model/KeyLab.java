package com.ltms.model;

public class KeyLab implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String gotoURL;
	private int sort;
	private String pic;

	public String getGotoURL() {
		return gotoURL;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getSort() {
		return sort;
	}

	public void setGotoURL(String gotoURL) {
		this.gotoURL = gotoURL;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
}
