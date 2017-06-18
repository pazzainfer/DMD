package com.leven.dmd.gef.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.tmpfile.model.DBView;
import com.leven.dmd.gef.tmpfile.model.SchemaTemplate;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
/**
 * ͼ�μܹ���ʵ����
 * @author lifeng
 * 2012-7-11 ����03:28:34
 */
@SuppressWarnings("serial")
public class Schema extends PropertyAwareObject implements IPropertySource,INavigatorTreeNode {
	private String packagePath;

	private String name;
	/**
	 * 用以保存所有表记录
	 */
	private ArrayList<Table> allTables = new ArrayList<Table>();
	
	private ArrayList<Table> tables = new ArrayList<Table>();
	private Map<String,Table> tablesMap = new HashMap<String,Table>();
	private ArrayList<TablePackage> tablePackages = new ArrayList<TablePackage>();
	private Map<String,TablePackage> tablePackagesMap = new HashMap<String,TablePackage>();
	private ArrayList<DBView> dbViews = new ArrayList<DBView>();
	private Map<String,DBView> dbViewsMap = new HashMap<String,DBView>();
	
	private ArrayList<Tablespace> tablespaces = new ArrayList<Tablespace>();
	private Map<String,Tablespace> tablespacesMap = new HashMap<String,Tablespace>();
	
    private boolean layoutManualDesired = true;
    private boolean layoutManualAllowed = false;
    
    private DiagramRuler leftRuler,topRuler;

    private boolean rulersVisibility = true;
    private SchemaTemplate schemaTemplate = new SchemaTemplate(""); //$NON-NLS-1$
    
    private List<SchemaTemplate> schemaTemplateList = new ArrayList<SchemaTemplate>();
    /**
     * 引用外部模板路径。
     */
    private List<String> outTempPaths = new ArrayList();

	//备用属性
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;
	private Object tempField4;
	private Object tempField5;
    
    public DiagramRuler getRuler(int orientation) {
    	DiagramRuler result = null;
        switch (orientation) {
            case PositionConstants.NORTH :
               if(topRuler==null)
               topRuler = new DiagramRuler(true);
               result = topRuler;
               break;
            case PositionConstants.WEST :
               if(leftRuler==null)
               leftRuler= new DiagramRuler(false);
               result = leftRuler;
               break;
        }
        return result;
     }
     public boolean isRulerVisibility() {
        return rulersVisibility;
     }
     public void setRulerVisibility(boolean newValue) {
        rulersVisibility = newValue;
     }
     public DiagramRuler getTopRuler() {
        return topRuler;
     }
  
     public void setTopRuler(DiagramRuler topRuler) {
        this.topRuler = topRuler;
     }
	/**
	 * @param name
	 */
	public Schema(String name) {
		super();
		if (name == null)
			throw new NullPointerException("Name cannot be null"); //$NON-NLS-1$
		this.name = name;
	}
	/**
	 * 添加表
	 * @param table
	 */
	public void addTable(Table table) {
		if(table.getSchema()==null){
			table.setSchema(this);
		}
		table.setPath(table.getName());
		if(isPackageOpen()){
			tables.add(table);
			allTables.add(table);
			tablesMap.put(table.getPath(),table);
		}else {
			tables.add(table);
			tablesMap.put(table.getPath(), table);
			firePropertyChange(CHILD, null, table);
		}
		allTables.add(table);
	}

	public void addTable(Table table, int i) {
		if(table.getSchema()==null){
			table.setSchema(this);
		}
		table.setPath(table.getName());
		if(isPackageOpen()){
			tables.add(i,table);
			tablesMap.put(table.getPath(),table);
		}else {
			tables.add(i,table);
			tablesMap.put(table.getPath(), table);
			firePropertyChange(CHILD, null, table);
		}
		allTables.add(table);
	}

	public void removeTable(Table table) {
		if(table!=null){
			if(isPackageOpen()){
				tables.remove(table);
				tablesMap.remove(table.getPath());
			} else {
				tables.remove(table);
				tablesMap.remove(table.getPath());
				firePropertyChange(CHILD, table, null);
			}
			allTables.remove(table);
		}
	}
	public void addTablePackage(TablePackage tablePackage) {
		if(tablePackage.getSchema()==null){
			tablePackage.setSchema(this);
		}
		tablePackage.setPath(tablePackage.getName());
		if(isPackageOpen()){
			this.tablePackages.add(tablePackage);
			tablePackagesMap.put(tablePackage.getPath(),tablePackage);
		}else {
			tablePackages.add(tablePackage);
			tablePackagesMap.put(tablePackage.getPath(), tablePackage);
			firePropertyChange(CHILD, null, tablePackage);
		}
	}
	
