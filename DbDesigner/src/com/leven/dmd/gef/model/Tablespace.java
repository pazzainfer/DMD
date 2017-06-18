package com.leven.dmd.gef.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
/**
 * 数据库表空间
 * @author leven
 * create at 2013-10-28 上午2:59:36
 */
public class Tablespace implements IPropertySource,INavigatorTreeNode {
	private Schema schema;
	
	private String name="";
	private long size;
	private long remainSize;
	private long useSize;
	/**
	 * 数据库文件清单
	 */
	private List<TablespaceFile> fileList = new ArrayList<TablespaceFile>();
	private String contents="";
	private String description="";
	/**
	 * 指定为数据库创建的回滚表空间
	 */
	private boolean isUndo;
	/**
	 * 表空间状态:ONLINE、OFFLINE、READ ONLY。ONLINE是正常工作的状态，OFFLINE状态下，是不允许访问数据的
	 */
	private String status;
	/**
	 * 指出在表空间的extent的最小值，这个参数可以减少空间碎片，保证在表空间的extent是这个数值的整数倍
	 */
	private long minExtent;
	/**
	 * 设置块的大小，如果要设置这个参数，必须设置成db_block_size的整数倍
	 */
	private long blockSize;
	/**
	 * 这个表空间上所有用户对象的日志属性
	 */
	private String logging;
	/**
	 * 表空间进入强制日志模式,高于logging参数中的nologging选项
	 */
	private boolean isForceLog=false;
	/**
	 * 是否临时表空间(否则是永久表空间)。
	 * 这个参数生成的临时表空间创建后一直都是字典管理，不能使用extent management local选项。
	 * 如果要创建本地管理表空间，必须使用create temporary tablespace。
	 * 声明了这个参数就不能声明block size。
	 */
	private boolean isTemporary;
	
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;
	private Object tempField4;

	public Tablespace(String name, Schema schema) {
		super();
		this.schema = schema;
		this.name = name;
	}
	
	public Tablespace(String name) {
		super();
		this.name = name;
	}
	
	public INavigatorTreeNode getParent() {
		return schema;
	}
	public List getChildren() {
		return null;
	}
	public void setChildren(List children) {
	}
	public String getName() {
		return name;
	}
	public Object getRoot() {
		return schema;
	}
	public Object getData() {
		return null;
	}
	public boolean hasChildren() {
		return false;
	}
	public int getNodeType() {
		return INavigatorNodeTypeConstants.TYPE_TABLESPACE_NODE;
	}

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return null;
	}

	public Object getPropertyValue(Object arg0) {
		return null;
	}
	public boolean isPropertySet(Object arg0) {
		return false;
	}
	public void resetPropertyValue(Object arg0) {
		
	}
	public void setPropertyValue(Object arg0, Object arg1) {
		
	}
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getRemainSize() {
		return remainSize;
	}
	public void setRemainSize(long remainSize) {
		this.remainSize = remainSize;
	}
	public long getUseSize() {
		return useSize;
	}
	public void setUseSize(long useSize) {
		this.useSize = useSize;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TablespaceFile> getFileList() {
		return fileList;
	}
	public void setFileList(List<TablespaceFile> fileList) {
		this.fileList = fileList;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isUndo() {
		return isUndo;
	}
	public void setUndo(boolean isUndo) {
		this.isUndo = isUndo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getMinExtent() {
		return minExtent;
	}
	public void setMinExtent(long minExtent) {
		this.minExtent = minExtent;
	}
	public long getBlockSize() {
		return blockSize;
	}
	public void setBlockSize(long blockSize) {
		this.blockSize = blockSize;
	}
	public String getLogging() {
		return logging;
	}
	public void setLogging(String logging) {
		this.logging = logging;
	}
	public boolean isForceLog() {
		return isForceLog;
	}
	public void setForceLog(boolean isForceLog) {
		this.isForceLog = isForceLog;
	}
	public boolean isTemporary() {
		return isTemporary;
	}
	public void setTemporary(boolean isTemporary) {
		this.isTemporary = isTemporary;
	}
}