package com.leven.dmd.gef.wizard.tableeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.util.constant.ITableViewerConstants;
import com.leven.dmd.gef.wizard.tableeditor.action.ColumnAddAction;
import com.leven.dmd.gef.wizard.tableeditor.action.ColumnCopyTemplateAction;
import com.leven.dmd.gef.wizard.tableeditor.action.ColumnCreateAction;
import com.leven.dmd.gef.wizard.tableeditor.action.ColumnDeleteAction;
import com.leven.dmd.gef.wizard.tableeditor.action.ColumnEditAction;
import com.leven.dmd.gef.wizard.tableeditor.action.ColumnImportAction;
import com.leven.dmd.gef.wizard.tableeditor.action.TableRowMoveDownAction;
import com.leven.dmd.gef.wizard.tableeditor.action.TableRowMoveDownToEndAction;
import com.leven.dmd.gef.wizard.tableeditor.action.TableRowMoveUpAction;
import com.leven.dmd.gef.wizard.tableeditor.action.TableRowMoveUpToStartAction;
/**
 * �Զ�����༭���
 * @author leven
 * 2012-8-17 ����03:12:50
 */
public class CustomTableViewer extends ViewForm {
	public static final String[] COLUMN_NAME = {"", Messages.CustomTableViewer_2, Messages.CustomTableViewer_1,Messages.CustomTableViewer_0,Messages.CustomTableViewer_3,Messages.CustomTableViewer_4,Messages.CustomTableViewer_5,Messages.CustomTableViewer_6}; //$NON-NLS-1$
	private static final ColumnLayoutData[] columnLayouts = { 
			new ColumnWeightData(20), 
			new ColumnWeightData(95),
			new ColumnWeightData(100), 
			new ColumnWeightData(30), 
			new ColumnWeightData(80),
			new ColumnWeightData(25),
			new ColumnWeightData(45),
			new ColumnWeightData(30)
			}; 
	private TableViewer viewer;
	private ColumnAddAction columnAddAction;
	private ColumnCreateAction columnCreateAction;
	private ColumnEditAction columnEditAction;
	private ColumnCopyTemplateAction columnCopyTemplateAction;
	private ColumnDeleteAction columnDeleteAction;
	private TableRowMoveUpAction columnMoveUpAction;
	private TableRowMoveUpToStartAction columnMoveUpToStartAction;
	private TableRowMoveDownAction columnMoveDownAction;
	private TableRowMoveDownToEndAction columnMoveDownToEndAction;
	private ColumnImportAction columnImportAction;
	
	private Text tableText;
	
	private Map<String,Column> columnMap = new HashMap<String,Column>();
	private Schema schema;
	private CustomIndexViewer indexViewer;

