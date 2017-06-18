package com.leven.dmd.gef.model;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;

@SuppressWarnings("serial")
public class TablePackage extends PropertyAwareObject implements IPropertySource,INavigatorTreeNode {

	private String name = ""; //$NON-NLS-1$
	private String description = ""; //$NON-NLS-1$

	private Schema schema;
	private ArrayList<Table> tables = new ArrayList<Table>();
	private ArrayList<TablePackage> tablePackages = new ArrayList<TablePackage>();
	private Rectangle bounds;
	private Point location = new Point();
	private String path = "";
	/**
	 * ��״̬,0:�ر�;1:��
	 */
	private int status;
	
	//备用属性
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;
	private Object tempField4;
	private Object tempField5;
	/**
	 * 包
	 * @param name 包名
	 */
	public TablePackage(String name) {
		this.name = name;
	}
	/**
	 * 包
	 * @param name 包名
	 * @param schema 画布对象
	 */
	public TablePackage(String name, Schema schema) {
		this.name = name;
		this.schema = schema;
	}
	/**
	 * 包
	 * @param name 包名
	 * @param path 路径
	 * @param schema 画布对象
	 */
	public TablePackage(String name,String path, Schema schema) {
		this(name, schema);
		this.path = path;
	}
	
	public boolean addTable(Table table) {
		if(tables.contains(table)){
			return false;
		}else {
			if(table.getSchema()==null){
				table.setSchema(schema);
			}
			table.setPath(this.getPath() + "/" + table.getName());
			tables.add(table);
			schema.getTablesMap().put(table.getPath(), table);
			if(schema.isPackageOpen() && path.equals(schema.getPackagePath())){
				schema.firePropertyChange(CHILD, null, table);
			}
			schema.getAllTables().add(table);
			return true;
		}
	}

	public void addTable(Table table, int i) {
		if(table.getSchema()==null){
			table.setSchema(schema);
		}
		table.setPath(this.getPath() + "/" + table.getName());
		tables.add(i, table);
		schema.getTablesMap().put(table.getPath(), table);
		if(schema.isPackageOpen() && path.equals(schema.getPackagePath())){
			schema.firePropertyChange(CHILD, null, table);
		}
		schema.getAllTables().add(table);
	}

	public boolean removeTable(Table table) {
		boolean result ;
		if(table!=null){
			result = tables.remove(table);
			schema.getTablesMap().remove(table.getPath());
			if(schema.isPackageOpen() && path.equals(schema.getPackagePath())){
				schema.firePropertyChange(CHILD, table, null);
			}
		}else {
			result = false;
		}
		schema.getAllTables().remove(table);
		return result;
	}
	
	public boolean addTablePackage(TablePackage tablePackage) {
		if(tablePackages.contains(tablePackage)){
			return false;
		}else {
			if(tablePackage.getSchema()==null){
				tablePackage.setSchema(schema);
			}
			tablePackage.setPath(this.getPath() + "/" + tablePackage.getName());
			schema.getTablePackagesMap().put(tablePackage.getPath(), tablePackage);
			tablePackages.add(tablePackage);
			if(schema.isPackageOpen() && path.equals(schema.getPackagePath())){
				schema.firePropertyChange(CHILD, tablePackage, null);
			}
			return true;
		}
	}
	
	public void addTablePackage(TablePackage tablePackage, int i) {
		if(tablePackage.getSchema()==null){
			tablePackage.setSchema(schema);
		}
		tablePackage.setPath(this.getPath() + "/" + tablePackage.getName());
		tablePackages.add(i, tablePackage);
		schema.getTablePackagesMap().put(tablePackage.getPath(), tablePackage);
		if(schema.isPackageOpen() && path.equals(schema.getPackagePath())){
			schema.firePropertyChange(CHILD, tablePackage, null);
		}
	}
	
	public boolean removeTablePackage(TablePackage tablePackage) {
		boolean result ;
		if(tablePackage!=null){
			result = tablePackages.remove(tablePackage);
			schema.getTablePackagesMap().remove(tablePackage.getPath());
			if(schema.isPackageOpen() && path.equals(schema.getPackagePath())){
				schema.firePropertyChange(CHILD, null, tablePackage);
			}
		}else {
			result = false;
		}
		return result;
	}
	
	public void modifyName(String newname) {
		String oldName = this.name;
		if (!newname.equals(oldName)) {
			if (!newname.equals(oldName)) {
				String[] pnodes = path.split("/");
				if(pnodes.length==1){
					schema.getTablePackagesMap().remove(path);
					this.path = newname;
					schema.getTablePackagesMap().put(path,this);
				}else {
					pnodes[pnodes.length-1] = newname;
					String newPath = "";
					schema.getTablePackagesMap().remove(path);
					for(int i=0;i<pnodes.length;i++){
						newPath += pnodes[i];
						if(i!=(pnodes.length-1)){
							newPath+="/";
						}
					}
					this.path = newPath;
					schema.getTablePackagesMap().put(newPath,this);
				}
				this.name = newname;
				firePropertyChange(NAME, null, newname);
			}
		}
	}
	
	public void modifyBounds(Rectangle bounds) {
		Rectangle oldBounds = this.bounds;
		if (!bounds.equals(oldBounds)) {
			this.bounds = bounds;
			this.location = bounds.getLocation();
			firePropertyChange(BOUNDS, null, bounds);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o){
			return true;
		}
		if (o == null || !(o instanceof TablePackage))
			return false;
		TablePackage t = (TablePackage) o;
		if(name==null){
			return false;
		}
		if (name.equals(t.getName())) {
			return true;
		} else
			return false;
	}

	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[]{
				new TextPropertyDescriptor("name",Messages.TablePackage_3) //$NON-NLS-1$
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
		if(id.equals("name")){ //$NON-NLS-1$
			if(!this.getName().equals(value) && schema.getTablePackagesMap().get(value)==null){
				this.modifyName((String)value);
			}
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public ArrayList<Table> getTables() {
		return tables;
	}
	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}
	public Rectangle getBounds() {
		return bounds;
	}
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}

	public INavigatorTreeNode getParent() {
		if(path==null || "".equals(path)){
			return schema;
		}
		String[] pnodes = path.split("/");
		String pPath = "";
		if(pnodes.length>1){
			for(int i=0;i<pnodes.length-1;i++){
				pPath += pnodes[i];
				if(i!=(pnodes.length-2)){
					pPath+="/";
				}
			}
		}
		if("".equals(pPath)){
			return schema;
		}else {
			return schema.getPackageByPath(pPath);
		}
	}

	public List getChildren() {
		List childs = new ArrayList();
		childs.addAll(tables);
		childs.addAll(tablePackages);
		return childs;
	}

	public void setChildren(List children) {
		
	}

	public Object getRoot() {
		return schema;
	}

	public boolean hasChildren() {
		return (tables!=null && !tables.isEmpty()) || (tablePackages!=null && !tablePackages.isEmpty());
	}

	public int getNodeType() {
		return INavigatorNodeTypeConstants.TYPE_PACKAGE_NODE;
	}

	public Object getData() {
		return this;
	}

	public ArrayList<TablePackage> getTablePackages() {
		return tablePackages;
	}

	public void setTablePackages(ArrayList<TablePackage> tablePackages) {
		this.tablePackages = tablePackages;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}