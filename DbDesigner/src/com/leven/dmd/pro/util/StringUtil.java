package com.leven.dmd.pro.util;

public class StringUtil {
	/**
	 * �ַ���ת����ÿ��ָ���ַ�����ִ�л��в���
	 * @author leven
	 * 2012-8-29 ����08:27:36
	 * @param source
	 * @param lineCount
	 * @return
	 */
	public static String exchange(String source,int lineCount){
		StringBuffer target = new StringBuffer();
		char[] chars = source.toCharArray();
		for(int i=1;i<chars.length+1;i++){
			char temp = chars[i-1];
			target.append(temp);
			if(i % lineCount==0){
				target.append("\n");
			}
		}
		return target.toString();
	}
	/**
	 * �ַ���ת����ÿ��ָ���ַ�����ִ�л��в����������ַ���2λ�ַ�����
	 * @author leven
	 * 2012-8-29 ����08:57:36
	 * @param source
	 * @param lineCount
	 * @return
	 */
	public static String exchangeToMultiString(String source,int lineCount){
		StringBuffer target = new StringBuffer();
		char[] chars = source.toCharArray();
		int count = 0;
		for(int i=1;i<chars.length+1;i++){
			char temp = chars[i-1];
			if(count==(lineCount-1)){
				if(isChineseChar(temp)){
					target.append("\n");
					target.append(temp);
					count = 2;
				}else {
					target.append(temp);
					target.append("\n");
					count = 0;
				}
			} else {
				target.append(temp);
				if(isChineseChar(temp)){
					count += 2;
				}else {
					count ++;
				}
				if(count % (lineCount)==0){
					target.append("\n");
					count = 0;
				}
			}
		}
		return target.toString();
	}
	public static int getNeedLineOfString(String source,int lineCount){
		StringBuffer target = new StringBuffer();
		char[] chars = source.toCharArray();
		int count = 0;
		int line = 0;
		for(int i=1;i<chars.length+1;i++){
			char temp = chars[i-1];
			if(count==(lineCount-1)){
				if(isChineseChar(temp)){
					line ++ ;
					count = 2;
				}else {
					line ++ ;
					count = 0;
				}
			} else {
				line ++ ;
				if(isChineseChar(temp)){
					count += 2;
				}else {
					count ++;
				}
				if(count % (lineCount)==0){
					line ++ ;
					count = 0;
				}
			}
		}
		return line;
	}
	/**
	 * �жϸ��ַ��Ƿ�Ϊ�����ַ�
	 * @author leven
	 * 2012-8-29 ����08:42:53
	 * @param ch
	 * @return
	 */
	public static boolean isChineseChar(char ch){
		if(new Character(ch).compareTo(new Character('\u4e00'))>=0
				&& new Character(ch).compareTo(new Character('\u9fa5'))<=0){
			return true;
		}
		return false;
	}
	/**
	 * ��ʵ������(�շ�)ת�����»��߷ָ���ʽ�����ݱ���
	 * @author leven
	 * 2012-11-20 ����02:54:35
	 * @param entityName
	 * @return
	 */
	public static String entityToDbTableName(String entityName){
		String result = "";
		char[] chars = entityName.toCharArray();
		for(int i=0;i<chars.length;i++){
			if(i!=0 && chars[i]>='A' && chars[i]<='Z'){
				result += "_" + chars[i];
			} else {
				result += chars[i];
			}
		}
		return result.toUpperCase();
	}
}
