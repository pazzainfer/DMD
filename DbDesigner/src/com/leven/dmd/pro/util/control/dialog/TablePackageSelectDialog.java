package com.leven.dmd.pro.util.control.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.util.widget.TablePackageSelectTreeViewer;

public class TablePackageSelectDialog extends Dialog {
	private TreeViewer viewer;
	private Schema schema;
	private Object data;

	public TablePackageSelectDialog(Shell parentShell,Schema schema) {
		super(parentShell);
		this.schema = schema;
		this.setShellStyle(SWT.RESIZE | SWT.MAX | SWT.CLOSE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText(Messages.TablePackageSelectDialog_0);
		Composite comp = new Composite(parent,SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp.setLayout(new FillLayout());
		viewer = new TablePackageSelectTreeViewer(comp, schema, false);
		return comp;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(400,300);
	}
	
	@Override
	protected void okPressed() {
		if(viewer.getSelection().isEmpty()){
			MessageBox mb = new MessageBox(getShell(),SWT.ICON_ERROR);
			mb.setMessage(Messages.TablePackageSelectDialog_1);
			mb.setText(Messages.TablePackageSelectDialog_2);
			mb.open();
			return;
		}else {
			Object obj = ((TreeSelection)viewer.getSelection()).getFirstElement();
			if((obj instanceof Schema) || (obj instanceof TablePackage)){
				setData(obj);
			}else {
				MessageBox mb = new MessageBox(getShell(),SWT.ICON_ERROR);
				mb.setMessage(Messages.TablePackageSelectDialog_3);
				mb.setText(Messages.TablePackageSelectDialog_2);
				mb.open();
				return;
			}
		}
		super.okPressed();
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
