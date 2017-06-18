package com.leven.dmd.pro.nav.constant;

public interface INavigatorNodeTypeConstants {
	/**
	 * 根节点
	 */
	public static final int TYPE_ROOT = 0;
	/**
	 * Schema文件节点
	 */
	public static final int TYPE_SCHEMA_FILE = 1;
	/**
	 * 数据表文件夹
	 */
	public static final int TYPE_TABLE_FOLDER = 2;
	/**
	 * 主题文件夹
	 */
	public static final int TYPE_PACKAGE_FOLDER = 3;
	/**
	 * 模板文件夹
	 */
	public static final int TYPE_TEMPALTE_FOLDER = 4;
	/**
	 * 字段文件夹
	 */
	public static final int TYPE_COLUMN_FOLDER = 5;
	/**
	 * 字段节点
	 */
	public static final int TYPE_COLUMN_NODE = 6;
	/**
	 * 数据表节点
	 */
	public static final int TYPE_TABLE_NODE = 7;
	/**
	 * 主题节点
	 */
	public static final int TYPE_PACKAGE_NODE = 8;
	/**
	 * 字段模板文件夹
	 */
	public static final int TYPE_COLUMN_TEMPLATE_FOLDER = 9;
	/**
	 * 序列文件夹
	 */
	public static final int TYPE_SEQUENCE_TEMPLATE_FOLDER = 11;
	/**
	 * 字段模板节点
	 */
	public static final int TYPE_COLUMN_TEMPLATE_NODE = 12;
	/**
	 * 序列模板节点
	 */
	public static final int TYPE_SEQUENCE_TEMPLATE_NODE = 14;
	/**
	 * 视图文件夹
	 */
	public static final int TYPE_VIEW_FOLDER = 15;
	/**
	 * 视图节点
	 */
	public static final int TYPE_VIEW_NODE = 16;
	/**
	 * 表空间文件夹
	 */
	public static final int TYPE_TABLESPACE_FOLDER = 17;
	/**
	 * 表空间节点
	 */
	public static final int TYPE_TABLESPACE_NODE = 18;
	
	
	public static final int FOLDER_INDEX_TABLE = -1;
	public static final int FOLDER_INDEX_PACKAGE = 0;
	public static final int FOLDER_INDEX_TEMP = 1;
	public static final int FOLDER_INDEX_SEQUENCE = 2;
	public static final int FOLDER_INDEX_VIEW = 3;
	public static final int FOLDER_INDEX_TABLESPACE = 4;
}
