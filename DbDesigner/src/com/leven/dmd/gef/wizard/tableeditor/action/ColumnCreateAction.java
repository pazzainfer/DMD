package com.leven.dmd.gef.wizard.tableeditor.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.model.ColumnTypeMatcher;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DBUtil;

public class ColumnCreateAction extends Action {
	private TableViewer viewer;
	private Schema schema;
	private Text tableName;
	
	/**
	 * 将选中字段同步插入到数据表中
	 * @param viewer
	 * @param columnMap
	 * @param schema
	 */
	public ColumnCreateAction(TableViewer viewer,Schema schema,Text tableName) {
		setText(Messages.ColumnCreateAction_0);
		this.viewer = viewer;
		this.schema = schema;
		this.tableName = tableName;
		this.setToolTipText(Messages.ColumnCreateAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.COLUMN_CREATE));
		this.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.COLUMN_CREATE_DISABLED));
		this.setEnabled(false);
	}
	
	public void run() {
		StructuredSelection select = (StructuredSelection) viewer
			.getSelection();
		if(select.isEmpty()){
			MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			mb.setText(Messages.ColumnDeleteAction_2);
			mb.setMessage(Messages.ColumnCreateAction_1);
			mb.open();
		}else{
			doColumnCreate((Column) select.getFirstElement(),schema,tableName.getText().trim());
		}
	}
	
	private void doColumnCreate(Column column,Schema schema,String tableName){
		DBConfigSelectDialog dialog = new DBConfigSelectDialog(Display.getCurrent().getActiveShell());
		dialog.create();
		if(dialog.open()!=Dialog.OK){
			return;
		}
		DBConfigModel config = dialog.getDbConfigModel();
		boolean configOk;
		if(!DBConfigUtil.isConfigAvailable(config)){
			configOk = false;
		}else {
			configOk = true;
		}
		if(!configOk){
			MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			mb.setText(Messages.ColumnDeleteAction_2);
			mb.setMessage(Messages.ColumnCreateAction_2);
			mb.open();
			return;
		}
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		boolean exist = false;
		StringBuffer columnSql = new StringBuffer();
		try {
			conn = DBUtil.getConnection(config);
			if(conn==null){
				MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
				mb.setText(Messages.ColumnDeleteAction_2);
				mb.setMessage(Messages.ColumnCreateAction_3);
				mb.open();
				return;
			}
			rs = conn.getMetaData().getTables(null, config.getUsername().toUpperCase(), tableName.toUpperCase(), new String[]{"TABLE"}); //$NON-NLS-1$
			if(rs.next()){
				exist = true;
			} else {
				exist = false;
			}
			if(exist){
				columnSql.append("ALTER TABLE "); //$NON-NLS-1$
				rs = conn.getMetaData().getColumns(null, config.getUsername().toUpperCase(), tableName.toUpperCase(), column.getName());
				if(rs.next()){
					stat = conn.createStatement();
					columnSql.append(tableName.toUpperCase());
					columnSql.append(" MODIFY "); //$NON-NLS-1$
					columnSql.append(column.getName());
					columnSql.append(" "); //$NON-NLS-1$
					columnSql.append(ColumnTypeMatcher.getType(column.getType(),config.getDriver()));
					if(ColumnType.hasLength(column.getType())){
						columnSql.append("(" + column.getLength()); //$NON-NLS-1$
						if(ColumnType.hasScale(column.getType())){
							columnSql.append("," + column.getScale()); //$NON-NLS-1$
						}
						columnSql.append(")"); //$NON-NLS-1$
					}
					stat.execute(columnSql.toString());

					columnSql = new StringBuffer("comment on column ");	//$NON-NLS-1$
					columnSql.append(tableName.toUpperCase()+"."+column.getName());//$NON-NLS-1$
					columnSql.append(" is '"+column.getCnName()+"'");//$NON-NLS-1$ //$NON-NLS-2$
					stat.execute(columnSql.toString());
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION);
					mb.setText(Messages.ColumnCreateAction_5);
					mb.setMessage(Messages.ColumnCreateAction_6);
					mb.open();
				}else {
					stat = conn.createStatement();
					columnSql.append(tableName.toUpperCase());
					columnSql.append(" ADD "); //$NON-NLS-1$
					columnSql.append(column.getName());
					columnSql.append(" "); //$NON-NLS-1$
					columnSql.append(ColumnTypeMatcher.getType(column.getType(),config.getDriver()));
					if(ColumnType.hasLength(column.getType())){
						columnSql.append("(" + column.getLength()); //$NON-NLS-1$
						if(ColumnType.hasScale(column.getType())){
							columnSql.append("," + column.getScale()); //$NON-NLS-1$
						}
						columnSql.append(")"); //$NON-NLS-1$
					}
					stat.execute(columnSql.toString());
					columnSql = new StringBuffer("comment on column ");	//$NON-NLS-1$
					columnSql.append(tableName.toUpperCase()+"."+column.getName());//$NON-NLS-1$
					columnSql.append(" is '"+column.getCnName()+"'");//$NON-NLS-1$ //$NON-NLS-2$
					stat.execute(columnSql.toString());
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION);
					mb.setText(Messages.ColumnCreateAction_5);
					mb.setMessage(Messages.ColumnCreateAction_6);
					mb.open();
				}
			}else {
				MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
				mb.setText(Messages.ColumnDeleteAction_2);
				mb.setMessage(Messages.ColumnCreateAction_10+tableName+Messages.ColumnCreateAction_11);
				mb.open();
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			mb.setText(Messages.ColumnDeleteAction_2);
			mb.setMessage(columnSql.toString());
			mb.open();
		} finally {
			try{
				if(rs!=null){
					rs.close();
					rs = null;
				}
				if(stat!=null){
					stat.close();
					stat = null;
				}
				if(conn!=null){
					conn.close();
					conn = null;
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
}