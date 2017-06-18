package com.leven.dmd.gef.figures;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.util.IColorConstants;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
/**
 * ��ͼ��
 * @author lifeng
 * 2012-7-11 ����03:24:37
 */
public class TableFigure extends Figure {

	private ColumnsFigure columnsFigure = new ColumnsFigure();
	private Label nameLabel;
	public static final int MIN_WIDTH = 200;
	public static final int MIN_HEIGHT = 80;
	/**
	 * �Ƿ�Բ��ͼ��
	 */
	private boolean isRounded = true;

	public TableFigure(Label name, int status) {
		this(name, null,status);
	}
	/**
	 * ��ͼ��
	 * @param name ����
	 * @param status ͼ��״̬
	 * @param isRounded �Ƿ���ҪԲ��ͼ��
	 */
	public TableFigure(Label name, int status, boolean isRounded) {
		this(name, null,status);
		this.isRounded = isRounded;
	}

	public TableFigure(Label name, List colums,int status) {

		nameLabel = name;
		ToolbarLayout layout = new ToolbarLayout();
		layout.setVertical(true);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(2);
		setLayoutManager(layout);
		setBorder(new LineBorder(new Color(null,IColorConstants.TABLE_BORDER), 1));
//		setBackgroundColor(tableColor);
		setForegroundColor(ColorConstants.black);
		setOpaque(false);

		Table.changeLabelColor(name,status);
		name.setIcon(Activator.getImage(ImageKeys.TABLE_MODEL));
		add(name);
		add(columnsFigure);

	}
	/**
	 * ���ػ���������������Բ��ͼ��ѡ��
	 */
	@Override
	public void paint(Graphics graphics) {
		if (getLocalBackgroundColor() != null)
			graphics.setBackgroundColor(getLocalBackgroundColor());
		if (getLocalForegroundColor() != null)
			graphics.setForegroundColor(getLocalForegroundColor());
		if (font != null)
			graphics.setFont(font);

		graphics.pushState();
		try {
			paintFigure(graphics);
			graphics.restoreState();
			paintClientArea(graphics);
			if(isRounded){
				graphics.setForegroundColor(((LineBorder)getBorder()).getColor());
				//����Բ��ͼ�α߿�
				graphics.drawRoundRectangle(getClientArea(), 8, 8);
			}else {
				paintBorder(graphics);
			}
		} finally {
			graphics.popState();
		}
	}

	public void setSelected(boolean isSelected) {
		LineBorder lineBorder = (LineBorder) getBorder();
		lineBorder.setColor(new Color(null,IColorConstants.TABLE_BORDER));
		if (isSelected) {
			lineBorder.setWidth(1);
		} else {
			lineBorder.setWidth(1);
		}
	}


	@Override
	public Dimension getMinimumSize(int wHint, int hHint) {
		return super.getMinimumSize(MIN_WIDTH, MIN_HEIGHT);
	}

	public Label getNameLabel() {
		return nameLabel;
	}

	public ColumnsFigure getColumnsFigure() {
		return columnsFigure;
	}
}