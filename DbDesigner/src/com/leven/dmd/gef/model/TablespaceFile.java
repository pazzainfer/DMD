package com.leven.dmd.gef.model;

public class TablespaceFile {
	private String filePath;
	private long size;
	/**
	 * 大小是否可扩展，如果可以扩展，则指定为on为off，就有后面的递增和最大尺寸了。
	 * 例:autoextend on next 10M maxsize 500M
	 */
	private boolean isExpand;
	/**
	 * 递增尺寸单位
	 */
	private long expandUnit;
	/**
	 * 最大尺寸
	 */
	private long maxSize;
	/**
	 * 表空间文件对象
	 * @param filePath 文件路径
	 * @param size 文件大小(单位:M)
	 */
	public TablespaceFile(String filePath, long size) {
		super();
		this.filePath = filePath;
		this.size = size;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public boolean isExpand() {
		return isExpand;
	}
	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
	}
	public long getExpandUnit() {
		return expandUnit;
	}
	public void setExpandUnit(long expandUnit) {
		this.expandUnit = expandUnit;
	}
	public long getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}
}