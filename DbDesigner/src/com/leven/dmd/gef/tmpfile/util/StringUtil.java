package com.leven.dmd.gef.tmpfile.util;

public class StringUtil {
	/**
	 * 校验字符串是否为空
	 * @author leven
	 * 2012-11-13 01:49:12
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		}
		return false;
	}
	/**
	 * 校验字符串是否为整数
	 * @author leven
	 * 2012-11-13 01:48:53
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (str.matches("[\\d]{1,}") && Integer.parseInt(str)>0) {
			return true;
		}
		return false;
	}
	/**
	 * 校验字符串是否为正确的数据库名称
	 * @param name
	 * @return
	 */
	public static boolean isRightDBName(String name) {
		if(name==null || "".equals(name)){
			return false;
		}
		String reg = ".*[\\/\\\\\\?:\\*<>|\"!^()-=\\+{}\\s]{1}.*";
		if(name.matches(reg)){
			return false;
		}else {
			return true;
		}
	}
}
