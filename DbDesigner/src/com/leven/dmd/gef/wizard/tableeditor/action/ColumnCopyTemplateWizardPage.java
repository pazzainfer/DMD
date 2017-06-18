package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.Iterator;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;

public class ColumnCopyTemplateWizardPage extends WizardPage {
	private Table table;
	private ColumnCopyTemplateWizard wizard;

	protected ColumnCopyTemplateWizardPage(ColumnCopyTemplateWizard wizard) {
		super("ColumnCopyTemplateWizardPage"); //$NON-NLS-1$
		this.setTitle(Messages.ColumnCopyTemplateWizardPage_1);
		this.setMessage(Messages.ColumnCopyTemplateWizardPage_2,2);
		this.wizard = wizard;
		this.setPageComplete(false);
	}

	public void createControl(Composite parent) {
		table = new Table(parent, SWT.FULL_SELECTION | SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true); 
		TableItem item;
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnCopyTemplateWizardPage_3);
		tableColumn.setWidth(100);
		tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnCopyTemplateWizardPage_4);
		tableColumn.setWidth(80);
		tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnCopyTemplateWizardPage_5);
		tableColumn.setWidth(80);
		tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnCopyTemplateWizardPage_6);
		tableColumn.setWidth(70);
		tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnCopyTemplateWizardPage_7);
		tableColumn.setWidth(70);
		tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText(Messages.ColumnCopyTemplateWizardPage_8);
		tableColumn.setWidth(150);
		ColumnTemplate temp;
		for(Iterator<ColumnTemplate> it = wizard.getColumnTemplates().iterator();it.hasNext();){
			temp = it.next();
			item = new TableItem(table, SWT.CHECK);
			item.setText(new String[]{temp.getColumnName(),temp.getColumnCnName(),
					temp.getColumnType().getType(),temp.getColumnLength()+"", //$NON-NLS-1$
					temp.getColumnScale()+"",temp.getDescription()}); //$NON-NLS-1$
		}
		table.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelection().length>0){
					if(wizard.getColumnMap().get(table.getSelection()[0].getText(0))==null){
						ColumnCopyTemplateWizardPage.this.setPageComplete(true);
						ColumnCopyTemplateWizardPage.this.setMessage(Messages.ColumnCopyTemplateWizardPage_11, 1);
					}else {
						ColumnCopyTemplateWizardPage.this.setPageComplete(false);
						ColumnCopyTemplateWizardPage.this.setMessage(Messages.ColumnCopyTemplateWizardPage_12, 3);
					}
				}else {
					ColumnCopyTemplateWizardPage.this.setPageComplete(false);
					ColumnCopyTemplateWizardPage.this.setMessage(Messages.ColumnCopyTemplateWizardPage_13, 3);
				}
			}
		});
		
		this.setControl(table);
	}

	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
}
