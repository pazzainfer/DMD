package com.leven.dmd.pro.pref.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Platform;

import com.leven.dmd.pro.pref.model.DBConfigList;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.thoughtworks.xstream.XStream;

public class DBConfigFileUtil {
	/**
	 * ��ȡ����Դ������Ϣ�ļ�����,���������б�
	 * @author leven
	 * 2012-12-5 ����11:53:58
	 * @return
	 */
	public static List<DBConfigModel> getFileContent(){
		String path = Platform.getInstallLocation().getURL().getPath();
		String filePath = path.substring(1) + "workspace/dbconfig.xml";
		File file = new File(filePath);
		if(file.exists()){
			return getDBConfigFromXML(file).getConfigs();
		}
		return new ArrayList<DBConfigModel>();
	}
	/**
	 * ��������Ϣ�б�����д�뵽�����ռ��ļ�����
	 * @author leven
	 * 2012-12-5 ����11:54:26
	 * @param list
	 */
	public static void WriteDBConfigXML(List<DBConfigModel> list){
		String path = Platform.getInstallLocation().getURL().getPath();
		String filePath = path.substring(1) + "workspace/dbconfig.xml";
		DBConfigList dbConfigList = new DBConfigList();
		dbConfigList.setConfigs(list);
		WriteDBConfigXML(filePath,dbConfigList);
	}
	/**
	 * ��ָ���ļ�,���������б�
	 * @author leven
	 * 2012-12-5 ����11:55:18
	 * @param file
	 * @return
	 */
	public static DBConfigList getDBConfigFromXML(File file) {
		DBConfigList dbConfigList = null;
		FileReader input = null;
		try {
			input = new FileReader(file);
			XStream xStream = createXStrem();
			dbConfigList = (DBConfigList) xStream.fromXML(input);
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return dbConfigList;
	}
	
	private static void WriteDBConfigXML(String filePath, DBConfigList dbConfigList) {
		XStream xStream = createXStrem();
		String xml = xStream.toXML(dbConfigList);
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(xml.getBytes());
			DataOutputStream to = new DataOutputStream(new FileOutputStream(
					file));
			baos.writeTo(to);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static XStream createXStrem() {
	    XStream xStream = new XStream();
	    xStream.alias("DBConfigList", DBConfigList.class);
	    xStream.aliasField("configs", DBConfigList.class, "configs");
	    
	    xStream.alias("config", DBConfigModel.class);
	    xStream.useAttributeFor(DBConfigModel.class, "name");
	    xStream.useAttributeFor(DBConfigModel.class, "type");
	    xStream.useAttributeFor(DBConfigModel.class, "driver");
	    xStream.useAttributeFor(DBConfigModel.class, "url");
	    xStream.useAttributeFor(DBConfigModel.class, "jarPath");
	    xStream.useAttributeFor(DBConfigModel.class, "username");
	    xStream.useAttributeFor(DBConfigModel.class, "password");
	    xStream.useAttributeFor(DBConfigModel.class, "isDefault");
	    return xStream;
	  }
}
