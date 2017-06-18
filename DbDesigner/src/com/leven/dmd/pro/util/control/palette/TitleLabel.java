package com.leven.dmd.pro.util.control.palette;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.internal.ui.palette.PaletteColorUtil;
import org.eclipse.swt.graphics.Image;

public class TitleLabel extends Label {
	protected static final Border BORDER = new MarginBorder(4, 3, 4, 3);
	protected static final Border TOOL_TIP_BORDER = new MarginBorder(0, 2,
			0, 2);

	public TitleLabel(boolean isHorizontal,String text,Image icon) {
		super(text, icon);
		setLabelAlignment(PositionConstants.LEFT);
		setBorder(BORDER);
		Label tooltip = new Label(getText());
		tooltip.setBorder(TOOL_TIP_BORDER);
		setToolTip(tooltip);
		setForegroundColor(ColorConstants.listForeground);
	}

	public IFigure getToolTip() {
		if (isTextTruncated())
			return super.getToolTip();
		return null;
	}

	protected void paintFigure(Graphics graphics) {

		// paint the gradient
		graphics.pushState();
		org.eclipse.draw2d.geometry.Rectangle r = org.eclipse.draw2d.geometry.Rectangle.SINGLETON;
		r.setBounds(getBounds());
		graphics.setForegroundColor(PaletteColorUtil.WIDGET_LIST_BACKGROUND);
		graphics.setBackgroundColor(PaletteColorUtil.WIDGET_BACKGROUND);
		graphics.fillGradient(r, true);

		// draw bottom border
		graphics.setForegroundColor(PaletteColorUtil.WIDGET_NORMAL_SHADOW);
		graphics.drawLine(r.getBottomLeft().getTranslated(0, -1), r
				.getBottomRight().getTranslated(0, -1));

		graphics.popState();

		// paint the text and icon
		super.paintFigure(graphics);

		// paint the focus rectangle around the text
		if (hasFocus()) {
			org.eclipse.draw2d.geometry.Rectangle textBounds = getTextBounds();
			// We reduce the width by 1 because FigureUtilities grows it by
			// 1 unnecessarily
			textBounds.width--;
			graphics.drawFocus(bounds.getResized(-1, -1).intersect(
					textBounds.getExpanded(getInsets())));
		}
	}
}
