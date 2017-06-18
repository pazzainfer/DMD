package com.leven.dmd.gef.directedit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * ͼ��ֱ�ӱ༭������
 * 
 * @author lifeng 2012-7-11 ����02:58:02
 */
public class ExtendedDirectEditManager extends DirectEditManager {

	Font figureFont;
	protected VerifyListener verifyListener;
	protected Label label;
	protected String originalValue;
	private boolean committing = false;
	private ICellEditorValidator validator = null;

	/**
	 * ����һ��ActivityDirectEditManager����
	 * 
	 * @param source
	 * @param editorType
	 * @param locator
	 * @param label
	 * @param validator
	 */
	public ExtendedDirectEditManager(GraphicalEditPart source,
			Class editorType, CellEditorLocator locator, Label label,
			ICellEditorValidator validator) {
		super(source, editorType, locator);
		this.label = label;
		this.originalValue = label.getText();
		this.validator = validator;
	}

	protected void bringDown() {
		Font disposeFont = figureFont;
		figureFont = null;
		super.bringDown();
		if (disposeFont != null)
			disposeFont.dispose();
	}

	protected void initCellEditor() {

		Text text = (Text) getCellEditor().getControl();

		verifyListener = new VerifyListener() {

			/**
			 * �޸ı༭ͼ�εĴ�С����Ӧ�ı��ı仯
			 */
			public void verifyText(VerifyEvent event) {
				Text text = (Text) getCellEditor().getControl();
				String oldText = text.getText();
				String leftText = oldText.substring(0, event.start);
				String rightText = oldText.substring(event.end,
						oldText.length());
				GC gc = new GC(text);
				if (leftText == null)
					leftText = "";
				if (rightText == null)
					rightText = "";

				String s = leftText + event.text + rightText;

				Point size = gc.textExtent(leftText + event.text + rightText);

				gc.dispose();
				if (size.x != 0)
					size = text.computeSize(size.x, SWT.DEFAULT);
				else {
					size.x = size.y;
				}
				getCellEditor().getControl().setSize(size.x, size.y);
			}

		};
		text.addVerifyListener(verifyListener);

		originalValue = this.label.getText();
		getCellEditor().setValue(originalValue);

		IFigure figure = ((GraphicalEditPart) getEditPart()).getFigure();
		figureFont = figure.getFont();
		FontData data = figureFont.getFontData()[0];
		Dimension fontSize = new Dimension(0, data.getHeight());

		this.label.translateToAbsolute(fontSize);
		data.setHeight(fontSize.height);
		figureFont = new Font(null, data);

		getCellEditor().setValidator(validator);

		text.setFont(figureFont);
		text.selectAll();
	}

	protected void commit() {

		if (committing)
			return;
		committing = true;
		try {
			getCellEditor().getControl().setVisible(false);
			if (isDirty()) {
				CommandStack stack = getEditPart().getViewer().getEditDomain()
						.getCommandStack();
				Command command = getEditPart().getCommand(
						getDirectEditRequest());

				if (command != null && command.canExecute())
					stack.execute(command);
			}
		} finally {
			bringDown();
			committing = false;
		}
	}

	protected void unhookListeners() {
		super.unhookListeners();
		Text text = (Text) getCellEditor().getControl();
		text.removeVerifyListener(verifyListener);
		verifyListener = null;
	}

}