	public void addTablePackage(TablePackage tablePackage, int i) {
		if(tablePackage.getSchema()==null){
			tablePackage.setSchema(this);
		}
		tablePackage.setPath(tablePackage.getName());
		if(isPackageOpen()){
			tablePackages.add(tablePackage);
			tablePackagesMap.put(tablePackage.getPath(),tablePackage);
		}else {
			tablePackages.add(i,tablePackage);
			tablePackagesMap.put(tablePackage.getPath(), tablePackage);
			firePropertyChange(CHILD, null, tablePackage);
		}
	}
	
	public void removeTablePackage(TablePackage tablePackage) {
		if(tablePackage!=null && tablePackage.getPath()!=null){
			if(isPackageOpen()){
				tablePackages.remove(tablePackage);
				tablePackagesMap.remove(tablePackage.getPath());
			} else {
				tablePackages.remove(tablePackage);
				tablePackagesMap.remove(tablePackage.getPath());
				firePropertyChange(CHILD, tablePackage, null);
			}
		}
	}
	public void addDBView(DBView dbView) {
		if(dbView.getSchema()==null){
			dbView.setSchema(this);
		}
		dbViews.add(dbView);
		dbViewsMap.put(dbView.getName(), dbView);
	}
	
	public void addDBView(DBView dbView, int i) {
		if(dbView.getSchema()==null){
			dbView.setSchema(this);
		}
		dbViews.add(i,dbView);
		dbViewsMap.put(dbView.getName(), dbView);
	}
	
	public void removeDBView(DBView dbView) {
		if(dbView!=null && dbView.getName()!=null){
			dbViews.remove(dbView);
			dbViewsMap.remove(dbView.getName());
		}
	}
	public void addTablespace(Tablespace tablespace) {
		if(tablespace.getSchema()==null){
			tablespace.setSchema(this);
		}
		tablespaces.add(tablespace);
		tablespacesMap.put(tablespace.getName(), tablespace);
	}
	
	public void addTablespace(Tablespace tablespace, int i) {
		if(tablespace.getSchema()==null){
			tablespace.setSchema(this);
		}
		tablespaces.add(i,tablespace);
		tablespacesMap.put(tablespace.getName(), tablespace);
	}
	
	public void removeTablespace(Tablespace tablespace) {
		if(tablespace!=null && tablespace.getName()!=null){
			tablespaces.remove(tablespace);
			tablespacesMap.remove(tablespace.getName());
		}
	}
	/**
	 * 是否处于包打开状态中。
	 * @return
	 */
	public boolean isPackageOpen(){
		return packagePath!=null && !"".equals(packagePath);
	}
	
	public TablePackage getOpenPackage(){
		if(isPackageOpen()){
			return tablePackagesMap.get(packagePath);
		}
		return null;
	}
	
	public void commitPackageOpen(TablePackage tablePackage){
		setPackagePath(tablePackage.getPath());
		List list = new ArrayList();
		list.addAll(tablePackage.getTables());
		list.addAll(tablePackage.getTablePackages());
		firePropertyChange(REFRESH, null, list);
	}
	/**
	 * ����������ҳ��ʱ���ø÷���
	 * @author leven
	 * 2012-11-6 ����03:58:26
	 */
	public void commitPackageClose(){
		TablePackage tablePackage = getOpenPackage();
		if(tablePackage!=null){
			Object parent = tablePackage.getParent();
			if(parent instanceof Schema){
				setPackagePath(null);
				List list = new ArrayList();
				list.addAll(getTables());
				list.addAll(getTablePackages());
				firePropertyChange(REFRESH, null, list);
			}else {
				TablePackage pPackage = (TablePackage)parent;
				setPackagePath(pPackage.getPath());
				List list = new ArrayList();
				list.addAll(pPackage.getTables());
				list.addAll(pPackage.getTablePackages());
				firePropertyChange(REFRESH, null, list);
			}
		}
	}
	/**
	 * 返回主画布最顶层
	 */
	public void commitGoTop(){
		setPackagePath(null);
		List list = new ArrayList();
		list.addAll(getTables());
		list.addAll(getTablePackages());
		firePropertyChange(REFRESH, null, list);
	}
	
	/**
	 * @return the Tables for the current schema
	 */
	public ArrayList<Table> getTables() {
		return tables;
	}

	/**
	 * @return the name of the schema
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param layoutManualAllowed
	 *            The layoutManualAllowed to set.
	 */
	public void setLayoutManualAllowed(boolean layoutManualAllowed) {
		this.layoutManualAllowed = layoutManualAllowed;
	}

