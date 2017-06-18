package com.leven.dmd.gef.model;

import java.util.ArrayList;
import java.util.List;
/**
 * ��������
 * @author leven
 * 2012-9-6 ����02:10:08
 */
public class IndexType {
	private String type;
	private static List<String> types = new ArrayList<String>();

	public static IndexType NORMAL = new IndexType("Normal");
	public static IndexType UNIQUE = new IndexType("Unique");
	public static IndexType BITMAP = new IndexType("Bitmap");
	
	public static String typeArray[] = new String[]{
		"Normal","Unique","Bitmap"
	};
	
	public IndexType(String type)
	{
		this.type = type;
		String typeToAdd = this.getType();
		types.add(typeToAdd);
	}
	/**
	 * ��������������
	 * @author leven
	 * 2012-9-6 ����02:11:19
	 * @param type
	 * @return
	 */
	public static int getTypeIndex(String type){
		if(type.equals("Unique")){
			return 1;
		} else if(type.equals("Bitmap")){
			return 2;
		} else{
			return 0;
		}
	}
	

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
