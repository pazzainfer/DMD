package com.leven.dmd.gef.editor.guide;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Rectangle;

import com.leven.dmd.gef.model.PropertyAwareObject;

public class ElementBase extends PropertyAwareObject implements IAdaptable {
	private static final long serialVersionUID = 1L;
	/**
	 * ����
	 */
	private Rectangle layout;
	/**
	 * ��ò���
	 * @return
	 */
	public Rectangle getLayout() {
		return layout;
	}
	public void setLayout(Rectangle layout) {
		this.layout = layout;
	}

	/**
	 * ��ֱ�򵼡�ˮƽ��
	 */
	private Guide verticalGuide, horizontalGuide;

	public Guide getVerticalGuide() {
		return verticalGuide;
	}

	/**
	 * ���õ�������
	 * @param verticalGuide
	 */
	public void setVerticalGuide(Guide verticalGuide) {
		this.verticalGuide = verticalGuide;
	}

	public Guide getHorizontalGuide() {
		return horizontalGuide;
	}

	public void setHorizontalGuide(Guide horizontalGuide) {
		this.horizontalGuide = horizontalGuide;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}
}