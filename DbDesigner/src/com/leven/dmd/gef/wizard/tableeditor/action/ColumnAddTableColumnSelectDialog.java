package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.Iterator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class ColumnAddTableColumnSelectDialog extends Dialog {
	private Schema schema;
	private Tree tree;
	
	private Column data;

	protected ColumnAddTableColumnSelectDialog(Shell parentShell,Schema schema) {
		super(parentShell);
		this.schema = schema;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText(Messages.ColumnAddTableColumnSelectDialog_0);
		parent.setLayout(new GridLayout(1,false));
		tree = new Tree(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		TreeItem headItem = new TreeItem(tree, SWT.NONE);
		headItem.setText("Tables"); //$NON-NLS-1$
		headItem.setImage(Activator.getImage(ImageKeys.TABLE_LIST));
		Table table;
		Column column;
		TreeItem item;
		TreeItem childItem;
		for(Iterator<Table> it = schema.getTables().iterator();it.hasNext();){
			table = it.next();
			item = new TreeItem(headItem, SWT.NONE);
			item.setText(table.getName()+"("+table.getCnName()+")"); //$NON-NLS-1$ //$NON-NLS-2$
			item.setImage(Activator.getImage(ImageKeys.TABLE_MODEL));
			for(Iterator<Column> columnIt = table.getColumns().iterator();columnIt.hasNext();){
				column = columnIt.next();
				childItem = new TreeItem(item, SWT.NONE);
				childItem.setText(column.getLabelText());
				childItem.setImage(Activator.getImage(ImageKeys.COLUMN_MODEL));
				childItem.setData("data", column); //$NON-NLS-1$
			}
		}
		tree.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ColumnAddTableColumnSelectDialog.this.okPressed();
			}
		});
		return tree;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500,300);
	}

	@Override
	protected void okPressed() {
		TreeItem[] items = tree.getSelection();
		if(items.length>0){
			Object obj = items[0].getData("data"); //$NON-NLS-1$
			if(obj!=null && (obj instanceof Column)){
				Column c = (Column)obj;
				this.setData(c);
			}
		}
		super.okPressed();
	}

	public Column getData() {
		return data;
	}
	public void setData(Column data) {
		this.data = data;
	}
}
