package com.leven.dmd.gef.wizard.tableeditor;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
/**
 * �Զ�����ı��򼰰�ť������ı���б༭��
 * ��չԭ�б༭�������ı���ͶԻ����ܶ����ݽ��б༭
 * @author leven
 * 2012-9-6 ����04:04:57
 */
public class CustomDialogCellEditor extends DialogCellEditor {

	private Text text;
	
	public CustomDialogCellEditor() {
		super();
	}

	public CustomDialogCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		String value = (String)getValue();
		
		return null;
	}

	@Override
	protected Control createContents(final Composite cell) {
		this.text = new Text(cell, SWT.NONE);
		this.text.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
					
			}
			public void focusLost(FocusEvent e) {
				doSetValue(text.getText());
			}
		});
		this.text.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					doSetValue(text.getText());
				}
			}
			public void keyReleased(KeyEvent e) {
				
			}
		});

		return this.text;
	}

	@Override
	protected void doSetFocus() {
		if (text != null) {
			text.selectAll();
			text.setFocus();
		}
	}
}
