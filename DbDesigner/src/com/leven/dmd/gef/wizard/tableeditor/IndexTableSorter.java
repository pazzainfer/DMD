package com.leven.dmd.gef.wizard.tableeditor;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.leven.dmd.gef.model.TableIndex;
/**
 * ������������
 * @author leven
 * 2012-8-30 ����06:18:06
 */
public class IndexTableSorter extends ViewerSorter {
	
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

		TableIndex p1 = (TableIndex) e1;
		TableIndex p2 = (TableIndex) e2;
		// Ĭ��������
		switch (column) {
		case 0:
			result = collator.compare(p1.getName()==null?"":p1.getName(), 
					p2.getName()==null?"":p2.getName());
			break;
		case 1:
			result = collator.compare(p1.getColumns()==null?"":p1.getColumns(), 
					p2.getColumns()==null?"":p2.getColumns());
			break;
		case 2:
			result = collator.compare(p1.getType()==null?"":p1.getType(), 
					p2.getType()==null?"":p2.getType());
			break;
		case 3:
			result = collator.compare(p1.getComments()==null?"":p1.getComments(), 
					p2.getComments()==null?"":p2.getComments());
			break;
		}
		// �����ʱΪ����
		if (order == DESCENDING)
			result = -result;
		return result;
	}
}
