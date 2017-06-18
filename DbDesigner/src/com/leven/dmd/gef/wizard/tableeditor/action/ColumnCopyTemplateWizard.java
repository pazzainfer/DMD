package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class ColumnCopyTemplateWizard extends Wizard {

	private Schema schema;
	private Map<String,Column> columnMap;
	private TableViewer viewer;
	private ColumnCopyTemplateWizardPage page;
	
	private ArrayList<ColumnTemplate> columnTemplates = new ArrayList<ColumnTemplate>();
	
	public ColumnCopyTemplateWizard(Schema schema
			,TableViewer viewer,Map<String,Column> columnMap) {
		super();
		this.setWindowTitle(Messages.ColumnCopyTemplateWizard_0);
		this.setDefaultPageImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.COLUMN_WIZARD));
		this.schema = schema;
		this.columnMap = columnMap;
		this.viewer = viewer;
		columnTemplates = schema.getSchemaTemplate().getColumnTemplates();
	}
	@Override
	public boolean performFinish() {
		TableItem item = page.getTable().getSelection()[0];
		Column c = new Column();
		c.setName(item.getText(0));
		c.setCnName(item.getText(1));
		c.setType(item.getText(2));
		c.setLength(item.getText(3).equals("")? //$NON-NLS-1$
				0:Integer.parseInt(item.getText(3)));
		c.setScale(item.getText(4).equals("")? //$NON-NLS-1$
				0:Integer.parseInt(item.getText(4)));
		c.setColumnTemplate(columnTemplates.get(page.getTable().getSelectionIndex()));
		if(columnMap.containsKey(c.getName()) || columnMap.containsKey(c.getName().toLowerCase())){
			MessageBox mb = new MessageBox(this.getShell(),SWT.ICON_WARNING);
			mb.setText(Messages.ColumnCopyTemplateWizard_3);
			mb.setMessage(Messages.ColumnCopyTemplateWizard_4+c.getName()+Messages.ColumnCopyTemplateWizard_5);
			mb.open();
			return false;
		}
		c.setTempCanEdit(false);
		ArrayList columnList = ((ArrayList)viewer.getInput());
		viewer.add(c);
	    columnList.add(c);
	    columnMap.put(c.getName(), c);
		return true;
	}
	@Override
	public void addPages() {
		page = new ColumnCopyTemplateWizardPage(this);
		this.addPage(page);
	}
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public ColumnCopyTemplateWizardPage getPage() {
		return page;
	}
	public void setPage(ColumnCopyTemplateWizardPage page) {
		this.page = page;
	}
	public Map<String, Column> getColumnMap() {
		return columnMap;
	}
	public void setColumnMap(Map<String, Column> columnMap) {
		this.columnMap = columnMap;
	}
	public ArrayList<ColumnTemplate> getColumnTemplates() {
		return columnTemplates;
	}
	public void setColumnTemplates(ArrayList<ColumnTemplate> columnTemplates) {
		this.columnTemplates = columnTemplates;
	}
}
