package com.leven.dmd.pro.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FileUtil {

	/**
	 * �����ļ���д���ַ�������,���سɹ���ʶ
	 * @author leven
	 * 2012-12-12 ����02:02:32
	 * @param fileContent �ļ�����
	 * @param savePath �ļ�·��
	 * @return
	 */
	public static boolean makeNewFileWithContent(String fileContent,String savePath){
		if (savePath != null) {
			FileWriter fw = null;
			try {
				File file = new File(savePath);
				if(!file.exists()){
					file.createNewFile();
				}
				fw = new FileWriter(file);
				fw.write(fileContent);
				fw.flush();
			} catch (IOException e) {
				return false;
			} finally{
				if(fw != null){
					try {
						fw.close();
					} catch (IOException e) {
						return false;
					}
				}
			}
		}else {
			return false;
		}
		return true;
	}
	/**
	 * ��ָ���ļ��ж�ȡ����
	 * @author leven
	 * 2012-12-12 ����04:35:28
	 * @param filePath
	 * @return
	 */
	public static Object readObjectFromFile(String filePath){
		if(filePath==null){
			return null;
		}
		Object obj = null;
		try{
			File file = new File(filePath);
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			obj = ois.readObject();
			ois.close();
		}catch (Exception e){
			return null;
		}
		return obj;
	}
}
