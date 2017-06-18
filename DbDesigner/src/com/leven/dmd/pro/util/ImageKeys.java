package com.leven.dmd.pro.util;

import java.util.ArrayList;
import java.util.List;

public class ImageKeys {
	public static List<String> keys;
	
	public static final String SCHEMA = "icons/schemaeditor/schema_file.png";
	public static final String SCHEMA_NEW = "icons/schemaeditor/schema_file_new.png";
	
	public static final String NAVIGATOR_VIEW = "icons/navigator/navigator_view.png";
	public static final String TABLE_FOLDER = "icons/schemaeditor/table_list.png";
	public static final String PACKAGE = "icons/schemaeditor/package.png";
	public static final String TABLE = "icons/schemaeditor/table.png";
	public static final String TABLE_ADDED = "icons/schemaeditor/table_added.png";
	public static final String TABLE_CHANGED = "icons/schemaeditor/table_changed.png";
	public static final String TABLE_NORMAL = "icons/schemaeditor/table_normal.png";
	public static final String COLUMN = "icons/schemaeditor/label_column.png";
	public static final String COLUMN_PK = "icons/schemaeditor/label_key.png";
	public static final String COLUMN_TEMPLATE = "icons/schemaeditor/tree/domain.png";
	public static final String SEQUENCE_TEMPLATE = "icons/schemaeditor/tree/sequence.png";
	public static final String VIEW = "icons/schemaeditor/tree/view.png";
	public static final String TABLESPACE = "icons/schemaeditor/tree/tablespace.png";
	
	public static final String FOLDER = "icons/schemaeditor/tree/folder.png";
	
	public static final String ACTION_REFRESH = "icons/schemaeditor/action/action_refresh.png";
	public static final String ACTION_EDIT = "icons/schemaeditor/tree/edit.png";
	public static final String ACTION_EDIT_DISABLED = "icons/schemaeditor/tree/edit_disabled.png";
	public static final String ACTION_DELETE = "icons/schemaeditor/tree/delete.png";
	public static final String ACTION_DELETE_DISABLED = "icons/schemaeditor/tree/delete_disabled.png";
	public static final String ACTION_ADD = "icons/schemaeditor/tree/add.png";
	public static final String ACTION_ADD_DISABLED = "icons/schemaeditor/tree/add_disabled.png";
	

	public static final String CHECKBOX_TRUE = "icons/schemaeditor/label/checkbox_true.png";
	public static final String CHECKBOX_FALSE = "icons/schemaeditor/label/checkbox_false.png";
	static{
		keys = new ArrayList<String>();
		keys.add(SCHEMA);
		keys.add(SCHEMA_NEW);
		keys.add(TABLE_FOLDER);
		keys.add(PACKAGE);
		
		keys.add(NAVIGATOR_VIEW);
		keys.add(TABLE);
		keys.add(TABLE_ADDED);
		keys.add(TABLE_CHANGED);
		keys.add(TABLE_NORMAL);
		keys.add(COLUMN);
		keys.add(COLUMN_PK);
		keys.add(COLUMN_TEMPLATE);
		keys.add(SEQUENCE_TEMPLATE);
		keys.add(FOLDER);
		keys.add(VIEW);
		keys.add(TABLESPACE);
		
		keys.add(ACTION_REFRESH);
		keys.add(ACTION_EDIT);
		keys.add(ACTION_EDIT_DISABLED);
		keys.add(ACTION_DELETE);
		keys.add(ACTION_DELETE_DISABLED);
		keys.add(ACTION_ADD);
		keys.add(ACTION_ADD_DISABLED);
		
		keys.add(CHECKBOX_TRUE);
		keys.add(CHECKBOX_FALSE);
	}
}
