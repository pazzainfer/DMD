package com.leven.dmd.gef.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
/**
 * 数据库表模型
 * @author lifeng
 * 2012-7-11 ����03:28:55
 */
@SuppressWarnings("serial")
public class Table extends PropertyAwareObject implements IPropertySource,INavigatorTreeNode {

	private Schema schema;
	private String name;
	private String cnName = ""; //$NON-NLS-1$
	private String description = ""; //$NON-NLS-1$
	private ArrayList<Column> columns = new ArrayList<Column>();
	private ArrayList<TableIndex> indexes = new ArrayList<TableIndex>();
	/**
	 * 字段Map
	 */
	private Map<String,Column> columnMap = new HashMap<String,Column>();
	private Rectangle bounds;
	private Point location;

	private ArrayList<Relationship> primaryKeyRelationships = new ArrayList<Relationship>();
	private ArrayList<Relationship> foreignKeyRelationships = new ArrayList<Relationship>();
	
	private ArrayList<Relationship> oldPrimaryKeyRelationships = new ArrayList<Relationship>();
	private ArrayList<Relationship> oldForeignKeyRelationships = new ArrayList<Relationship>();
	/**
	 * 标识路径
	 */
	private String path = "";
	private String quotePath;
	/**
	 * 补充SQL
	 */
	private String otherSql="";
	/**
	 * 序号,用于排序显示。
	 */
	private int seqno = 0;
	
	//备用属性
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;
	private Object tempField4;
	private Object tempField5;
	/**
	 * 表状态
	 * （IEditorConstant.java）0:正常;1:已改变;2:新增
	 */
	private int status;
	/**
	 * 表空间
	 */
	private String tablespace="";
	/**
	 * 数据表是否为引用表
	 * @return
	 */
	public boolean isQuote(){
		return quotePath!=null && !"".equals(quotePath);
	}
	/**
	 * 获取所有字段数组(用于下拉框的下拉项)
	 * @return
	 */
	public String[] getComboValues(){
		String[] combos = new String[columns.size()];
		for(int i=0;i<columns.size();i++){
			combos[i] = ((Column)columns.get(i)).getName();
		}
		return combos;
	}
	/**
	 * 变更表图元的图标
	 * @author lifeng
	 * @param label
	 * @param status
	 * void
	 * @datetime 2012-7-17 06:37:57
	 */
	public static void changeLabelColor(Label label,int status){
		if(status==IEditorConstant.TABLE_STATUS_NORMAL){
			label.setForegroundColor(ColorConstants.black);
			label.setIcon(Activator.getImage(ImageKeys.TABLE_NORMAL));
		}else if(status==IEditorConstant.TABLE_STATUS_CHANGED){
			label.setForegroundColor(ColorConstants.darkGreen);
			label.setIcon(Activator.getImage(ImageKeys.TABLE_CHANGED));
		}else if(status==IEditorConstant.TABLE_STATUS_ADDED){
			label.setForegroundColor(ColorConstants.red);
			label.setIcon(Activator.getImage(ImageKeys.TABLE_ADDED));
		}
	}
	/**
	 * 数据表
	 * @param name 表名
	 */
	public Table(String name) {
		this.name = name;
	}
	/**
	 * 数据表
	 * @param name 表名
	 * @param schema 画布对象
	 */
	public Table(String name, Schema schema) {
		this.name = name;
		this.schema = schema;
	}
	/**
	 * 数据表
	 * @param name 表名
	 * @param path 路径
	 * @param schema 画布对象
	 */
	public Table(String name,String path, Schema schema) {
		this(name);
		if (name == null)
			throw new NullPointerException("Name cannot be null"); //$NON-NLS-1$
		if (schema == null)
			throw new NullPointerException("Schema cannot be null"); //$NON-NLS-1$
		this.name = name;
		this.path = path;
		this.schema = schema;
	}

	public boolean addColumn(Column column) {
		if (columns.contains(column)) {
			return false;
		}
		column.setTable(this);
		columns.add(column);
		if(column.isPk()){
			addIndex(column);
		}
		columnMap.put(column.getName(), column);
		firePropertyChange(CHILD, null, column);
		return true;
	}