	/**
	 * @return Returns the layoutManualDesired.
	 */
	public boolean isLayoutManualDesired() {
		return layoutManualDesired;
	}

	/**
	 * @param layoutManualDesired
	 *            The layoutManualDesired to set.
	 */
	public void setLayoutManualDesired(boolean layoutManualDesired) {
		this.layoutManualDesired = layoutManualDesired;
		firePropertyChange(LAYOUT, null, new Boolean(layoutManualDesired));
	}

	/**
	 * @return Returns whether we can lay out individual tables manually using
	 *         the XYLayout
	 */
	public boolean isLayoutManualAllowed() {
		return layoutManualAllowed;
	}
	
	public ArrayList<TablePackage> getTablePackages() {
		return tablePackages;
	}
	public void setTablePackages(ArrayList<TablePackage> tablePackages) {
		this.tablePackages = tablePackages;
	}
	public Object getEditableValue() {
		return null;
	}
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[]{
				new PropertyDescriptor("name",Messages.Schema_1) //$NON-NLS-1$
		};
	}
	public Object getPropertyValue(Object id) {
		if(id.equals("name")){ //$NON-NLS-1$
			return this.name;
		}
		return null;
	}
	public boolean isPropertySet(Object id) {
		if(id.equals("name")){ //$NON-NLS-1$
			return true;
		}
		return false;
	}
	public void resetPropertyValue(Object id) {
		
	}
	public void setPropertyValue(Object id, Object value) {
		
	}
	
	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}
	public DiagramRuler getLeftRuler() {
		return leftRuler;
	}
	public void setLeftRuler(DiagramRuler leftRuler) {
		this.leftRuler = leftRuler;
	}
	public boolean isRulersVisibility() {
		return rulersVisibility;
	}
	public void setRulersVisibility(boolean rulersVisibility) {
		this.rulersVisibility = rulersVisibility;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SchemaTemplate getSchemaTemplate() {
		return schemaTemplate;
	}
	public void setSchemaTemplate(SchemaTemplate schemaTemplate) {
		this.schemaTemplate = schemaTemplate;
	}
	public ArrayList<DBView> getDbViews() {
		return dbViews;
	}
	public void setDbViews(ArrayList<DBView> dbViews) {
		this.dbViews = dbViews;
	}
	public Map<String, DBView> getDbViewsMap() {
		return dbViewsMap;
	}
	public void setDbViewsMap(Map<String, DBView> dbViewsMap) {
		this.dbViewsMap = dbViewsMap;
	}
	public Table getTableByPath(String path){
		return this.tablesMap.get(path);
	}
	public TablePackage getPackageByPath(String path){
		return this.tablePackagesMap.get(path);
	}
	public String getPackagePath() {
		return packagePath;
	}
	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
	
	public INavigatorTreeNode getParent() {
		return null;
	}
	public List getChildren() {
		return null;
	}
	public void setChildren(List children) {
		
	}
	public Object getRoot() {
		return this;
	}
	public Object getData() {
		return null;
	}
	public boolean hasChildren() {
		return true;
	}
	public int getNodeType() {
		return INavigatorNodeTypeConstants.TYPE_ROOT;
	}
	public Map<String, Table> getTablesMap() {
		return tablesMap;
	}
	public void setTablesMap(Map<String, Table> tablesMap) {
		this.tablesMap = tablesMap;
	}
	public Map<String, TablePackage> getTablePackagesMap() {
		return tablePackagesMap;
	}
	public void setTablePackagesMap(Map<String, TablePackage> tablePackagesMap) {
		this.tablePackagesMap = tablePackagesMap;
	}
	public ArrayList<Table> getAllTables() {
		return allTables;
	}
	public void setAllTables(ArrayList<Table> allTables) {
		this.allTables = allTables;
	}
	public List<SchemaTemplate> getSchemaTemplateList() {
		return schemaTemplateList;
	}
	public void setSchemaTemplateList(List<SchemaTemplate> schemaTemplateList) {
		this.schemaTemplateList = schemaTemplateList;
	}
	public List<String> getOutTempPaths() {
		return outTempPaths;
	}
	public void setOutTempPaths(List<String> outTempPaths) {
		this.outTempPaths = outTempPaths;
	}
	public ArrayList<Tablespace> getTablespaces() {
		return tablespaces;
	}
	public void setTablespaces(ArrayList<Tablespace> tablespaces) {
		this.tablespaces = tablespaces;
	}
	public Map<String, Tablespace> getTablespacesMap() {
		return tablespacesMap;
	}
	public void setTablespacesMap(Map<String, Tablespace> tablespacesMap) {
		this.tablespacesMap = tablespacesMap;
	}
}