package com.leven.dmd.gef.action;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnTypeMatcher;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.view.SchemaNavigatorView;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;

public class ImportTableFromDBAction extends EditorPartAction {
		
	public ImportTableFromDBAction(IEditorPart editor) {
		super(editor);
	}

	public ImportTableFromDBAction() {
		super(null);
	}

	public ImportTableFromDBAction(IEditorPart editor, int style) {
		super(editor, style);
	}

	protected void init() {
		super.init();
		setId(ActionFactory.IMPORT.getId());
		setToolTipText(Messages.ImportTableFromDBAction_0);
		setText(Messages.ImportTableFromDBAction_0 + "@Ctrl+I");
		this.setEnabled(false);
		this.setImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.IMPORT));
		this.setDisabledImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.IMPORT_DISABLED));
	}

	protected boolean calculateEnabled() {
		return true;
	}

	public void run() {
		Schema schema = ((SchemaDiagramEditor)this.getEditorPart()).getSchema();
		DBConfigSelectDialog selectDialog = new DBConfigSelectDialog(Display.getCurrent().getActiveShell());
		selectDialog.create();
		if(selectDialog.open()!=Dialog.OK){
			return;
		}
		DBConfigModel config = selectDialog.getDbConfigModel();
		if(!DBConfigUtil.isConfigAvailable(config)){
			 MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR);
			 mb.setText(Messages.ImportTableFromDBAction_2);
			 mb.setMessage(Messages.ImportTableFromDBAction_3);
			 mb.open();
			 return;
		 }
		ImportTableFromDBDialog dialog = new ImportTableFromDBDialog(this.getEditorPart().getSite().getShell()
				,config,true);
		dialog.create();
		dialog.open();
		if(dialog.getData().size()>0){
			Connection conn = null;
			try {
				conn = DBConfigUtil.getConnection(config);
				for(int i=0;i<dialog.getData().size();i++){
					try {
						String[] tabinfo =  dialog.getData().get(i);
						if(!schema.getTablesMap().containsKey(tabinfo[0])){
							addTable(tabinfo,schema,conn,config);
						}
					} catch (Exception e) {
						continue;
					}
				}
			} catch (Exception e) {
				return;
			} finally {
				try {
					if(conn!=null){
						conn.close();
						conn=null;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void addTable(String[] info,Schema schema,Connection conn,DBConfigModel config) throws SQLException{
		Table table = new Table(info[0], schema);
		table.setCnName(info[1]);
		ResultSet rs = null;
		try {
			DatabaseMetaData data = conn.getMetaData();
			rs = data.getColumns(null, config.getUsername().toUpperCase(), info[0], "%%"); //$NON-NLS-1$
			Column column;
			while(rs.next()){
				column = new Column();
				column.setName(rs.getString("COLUMN_NAME")); //$NON-NLS-1$
				column.setType(ColumnTypeMatcher.getSchemaType(rs.getString("TYPE_NAME"), config.getDriver()).getType()); //$NON-NLS-1$
				column.setLength(rs.getInt("COLUMN_SIZE")); //$NON-NLS-1$
				column.setScale(rs.getInt("DECIMAL_DIGITS")); //$NON-NLS-1$
				String remarks = rs.getString("REMARKS"); //$NON-NLS-1$
				column.setCnName(remarks==null?"":remarks); //$NON-NLS-1$
				table.addColumn(column);
			}
			
			rs = data.getPrimaryKeys(null, config.getUsername().toUpperCase(), info[0]);
			while(rs.next()){
				String key = rs.getString("COLUMN_NAME"); //$NON-NLS-1$
				if(table.getColumnMap().containsKey(key)){
					table.getColumnMap().get(key).setPk(true);
				}
			}
		} catch (Exception e) {
			schema.removeTable(table);
		} finally{
			if(rs!=null){
				rs.close();
				rs=null;
			}
		}
		TablePackage tablePackage;
		if(schema.isPackageOpen() && 
				(tablePackage=schema.getOpenPackage())!=null){
			tablePackage.addTable(table);
		}else {
			schema.addTable(table);
		}
		table.modifyName(info[0]);
		refreshView(table);
	}
	
	private void refreshView(Table table){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			try{
				IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
				if(registry!=null && registry instanceof SchemaNavigatorView){
					SchemaNavigatorView view = (SchemaNavigatorView)registry;
					Object parent;
					if((parent=table.getParent()) instanceof TablePackage){
						view.getCommonViewer().refresh(parent);
					}else {
						Object tablesFolder = ((INavigatorTreeNode)((INavigatorTreeNode)((INavigatorTreeNode)view.getCommonViewer().getInput()).getChildren().get(0))
							.getChildren().get(0)).getChildren().get(0);
						view.getCommonViewer().refresh(tablesFolder);
					}
				}
				((SchemaDiagramEditor)page.getActiveEditor()).setDirty(true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}