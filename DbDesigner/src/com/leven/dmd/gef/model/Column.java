package com.leven.dmd.gef.model;

import java.util.List;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;

/**
 * 数据表字段
 * @author lifeng
 * 2012-7-11 03:26:54
 */
@SuppressWarnings("serial")
public class Column extends PropertyAwareObject implements IPropertySource,Cloneable,INavigatorTreeNode {

	private ColumnTemplate columnTemplate;
	private boolean isTempCanEdit = false;
	
	private String name=""; //$NON-NLS-1$
	private String type;
	private int length = 0;
	private int scale = 0;
	private String cnName = ""; //$NON-NLS-1$
	private String dbType;
	private Object data;
	private Object root;
	private Table table;
	
	private String description;
	/**
	 * �Ƿ�Ϊ���
	 */
	private boolean pk = false;
	
	//�����б�ѡ��ʹ��
	private boolean checked = false;
	//备用属性
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;
	private Object tempField4;
	private Object tempField5;

	public Column() {
		super();
	}

	public Column(String name, ColumnType type,String cnName,boolean pk) {
		super();
		this.name = name;
		this.type = type.getType();
		this.cnName = cnName;
		this.setPk(pk);
	}
	
	public Column(String name, String type,int length,String cnName,boolean pk) {
		super();
		this.name = name;
		this.type = type;
		this.cnName = cnName;
		this.length = length;
		this.setPk(pk);
	}

	/**
	 * �����ֶζ���,����һ���ֶ�ģ��
	 * @author leven
	 * 2012-11-2 ����10:37:53
	 * @return
	 */
	public ColumnTemplate copyToTemplate(){
		ColumnTemplate temp = new ColumnTemplate();
		temp.setCode(System.currentTimeMillis()+"");
		temp.setColumnName(this.getName());
		temp.setColumnType(ColumnType.getColumnType(this.getType()));
		temp.setColumnLength(this.getLength());
		temp.setColumnScale(this.getScale());
		temp.setColumnCnName(this.getCnName());
		return temp;
	}
	
	public void refreshColumn(){
		firePropertyChange(REFRESH_COLUMN, null, this);
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		if(columnTemplate!=null){
			return columnTemplate.getColumnType().getType();
		}
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		this.type = type==null?"":type; //$NON-NLS-1$
		firePropertyChange(NAME, null, type);
	}

	/**
	 * @param column
	 * @return
	 */
	public String getLabelText() {
		String lengthLab = ""; //$NON-NLS-1$
		if(ColumnType.hasLength(this.getType())){
			if(ColumnType.hasScale(this.getType())){
				lengthLab = "("+getLength()+","+getScale()+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}else {
				lengthLab = "("+getLength()+")"; //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		String labelText = ((getCnName()==null||"".equals(getCnName()))?getName():getCnName()) + ":" + getType() + lengthLab; //$NON-NLS-1$
		return labelText;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getLength() {
		if(columnTemplate!=null){
			return columnTemplate.getColumnLength();
		}
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public void setLength(String length) {
		if(length!=null && length.matches("[\\d]{1,}")){ //$NON-NLS-1$
			this.length = Integer.parseInt(length);
		}
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName==null?"":cnName; //$NON-NLS-1$
	}
	public boolean isPk() {
		return pk;
	}
	public void setPk(boolean pk) {
		if(!this.pk && pk){
			if(this.getTable()!=null){
				this.getTable().addIndex(this);
			}
		}else if(this.pk && !pk){
			if(this.getTable()!=null){
				this.getTable().removeIndex(this);
			}
		}
		this.pk = pk;
	}

	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors =  new IPropertyDescriptor[4];
		
		PropertyDescriptor	descriptor = new TextPropertyDescriptor("name",Messages.Column_13); //$NON-NLS-1$
		descriptor.setDescription("01"); //$NON-NLS-1$
		descriptors[0] = descriptor;
		
		descriptor = new ComboBoxPropertyDescriptor("type",Messages.Column_16,ColumnType.typeArray); //$NON-NLS-1$
		descriptor.setDescription("02"); //$NON-NLS-1$
		descriptors[1] = descriptor;
		
		descriptor = new TextPropertyDescriptor("length",Messages.Column_19); //$NON-NLS-1$
		descriptor.setDescription("03"); //$NON-NLS-1$
		descriptors[2] = descriptor;

		descriptor = new PropertyDescriptor("description",Messages.Column_22); //$NON-NLS-1$
		descriptor.setDescription("04"); //$NON-NLS-1$
		descriptors[3] = descriptor;
		
		return descriptors;
	}

	public Object getPropertyValue(Object id) {
		if(id.equals("name")){ //$NON-NLS-1$
			return this.name;
		}else if(id.equals("type")){ //$NON-NLS-1$
			return ColumnType.getTypeIndex(type);
		}else if(id.equals("length")){ //$NON-NLS-1$
			if(ColumnType.hasLength(this.getType())){
				return this.length + ""; //$NON-NLS-1$
			}else {
				return ""; //$NON-NLS-1$
			}
		}else if(id.equals("description")){ //$NON-NLS-1$
			return this.cnName;
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		if(id.equals("name")){ //$NON-NLS-1$
			return true;
		}else if(id.equals("type")){ //$NON-NLS-1$
			return true;
		}else if(id.equals("length")){ //$NON-NLS-1$
			return true;
		}else if(id.equals("description")){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

	public void resetPropertyValue(Object id) {
		
	}

	public void setPropertyValue(Object id, Object value) {
		if(id.equals("name")){ //$NON-NLS-1$
			this.setName((String)value);
		}else if(id.equals("type")){ //$NON-NLS-1$
			this.setType(ColumnType.typeArray[(Integer)value]);
		}else if(id.equals("length")){ //$NON-NLS-1$
			if(ColumnType.hasLength(this.getType())){
				if(((String)value).matches("[\\d]*")){ //$NON-NLS-1$
					this.setLength(Integer.parseInt((String)value));
				}
			}
		}else if(id.equals("description")){ //$NON-NLS-1$
			this.setCnName((String)value);
		}
		refreshColumn();
	}
	

	@Override
	public Object clone() throws CloneNotSupportedException {
		Column o = new Column();
		o.setName(new String(name));
		o.setType(new String(type));
		o.setLength(length);
		o.setScale(scale);
		o.setCnName(new String(cnName));
		o.setColumnTemplate(columnTemplate);
		return o;
	}

	public int getScale() {
		if(columnTemplate!=null){
			return columnTemplate.getColumnScale();
		}
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public ColumnTemplate getColumnTemplate() {
		return columnTemplate;
	}
	public void setColumnTemplate(ColumnTemplate columnTemplate) {
		this.columnTemplate = columnTemplate;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}

	public INavigatorTreeNode getParent() {
		return table;
	}

	public List getChildren() {
		return null;
	}

	public void setChildren(List children) {
		
	}

	public Object getRoot() {
		return root;
	}
	public void setRoot(Object root) {
		this.root = root;
	}
	public Object getData() {
		return this;
	}

	public boolean hasChildren() {
		return false;
	}

	public int getNodeType() {
		return INavigatorNodeTypeConstants.TYPE_COLUMN_NODE;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public boolean isTempCanEdit() {
		return isTempCanEdit;
	}
	public void setTempCanEdit(boolean isTempCanEdit) {
		this.isTempCanEdit = isTempCanEdit;
	}
}