	/**
	 * 字段列表
	 * @param parent 父容器
	 * @param input 字段清单
	 * @param associates 所有关联字段清单
	 * @param schema 画布对象
	 * @param tableText 表名输入域对象
	 * @param canEdit 是否可编辑
	 */
	public CustomTableViewer(Composite parent, ArrayList<Column> input,
			final ArrayList<String> associates,Schema schema,Text tableText, boolean canEdit,CustomIndexViewer indexViewer) {
		super(parent, SWT.NONE);
		this.schema = schema;
		this.tableText = tableText;
		this.indexViewer = indexViewer;
		Column tempColumn;
		if(input!=null && input.size()>0){
			for(int i=0;i<input.size();i++){
				tempColumn = input.get(i);
				columnMap.put(tempColumn.getName().toLowerCase(), tempColumn);
			}
		}
		this.setLayout(new FillLayout());
		final Table table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		viewer = new TableViewer(table);
		// ���ñ�ͷ�ͱ���߿ɼ�
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		TableLayout layout = new TableLayout();
		for(int i=0;i<columnLayouts.length;i++){
			layout.addColumnData(columnLayouts[i]);
		}
			
		viewer.getTable().setLayout(layout);
		viewer.setSorter(new TableSorter());
		TableColumn column;
		for (int i = 0; i < COLUMN_NAME.length; i++) {
			if(i==ITableViewerConstants.COLUMN_ISPK_INDEX){
				column = new TableColumn(viewer.getTable(), SWT.CENTER);
			} else {
				column = new TableColumn(viewer.getTable(), SWT.LEFT);
			}
			column.setText(COLUMN_NAME[i]);
			viewer.getTable().getColumn(i).pack();
			final int index = i;
			if(index>=1){
				column.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						((TableSorter) viewer.getSorter()).doSort(index);
						viewer.refresh();
					}
				});
			}
		}

		// �������
		viewer.setContentProvider(new CustomTableViewerContentProvider());
		// ������ͼ
		viewer.setLabelProvider(new CustomTableViewerLabelProvider());
		// ���ñ����ݶ��󣬸÷����ǳ���Ҫ���Ǳ��������
		viewer.setInput(input);
		viewer.setColumnProperties(COLUMN_NAME);

		if(canEdit){	//可编辑状态下，添加表格编辑器，添加工具栏
			createActions();
			createContextMenu();
		CellEditor[] editors = new CellEditor[8];
		editors[0] = null;
		editors[1] = new TextCellEditor(viewer.getTable());
		editors[2] = new TextCellEditor(viewer.getTable());
		editors[3] = null;
		ComboBoxCellEditor typeEditor = new ComboBoxCellEditor(viewer.getTable(),ColumnType.typeArray,SWT.READ_ONLY);
		typeEditor.setActivationStyle(SWT.Expand);
		editors[4] = typeEditor;
		editors[5] = new TextCellEditor(viewer.getTable());
		editors[6] = new TextCellEditor(viewer.getTable());
		CheckboxCellEditor pkEditor = new CheckboxCellEditor(viewer.getTable());
		editors[7] = pkEditor;
		// 设置表格编辑器
		viewer.setCellEditors(editors);
		// 设置表格编辑监听
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				Column p = (Column)element;
				if(p.getColumnTemplate()!=null && p.getColumnTemplate().getColumnName()!=null){
					if(property.equals(COLUMN_NAME[7])){
						return true;
					}else {
						if(p.isTempCanEdit()){
							if(property.equals(COLUMN_NAME[2]) || property.equals(COLUMN_NAME[1])){
								return true;
							}
						}
						return false;
					}
				} 
				if (property.equals(COLUMN_NAME[2])) {
					return true;
				} else if (property.equals(COLUMN_NAME[1])) {
					return true;
				} else if (property.equals(COLUMN_NAME[4])) {
					return true;
				} else if (property.equals(COLUMN_NAME[5])) {
					if(ColumnType.hasLength(p.getType())){
						return true;
					} else {
						return false;
					}
				} else if (property.equals(COLUMN_NAME[6])) {
					if(ColumnType.hasScale(p.getType())){
						return true;
					} else {
						return false;
					}
				} else if (property.equals(COLUMN_NAME[7])) {
					return true;
				} else {
					return false;
				}
			}

			public Object getValue(Object element, String property) {
				Column p = (Column) element;
				if (property.equals(COLUMN_NAME[2])) {
					return p.getCnName();
				} else if (property.equals(COLUMN_NAME[1])) {
					return p.getName();
				} else if (property.equals(COLUMN_NAME[4])) {
					return ColumnType.getTypeIndex(p.getType());
				} else if (property.equals(COLUMN_NAME[5])) {
					return p.getLength()+""; //$NON-NLS-1$
				} else if (property.equals(COLUMN_NAME[6])) {
					return p.getScale()+""; //$NON-NLS-1$
				} else if (property.equals(COLUMN_NAME[7])) {
					return p.isPk();
				} else {
					return null;
				}
			}

			public void modify(Object element, String property, Object value) {
				try{
				if (element instanceof Item) {
					element = ((Item) element).getData();
				}
				Column p = (Column) element;
				if (property.equals(COLUMN_NAME[2])) {
					p.setCnName((String)value);
				}else if (property.equals(COLUMN_NAME[1])) {
					Column temp = columnMap.get((String)value);
					if(associates != null && associates.size()>0){
						for(Iterator<String> it = associates.iterator();it.hasNext();){
							if(p.getName().equals(it.next())){
								MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
								mb.setText(Messages.CustomTableViewer_9);
								mb.setMessage(Messages.CustomTableViewer_10);
								mb.open();
								return;
							}
						}
					}
					if(temp==null){
						if(value == null || ((String)value).trim().equals("")){ //$NON-NLS-1$
							MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
							mb.setText(Messages.CustomTableViewer_9);
							mb.setMessage(Messages.CustomTableViewer_13);
							mb.open();
							return;
						}else {
							columnMap.remove(p.getName().toLowerCase());
							p.setName((String)value);
							columnMap.put(((String)value).toLowerCase(), p);
						}
					} else {
						if(!temp.getName().equals(p.getName())){
							MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
							mb.setText(Messages.CustomTableViewer_9);
							mb.setMessage(Messages.CustomTableViewer_15);
							mb.open();
							return;
						}
					}
				} else if (property.equals(COLUMN_NAME[4])) {
					if(((Integer)value)>=0){
						p.setType(ColumnType.typeArray[(Integer)value]);
					}
				} else if (property.equals(COLUMN_NAME[5])) {
					String val = ((String)value);
					if(ColumnType.hasLength(p.getType())){
						if(!val.matches("[\\d]{1,}") || Integer.parseInt(val) <= 0){ //$NON-NLS-1$
							MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
							mb.setText(Messages.CustomTableViewer_9);
							mb.setMessage(Messages.CustomTableViewer_18);
							mb.open();
							return;
						} else {
							p.setLength(value.equals("")?0:Integer.parseInt(val)); //$NON-NLS-1$
						}
					}else {
						p.setLength(0);
					}
				} else if (property.equals(COLUMN_NAME[6])) {
					String val = ((String)value);
					if(ColumnType.hasScale(p.getType())){
						if(!val.matches("[\\d]{1,}") || Integer.parseInt(val) <= 0){ //$NON-NLS-1$
							MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
							mb.setText(Messages.CustomTableViewer_9);
							mb.setMessage(Messages.CustomTableViewer_22);
							mb.open();
							return;
						} else {
							p.setScale(value.equals("")?0:Integer.parseInt(val)); //$NON-NLS-1$
						}
					}else {
						p.setScale(0);
					}
				} else if (property.equals(COLUMN_NAME[7])) {
					Boolean isPk = (Boolean)value;
					if(p.isPk() && !isPk){
						CustomTableViewer.this.getIndexViewer().refreshWhenColumnNotPK(p.getName());
					}else if(!p.isPk() && isPk){
						CustomTableViewer.this.getIndexViewer().refreshWhenColumnPK(p.getName());
					}
					p.setPk(isPk);
				}
				viewer.refresh();
			}catch (Exception e1){
			 e1.printStackTrace();	
			}
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection select = (StructuredSelection) event
					.getSelection();
				if(select.isEmpty()){
					if(columnCreateAction!=null){
						columnCreateAction.setEnabled(false);
					}
					columnDeleteAction.setEnabled(false);
					columnEditAction.setEnabled(false);
					columnMoveUpAction.setEnabled(false);
					columnMoveUpToStartAction.setEnabled(false);
					columnMoveDownAction.setEnabled(false);
					columnMoveDownToEndAction.setEnabled(false);
				} else {
					columnDeleteAction.setEnabled(true);
					if(select.size()==1){
						if(columnCreateAction!=null){
							columnCreateAction.setEnabled(true);
						}
						columnEditAction.setEnabled(true);
						columnMoveUpAction.setEnabled(true);
						columnMoveUpToStartAction.setEnabled(true);
						columnMoveDownAction.setEnabled(true);
						columnMoveDownToEndAction.setEnabled(true);
					}else {
						columnEditAction.setEnabled(false);
						columnMoveUpAction.setEnabled(false);
						columnMoveUpToStartAction.setEnabled(false);
						columnMoveDownAction.setEnabled(false);
						columnMoveDownToEndAction.setEnabled(false);
					}
				}
			}
		});
		viewer.getTable().addMenuDetectListener(new MenuDetectListener(){
			public void menuDetected(MenuDetectEvent e) {
				StructuredSelection select = (StructuredSelection) viewer
					.getSelection();
				if(select.isEmpty()){
					if(columnCreateAction!=null){
						columnCreateAction.setEnabled(false);
					}
					columnDeleteAction.setEnabled(false);
					columnEditAction.setEnabled(false);
					columnMoveUpAction.setEnabled(false);
					columnMoveUpToStartAction.setEnabled(false);
					columnMoveDownAction.setEnabled(false);
					columnMoveDownToEndAction.setEnabled(false);
				} else {
					columnDeleteAction.setEnabled(true);
					if(select.size()==1){
						if(columnCreateAction!=null){
							columnCreateAction.setEnabled(true);
						}
						columnEditAction.setEnabled(true);
						columnMoveUpAction.setEnabled(true);
						columnMoveUpToStartAction.setEnabled(true);
						columnMoveDownAction.setEnabled(true);
						columnMoveDownToEndAction.setEnabled(true);
					}else {
						columnEditAction.setEnabled(false);
						columnMoveUpAction.setEnabled(false);
						columnMoveUpToStartAction.setEnabled(false);
						columnMoveDownAction.setEnabled(false);
						columnMoveDownToEndAction.setEnabled(false);
					}
				}
			}
		});
		ToolBar toolBar = new ToolBar(this, SWT.FLAT); // ����һ��ToolBar����
		ToolBarManager manager = new ToolBarManager(toolBar);
		manager.add(columnMoveUpAction);
		manager.add(columnMoveUpToStartAction);
		manager.add(columnMoveDownAction);
		manager.add(columnMoveDownToEndAction);
		manager.add(new Separator());
		if(columnCreateAction!=null){
			manager.add(columnCreateAction);
		}
		manager.add(columnImportAction);
		manager.add(columnCopyTemplateAction);
		manager.add(columnAddAction);
		manager.add(columnEditAction);
		manager.add(columnDeleteAction);
		manager.update(true);
		
		Label topLeft = new Label(this,SWT.NONE);
		topLeft.setText(Messages.CustomTableViewer_24);
		
		this.setTopRight(toolBar);
		this.setTopLeft(topLeft);
		}
		
		this.setContent(table);
	}
	private void createActions() {
		columnCopyTemplateAction = new ColumnCopyTemplateAction(viewer,columnMap,schema);
		columnImportAction = new ColumnImportAction(viewer,columnMap,schema);
		columnAddAction = new ColumnAddAction(viewer,columnMap,schema,this);
		columnEditAction = new ColumnEditAction(viewer,columnMap,schema,this);
		columnDeleteAction = new ColumnDeleteAction(viewer,columnMap,this);
		columnMoveUpAction = new TableRowMoveUpAction(viewer);
		columnMoveUpToStartAction = new TableRowMoveUpToStartAction(viewer);
		columnMoveDownAction = new TableRowMoveDownAction(viewer);
		columnMoveDownToEndAction = new TableRowMoveDownToEndAction(viewer);
		columnCreateAction = new ColumnCreateAction(viewer,schema,tableText);
	}

	private void createContextMenu() {
		MenuManager menu = new MenuManager();
		
		if(columnCreateAction!=null){
			menu.add(columnCreateAction);
		}
		menu.add(columnAddAction);
		menu.add(columnEditAction);
		menu.add(columnImportAction);
		menu.add(columnCopyTemplateAction);
		menu.add(columnDeleteAction);
		menu.add(new Separator());
		menu.add(columnMoveUpAction);
		menu.add(columnMoveUpToStartAction);
		menu.add(columnMoveDownAction);
		menu.add(columnMoveDownToEndAction);
		Menu m = menu.createContextMenu(getShell());
		// ���ö�������Ϊ���Ĳ˵�
		viewer.getTable().setMenu(m);
	}
	public TableViewer getViewer() {
		return viewer;
	}
	public void setViewer(TableViewer viewer) {
		this.viewer = viewer;
	}
	public CustomIndexViewer getIndexViewer() {
		return indexViewer;
	}
	public void setIndexViewer(CustomIndexViewer indexViewer) {
		this.indexViewer = indexViewer;
	}
}