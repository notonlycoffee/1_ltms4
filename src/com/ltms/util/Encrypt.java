package com.ltms.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//加密类
public class Encrypt {
	
	//传入一个字符串，返回加密后的字符串
	public static String encrypt(String password){
		MessageDigest m;
		String pwd = ""; //加密后的密码
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(password.getBytes("UTF8"));
			byte s[ ] = m.digest( );
			for (int i=0; i<s.length;i++){
				pwd += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pwd;
	}
	
}
