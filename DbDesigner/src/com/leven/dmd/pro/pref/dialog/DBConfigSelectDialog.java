package com.leven.dmd.pro.pref.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.provider.DBConfigTableSorter;
import com.leven.dmd.pro.pref.provider.DBConfigTableViewerContentProvider;
import com.leven.dmd.pro.pref.provider.DBConfigTableViewerLabelProvider;
import com.leven.dmd.pro.pref.util.DBConfigFileUtil;

public class DBConfigSelectDialog extends Dialog {
	private DBConfigModel dbConfigModel;
	private TableViewer viewer;
	private List<DBConfigModel> list;
	private static final String[] COLUMN_NAME = {Messages.DBConfigSelectDialog_0, Messages.DBConfigSelectDialog_1};
	private static final ColumnLayoutData[] columnLayouts = { 
			new ColumnWeightData(80), 
			new ColumnWeightData(100)
			}; 

	public DBConfigSelectDialog(Shell parentShell) {
		super(parentShell);
		this.list = DBConfigFileUtil.getFileContent();
		this.setShellStyle(SWT.RESIZE | SWT.MAX | SWT.CLOSE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText(Messages.DBConfigSelectDialog_2);
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
		viewer.setSorter(new DBConfigTableSorter());
		TableColumn column;
		for (int i = 0; i < COLUMN_NAME.length; i++) {
			column = new TableColumn(viewer.getTable(), SWT.LEFT);
			column.setText(COLUMN_NAME[i]);
			column.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					((DBConfigTableSorter) viewer.getSorter()).doSort(1);
					viewer.refresh();
				}
			});
			viewer.getTable().getColumn(i).pack();
		}
		viewer.setContentProvider(new DBConfigTableViewerContentProvider());
		viewer.setLabelProvider(new DBConfigTableViewerLabelProvider());
		viewer.setInput(list);
		table.setToolTipText(Messages.DBConfigSelectDialog_3);
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
			mb.setText(Messages.DBConfigSelectDialog_4);
			mb.setMessage(Messages.DBConfigSelectDialog_5);
			mb.open();
			return;
		}else {
			dbConfigModel = (DBConfigModel) select.getFirstElement();
		}
		super.okPressed();
	}

	public DBConfigModel getDbConfigModel() {
		return dbConfigModel;
	}
	public void setDbConfigModel(DBConfigModel dbConfigModel) {
		this.dbConfigModel = dbConfigModel;
	}
}
