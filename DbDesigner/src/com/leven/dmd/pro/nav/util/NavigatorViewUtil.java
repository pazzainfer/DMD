package com.leven.dmd.pro.nav.util;

import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.domain.NavigatorTreeNode;
import com.leven.dmd.pro.nav.view.SchemaNavigatorView;

public class NavigatorViewUtil {
	/**
	 * @author leven
	 * 2012-12-3 ����02:41:36
	 * @param obj
	 */
	public static void refresh(final Object obj) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
			if (registry != null && registry instanceof SchemaNavigatorView) {
				final SchemaNavigatorView view = (SchemaNavigatorView) registry;
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						if (obj != null) {
							view.getCommonViewer().refresh(obj);
						} else {
							view.getCommonViewer().refresh();
						}
					}
				});
			}
		}
	}
	/**
	 * ȡϵͳ��ǰ�༭�������,ˢ����Դ����ͼ
	 * @author leven
	 * 2012-11-30 ����05:10:15
	 */
	public static void refreshViewInput(final Object obj){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			IEditorPart editor = page.getActiveEditor();
			if(editor!=null && (editor instanceof SchemaDiagramEditor)){
				refreshViewInput((SchemaDiagramEditor)editor,obj);
			}else {
				IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
				if(registry!=null && registry instanceof SchemaNavigatorView){
					final SchemaNavigatorView view = (SchemaNavigatorView)registry;
					view.getCommonViewer().setInput(getInput(view));
					Display.getDefault().asyncExec(new Runnable() { 
			            public void run() { 
			            	if(obj!=null){
			            		view.getCommonViewer().refresh(obj);
			            	}else {
			            		view.getCommonViewer().refresh();
			            	}
			            }
					});
				}
			}
		}
	}
	/**
	 * ȡȡָ���༭�������,ˢ����Դ����ͼ
	 * @author leven
	 * 2012-11-30 ����05:10:52
	 * @param editor
	 */
	public static void refreshViewInput(final SchemaDiagramEditor editor,final Object obj){
		Display.getDefault().asyncExec(new Runnable() { 
            public void run() { 
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if(page!=null){
					IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
					if(registry!=null && registry instanceof SchemaNavigatorView){
						SchemaNavigatorView view = (SchemaNavigatorView)registry;
						if(editor!=null && (editor instanceof SchemaDiagramEditor)){
							Schema schema = ((SchemaDiagramEditor)editor).getSchema();
							view.setSchema(schema);
							view.getCommonViewer().setInput(getInput(schema,view));
							if(obj!=null){
			            		view.getCommonViewer().refresh(obj);
			            	}else {
			            		view.getCommonViewer().refresh();
			            	}
						}else {
							view.getCommonViewer().setInput(getInput(view));
							if(obj!=null){
			            		view.getCommonViewer().refresh(obj);
			            	}else {
			            		view.getCommonViewer().refresh();
			            	}
						}
					}
				}
            }
		});
	}
	/**
	 * ȡָ��schema��������,ˢ����Դ����ͼ
	 * @author leven
	 * 2012-11-30 ����05:11:07
	 * @param schema
	 */
	public static void refreshViewInput(final Schema schema,final Object obj){
		Display.getDefault().asyncExec(new Runnable() { 
            public void run() { 
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if(page!=null){
					IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
					if(registry!=null && registry instanceof SchemaNavigatorView){
						SchemaNavigatorView view = (SchemaNavigatorView)registry;
						view.setSchema(schema);
						view.getCommonViewer().setInput(getInput(schema,view));
						if(obj!=null){
		            		view.getCommonViewer().refresh(obj);
		            	}else {
		            		view.getCommonViewer().refresh();
		            	}
					}
				}
            }
		});
	}
	/**
	 * ˢ�¸�Ŀ¼��(֮���ѱ༭���Dirtyֵ����Ϊtrue)
	 * @author leven
	 * 2012-12-12 ����04:46:57
	 * @param index Ŀ¼���
	 * 0:��ݱ�	 1:����	2:�ֶ�ģ��	3:�ֶ�����ģ��	4:����
	 */
	public static void refreshRootFolder(final int index){
		IWorkbenchPage page = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
			if (registry != null && registry instanceof SchemaNavigatorView) {
				final SchemaNavigatorView view = (SchemaNavigatorView) registry;
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						List children = ((INavigatorTreeNode)((INavigatorTreeNode)view.getCommonViewer().getInput()).getChildren().get(0)).getChildren();
						if(index>=0 && index<children.size()){
							Object sequencesFolder = children.get(index);
							view.getCommonViewer().refresh(sequencesFolder);
						}
					}
				});
			}
			((SchemaDiagramEditor)page.getActiveEditor()).setDirty(true);
		}
	}
	/**
	 * ˢ�¶���Ŀ¼��(֮���ѱ༭���Dirtyֵ����Ϊtrue)
	 * @author leven
	 * 2012-12-12 ����04:46:57
	 * @param indexes Ŀ¼�������
	 * 0:��ݱ�	 1:����	2:�ֶ�ģ��	3:�ֶ�����ģ��	4:����
	 */
	public static void refreshRootFolder(final int[] indexes){
		IWorkbenchPage page = PlatformUI.getWorkbench()
		.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
			if (registry != null && registry instanceof SchemaNavigatorView) {
				final SchemaNavigatorView view = (SchemaNavigatorView) registry;
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						for(int i : indexes){
							Object sequencesFolder = (((INavigatorTreeNode)((INavigatorTreeNode)view.getCommonViewer().getInput()).getChildren().get(0))
							).getChildren().get(i);
							view.getCommonViewer().refresh(sequencesFolder);
						}
					}
				});
			}
			((SchemaDiagramEditor)page.getActiveEditor()).setDirty(true);
		}
	}
	/**
	 * ��ȡĬ�ϵ���Դ������
	 * @author leven
	 * 2012-11-30 ����05:11:25
	 * @return
	 */
	public static Object getDefaultInput(){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			IEditorPart editor = page.getActiveEditor();
			IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
			if(registry!=null && registry instanceof SchemaNavigatorView){
				SchemaNavigatorView view = (SchemaNavigatorView)registry;
				
				if(editor!=null && (editor instanceof SchemaDiagramEditor)){
					Schema schema = ((SchemaDiagramEditor)editor).getSchema();
					view.setSchema(schema);
					return getInput(schema,view);
				}else {
					return getInput(view);
				}
			}
		}
		return getInput();
	}
	/**
	 * ��ȡĬ�ϵ���Դ������
	 * @author leven
	 * 2012-11-30 ����05:12:51
	 * @param view
	 * @return
	 */
	public static Object getDefaultInput(SchemaNavigatorView view){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			IEditorPart editor = page.getActiveEditor();
			if(editor!=null && (editor instanceof SchemaDiagramEditor)){
				Schema schema = ((SchemaDiagramEditor)editor).getSchema();
				view.setSchema(schema);
				return getInput(schema,view);
			}else {
				return getInput(view);
			}
		}
		return getInput();
	}
	
	private static INavigatorTreeNode getInput(Schema schema,SchemaNavigatorView view){
		NavigatorTreeNode root = new NavigatorTreeNode("workspace",null,schema,INavigatorNodeTypeConstants.TYPE_ROOT); //$NON-NLS-1$
		
		NavigatorTreeNode file = new NavigatorTreeNode(Messages.NavigatorViewUtil_1+schema.getName()+Messages.NavigatorViewUtil_2,schema,schema,INavigatorNodeTypeConstants.TYPE_SCHEMA_FILE);
		
		/*NavigatorTreeNode tables = new NavigatorTreeNode(Messages.NavigatorViewUtil_3,schema,INavigatorNodeTypeConstants.TYPE_TABLE_FOLDER,view);
		tables.setChildren(schema.getAllTables());
		file.addChild(tables);*/
		
		NavigatorTreeNode packages = new NavigatorTreeNode(Messages.NavigatorViewUtil_4,schema,INavigatorNodeTypeConstants.TYPE_PACKAGE_FOLDER,view);
		packages.setChildren(schema.getTablePackages());
		file.addChild(packages);
		
		NavigatorTreeNode columnTemplate = new NavigatorTreeNode(Messages.NavigatorViewUtil_5,schema,INavigatorNodeTypeConstants.TYPE_COLUMN_TEMPLATE_FOLDER,view);
		columnTemplate.setChildren(schema.getSchemaTemplate().getColumnTemplates());
		file.addChild(columnTemplate);
		
		NavigatorTreeNode sequenceTemplate = new NavigatorTreeNode(Messages.NavigatorViewUtil_6,schema,INavigatorNodeTypeConstants.TYPE_SEQUENCE_TEMPLATE_FOLDER,view);
		sequenceTemplate.setChildren(schema.getSchemaTemplate().getSequenceTemplates());
		file.addChild(sequenceTemplate);
		
		NavigatorTreeNode dbView = new NavigatorTreeNode(Messages.NavigatorViewUtil_0,schema,INavigatorNodeTypeConstants.TYPE_VIEW_FOLDER,view);
		dbView.setChildren(schema.getDbViews());
		file.addChild(dbView);
		
		NavigatorTreeNode tablespace = new NavigatorTreeNode(Messages.NavigatorViewUtil_7,schema,INavigatorNodeTypeConstants.TYPE_TABLESPACE_FOLDER,view);
		tablespace.setChildren(schema.getTablespaces());
		file.addChild(tablespace);
//		NavigatorTreeNode columnTypeTemplate = new NavigatorTreeNode("�ֶ�����ģ��",schema,INavigatorNodeTypeConstants.TYPE_COLUMN_TYPE_TEMPLATE_FOLDER,view);
//		columnTypeTemplate.setChildren(schema.getSchemaTemplate().getColumnTypeTemplates());
//		file.addChild(columnTypeTemplate);

		root.addChild(file);
		return root;
	}
	/**
	 * 设置当前编辑器的DIRTY状态
	 * @param isDirty
	 */
	public static void setEditorDirty(boolean isDirty){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			try{
				if(page.getActiveEditor()!=null){
					((SchemaDiagramEditor)page.getActiveEditor()).setDirty(isDirty);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static INavigatorTreeNode getInput(SchemaNavigatorView view){
		NavigatorTreeNode root = new NavigatorTreeNode("workspace",null,INavigatorNodeTypeConstants.TYPE_ROOT,view); //$NON-NLS-1$
		NavigatorTreeNode parent = new NavigatorTreeNode("workspace",null,INavigatorNodeTypeConstants.TYPE_ROOT,view); //$NON-NLS-1$
		root.addChild(parent);
		return root;
	}
	private static INavigatorTreeNode getInput(){
		NavigatorTreeNode root = new NavigatorTreeNode("workspace",null,null,INavigatorNodeTypeConstants.TYPE_ROOT); //$NON-NLS-1$
		NavigatorTreeNode parent = new NavigatorTreeNode("workspace",null,null,INavigatorNodeTypeConstants.TYPE_ROOT); //$NON-NLS-1$
		root.addChild(parent);
		return root;
	}
}
