package com.leven.dmd.gef.dnd;

import org.eclipse.gef.requests.CreationFactory;

import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;

/**
 * �ӵ�ɫ��ʵ�������󵽻����Ķ��󹤳�
 * @author lifeng 
 * 2012-7-11 ����03:03:27
 */
public class DataElementFactory implements CreationFactory {

	private Object template;

	public DataElementFactory(Object o) {
		template = o;
	}

	/**
	 * ��ȡ�µ�ʵ������,�ж��������ʱTable���Ͳ��Ҹö�����һ���ն���ʱ��
	 * ֱ�ӷ��ض����������򴴽�һ���µ�ʵ������
	 */
	public Object getNewObject() {
		try {
			if (template instanceof Table) {
				Table table = (Table) template;
				if (table != null && table.getName() != null) {
					return table;
				}
			}
			if (template instanceof TablePackage) {
				TablePackage table = (TablePackage) template;
				if (table != null && table.getName() != null) {
					return table;
				}
			}
			return ((Class) template).newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	public Object getObjectType() {
		return template;
	}

}