package com.leven.dmd.gef.wizard.tableeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.IndexType;
import com.leven.dmd.gef.model.TableIndex;
import com.leven.dmd.gef.util.constant.ITableViewerConstants;
import com.leven.dmd.gef.wizard.tableeditor.action.IndexAddAction;
import com.leven.dmd.gef.wizard.tableeditor.action.IndexDeleteAction;
import com.leven.dmd.gef.wizard.tableeditor.action.IndexTableRefreshAction;
import com.leven.dmd.gef.wizard.tableeditor.action.TableRowMoveDownAction;
import com.leven.dmd.gef.wizard.tableeditor.action.TableRowMoveDownToEndAction;
import com.leven.dmd.gef.wizard.tableeditor.action.TableRowMoveUpAction;
import com.leven.dmd.gef.wizard.tableeditor.action.TableRowMoveUpToStartAction;
/**
 * �Զ�����༭���
 * @author leven
 * 2012-8-17 ����03:12:50
 */
public class CustomIndexViewer extends ViewForm {
	public static final String[] COLUMN_NAME = {Messages.CustomIndexViewer_0, Messages.CustomIndexViewer_1,Messages.CustomIndexViewer_2,Messages.CustomIndexViewer_3};
	private static final ColumnLayoutData[] columnLayouts = { 
			new ColumnWeightData(100), 
			new ColumnWeightData(200), 
			new ColumnWeightData(100), 
			new ColumnWeightData(200)
			}; 
	private TableViewer viewer;
	private IndexAddAction indexAddAction;
	private IndexDeleteAction indexDeleteAction;
	private IndexTableRefreshAction indexTableRefreshAction;

	private TableRowMoveUpAction columnMoveUpAction;
	private TableRowMoveUpToStartAction columnMoveUpToStartAction;
	private TableRowMoveDownAction columnMoveDownAction;
	private TableRowMoveDownToEndAction columnMoveDownToEndAction;
	
	private Map<String,TableIndex> indexMap = new HashMap<String,TableIndex>();

	/**
	 * 表索引列表显示器
	 * @param parent 父容器控件
	 * @param input 索引清单
	 * @param columnTableViewer 字段列表控件
	 * @param canEdit 是否可编辑
	 */
	public CustomIndexViewer(Composite parent, ArrayList<TableIndex> input, final TableViewer columnTableViewer,boolean canEdit) {
		super(parent, SWT.NONE);
		TableIndex tempColumn;
		if(input!=null && input.size()>0){
			for(int i=0;i<input.size();i++){
				tempColumn = input.get(i);
				indexMap.put(tempColumn.getName(), tempColumn);
			}
		}
		this.setLayout(new FillLayout());
		final Table table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.SINGLE);
		viewer = new TableViewer(table);
		// ���ñ�ͷ�ͱ���߿ɼ�
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		TableLayout layout = new TableLayout();
		for(int i=0;i<columnLayouts.length;i++){
			layout.addColumnData(columnLayouts[i]);
		}
			
		viewer.getTable().setLayout(layout);
		viewer.setSorter(new IndexTableSorter());
		TableColumn column;
		for (int i = 0; i < COLUMN_NAME.length; i++) {
			column = new TableColumn(viewer.getTable(), SWT.LEFT);
			column.setText(COLUMN_NAME[i]);
			viewer.getTable().getColumn(i).pack();
		}

