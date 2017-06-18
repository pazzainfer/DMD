package com.leven.dmd.gef.editor;

import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.ConnectionCreationTool;
/**
 * �Զ������ߴ�������
 * @author leven
 * 2012-9-3 ����02:51:56
 */
public class CustomConnectionCreationTool extends ConnectionCreationTool {
	@Override
	protected boolean handleButtonDown(int button) {
		boolean result =  super.handleButtonDown(button);
		//��������״̬���жϵ�ǰ���߰�ť�Ƿ�Ϊ����״̬�����ܽ����Σ����ռ���Ĭ�Ϲ���
		//������ť���������հ����򣬽�ת���ڼ�Ĭ�Ϲ���
		if ((AbstractTool.STATE_INITIAL == button) && (isInState(AbstractTool.STATE_INITIAL))){
            getDomain().loadDefaultTool();
		}
        return result;  
	}
}
