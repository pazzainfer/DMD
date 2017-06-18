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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.gef.tmpfile.model.SchemaTemplate;

public class ColumnAddTemplateSelectDialog extends Dialog {
	private SchemaTemplate schemaTemplate;
	private Table table;
	private ColumnTemplate data;

	protected ColumnAddTemplateSelectDialog(Shell parentShell,SchemaTemplate schemaTemplate) {
		super(parentShell);
		this.schemaTemplate = schemaTemplate;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText(Messages.ColumnAddTemplateSelectDialog_0);
		parent.setLayout(new GridLayout(1,false));
		table = new Table(parent, SWT.FULL_SELECTION | SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true); 
		TableItem item;
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnAddTemplateSelectDialog_1);
		tableColumn.setWidth(100);
		tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnAddTemplateSelectDialog_2);
		tableColumn.setWidth(80);
		tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnAddTemplateSelectDialog_3);
		tableColumn.setWidth(80);
		tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnAddTemplateSelectDialog_4);
		tableColumn.setWidth(70);
		tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnAddTemplateSelectDialog_5);
		tableColumn.setWidth(70);
		tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnAddTemplateSelectDialog_6);
		tableColumn.setWidth(150);
		ColumnTemplate temp;
		for(Iterator<ColumnTemplate> it = schemaTemplate.getColumnTemplates().iterator();it.hasNext();){
			temp = it.next();
			item = new TableItem(table, SWT.CHECK);
				item.setText(new String[]{temp.getColumnName(),temp.getColumnCnName(),
						temp.getColumnType().getType(),temp.getColumnLength()+"", //$NON-NLS-1$
						temp.getColumnScale()+"",temp.getDescription()}); //$NON-NLS-1$
		}
		table.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ColumnAddTemplateSelectDialog.this.okPressed();
			}
		});
		return table;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500,300);
	}

	@Override
	protected void okPressed() {
		TableItem[] items = table.getSelection();
		if(items.length>0){
			this.setData(schemaTemplate.getColumnTemplates().get(table.getSelectionIndex()));
		}
		super.okPressed();
	}

	public ColumnTemplate getData() {
		return data;
	}
	public void setData(ColumnTemplate data) {
		this.data = data;
	}
	public SchemaTemplate getSchemaTemplate() {
		return schemaTemplate;
	}
	public void setSchemaTemplate(SchemaTemplate schemaTemplate) {
		this.schemaTemplate = schemaTemplate;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
}
