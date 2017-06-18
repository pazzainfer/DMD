package com.leven.dmd.pro.nav.provider;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.navigator.CommonActionProvider;

import com.leven.dmd.pro.nav.action.NavigatorInputRefreshAction;
import com.leven.dmd.pro.nav.action.NavigatorRefreshAction;
import com.leven.dmd.pro.nav.action.column.ColumnAddAction;
import com.leven.dmd.pro.nav.action.column.ColumnDeleteAction;
import com.leven.dmd.pro.nav.action.column.ColumnEditAction;
import com.leven.dmd.pro.nav.action.db.CompareSchemToDbAction;
import com.leven.dmd.pro.nav.action.db.CreateTableToDatabaseAction;
import com.leven.dmd.pro.nav.action.db.ExportDllAction;
import com.leven.dmd.pro.nav.action.db.ImportTableFromDBAction;
import com.leven.dmd.pro.nav.action.db.SingleDllExportAction;
import com.leven.dmd.pro.nav.action.pack.TablePackageAddAction;
import com.leven.dmd.pro.nav.action.pack.TablePackageDeleteAction;
import com.leven.dmd.pro.nav.action.pack.TablePackageEditAction;
import com.leven.dmd.pro.nav.action.pack.TablePackageToDBAction;
import com.leven.dmd.pro.nav.action.seq.SequenceCreateToDbAction;
import com.leven.dmd.pro.nav.action.seq.SequenceTemplateAddAction;
import com.leven.dmd.pro.nav.action.seq.SequenceTemplateDeleteAction;
import com.leven.dmd.pro.nav.action.seq.SequenceTemplateEditAction;
import com.leven.dmd.pro.nav.action.space.TablespaceAddEditAction;
import com.leven.dmd.pro.nav.action.space.TablespaceCreateToDbAction;
import com.leven.dmd.pro.nav.action.space.TablespaceDeleteAction;
import com.leven.dmd.pro.nav.action.table.TableAddAction;
import com.leven.dmd.pro.nav.action.table.TableCopyAction;
import com.leven.dmd.pro.nav.action.table.TableDeleteAction;
import com.leven.dmd.pro.nav.action.table.TableEditAction;
import com.leven.dmd.pro.nav.action.table.TableMoveAction;
import com.leven.dmd.pro.nav.action.table.TableQuoteCreateAction;
import com.leven.dmd.pro.nav.action.table.TableToDBAction;
import com.leven.dmd.pro.nav.action.temp.ColumnTemplateAddAction;
import com.leven.dmd.pro.nav.action.temp.ColumnTemplateDeleteAction;
import com.leven.dmd.pro.nav.action.temp.ColumnTemplateEditAction;
import com.leven.dmd.pro.nav.action.temp.TemplateImportFromFileAction;
import com.leven.dmd.pro.nav.action.temp.TemplateQuoteFromFileAction;
import com.leven.dmd.pro.nav.action.view.ViewAddEditAction;
import com.leven.dmd.pro.nav.action.view.ViewCreateToDbAction;
import com.leven.dmd.pro.nav.action.view.ViewDeleteAction;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;

public class SchemaNavigatorActionProvider extends CommonActionProvider {

	public SchemaNavigatorActionProvider() {
		
	}
	@Override
	public void fillContextMenu(IMenuManager menu) {
		ActionContext at = getContext();
		TreeSelection iSselection=(TreeSelection)at.getSelection();
		if(iSselection!=null && !iSselection.isEmpty()){
			Object[] objs = iSselection.toArray();
			if(objs.length==1 && objs[0] instanceof INavigatorTreeNode){
				Object obj = objs[0];
				int type = ((INavigatorTreeNode)obj).getNodeType();
				
				if(type == INavigatorNodeTypeConstants.TYPE_COLUMN_TEMPLATE_FOLDER){
					makeColumnTemplateFolderMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_COLUMN_TEMPLATE_NODE){
					makeColumnTemplateNodeMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_SEQUENCE_TEMPLATE_FOLDER){
					makeSequenceFolderMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_SEQUENCE_TEMPLATE_NODE){
					makeSequenceNodeMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_TABLE_FOLDER){
					makeTableFolderMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_PACKAGE_NODE){
					makePackageMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_PACKAGE_FOLDER){
					makePackageFolderMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_TABLE_NODE){
					makeTableMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_COLUMN_NODE){
					makeColumnMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_ROOT){
//					makeGlobalMenu(null,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_SCHEMA_FILE){
					makeSchemaFileMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_VIEW_FOLDER){
					makeViewFolderMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_VIEW_NODE){
					makeViewMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_TABLESPACE_FOLDER){
					makeTablespaceFolderMenu(obj,menu);
				} else if(type == INavigatorNodeTypeConstants.TYPE_TABLESPACE_NODE){
					makeTablespaceMenu(obj,menu);
				}
			}
		}else {
			
		}
		menu.add(new Separator());
		menu.add(new NavigatorRefreshAction(iSselection.isEmpty()?null:iSselection.toArray()));
	}
	
