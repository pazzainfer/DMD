package com.leven.dmd.gef.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.leven.dmd.gef.Messages;

/**
 * �ֶ�����ʵ����
 * @author lifeng
 * 2012-7-11 ����03:27:10
 */
@SuppressWarnings("serial")
public class ColumnType implements Serializable
{
	private String type;
	private String description;
	
	private static List<String> types = new ArrayList<String>();
	
	public static ColumnType VARCHAR = new ColumnType("VARCHAR",Messages.ColumnType_1); //$NON-NLS-1$
	public static ColumnType CHAR = new ColumnType("CHAR",Messages.ColumnType_3); //$NON-NLS-1$
	public static ColumnType INTEGER = new ColumnType("INTEGER",Messages.ColumnType_5); //$NON-NLS-1$
	public static ColumnType LONG = new ColumnType("LONG",Messages.ColumnType_7); //$NON-NLS-1$
	public static ColumnType NUMBER = new ColumnType("NUMBER",Messages.ColumnType_9); //$NON-NLS-1$
	public static ColumnType MONEY = new ColumnType("MONEY",Messages.ColumnType_11); //$NON-NLS-1$
	public static ColumnType DATE = new ColumnType("DATE",Messages.ColumnType_13); //$NON-NLS-1$
	public static ColumnType TIMESTAMP  = new ColumnType("TIMESTAMP",Messages.ColumnType_15); //$NON-NLS-1$
	public static ColumnType BLOB = new ColumnType("BLOB",Messages.ColumnType_17); //$NON-NLS-1$
	public static ColumnType CLOB = new ColumnType("CLOB",Messages.ColumnType_19); //$NON-NLS-1$
	
	public static String typeArray[] = new String[]{
		"VARCHAR","CHAR","INTEGER","LONG","NUMBER","MONEY","DATE","TIMESTAMP","BLOB","CLOB" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
	};
	
	public static String enTypeArray[] = new String[]{
		Messages.ColumnType_1,Messages.ColumnType_3,Messages.ColumnType_5,Messages.ColumnType_7,Messages.ColumnType_9,Messages.ColumnType_11,Messages.ColumnType_13,Messages.ColumnType_15,Messages.ColumnType_17,Messages.ColumnType_19
	};

	/**
	 * ��ȡ�ֶ����Ͷ�Ӧ��������(������-1,���ʾΪ�ҵ��ֶ�)
	 * @author leven
	 * 2012-11-1 ����09:22:44
	 * @param type
	 * @return
	 */
	public static int getTypeIndex(String type){
		if(type.equals("VARCHAR")||type.equals(Messages.ColumnType_1)){ //$NON-NLS-1$
			return 0;
		}else if(type.equals("CHAR")||type.equals(Messages.ColumnType_3)){ //$NON-NLS-1$
			return 1;
		}else if(type.equals("INTEGER")||type.equals(Messages.ColumnType_5)){ //$NON-NLS-1$
			return 2;
		}else if(type.equals("LONG")||type.equals(Messages.ColumnType_7)){ //$NON-NLS-1$
			return 3;
		}else if(type.equals("NUMBER")||type.equals(Messages.ColumnType_9)){ //$NON-NLS-1$
			return 4;
		}else if(type.equals("MONEY")||type.equals(Messages.ColumnType_11)){ //$NON-NLS-1$
			return 5;
		}else if(type.equals("DATE")||type.equals(Messages.ColumnType_13)){ //$NON-NLS-1$
			return 6;
		}else if(type.equals("TIMESTAMP")||type.equals(Messages.ColumnType_15)){ //$NON-NLS-1$
			return 7;
		}else if(type.equals("BLOB")||type.equals(Messages.ColumnType_17)){ //$NON-NLS-1$
			return 8;
		}else if(type.equals("CLOB")||type.equals(Messages.ColumnType_19)){ //$NON-NLS-1$
			return 9;
		}
		return -1;
	}
	/**
	 * ��ȡ�ֶ�����
	 * @author leven
	 * 2012-11-1 ����09:22:44
	 * @param type
	 * @return
	 */
	public static ColumnType getColumnType(String type){
		if(type.equals("CHAR")||type.equals(Messages.ColumnType_3)){ //$NON-NLS-1$
			return CHAR;
		}else if(type.equals("INTEGER")||type.equals(Messages.ColumnType_5)){ //$NON-NLS-1$
			return INTEGER;
		}else if(type.equals("LONG")||type.equals(Messages.ColumnType_7)){ //$NON-NLS-1$
			return LONG;
		}else if(type.equals("NUMBER")||type.equals(Messages.ColumnType_9)){ //$NON-NLS-1$
			return NUMBER;
		}else if(type.equals("MONEY")||type.equals(Messages.ColumnType_11)){ //$NON-NLS-1$
			return MONEY;
		}else if(type.equals("DATE")||type.equals(Messages.ColumnType_13)){ //$NON-NLS-1$
			return DATE;
		}else if(type.equals("TIMESTAMP")||type.equals(Messages.ColumnType_15)){ //$NON-NLS-1$
			return TIMESTAMP;
		}else if(type.equals("BLOB")||type.equals(Messages.ColumnType_17)){ //$NON-NLS-1$
			return BLOB;
		}else if(type.equals("CLOB")||type.equals(Messages.ColumnType_19)){ //$NON-NLS-1$
			return CLOB;
		}else{
			return VARCHAR;
		}
	}
	/**
	 * ���ֶ��Ƿ���Ҫָ������
	 * @author leven
	 * 2012-11-1 ����09:22:20
	 * @param type
	 * @return
	 */
	public static boolean hasLength(String type){
		if(type==null){
			return false;
		}
		if(type.equals("CHAR")||type.equals(Messages.ColumnType_3) || type.equals("VARCHAR")||type.equals(Messages.ColumnType_1)  //$NON-NLS-1$ //$NON-NLS-3$
				|| type.equals("NUMBER")||type.equals(Messages.ColumnType_9)){ //$NON-NLS-1$
			return true;
		}else{
			return false;
		}
	}
	/**
	 * ���ֶ������Ƿ���Ҫָ��С���㷶Χ
	 * @author leven
	 * 2012-11-1 ����09:21:50
	 * @param type
	 * @return
	 */
	public static boolean hasScale(String type){
		if(type.equals("NUMBER")||type.equals(Messages.ColumnType_9)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

	public ColumnType(String type,String description)
	{
		this.type = type;
		this.description = description;
		String typeToAdd = this.getType();
		types.add(typeToAdd);
	}

	/**
	 * @return Returns the type.
	 */
	public String getType()
	{
//		return description;
		return type;
	}

	public static boolean hasType(String type)
	{
		return types.contains(type.toUpperCase());
	}

	public static String getTypes()
	{
		StringBuffer typeBuffer = new StringBuffer();
		for (Iterator<String> iter = types.iterator(); iter.hasNext();)
		{
			typeBuffer.append(iter.next()).append(", "); //$NON-NLS-1$
		}
		if (types.size() >= 1)
		{
			typeBuffer.delete(typeBuffer.length() - 2, typeBuffer.length());
		}
		return typeBuffer.toString();
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setType(String type) {
		this.type = type;
	}
}