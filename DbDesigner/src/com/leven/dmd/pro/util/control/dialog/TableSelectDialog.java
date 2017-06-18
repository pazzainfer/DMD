package com.leven.dmd.pro.util.control.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TableIndex;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.util.widget.TableSelectTreeViewer;

public class TableSelectDialog extends Dialog {
	private TreeViewer viewer;
	private Schema schema;
	private Table data;

	public TableSelectDialog(Shell parentShell,Schema schema) {
		super(parentShell);
		this.schema = schema;
		this.setShellStyle(SWT.RESIZE | SWT.MAX | SWT.CLOSE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText(Messages.TableSelectDialog_0);
		Composite comp = new Composite(parent,SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp.setLayout(new FillLayout());
		viewer = new TableSelectTreeViewer(comp, schema, false);
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
			mb.setMessage(Messages.TableSelectDialog_1);
			mb.setText(Messages.TableSelectDialog_2);
			mb.open();
			return;
		}else {
			Object obj = ((TreeSelection)viewer.getSelection()).getFirstElement();
			if(!(obj instanceof Table)){
				MessageBox mb = new MessageBox(getShell(),SWT.ICON_ERROR);
				mb.setMessage(Messages.TableSelectDialog_3);
				mb.setText(Messages.TableSelectDialog_2);
				mb.open();
				return;
			}else {
				Table table = (Table)obj;
				Table newTable = new Table(table.getName(), schema); //$NON-NLS-1$
				newTable.setCnName(table.getCnName());
				newTable.setSeqno(table.getSeqno());
				newTable.setPath(table.getPath());
				newTable.setQuotePath(table.getQuotePath());
				newTable.setDescription(table.getDescription());
				newTable.setTablespace(table.getTablespace());
				newTable.setOtherSql(table.getOtherSql());
				Map<String,Column> columnMap = new HashMap<String,Column>();
				ArrayList<Column> columns = new ArrayList<Column>();
				ArrayList<TableIndex> indexs = new ArrayList<TableIndex>();
				Column temp;
				for(Column c : table.getColumns()){
					temp = new Column(c.getName(), c.getType(), c.getLength(), c.getCnName(), c.isPk());
					temp.setScale(c.getScale());
					temp.setColumnTemplate(c.getColumnTemplate());
					temp.setTempCanEdit(temp.isTempCanEdit());
					columnMap.put(temp.getName(), temp);
					columns.add(temp);
				}
				TableIndex t;
				for(TableIndex ti : table.getIndexes()){
					t = new TableIndex(ti.getName(), ti.getColumns(), ti.getType(), ti.getComments());
					indexs.add(t);
				}
				newTable.setColumnMap(columnMap);
				newTable.setColumns(columns);
				newTable.setIndexes(indexs);
				setData(newTable);
			}
		}
		super.okPressed();
	}

	public Table getData() {
		return data;
	}
	public void setData(Table data) {
		this.data = data;
	}
}