	public void addColumn(Column column, int index) {
		if (columns.contains(column)) {
			throw new IllegalArgumentException("Column already present"); //$NON-NLS-1$
		}
		column.setTable(this);
		columns.add(index, column);
		if(column.isPk()){
			addIndex(column);
		}
		columnMap.put(column.getName(), column);
		firePropertyChange(CHILD, null, column);
	}
	/**
	 * 移除表中的指定字段，并同时将包含该字段的索引移除。
	 * @param column
	 */
	public void removeColumn(Column column) {
		columns.remove(column);
		List<TableIndex> toBeRemoved = new ArrayList<TableIndex>();
		for(TableIndex index : indexes){
			if(index.getColumns().contains(","+column.getName()) || 
					index.getColumns().contains(column.getName()+",") || 
					index.getColumns().equals(column.getName())){
				toBeRemoved.add(index);
			}
		}
		for(TableIndex index : toBeRemoved){
			indexes.remove(index);
		}
		if(column!=null){
			columnMap.remove(column.getName());
			firePropertyChange(CHILD, column, null);
		}
	}
	public void removeAllColumn() {
		if(columns!=null && columns.size()>0){
			for(int i=0;i<columns.size();i++){
				columns.remove(i);
				columnMap.remove(columns.get(i).getName());
			}
		}
		firePropertyChange(CHILDREN, columns, null);
	}

	public void switchColumn(Column column, int index) {
		columns.remove(column);
		columns.add(index, column);
		firePropertyChange(REORDER, this, column);
	}

	/**
	 * Sets name without firing off any event notifications
	 * 
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param schema
	 *            The schema to set.
	 */
	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	/**
	 * Sets bounds without firing off any event notifications
	 * 
	 * @param bounds
	 *            The bounds to set.
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
		firePropertyChange(P_CONSTRAINT, null, bounds);
	}

	/**
	 * Adds relationship where the current object is the foreign key table in a
	 * relationship
	 * 
	 * @param table
	 *            the primary key relationship
	 */
	public void addForeignKeyRelationship(Relationship table) {
		foreignKeyRelationships.add(table);
		firePropertyChange(OUTPUT, null, table);
	}

	/**
	 * Adds relationship where the current object is the primary key table in a
	 * relationship
	 * 
	 * @param table
	 *            the foreign key relationship
	 */
	public void addPrimaryKeyRelationship(Relationship table) {
		primaryKeyRelationships.add(table);
		firePropertyChange(INPUT, null, table);
	}

	/**
	 * Removes relationship where the current object is the foreign key table in
	 * a relationship
	 * 
	 * @param table
	 *            the primary key relationship
	 */
	public void removeForeignKeyRelationship(Relationship table) {
		foreignKeyRelationships.remove(table);
		firePropertyChange(OUTPUT, table, null);
	}

	/**
	 * Removes relationship where the current object is the primary key table in
	 * a relationship
	 * 
	 * @param table
	 *            the foreign key relationship
	 */
	public void removePrimaryKeyRelationship(Relationship table) {
		primaryKeyRelationships.remove(table);
		firePropertyChange(INPUT, table, null);
	}

	/**
	 * If modified, sets name and fires off event notification
	 * 
	 * @param newname
	 *            The name to set.
	 */
	public void modifyName(String newname) {
		String oldName = this.name;
		if (!newname.equals(oldName)) {
			String[] pnodes = path.split("/");
			if(pnodes.length==1){
				schema.getTablesMap().remove(path);
				this.path = newname;
				schema.getTablesMap().put(path,this);
			}else {
				pnodes[pnodes.length-1] = newname;
				String newPath = "";
				schema.getTablesMap().remove(path);
				for(int i=0;i<pnodes.length;i++){
					newPath += pnodes[i];
					if(i!=(pnodes.length-1)){
						newPath+="/";
					}
				}
				this.path = newPath;
				schema.getTablesMap().put(newPath,this);
			}
			this.name = newname;
			firePropertyChange(NAME, null, newname);
		}
	}
	public void modifyCnName(String name) {
		String oldName = this.cnName;
		if (!name.equals(oldName)) {
			this.cnName = name;
			firePropertyChange(CNNAME, null, name);
		}
	}
	public void modifyDescription(String description) {
		String oldDescription = this.description;
		if (!description.equals(oldDescription)) {
			this.description = description;
			firePropertyChange(DESCRIPTION, null, description);
		}
	}
	

	/**
	 * If modified, sets bounds and fires off event notification
	 * 
	 * @param bounds
	 *            The bounds to set.
	 */
	public void modifyBounds(Rectangle bounds) {
		Rectangle oldBounds = this.bounds;
		if (!bounds.equals(oldBounds)) {
			this.location = bounds.getLocation();
			this.bounds = bounds;
			firePropertyChange(BOUNDS, null, bounds);
		}
	}
	
	public void modifyStatus(int newStatus) {
		int oldStatus = this.status;
		if (newStatus!=oldStatus) {
			this.status = newStatus;
			firePropertyChange(STATUS, null, newStatus);
		}
	}

