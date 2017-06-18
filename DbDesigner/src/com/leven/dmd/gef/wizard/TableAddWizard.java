package com.leven.dmd.gef.wizard;

import java.util.ArrayList;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
/**
 * ������ݱ���
 * @author lifeng
 * 2012-8-15 ����01:36:48
 */
public class TableAddWizard extends Wizard {

	private Table table;
	private ArrayList columns = new ArrayList();
	private ArrayList indexes = new ArrayList();
	private String tableName;
	private String tableDescription;
	private String tableSeqno;
	private Schema schema;
	private TableAddWizardPage page;
	
	public TableAddWizard(Schema schema,Table table){
		super();
		this.schema = schema;
		this.table = table;
		this.setWindowTitle(Messages.TableAddWizard_0);
		this.setDefaultPageImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.ADD_TABLE_WIZARD));
	}
	@Override
	public void addPages() {
		page = new TableAddWizardPage("page1",this); //$NON-NLS-1$
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		table.setDescription(tableDescription);
		try{
			table.setSeqno(Integer.parseInt(tableSeqno.trim()));
		}catch(Exception e){
			
		}
		columns = page.getColumns();
		for(int i=0;i<columns.size();i++){
			((Column)columns.get(i)).setTable(table);
			table.addColumn((Column)columns.get(i));
		}
		table.setStatus(IEditorConstant.TABLE_STATUS_ADDED);
		return false;
	}
	@Override
	public boolean performCancel() {
		return super.performCancel();
	}
	@Override
	public void dispose() {
		super.dispose();
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	public ArrayList getColumns() {
		return columns;
	}
	public void setColumns(ArrayList columns) {
		this.columns = columns;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public String getTableDescription() {
		return tableDescription;
	}
	public void setTableDescription(String tableDescription) {
		this.tableDescription = tableDescription;
	}
	public ArrayList getIndexes() {
		return indexes;
	}
	public void setIndexes(ArrayList indexes) {
		this.indexes = indexes;
	}
	public String getTableSeqno() {
		return tableSeqno;
	}
	public void setTableSeqno(String tableSeqno) {
		this.tableSeqno = tableSeqno;
	}
}