package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class ColumnEditWizard extends Wizard {

	private Map<String, Column> map;
	private Map<String, Column> columnMap;
	private ColumnEditWizardPage columnEditWizardPage;
	private Schema schema;
	private Column column;
	private boolean isTempCanEdit;

	public ColumnEditWizard(Schema schema, Map<String, Column> map, Map<String, Column> columnMap, Column column) {
		super();
		this.setWindowTitle(Messages.ColumnEditWizard_0);
		this.setDefaultPageImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID,ImageKeys.COLUMN_WIZARD));
		this.schema = schema;
		this.column = column;
		this.map = map;
		this.columnMap = columnMap;
		this.isTempCanEdit = column.isTempCanEdit();
	}
	

	@Override
	public void addPages() {
		columnEditWizardPage = new ColumnEditWizardPage(this,schema,column);
		addPage(columnEditWizardPage);
	}

	@Override
	public boolean performFinish() {
		Column column1 = getColumn();
		if(!column.getName().toLowerCase().equals(column1.getName().toLowerCase()) && 
				columnMap.containsKey(column1.getName().toLowerCase())){
			map.put("data", null); //$NON-NLS-1$
			return true;
		}else {
			map.put("data", column1); //$NON-NLS-1$
			return true;
		}
	}
	
	public Column getColumn(){
		Column column1 = new Column();
		if(columnEditWizardPage.getOptionFlag()==1){
			ColumnTemplate temp = columnEditWizardPage.getTemplate();
			column1.setType(temp.getColumnType().getType());
			column1.setLength(temp.getColumnLength());
			column1.setScale(temp.getColumnScale());
			column1.setName(temp.getColumnName());
			column1.setColumnTemplate(temp);
			if(isTempCanEdit){
				column1.setTempCanEdit(true);
			}else {
				column1.setTempCanEdit(false);
			}
		}else {
			String type = columnEditWizardPage.getColumnTypeCombo().getText();
			column1.setName(columnEditWizardPage.getColumnNameText().getText().trim());
			column1.setType(type);
			if(ColumnType.hasLength(type)){
				column1.setLength(Integer.parseInt(columnEditWizardPage
						.getColumnLengthText().getText().trim()));
			}else {
				column1.setLength(0);
			}
			if(ColumnType.hasScale(type)){
				column1.setLength(Integer.parseInt(columnEditWizardPage
						.getColumnScaleText().getText().trim()));
			}else{
				column1.setScale(0);
			}
		}
		column1.setCnName(columnEditWizardPage.getColumnCnNameText().getText().trim());
		column1.setDescription(columnEditWizardPage.getColumnDespText().getText().trim());
		column1.setPk(columnEditWizardPage.getYesPkButton().getSelection());
		return column1;
	}

	public ColumnEditWizardPage getColumnEditWizardPage() {
		return columnEditWizardPage;
	}
	public void setColumnEditWizardPage(ColumnEditWizardPage columnEditWizardPage) {
		this.columnEditWizardPage = columnEditWizardPage;
	}
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public Map<String, Column> getColumnMap() {
		return columnMap;
	}
	public void setColumnMap(Map<String, Column> columnMap) {
		this.columnMap = columnMap;
	}
	public boolean isTempCanEdit() {
		return isTempCanEdit;
	}
	public void setTempCanEdit(boolean isTempCanEdit) {
		this.isTempCanEdit = isTempCanEdit;
	}
}
