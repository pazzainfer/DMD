package com.leven.dmd.gef.directedit;

import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * �����༭�޸ĵ��ı���֤
 * 
 * @author lifeng 2012-7-11 ����03:01:24
 */
public class TableNameCellEditorValidator implements ICellEditorValidator {

	private ValidationMessageHandler handler;

	public TableNameCellEditorValidator(
			ValidationMessageHandler validationMessageHandler) {
		this.handler = validationMessageHandler;
	}

	public String isValid(Object value) {
		String name = (String) value;

		/*if (name.indexOf(" ") != -1) {
			String text = "�������ɰ����ո�!";
			return setMessageText(text);
		}*/

		if (name.length() == 0) {
			String text = "��������Ӧ�ð���һ���ַ�!";
			return setMessageText(text);
		}

		unsetMessageText();
		return null;

	}

	private String unsetMessageText() {
		handler.reset();
		return null;
	}

	private String setMessageText(String text) {
		handler.setMessageText(text);
		return text;
	}

}