		column = viewer.getTable().getColumn(ITableViewerConstants.INDEX_NAME_INDEX);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((IndexTableSorter) viewer.getSorter()).doSort(ITableViewerConstants.INDEX_NAME_INDEX);
				viewer.refresh();
			}
		});
		column = viewer.getTable().getColumn(ITableViewerConstants.INDEX_COLUMNS_INDEX);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((IndexTableSorter) viewer.getSorter()).doSort(ITableViewerConstants.INDEX_COLUMNS_INDEX);
				viewer.refresh();
			}
		});
		column = viewer.getTable().getColumn(ITableViewerConstants.INDEX_TYPE_INDEX);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((IndexTableSorter) viewer.getSorter()).doSort(ITableViewerConstants.INDEX_TYPE_INDEX);
				viewer.refresh();
			}
		});
		column = viewer.getTable().getColumn(ITableViewerConstants.INDEX_COMMENTS_INDEX);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((IndexTableSorter) viewer.getSorter()).doSort(ITableViewerConstants.INDEX_COMMENTS_INDEX);
				viewer.refresh();
			}
		});

		// �������
		viewer.setContentProvider(new CustomTableViewerContentProvider());
		// ������ͼ
		viewer.setLabelProvider(new CustomIndexTableViewerLabelProvider());
		// ���ñ����ݶ��󣬸÷����ǳ���Ҫ���Ǳ��������
		viewer.setInput(input);
		viewer.setColumnProperties(COLUMN_NAME);
		if(canEdit){
		createActions();
		createContextMenu();

		// ���õ�Ԫ��༭���������
		CellEditor[] editors = new CellEditor[4];
		editors[0] = new TextCellEditor(viewer.getTable());
		editors[1] = new DialogCellEditor(viewer.getTable()){
			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				String value = (String)getValue();
				Map<String,String> map = new HashMap<String,String>();
				ColumnSelectWizard wizard = new ColumnSelectWizard((ArrayList<Column>)columnTableViewer.getInput(),value.split(","),map); //$NON-NLS-1$
//				ColumnSelectDialog dialog = new ColumnSelectDialog(cellEditorWindow.getShell(),
//						columns,value.split(","));
//				dialog.create();
				WizardDialog dialog = new WizardDialog(cellEditorWindow.getShell(),wizard);
				dialog.create();
				if(dialog.open()==WizardDialog.OK){
					return map.get("result"); //$NON-NLS-1$
				}else {
					return null;
				}
			}
		};
		ComboBoxCellEditor typeEditor = new ComboBoxCellEditor(viewer.getTable(),IndexType.typeArray,SWT.READ_ONLY);
		typeEditor.setActivationStyle(SWT.Expand);
		editors[2] = typeEditor;
		editors[3] = new TextCellEditor(viewer.getTable());
		// ���õ�Ԫ��༭��
		viewer.setCellEditors(editors);
		// ���õ�Ԫ���޸���
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				TableIndex p = (TableIndex) element;
				if(p.isAutoCreated()){
					return false;
				}
				return true;
			}

			public Object getValue(Object element, String property) {
				TableIndex p = (TableIndex) element;
				if (property.equals(COLUMN_NAME[0])) {
					return p.getName();
				} else if (property.equals(COLUMN_NAME[1])) {
					return p.getColumns();
				} else if (property.equals(COLUMN_NAME[2])) {
					return IndexType.getTypeIndex(p.getType());
				} else if (property.equals(COLUMN_NAME[3])) {
					return p.getComments();
				}
				
				return null;
			}

			public void modify(Object element, String property, Object value) {
				if (element instanceof Item) {
					element = ((Item) element).getData();
				}
				TableIndex p = (TableIndex) element;
				if (property.equals(COLUMN_NAME[0])) {
					p.setName((String)value);
				}else if (property.equals(COLUMN_NAME[1])) {
					p.setColumns((String)value);
				} else if (property.equals(COLUMN_NAME[2])) {
					p.setType(IndexType.typeArray[(Integer)value]);
				} else if (property.equals(COLUMN_NAME[3])) {
					p.setComments((String)value);
				}
				viewer.refresh();
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection select = (StructuredSelection) event
					.getSelection();
				if(select.isEmpty()){
					indexDeleteAction.setEnabled(false);
					columnMoveUpAction.setEnabled(false);
					columnMoveUpToStartAction.setEnabled(false);
					columnMoveDownAction.setEnabled(false);
					columnMoveDownToEndAction.setEnabled(false);
					indexTableRefreshAction.setObj(null);
				} else {
					indexDeleteAction.setEnabled(true);
					columnMoveUpAction.setEnabled(true);
					columnMoveUpToStartAction.setEnabled(true);
					columnMoveDownAction.setEnabled(true);
					columnMoveDownToEndAction.setEnabled(true);
					indexTableRefreshAction.setObj(select.getFirstElement());
				}
			}
		});
		viewer.getTable().addMenuDetectListener(new MenuDetectListener(){
			public void menuDetected(MenuDetectEvent e) {
				StructuredSelection select = (StructuredSelection) viewer
					.getSelection();
				if(select.isEmpty()){
					indexDeleteAction.setEnabled(false);
					columnMoveUpAction.setEnabled(false);
					columnMoveUpToStartAction.setEnabled(false);
					columnMoveDownAction.setEnabled(false);
					columnMoveDownToEndAction.setEnabled(false);
					indexTableRefreshAction.setObj(null);
				} else {
					indexDeleteAction.setEnabled(true);
					columnMoveUpAction.setEnabled(true);
					columnMoveUpToStartAction.setEnabled(true);
					columnMoveDownAction.setEnabled(true);
					columnMoveDownToEndAction.setEnabled(true);
					indexTableRefreshAction.setObj(select.getFirstElement());
				}
			}
		});
		ToolBar toolBar = new ToolBar(this, SWT.FLAT); // ����һ��ToolBar����
		this.setTopRight(toolBar);
		ToolBarManager manager = new ToolBarManager(toolBar);
		manager.add(columnMoveUpAction);
		manager.add(columnMoveUpToStartAction);
		manager.add(columnMoveDownAction);
		manager.add(columnMoveDownToEndAction);
		manager.add(new Separator());
		manager.add(indexAddAction);
		manager.add(indexDeleteAction);
		manager.add(new Separator());
		manager.add(indexTableRefreshAction);
		manager.update(true);
		
		Label topLeft = new Label(this,SWT.NONE);
		topLeft.setText(Messages.CustomIndexViewer_6);
		this.setTopLeft(topLeft);
		}
		this.setContent(table);
	}
	private void createActions() {
		indexAddAction = new IndexAddAction(viewer,indexMap);
		indexDeleteAction = new IndexDeleteAction(viewer,indexMap);
		
		indexTableRefreshAction = new IndexTableRefreshAction(viewer);

		columnMoveUpAction = new TableRowMoveUpAction(viewer);
		columnMoveUpToStartAction = new TableRowMoveUpToStartAction(viewer);
		columnMoveDownAction = new TableRowMoveDownAction(viewer);
		columnMoveDownToEndAction = new TableRowMoveDownToEndAction(viewer);
	}

	private void createContextMenu() {
		MenuManager menu = new MenuManager();
		menu.add(columnMoveUpAction);
		menu.add(columnMoveUpToStartAction);
		menu.add(columnMoveDownAction);
		menu.add(columnMoveDownToEndAction);
		menu.add(new Separator());
		
		menu.add(indexAddAction);
		menu.add(indexDeleteAction);
		menu.add(new Separator());
		menu.add(indexTableRefreshAction);
		Menu m = menu.createContextMenu(getShell());
		// ���ö�������Ϊ���Ĳ˵�
		viewer.getTable().setMenu(m);
	}
	/**
	 * 当字段变更为PK时刷新索引
	 * @param column
	 */
	public void refreshWhenColumnPK(String column){
		ArrayList<TableIndex> input = (ArrayList<TableIndex>)viewer.getInput();
		boolean hasAuto = false;
		for(int i=0;i<input.size();i++){
			TableIndex index = input.get(i);
			if(index.isAutoCreated()){
				if(index.getColumns().equals(column) 
						|| index.getColumns().contains(column+",")
						|| index.getColumns().contains(","+column)){
					return;
				} else {
					if("".equals(index.getColumns())){
						index.setColumns(column);
						viewer.refresh();
					}else {
						index.setColumns(index.getColumns()+","+column);
						viewer.refresh();
					}
				}
				hasAuto = true;
				break;
			}
		}
		if(!hasAuto){
			TableIndex newIndex = new TableIndex("pk_auto", column, IndexType.UNIQUE.getType(),true);
			input.add(newIndex);
			viewer.add(newIndex);
		}
	}
	/**
	 * 当字段取消PK时刷新索引
	 * @param column
	 */
	public void refreshWhenColumnNotPK(String column){
		ArrayList<TableIndex> input = (ArrayList<TableIndex>)viewer.getInput();
		int removeIndex = -1;
		for(int i=0;i<input.size();i++){
			TableIndex index = input.get(i);
			if(index.isAutoCreated()){
				if(index.getColumns().contains(column+",")){
					index.setColumns(index.getColumns().replace(column+",", ""));
					return;
				}else if(index.getColumns().contains(","+column)){
					index.setColumns(index.getColumns().replace(","+column, ""));
					return;
				}else if(index.getColumns().equals(column)){
					removeIndex = i;
					break;
				}
				return;
			}
		}
		if(removeIndex>-1){
			viewer.remove(removeIndex);
			input.remove(removeIndex);
		}
		viewer.refresh();
	}
	public TableViewer getViewer() {
		return viewer;
	}
	public void setViewer(TableViewer viewer) {
		this.viewer = viewer;
	}
}
