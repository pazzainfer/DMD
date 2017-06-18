package com.leven.dmd.pro.pref.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.domain.DBTypeConstant;
import com.leven.dmd.pro.pref.provider.DBConfigTableViewerContentProvider;

public class DBTypeSelectDialog extends Dialog {
	private String dbType;
	private TableViewer viewer;
	private List<String> list;
	private static final String[] COLUMN_NAME = {Messages.DBTypeSelectDialog_0};
	private static final ColumnLayoutData[] columnLayouts = { 
			new ColumnWeightData(100)
			}; 

	public DBTypeSelectDialog(Shell parentShell) {
		super(parentShell);
		this.list = new ArrayList<String>();
		for(String temp : DBTypeConstant.dbTypeArray){
			list.add(temp);
		}
		this.setShellStyle(SWT.RESIZE | SWT.MAX | SWT.CLOSE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText(Messages.DBTypeSelectDialog_1);
		Composite comp = new Composite(parent,SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp.setLayout(new GridLayout(1,false));
		final Table table = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.SINGLE);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer = new TableViewer(table);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		TableLayout layout = new TableLayout();
		for(int i=0;i<columnLayouts.length;i++){
			layout.addColumnData(columnLayouts[i]);
		}
		viewer.getTable().setLayout(layout);
		TableColumn column;
		for (int i = 0; i < COLUMN_NAME.length; i++) {
			column = new TableColumn(viewer.getTable(), SWT.LEFT);
			column.setText(COLUMN_NAME[i]);
			viewer.getTable().getColumn(i).pack();
		}
		viewer.setContentProvider(new DBConfigTableViewerContentProvider());
		viewer.setLabelProvider(new ITableLabelProvider(){
			public void addListener(ILabelProviderListener listener) {}
			public void dispose() {}
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			public void removeListener(ILabelProviderListener listener) {}
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
			public String getColumnText(Object element, int columnIndex) {
				return (String)element;
			}
		});
		viewer.setInput(list);
		table.setToolTipText(Messages.DBTypeSelectDialog_2);
		viewer.setColumnProperties(COLUMN_NAME);
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent arg0) {
				okPressed();
			}
		});
		return comp;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(400,300);
	}

	@Override
	protected void okPressed() {
		StructuredSelection select = (StructuredSelection) viewer
			.getSelection();
		if(select.isEmpty()){
			MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			mb.setText(Messages.DBTypeSelectDialog_3);
			mb.setMessage(Messages.DBTypeSelectDialog_4);
			mb.open();
			return;
		}else {
			dbType = (String) select.getFirstElement();
		}
		super.okPressed();
	}

	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
}
