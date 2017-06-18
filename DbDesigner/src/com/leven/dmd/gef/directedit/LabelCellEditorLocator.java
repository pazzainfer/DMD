package com.leven.dmd.gef.directedit;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * ֱ�ӱ༭��ǩ��λ��
 * 
 * @author lifeng 2012-7-11 ����02:58:27
 */
public class LabelCellEditorLocator implements CellEditorLocator {

	private Label label;

	/**
	 * Ϊָ����ǩ����һ��CellEditorLocator��λ��
	 * @param label
	 */
	public LabelCellEditorLocator(Label label) {
		setLabel(label);
	}

	/**
	 * Ϊ�ؼ���ÿ����λ����չ1px�Ĵ�С
	 */
	public void relocate(CellEditor celleditor) {
		Text text = (Text) celleditor.getControl();

		Point pref = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle rect = label.getTextBounds().getCopy();
		label.translateToAbsolute(rect);
		if (text.getCharCount() > 1)
			text.setBounds(rect.x - 1, rect.y - 1, pref.x + 1, pref.y + 1);
		else
			text.setBounds(rect.x - 1, rect.y - 1, pref.y + 1, pref.y + 1);

	}

	protected Label getLabel() {
		return label;
	}

	protected void setLabel(Label label) {
		this.label = label;
	}

}