	public String getName() {
		return name;
	}


	/**
	 * @return Returns the foreignKeyRelationships.
	 */
	public ArrayList<Relationship> getForeignKeyRelationships() {
		return foreignKeyRelationships;
	}

	/**
	 * @return Returns the primaryKeyRelationships.
	 */
	public ArrayList<Relationship> getPrimaryKeyRelationships() {
		return primaryKeyRelationships;
	}

	/**
	 * @return Returns the schema.
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * @return Returns the bounds.
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	public String toString() {
		return name;
	}

	/**
	 * hashcode implementation for use as key in Map
	 */
	public int hashCode() {
		// just an extra line so that this does not fall over when the tool is
		// used incorrectly
		if (schema == null || name == null)
			return super.hashCode();
		String schemaName = schema.getName();
		return schemaName.hashCode() + name.hashCode();
	}

	/**
	 * equals implementation for use as key in Map
	 */
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Table))
			return false;
		Table t = (Table) o;
		if(schema==null || t.getSchema()==null || name==null){
			return false;
		}
		if (schema.getName().equals(t.getSchema().getName())
				&& name.equals(t.getName())) {
			return true;
		} else
			return false;
	}

	public ArrayList<Column> getColumns() {
		return columns;
	}
	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
		for(Column column : columns){
			if(column.isPk()){
				addIndex(column);
			}
		}
	}
	public void setPrimaryKeyRelationships(ArrayList<Relationship> primaryKeyRelationships) {
		this.primaryKeyRelationships = primaryKeyRelationships;
	}
	public void setForeignKeyRelationships(ArrayList<Relationship> foreignKeyRelationships) {
		this.foreignKeyRelationships = foreignKeyRelationships;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName==null?"":cnName; //$NON-NLS-1$
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description==null?"":description; //$NON-NLS-1$
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<String, Column> getColumnMap() {
		return columnMap;
	}
	public void setColumnMap(Map<String, Column> columnMap) {
		this.columnMap = columnMap;
	}
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	public ArrayList<TableIndex> getIndexes() {
		return indexes;
	}
	public void setIndexes(ArrayList<TableIndex> indexes) {
		this.indexes = indexes;
	}
	
	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors =  new IPropertyDescriptor[7];
		
		PropertyDescriptor	descriptor = new TextPropertyDescriptor("name",Messages.Table_0); //$NON-NLS-1$
		descriptor.setDescription("01"); //$NON-NLS-1$
		descriptor.setCategory(Messages.Table_10);
		descriptors[0] = descriptor;
		
		descriptor = new TextPropertyDescriptor("cnName",Messages.Table_12); //$NON-NLS-1$
		descriptor.setDescription("02"); //$NON-NLS-1$
		descriptor.setCategory(Messages.Table_14);
		descriptors[1] = descriptor;
		
		descriptor = new TextPropertyDescriptor("description",Messages.Table_16); //$NON-NLS-1$
		descriptor.setDescription("03"); //$NON-NLS-1$
		descriptor.setCategory(Messages.Table_18);
		descriptors[2] = descriptor;

		descriptor = new PropertyDescriptor("figure_x","x"); //$NON-NLS-1$ //$NON-NLS-2$
		descriptor.setDescription("04"); //$NON-NLS-1$
		descriptor.setCategory(Messages.Table_22);
		descriptors[3] = descriptor;
		
		descriptor = new PropertyDescriptor("figure_y","y"); //$NON-NLS-1$ //$NON-NLS-2$
		descriptor.setDescription("05"); //$NON-NLS-1$
		descriptor.setCategory(Messages.Table_22);
		descriptors[4] = descriptor;
		
		descriptor = new PropertyDescriptor("figure_width","width"); //$NON-NLS-1$ //$NON-NLS-2$
		descriptor.setDescription("06"); //$NON-NLS-1$
		descriptor.setCategory(Messages.Table_22);
		descriptors[5] = descriptor;
		
		descriptor = new PropertyDescriptor("figure_height","height"); //$NON-NLS-1$ //$NON-NLS-2$
		descriptor.setDescription("07"); //$NON-NLS-1$
		descriptor.setCategory(Messages.Table_22);
		descriptors[6] = descriptor;
		
		return descriptors;
	}

	public Object getPropertyValue(Object id) {
		if(id.equals("name")){ //$NON-NLS-1$
			return this.name;
		}else if(id.equals("cnName")){ //$NON-NLS-1$
			return this.cnName;
		}else if(id.equals("description")){ //$NON-NLS-1$
			return description==null?"":description; //$NON-NLS-1$
		}else if(id.equals("figure_x")){ //$NON-NLS-1$
			return this.bounds==null?0:this.bounds.x;
		}else if(id.equals("figure_y")){ //$NON-NLS-1$
			return this.bounds==null?0:this.bounds.y;
		}else if(id.equals("figure_width")){ //$NON-NLS-1$
			return this.bounds==null?0:this.bounds.width;
		}else if(id.equals("figure_height")){ //$NON-NLS-1$
			return this.bounds==null?0:this.bounds.height;
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		if(id.equals("name")){ //$NON-NLS-1$
			return true;
		}else if(id.equals("cnName")){ //$NON-NLS-1$
			return true;
		}else if(id.equals("description")){ //$NON-NLS-1$
			return true;
		}else if(id.equals("figure_x")){ //$NON-NLS-1$
			return true;
		}else if(id.equals("figure_y")){ //$NON-NLS-1$
			return true;
		}else if(id.equals("figure_width")){ //$NON-NLS-1$
			return true;
		}else if(id.equals("figure_height")){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
		if(id.equals("name")){ //$NON-NLS-1$
			this.modifyName((String)value);
		}else if(id.equals("cnName")){ //$NON-NLS-1$
			this.modifyCnName((String)value);
		}else if(id.equals("description")){ //$NON-NLS-1$
			this.setDescription((String)value);
		}
	}

	public ArrayList<Relationship> getOldPrimaryKeyRelationships() {
		return oldPrimaryKeyRelationships;
	}
	public void setOldPrimaryKeyRelationships(
			ArrayList<Relationship> oldPrimaryKeyRelationships) {
		this.oldPrimaryKeyRelationships = oldPrimaryKeyRelationships;
	}
	public ArrayList<Relationship> getOldForeignKeyRelationships() {
		return oldForeignKeyRelationships;
	}
	public void setOldForeignKeyRelationships(
			ArrayList<Relationship> oldForeignKeyRelationships) {
		this.oldForeignKeyRelationships = oldForeignKeyRelationships;
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
		return columns;
	}

	public void setChildren(List children) {
		
	}

	public Object getRoot() {
		return schema;
	}

	public boolean hasChildren() {
		return !columns.isEmpty();
	}

	public int getNodeType() {
		return INavigatorNodeTypeConstants.TYPE_TABLE_NODE;
	}

	public Object getData() {
		return this;
	}
	/**
	 * 当字段被设置为主键时，自动为其添加一条唯一索引。
	 * @param column
	 * @return 返回索引是否添加成功
	 */
	public boolean addIndex(Column column) {
		boolean hasAuto = false;
		for(TableIndex index : indexes){
			if(index.isAutoCreated()){
				if(index.getColumns().equals(column.getName()) 
						|| index.getColumns().contains(column.getName()+",")
						|| index.getColumns().contains(","+column.getName())){
					return false;
				} else {
					if("".equals(index.getColumns())){
						index.setColumns(column.getName());
					}else {
						index.setColumns(index.getColumns()+","+column.getName());
					}
				}
				hasAuto = true;
			}
		}
		if(!hasAuto){
			indexes.add(new TableIndex("pk_"+this.getName(), column.getName(), IndexType.UNIQUE.getType(),true));
			return true;
		}
		return false;
	}
	/**
	 * 当将字段从主键设置为非主键时，将自动添加的主键索引移除。
	 * @param column
	 * @return
	 */
	public boolean removeIndex(Column column) {
		int removeIndex = -1;
		for(int i=0;i<indexes.size();i++){
			TableIndex index = indexes.get(i);
			if(index.isAutoCreated()){
				if(index.getColumns().contains(column.getName()+",")){
					index.setColumns(index.getColumns().replace(column.getName()+",", ""));
					return true;
				}else if(index.getColumns().contains(","+column.getName())){
					index.setColumns(index.getColumns().replace(","+column.getName(), ""));
					return true;
				}else if(index.getColumns().equals(column.getName())){
					removeIndex = i;
					break;
				}
				return false;
			}
		}
		if(removeIndex>-1){
			indexes.remove(removeIndex);
			return true;
		}
		return false;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTablespace() {
		return tablespace;
	}
	public void setTablespace(String tablespace) {
		this.tablespace = tablespace;
	}
	public int getSeqno() {
		return seqno;
	}
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}
	public String getOtherSql() {
		return otherSql;
	}
	public void setOtherSql(String otherSql) {
		this.otherSql = otherSql;
	}
	public String getQuotePath() {
		return quotePath;
	}
	public void setQuotePath(String quotePath) {
		this.quotePath = quotePath;
	}
}