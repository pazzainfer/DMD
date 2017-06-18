package com.leven.dmd.gef.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TableIndex;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
/**
 * ������ݱ���
 * @author lifeng
 * 2012-8-15 ����01:36:48
 */
public class TableEditWizard extends Wizard {

	private Table table;
	private ArrayList columns = new ArrayList();
	private ArrayList indexes = new ArrayList();
	private Schema schema;
	
	private String tableName;
	private String tableCnName;
	private String tableDescription;
	private String tableSpace;
	private int tableSeqno;
	private String otherSql;
	private Map<String,Object> map;
	
	public TableEditWizard(Schema schema,Table table,Map<String,Object> map){
		super();
		this.schema = schema;
		this.table = table;
		this.map = map;
		Column temp,columnTemp;
		TableIndex indexTemp,indexTemp1;
		for(Iterator<Column> it=table.getColumns().iterator();it.hasNext();){
			temp = it.next();
			columnTemp = new Column(temp.getName(),
					ColumnType.getColumnType(temp.getType()),
					temp.getCnName(),temp.isPk());
			columnTemp.setLength(temp.getLength());
			columnTemp.setScale(temp.getScale());
			columnTemp.setCnName(temp.getCnName());
			columnTemp.setColumnTemplate(temp.getColumnTemplate());
			columns.add(columnTemp);
		}
		for(Iterator<TableIndex> it=table.getIndexes().iterator();it.hasNext();){
			indexTemp = it.next();
			indexTemp1 = new TableIndex(new String(indexTemp.getName()));
			indexTemp1.setColumns(new String(indexTemp.getColumns()));
			indexTemp1.setType(new String(indexTemp.getType()));
			indexTemp1.setComments(new String(indexTemp.getComments()));
			indexTemp1.setAutoCreated(indexTemp.isAutoCreated());
			indexes.add(indexTemp1);
		}
		tableName = new String(table.getName());
		tableCnName = new String(table.getCnName());
		otherSql = new String(table.getOtherSql());
		tableSpace = new String(table.getTablespace());
		tableDescription = new String(table.getDescription()==null?"":table.getDescription()); //$NON-NLS-1$
		tableSeqno = table.getSeqno();
		map.put("tableName",tableName); //$NON-NLS-1$
		map.put("tableCnName",tableCnName); //$NON-NLS-1$
		map.put("tableDescription",tableDescription); //$NON-NLS-1$
		map.put("tableSeqno",tableSeqno); //$NON-NLS-1$
		map.put("columns",columns); //$NON-NLS-1$
		map.put("tableSpace",tableSpace); //$NON-NLS-1$
		map.put("otherSql",otherSql); //$NON-NLS-1$
		this.setWindowTitle(Messages.TableEditWizard_5);
		this.setDefaultPageImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.TABLE_WIZARD));
	}
	@Override
	public void addPages() {
		TableEditWizardPage page = new TableEditWizardPage("page1",this); //$NON-NLS-1$
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		doSchemaChange();
		return false;
	}
	@Override
	public boolean performCancel() {
		return super.performCancel();
	}
	/**
	 * ��༭����-���µ�ģ��/��ͼ��
	 * ����ģ�����޸ı����ٽ����������ֶ��Ƴ����������µ��ֶ��б�
	 * @author lifeng
	 * void
	 * @datetime 2012-7-12 ����12:01:50
	 */
	private void doSchemaChange() {
		map.put("columns",columns); //$NON-NLS-1$
		map.put("indexes",indexes); //$NON-NLS-1$
		map.put("tableName",this.getTableName()); //$NON-NLS-1$
		map.put("tableCnName",this.getTableCnName()); //$NON-NLS-1$
		map.put("tableSpace",this.getTableSpace()); //$NON-NLS-1$
		map.put("otherSql",this.getOtherSql()); //$NON-NLS-1$
		map.put("tableDescription",this.getTableDescription()); //$NON-NLS-1$
		map.put("tableSeqno",this.getTableSeqno()); //$NON-NLS-1$
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
	public String getTableCnName() {
		return tableCnName;
	}
	public void setTableCnName(String tableCnName) {
		this.tableCnName = tableCnName;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
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
	public String getTableSpace() {
		return tableSpace;
	}
	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}
	public int getTableSeqno() {
		return tableSeqno;
	}
	public void setTableSeqno(int tableSeqno) {
		this.tableSeqno = tableSeqno;
	}
	public String getOtherSql() {
		return otherSql;
	}
	public void setOtherSql(String tableOtherSql) {
		this.otherSql = tableOtherSql;
	}
}