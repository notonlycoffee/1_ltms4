package com.ltms.util;

public class StringUtil {
	public static String trimString(String oldStr, int size){
		String newStr = oldStr;
		if(oldStr.length() > size){
			newStr = oldStr.substring(0, size) + "...";
		}
		return newStr;
	}
	
	public static String getStartNum(String groupInfo){
		if(groupInfo.equals("")){
			return "";
		}else{
			int pos = groupInfo.indexOf('号');
			return groupInfo.substring(0, pos);
		}
	}
	
	public static String getEndNum(String groupInfo){
		if(groupInfo.equals("")){
			return "";
		}else{
			int pos = groupInfo.indexOf('-');
			int pos2 = groupInfo.lastIndexOf('号');
			return groupInfo.substring(pos+2, pos2);
		}
	}
}