	private void makeColumnMenu(Object obj,IMenuManager menu){
		ColumnEditAction columnEditAction = new ColumnEditAction(obj);
		ColumnDeleteAction action = new ColumnDeleteAction(obj);
		menu.add(columnEditAction);
		menu.add(action);
	}
	private void makeTableFolderMenu(Object obj,IMenuManager menu){
		TableAddAction action = new TableAddAction(obj);
		menu.add(action);
		TableCopyAction copyAction = new TableCopyAction(obj);
		menu.add(copyAction);
		TableQuoteCreateAction quoteAction = new TableQuoteCreateAction(obj);
		menu.add(quoteAction);
		CreateTableToDatabaseAction intoDbAction = new CreateTableToDatabaseAction(obj);
		menu.add(intoDbAction);
		ImportTableFromDBAction importAction = new ImportTableFromDBAction(obj);
		menu.add(importAction);
	}
	private void makePackageMenu(Object obj,IMenuManager menu){
		TablePackageAddAction tablePackageAddAction = new TablePackageAddAction(obj);
		menu.add(tablePackageAddAction);
		TableAddAction action = new TableAddAction(obj);
		menu.add(action);
		TableCopyAction copyAction = new TableCopyAction(obj);
		menu.add(copyAction);
		TableQuoteCreateAction quoteAction = new TableQuoteCreateAction(obj);
		menu.add(quoteAction);
		TablePackageEditAction action1 = new TablePackageEditAction(obj);
		menu.add(action1);
		TablePackageDeleteAction action2 = new TablePackageDeleteAction(obj);
		menu.add(action2);
		menu.add(new Separator());
		TablePackageToDBAction tablePackageToDBAction = new TablePackageToDBAction(obj);
		menu.add(tablePackageToDBAction);
		ImportTableFromDBAction importAction = new ImportTableFromDBAction(obj);
		menu.add(importAction);
		SingleDllExportAction dllAction = new SingleDllExportAction(obj);
		menu.add(dllAction);
	}
	private void makeTableMenu(Object obj,IMenuManager menu){
		ColumnAddAction action = new ColumnAddAction(obj);
		menu.add(action);
		TableEditAction action1 = new TableEditAction(obj);
		menu.add(action1);
		TableMoveAction tableMoveAction = new TableMoveAction(obj);
		menu.add(tableMoveAction);
		TableDeleteAction action2 = new TableDeleteAction(obj);
		menu.add(action2);
		menu.add(new Separator());
		TableToDBAction tableToDBAction = new TableToDBAction(obj);
		menu.add(tableToDBAction);
		SingleDllExportAction dllAction = new SingleDllExportAction(obj);
		menu.add(dllAction);
	}
	private void makePackageFolderMenu(Object obj,IMenuManager menu){
		TablePackageAddAction action = new TablePackageAddAction(obj);
		menu.add(action);
	}
	private void makeSequenceFolderMenu(Object obj,IMenuManager menu){
		SequenceTemplateAddAction templateAddAction = new SequenceTemplateAddAction(obj);
		menu.add(templateAddAction);
	}
	
	private void makeSequenceNodeMenu(Object obj,IMenuManager menu){
		SequenceTemplateEditAction action1 = new SequenceTemplateEditAction(obj);
		menu.add(action1);
		SequenceTemplateDeleteAction action2 = new SequenceTemplateDeleteAction(obj);
		menu.add(action2);
		SingleDllExportAction action3 = new SingleDllExportAction(obj);
		menu.add(action3);
		SequenceCreateToDbAction action4 = new SequenceCreateToDbAction(obj);
		menu.add(action4);
	}
	private void makeColumnTemplateFolderMenu(Object obj,IMenuManager menu){
		ColumnTemplateAddAction templateAddAction = new ColumnTemplateAddAction(obj);
		menu.add(templateAddAction);
	}
	private void makeColumnTemplateNodeMenu(Object obj,IMenuManager menu){
		ColumnTemplateEditAction action1 = new ColumnTemplateEditAction(obj);
		menu.add(action1);
		ColumnTemplateDeleteAction action2 = new ColumnTemplateDeleteAction(obj);
		menu.add(action2);
	}
	private void makeSchemaFileMenu(Object obj,IMenuManager menu){
		ExportDllAction dllAction = new ExportDllAction(obj);
		menu.add(dllAction);
		CompareSchemToDbAction compareSchemToDbAction = new CompareSchemToDbAction(obj);
		menu.add(compareSchemToDbAction);
		TemplateImportFromFileAction templateImportFromFileAction = new TemplateImportFromFileAction(obj);
		menu.add(templateImportFromFileAction);
		TemplateQuoteFromFileAction templateQuoteFromFileAction = new TemplateQuoteFromFileAction(obj);
		menu.add(templateQuoteFromFileAction);
		menu.add(new Separator());
		menu.add(new NavigatorInputRefreshAction());
	}

	private void makeViewMenu(Object obj, IMenuManager menu) {
		ViewAddEditAction viewAddEditAction = new ViewAddEditAction(obj);
		menu.add(viewAddEditAction);
		ViewDeleteAction action = new ViewDeleteAction(obj);
		menu.add(action);
		SingleDllExportAction dllAction = new SingleDllExportAction(obj);
		menu.add(dllAction);
		ViewCreateToDbAction action1 = new ViewCreateToDbAction(obj);
		menu.add(action1);
	}
	private void makeViewFolderMenu(Object obj, IMenuManager menu) {
		ViewAddEditAction action = new ViewAddEditAction(obj);
		menu.add(action);
	}
	
	private void makeTablespaceMenu(Object obj, IMenuManager menu) {
		TablespaceAddEditAction tablespaceAddEditAction = new TablespaceAddEditAction(obj);
		menu.add(tablespaceAddEditAction);
		TablespaceDeleteAction action = new TablespaceDeleteAction(obj);
		menu.add(action);
		SingleDllExportAction dllAction = new SingleDllExportAction(obj);
		menu.add(dllAction);
		TablespaceCreateToDbAction action1 = new TablespaceCreateToDbAction(obj);
		menu.add(action1);
	}
	private void makeTablespaceFolderMenu(Object obj, IMenuManager menu) {
		TablespaceAddEditAction action = new TablespaceAddEditAction(obj);
		menu.add(action);
	}
}
