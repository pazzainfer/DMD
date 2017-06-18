package com.leven.dmd.pro.nav.action.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.leven.dmd.gef.tmpfile.model.DBView;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;

public class ViewCreateToDbAction extends Action {
	private Object obj;
	
	public ViewCreateToDbAction() {
		super();
		this.setText(Messages.ViewCreateToDbAction_0);
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.SYNCHRONIZE_IMAGE_PATH));
	}

	public ViewCreateToDbAction(Object obj) {
		super();
		this.setText(Messages.ViewCreateToDbAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.SYNCHRONIZE_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		super.run();
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
				 mb.setText(Messages.ViewCreateToDbAction_2);
				 mb.setMessage(Messages.ViewCreateToDbAction_3);
				 mb.open();
				 return;
			}
			
			sql = DbTableUtil.getViewSql((DBView)obj, config.getType());
			conn = DBConfigUtil.getConnection(config);
			stat = conn.createStatement();
			stat.execute(sql);
		} catch (SQLException e) {
			System.out.print(e.toString());
			System.out.println(sql);
			System.out.println("/--------------------------------------/"); //$NON-NLS-1$
			 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			 mb.setText(Messages.ViewCreateToDbAction_2);
			 mb.setMessage(Messages.ViewCreateToDbAction_6);
			 mb.open();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			 mb.setText(Messages.ViewCreateToDbAction_2);
			 mb.setMessage(Messages.ViewCreateToDbAction_8);
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
		 mb.setText(Messages.ViewCreateToDbAction_9);
		 mb.setMessage(Messages.ViewCreateToDbAction_10);
		 mb.open();
	}

}
