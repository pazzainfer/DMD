package com.leven.dmd.pro.nav.action.space;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.leven.dmd.gef.model.Tablespace;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;

public class TablespaceCreateToDbAction extends Action {
	private Object obj;
	
	public TablespaceCreateToDbAction() {
		super();
		this.setText(Messages.TablespaceCreateToDbAction_0);
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.SYNCHRONIZE_IMAGE_PATH));
	}

	public TablespaceCreateToDbAction(Object obj) {
		super();
		this.setText(Messages.TablespaceCreateToDbAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.SYNCHRONIZE_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		super.run();
		Tablespace space = (Tablespace)obj;
		if(space.getFileList().size()<1){
			 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			 mb.setText(Messages.TablespaceCreateToDbAction_2);
			 mb.setMessage(Messages.TablespaceCreateToDbAction_1);
			 mb.open();
			return;
		}
		Connection conn = null;
		Statement stat = null;
		String sql = null;
		try {
			DBConfigSelectDialog dialog = new DBConfigSelectDialog(Display.getCurrent().getActiveShell());
			dialog.create();
			if(dialog.open()!=Dialog.OK){
				return;
			}
			DBConfigModel config = dialog.getDbConfigModel();
			if(!DBConfigUtil.isConfigAvailable(config)){
				 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
				 mb.setText(Messages.TablespaceCreateToDbAction_2);
				 mb.setMessage(Messages.TablespaceCreateToDbAction_3);
				 mb.open();
				 return;
			}
			
			sql = DbTableUtil.getTablespaceSql(space, config.getType());
			conn = DBConfigUtil.getConnection(config);
			stat = conn.createStatement();
			stat.execute(sql);
		} catch (SQLException e) {
			System.out.print(e.toString());
			System.out.println(sql);
			System.out.println("/--------------------------------------/"); //$NON-NLS-1$
			 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			 mb.setText(Messages.TablespaceCreateToDbAction_2);
			 mb.setMessage(Messages.TablespaceCreateToDbAction_6);
			 mb.open();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			 mb.setText(Messages.TablespaceCreateToDbAction_2);
			 mb.setMessage(Messages.TablespaceCreateToDbAction_8);
			 mb.open();
			return;
		} finally {
			try {
				if(stat!=null){
					stat.close();
					stat = null;
				}
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION);
		 mb.setText(Messages.TablespaceCreateToDbAction_9);
		 mb.setMessage(Messages.TablespaceCreateToDbAction_10);
		 mb.open();
	}

}
