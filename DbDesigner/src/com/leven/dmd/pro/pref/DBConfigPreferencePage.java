package com.leven.dmd.pro.pref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.action.DBConfigAddAction;
import com.leven.dmd.pro.pref.action.DBConfigDeleteAction;
import com.leven.dmd.pro.pref.action.DBConfigEditAction;
import com.leven.dmd.pro.pref.action.TableRowMoveDownAction;
import com.leven.dmd.pro.pref.action.TableRowMoveDownToEndAction;
import com.leven.dmd.pro.pref.action.TableRowMoveUpAction;
import com.leven.dmd.pro.pref.action.TableRowMoveUpToStartAction;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.provider.DBConfigTableSorter;
import com.leven.dmd.pro.pref.provider.DBConfigTableViewerContentProvider;
import com.leven.dmd.pro.pref.provider.DBConfigTableViewerLabelProvider;
import com.leven.dmd.pro.pref.util.DBConfigFileUtil;

public class DBConfigPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	private TableViewer viewer;
	private static final String[] COLUMN_NAME = {Messages.DBConfigPreferencePage_0, Messages.DBConfigPreferencePage_1};
	private static final ColumnLayoutData[] columnLayouts = { 
			new ColumnWeightData(80), 
			new ColumnWeightData(100)
			}; 
	private DBConfigAddAction dbConfigAddAction;
	private DBConfigEditAction dbConfigEditAction;
	private DBConfigDeleteAction dbConfigDeleteAction;
	private TableRowMoveUpAction moveUpAction;
	private TableRowMoveUpToStartAction moveUpToStartAction;
	private TableRowMoveDownAction moveDownAction;
	private TableRowMoveDownToEndAction moveDownToEndAction;
	
	private List<DBConfigModel> input = new ArrayList<DBConfigModel>();
	private Map<String,DBConfigModel> configMap = new HashMap<String,DBConfigModel>();
	
	public void init(IWorkbench workbench) {
		input = DBConfigFileUtil.getFileContent();
		for(DBConfigModel model : input){
			configMap.put(model.getName(), model);
		}
	}
	

	@Override
	protected void performApply() {
		DBConfigFileUtil.WriteDBConfigXML((List<DBConfigModel>)viewer.getInput());
		super.performApply();
	}

	@Override
	public boolean performOk() {
		DBConfigFileUtil.WriteDBConfigXML((List<DBConfigModel>)viewer.getInput());
		return super.performOk();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite comp = new Composite(parent,SWT.NONE);
		comp.setLayout(new GridLayout(1,false));
		ViewForm root = new ViewForm(comp,SWT.NONE);
		root.setLayoutData(new GridData(GridData.FILL_BOTH));
		root.setLayout(new FillLayout());
		viewer = new TableViewer(root, SWT.NONE);
		final Table table = new Table(root, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.SINGLE);
		viewer = new TableViewer(table);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		TableLayout layout = new TableLayout();
		for(int i=0;i<columnLayouts.length;i++){
			layout.addColumnData(columnLayouts[i]);
		}
		viewer.getTable().setLayout(layout);
		viewer.setSorter(new DBConfigTableSorter());
		TableColumn column;
		for (int i = 0; i < COLUMN_NAME.length; i++) {
			column = new TableColumn(viewer.getTable(), SWT.LEFT);
			column.setText(COLUMN_NAME[i]);
			column.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					((DBConfigTableSorter) viewer.getSorter()).doSort(1);
					viewer.refresh();
				}
			});
			viewer.getTable().getColumn(i).pack();
		}
		viewer.setContentProvider(new DBConfigTableViewerContentProvider());
		viewer.setLabelProvider(new DBConfigTableViewerLabelProvider());
		viewer.setInput(input);
		createActions();
		createContextMenu();
		viewer.setColumnProperties(COLUMN_NAME);
		viewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection select = (StructuredSelection) event
					.getSelection();
				if(select.isEmpty()){
					dbConfigDeleteAction.setEnabled(false);
					dbConfigEditAction.setEnabled(false);
					moveUpAction.setEnabled(false);
					moveUpToStartAction.setEnabled(false);
					moveDownAction.setEnabled(false);
					moveDownToEndAction.setEnabled(false);
				} else {
					dbConfigDeleteAction.setEnabled(true);
					dbConfigEditAction.setEnabled(true);
					moveUpAction.setEnabled(true);
					moveUpToStartAction.setEnabled(true);
					moveDownAction.setEnabled(true);
					moveDownToEndAction.setEnabled(true);
				}
			}
		});
		ToolBar toolBar = new ToolBar(root, SWT.FLAT); // ����һ��ToolBar����
		toolBar.setLayout(new RowLayout());
		toolBar.setSize(200, 20);
		ToolBarManager manager = new ToolBarManager(toolBar);
		manager.add(dbConfigAddAction);
		manager.add(dbConfigEditAction);
		manager.add(dbConfigDeleteAction);
		manager.add(new Separator());
		manager.add(moveUpAction);
		manager.add(moveUpToStartAction);
		manager.add(moveDownAction);
		manager.add(moveDownToEndAction);
		manager.update(true);

		Label leftTitile = new Label(root,SWT.TOP);
		leftTitile.setText(Messages.DBConfigPreferencePage_2);
		
		root.setTopRight(toolBar);
		root.setTopLeft(leftTitile);
		root.setContent(table);
		return comp;
	}
	
	private void createActions() {
		dbConfigAddAction = new DBConfigAddAction(viewer,configMap);
		dbConfigEditAction = new DBConfigEditAction(viewer,configMap);
		dbConfigDeleteAction = new DBConfigDeleteAction(viewer,configMap);
		moveUpAction = new TableRowMoveUpAction(viewer);
		moveUpToStartAction = new TableRowMoveUpToStartAction(viewer);
		moveDownAction = new TableRowMoveDownAction(viewer);
		moveDownToEndAction = new TableRowMoveDownToEndAction(viewer);
	}

	private void createContextMenu() {
		MenuManager menu = new MenuManager();
		
		menu.add(dbConfigAddAction);
		menu.add(dbConfigEditAction);
		menu.add(dbConfigDeleteAction);
		menu.add(new Separator());
		menu.add(moveUpAction);
		menu.add(moveUpToStartAction);
		menu.add(moveDownAction);
		menu.add(moveDownToEndAction);
		Menu m = menu.createContextMenu(getShell());
		// ���ö�������Ϊ���Ĳ˵�
		viewer.getTable().setMenu(m);
	}
}
