package com.leven.dmd.pro.nav.action.seq;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;

public class SequenceCreateToDbAction extends Action {
	private Object obj;
	
	public SequenceCreateToDbAction() {
		super();
		this.setText(Messages.SequenceCreateToDbAction_0);
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.SYNCHRONIZE_IMAGE_PATH));
	}

	public SequenceCreateToDbAction(Object obj) {
		super();
		this.setText(Messages.SequenceCreateToDbAction_3);
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
				 MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR);
				 mb.setText(Messages.SequenceCreateToDbAction_1);
				 mb.setMessage(Messages.SequenceCreateToDbAction_2);
				 mb.open();
				 return;
			}
			
			sql = DbTableUtil.getSequenceSql((SequenceTemplate)obj, config.getType());
			conn = DBConfigUtil.getConnection(config);
			stat = conn.createStatement();
			stat.execute(sql);
		} catch (SQLException e) {
			System.out.print(e.toString());
			System.out.println(sql);
			System.out.println("/--------------------------------------/");
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
					, Messages.SequenceCreateToDbAction_1, Messages.SequenceCreateToDbAction_4); 
			return;
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
					, Messages.SequenceCreateToDbAction_8, Messages.SequenceCreateToDbAction_5);
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
		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
				, Messages.SequenceCreateToDbAction_6, Messages.SequenceCreateToDbAction_7);
	}

}
