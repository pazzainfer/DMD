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

public class ColumnAddWizard extends Wizard {

	private Map<String, Column> map;
	private Map<String, Column> columnMap;
	private ColumnAddWizardPage columnAddWizardPage;
	private Schema schema;

	public ColumnAddWizard(Schema schema, Map<String, Column> map, Map<String, Column> columnMap) {
		super();
		this.setWindowTitle(Messages.ColumnAddWizard_0);
		this.setDefaultPageImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID,ImageKeys.COLUMN_WIZARD));
		this.schema = schema;
		this.map = map;
		this.columnMap = columnMap;
	}
	

	@Override
	public void addPages() {
		columnAddWizardPage = new ColumnAddWizardPage(this,schema);
		addPage(columnAddWizardPage);
	}

	@Override
	public boolean performFinish() {
		Column column1 = getColumn();
		if(columnMap.containsKey(column1.getName().toLowerCase())){
			map.put("data", null); //$NON-NLS-1$
			return true;
		}else {
			map.put("data", column1); //$NON-NLS-1$
			return true;
		}
	}
	
	public Column getColumn(){
		Column column1 = new Column();
		if(columnAddWizardPage.getOptionFlag()==1){
			ColumnTemplate temp = columnAddWizardPage.getTemplate();
			column1.setType(temp.getColumnType().getType());
			column1.setLength(temp.getColumnLength());
			column1.setScale(temp.getColumnScale());
			column1.setName(columnAddWizardPage.getColumnNameText().getText().trim());
			column1.setColumnTemplate(temp);
			column1.setTempCanEdit(true);
		}else {
			String type = columnAddWizardPage.getColumnTypeCombo().getText();
			column1.setName(columnAddWizardPage.getColumnNameText().getText().trim());
			column1.setType(type);
			if(ColumnType.hasLength(type)){
				column1.setLength(Integer.parseInt(columnAddWizardPage
						.getColumnLengthText().getText().trim()));
			}else {
				column1.setLength(0);
			}
			if(ColumnType.hasScale(type)){
				column1.setScale(Integer.parseInt(columnAddWizardPage
						.getColumnScaleText().getText().trim()));
			}else{
				column1.setScale(0);
			}
		}
		column1.setCnName(columnAddWizardPage.getColumnCnNameText().getText().trim());
		column1.setDescription(columnAddWizardPage.getColumnDespText().getText().trim());
		column1.setPk(columnAddWizardPage.getYesPkButton().getSelection());
		return column1;
	}

	public ColumnAddWizardPage getColumnAddWizardPage() {
		return columnAddWizardPage;
	}
	public void setColumnAddWizardPage(ColumnAddWizardPage columnAddWizardPage) {
		this.columnAddWizardPage = columnAddWizardPage;
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
}
