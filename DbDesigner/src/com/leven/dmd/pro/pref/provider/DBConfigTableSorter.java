package com.leven.dmd.pro.pref.provider;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.leven.dmd.pro.pref.model.DBConfigModel;

public class DBConfigTableSorter  extends ViewerSorter {
	
	private static final int ASCENDING = 0;
	private static final int DESCENDING = 1;
	private int order;// �ж��������ǽ���
	private int column;// �ж��������

	public void doSort(int column) {
		// �����ͬһ�У��ı����е�˳��
		if (column == this.column) {
			order = 1 - order;
		} else {// �������һ�У���Ĭ��Ϊ��������
			this.column = column;
			order = ASCENDING;
		}
	}

	// ���Ǹ���ķ��������رȽϽ����-1,0,1�������
	public int compare(Viewer viewer, Object e1, Object e2) {
		int result = 0;

		DBConfigModel p1 = (DBConfigModel) e1;
		DBConfigModel p2 = (DBConfigModel) e2;
		// Ĭ��������
		switch (column) {
		case 0:
			result = collator.compare(p1.getName()==null?"":p1.getName(), 
					p2.getName()==null?"":p2.getName());
			break;
		case 1:
			result = collator.compare(p1.getType()==null?"":p1.getType(), 
					p2.getType()==null?"":p2.getType());
			break;
		}
		// �����ʱΪ����
		if (order == DESCENDING)
			result = -result;
		return result;
	}
}
