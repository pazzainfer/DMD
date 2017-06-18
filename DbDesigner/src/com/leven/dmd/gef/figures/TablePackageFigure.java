package com.leven.dmd.gef.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
/**
 * ��ͼ��
 * @author lifeng
 * 2012-7-11 ����03:24:37
 */
public class TablePackageFigure extends Figure {

	public static Color tableColor = new Color(null, 255, 255, 206);
	private Label nameLabel;

	public TablePackageFigure(Label name) {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setVertical(true);
		layout.setStretchMinorAxis(true);
		setLayoutManager(layout);
		setForegroundColor(ColorConstants.black);
		setOpaque(true);
		
		ImageFigure img = new ImageFigure(Activator.getImage(ImageKeys.PACKAGE_FIGURE));
			add(img);
		if(!name.getText().equals("")){
			nameLabel = name;
			add(name);
		}
	}


	public Label getNameLabel() {
		return nameLabel;
	}

}