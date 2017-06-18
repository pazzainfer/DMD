package com.leven.dmd.gef.wizard;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
/**
 * �༭���j��
 * @author lifeng
 * 2012-8-15 ����01:36:48
 */
public class RelationshipEditWizard extends Wizard {
	private Table forignTable;
	private Table primaryTable;
	private String forignColumn;
	private String primaryColumn;
	Map<String,String> columnMap = new HashMap<String,String>();

	public RelationshipEditWizard(Table forignTable,Table primaryTable,
			Map<String,String> columnMap){
		super();
		this.forignTable = forignTable;
		this.primaryTable = primaryTable;
		this.setForignColumn(columnMap.get("target")); //$NON-NLS-1$
		this.setPrimaryColumn(columnMap.get("source")); //$NON-NLS-1$
		this.columnMap = columnMap;
		this.setWindowTitle(forignTable.getName() + " --> " + primaryTable.getName() +  //$NON-NLS-1$
				 Messages.RelationshipEditWizard_3);
		this.setDefaultPageImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.COLUMN_WIZARD));
	}
	@Override
	public void addPages() {
		RelationshipEditWizardPage page = new RelationshipEditWizardPage(this,"page1",forignTable,primaryTable); //$NON-NLS-1$
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		columnMap.remove("foreignColumn"); //$NON-NLS-1$
		columnMap.remove("primaryColumn"); //$NON-NLS-1$
		columnMap.put("target", forignColumn); //$NON-NLS-1$
		columnMap.put("source", primaryColumn); //$NON-NLS-1$
		return true;
	}
	
	public Table getForignTable() {
		return forignTable;
	}
	public void setForignTable(Table forignTable) {
		this.forignTable = forignTable;
	}
	public Table getPrimaryTable() {
		return primaryTable;
	}
	public void setPrimaryTable(Table primaryTable) {
		this.primaryTable = primaryTable;
	}
	public String getForignColumn() {
		return forignColumn;
	}
	public void setForignColumn(String forignColumn) {
		this.forignColumn = forignColumn;
	}
	public String getPrimaryColumn() {
		return primaryColumn;
	}
	public void setPrimaryColumn(String primaryColumn) {
		this.primaryColumn = primaryColumn;
	}
}