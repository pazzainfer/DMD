package com.leven.dmd.gef.wizard.tableeditor;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.leven.dmd.gef.model.Column;
/**
 * ������������
 * @author leven
 * 2012-8-30 ����06:18:06
 */
public class TableSorter extends ViewerSorter {
	
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

		Column p1 = (Column) e1;
		Column p2 = (Column) e2;
		// Ĭ��������
		switch (column) {
		case 0:
			return 0;
		case 1:
			result = collator.compare(p1.getName()==null?"":p1.getName(), 
					p2.getName()==null?"":p2.getName());
			break;
		case 2:
			result = collator.compare(p1.getCnName()==null?"":p1.getCnName(), 
					p2.getCnName()==null?"":p2.getCnName());
			break;
		case 3:
			result = collator.compare(p1.getColumnTemplate()==null?"":p1.getColumnTemplate().getName(), 
					p2.getColumnTemplate()==null?"":p2.getColumnTemplate().getName());
			break;
		case 4:
			result = collator.compare(p1.getType()==null?"":p1.getType(), 
					p2.getType()==null?"":p2.getType());
			break;
		case 5:
			int ret = p1.getLength() - p2.getLength();
			if(ret == 0){
				result = 0;
			} else if(ret > 0){
				result = 1;
			} else {
				result = -1;
			}
			break;
		case 6:
			int ret1 = p1.getScale() - p2.getScale();
			if(ret1 == 0){
				result = 0;
			} else if(ret1 > 0){
				result = 1;
			} else {
				result = -1;
			}
			break;
		case 7:
			result = (p1.isPk()?1:0) - (p2.isPk()?1:0);
			break;
		}
		// ����ʱΪ����
		if (order == DESCENDING)
			result = -result;
		return result;
	}
}
