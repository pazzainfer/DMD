package com.leven.dmd.pro.nav.action.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.leven.dmd.gef.tmpfile.model.SchemaTemplate;
import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.util.ImageHelper;
import com.leven.dmd.pro.util.ImageKeys;

public class ExportDllSequenceSelectWizardPage extends WizardPage implements SelectionListener {
	
	private ListViewer listViewer;
	private TreeViewer treeViewer;
	private Button add;
	private Button remove;
	private Button removeAll;
	private List<SequenceTemplate> selectedList = new ArrayList<SequenceTemplate>();
	private Map<String, SequenceTemplate> selectedMap = new HashMap<String,SequenceTemplate>();
	private SchemaTemplate schemaTemplate;

	protected ExportDllSequenceSelectWizardPage(SchemaTemplate schemaTemplate
			,List<SequenceTemplate> selectedList,Map<String, SequenceTemplate> selectedMap) {
		super("ExportDllSequenceWizardPage"); //$NON-NLS-1$
		this.setTitle(Messages.ExportDllSequenceSelectWizardPage_1);
		this.setMessage(Messages.ExportDllSequenceSelectWizardPage_2, 2);
		this.schemaTemplate = schemaTemplate;
		this.selectedList = selectedList;
		this.selectedMap = selectedMap;
	}

	public void createControl(Composite parent) {
		Composite comp = new Composite(parent,SWT.NONE);
		comp.setLayout(new FormLayout());
		int midBtnWidth = 100;
		
		GridData btnGrid = new GridData(midBtnWidth - 10, 23);
		
		FormData leftFormData = new FormData();
		leftFormData.left = new FormAttachment(0, 1);
		leftFormData.top = new FormAttachment(0, 1);
		leftFormData.bottom = new FormAttachment(100, -1);
		//ʹ�������λ��������ƫ�ư����ť���
		leftFormData.right = new FormAttachment(50, -(midBtnWidth/2));

		FormData rightFormData = new FormData();
		//ʹ�ұ�����λ��������ƫ�ư����ť���
		rightFormData.left = new FormAttachment(50, midBtnWidth/2);
		rightFormData.top = new FormAttachment(0, 1);
		rightFormData.bottom = new FormAttachment(100, -1);
		rightFormData.right = new FormAttachment(100, -1);
		
		FormData midFormData = new FormData();
		//ʹ�ұ�����λ��������ƫ�ư����ť���
		midFormData.left = new FormAttachment(50, -(midBtnWidth/2));
		midFormData.top = new FormAttachment(0, 20);
		midFormData.bottom = new FormAttachment(100, -1);
		midFormData.right = new FormAttachment(50, midBtnWidth/2);
		
		GridData labGrid = new GridData(GridData.FILL_HORIZONTAL);
		labGrid.heightHint = 14;
		
		Composite leftComp = new Composite(comp, SWT.NONE);
		leftComp.setLayoutData(leftFormData);
		leftComp.setLayout(new GridLayout(1,false));
		Label leftLab = new Label(leftComp, SWT.NONE);
		leftLab.setText(Messages.ExportDllSequenceSelectWizardPage_3);
		leftLab.setLayoutData(labGrid);
		

		ScrolledComposite leftListComp = new ScrolledComposite(leftComp,SWT.H_SCROLL|SWT.V_SCROLL);
		leftListComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		makeSourceTree(leftListComp);
		leftListComp.setExpandHorizontal(true);
		leftListComp.setExpandVertical(true);
		///////
		
		Composite btnComp = new Composite(comp, SWT.NONE);
		btnComp.setLayoutData(midFormData);
		btnComp.setLayout(new GridLayout(1,false));
		
		add = new Button(btnComp, SWT.PUSH);
		add.setText(Messages.ExportDllSequenceSelectWizardPage_4);
		add.setLayoutData(btnGrid);
		add.addSelectionListener(this);
		
		remove = new Button(btnComp, SWT.PUSH);
		remove.setText(Messages.ExportDllSequenceSelectWizardPage_5);
		remove.setLayoutData(btnGrid);
		remove.addSelectionListener(this);
		
		removeAll = new Button(btnComp, SWT.PUSH);
		removeAll.setText(Messages.ExportDllSequenceSelectWizardPage_6);
		removeAll.setLayoutData(btnGrid);
		removeAll.addSelectionListener(this);
		
		///////
		Composite rightComp = new Composite(comp, SWT.NONE);
		rightComp.setLayoutData(rightFormData);
		rightComp.setLayout(new GridLayout(1,false));
		Label rightLab = new Label(rightComp, SWT.NONE);
		rightLab.setText(Messages.ExportDllSequenceSelectWizardPage_7);
		rightLab.setLayoutData(labGrid);
		
		
		ScrolledComposite rightListComp = new ScrolledComposite(rightComp, 
				SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
		rightListComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		makeSelectedList(rightListComp);
		rightListComp.setExpandHorizontal(true);
		rightListComp.setExpandVertical(true);
		
		this.setControl(comp);
	}
	
	private void makeSelectedList(ScrolledComposite rightListComp) {
		listViewer = new ListViewer(rightListComp, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		listViewer.setContentProvider(new IStructuredContentProvider (){
			public void dispose() {}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
			public Object[] getElements(Object inputElement) {
				return ((ArrayList)inputElement).toArray();
			}
		});
		listViewer.setLabelProvider(new ILabelProvider() {
			public void addListener(ILabelProviderListener listener) {}
			public void dispose() {}
			public boolean isLabelProperty(Object element, String property) {return false;}
			public void removeListener(ILabelProviderListener listener) {}
			public Image getImage(Object element) {
				return ImageHelper.getImage(ImageKeys.COLUMN);
			}
			public String getText(Object element) {
				if(element instanceof SequenceTemplate){
					return ((SequenceTemplate)element).getName();
				}
				return null;
			}
		});
		listViewer.setInput(selectedList);
		rightListComp.setContent(listViewer.getControl());
	}

	private void makeSourceTree(ScrolledComposite leftListComp) {
		treeViewer = new TreeViewer(leftListComp);
		treeViewer.setLabelProvider(new ILabelProvider() {
			public void addListener(ILabelProviderListener listener) {}
			public void dispose() {}
			public boolean isLabelProperty(Object element, String property) {return false;}
			public void removeListener(ILabelProviderListener listener) {}
			public Image getImage(Object element) {
				if(element instanceof SequenceTemplate){
					return ImageHelper.getImage(ImageKeys.SEQUENCE_TEMPLATE);
				}else if(element instanceof SchemaTemplate){
					return ImageHelper.getImage(ImageKeys.FOLDER);
				} else {
					return null;
				}
			}
			public String getText(Object element) {
				if(element instanceof SequenceTemplate){
					return ((SequenceTemplate)element).getName();
				} else if(element instanceof SchemaTemplate){
					return Messages.ExportDllSequenceSelectWizardPage_8;
				} else {
					return null;
				}
			}
		});
		treeViewer.setContentProvider(new ITreeContentProvider(){
			public void dispose() {}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
			public Object[] getElements(Object inputElement) {
				return (Object[])inputElement;
			}
			public Object[] getChildren(Object parentElement) {
				if(parentElement instanceof SchemaTemplate){
					return ((SchemaTemplate)parentElement).getSequenceTemplates().toArray();
				} else {
					return null;
				}
			}
			public Object getParent(Object element) {
				if(element instanceof SequenceTemplate){
					return ((SequenceTemplate)element).getSchemaTemplate();
				} else {
					return null;
				}
			}
			public boolean hasChildren(Object element) {
				if(element instanceof SchemaTemplate){
					return true;
				} else {
					return false;
				}
			}
		});
		treeViewer.setInput(new Object[]{schemaTemplate});
		leftListComp.setContent(treeViewer.getControl());
	}
	/**
	 * �ı������б�ֵ
	 * @author leven
	 * 2012-9-21 ����09:03:21
	 * @param select
	 * @param from
	 * @param to
	 */
	private void add(Object[] select, TreeViewer tree, ListViewer list) {
		// ѭ������ѡ�е�ѡ��ֵ
		SequenceTemplate temp;
		for (int i = 0; i < select.length; i++) {
			// ��һ���б����Ƴ���ѡ��ֵ
//			tree.remove(select[i]);
			if(!(select[i] instanceof SequenceTemplate)){
				continue;
			}
			temp = (SequenceTemplate)select[i];
			if(select[i] instanceof SequenceTemplate && !selectedMap.containsKey(temp.getName())){
				ArrayList<SequenceTemplate> li = (ArrayList<SequenceTemplate>)list.getInput();
				li.add(temp);
				list.setInput(li);
				selectedMap.put(temp.getName(), temp);
			}
		}
	}
	private void remove(Object[] select,ListViewer list, TreeViewer tree) {
		// ѭ������ѡ�е�ѡ��ֵ
		for (int i = 0; i < select.length; i++) {
			// ��һ���б����Ƴ���ѡ��ֵ
			SequenceTemplate c = (SequenceTemplate)select[i];
			list.remove(c);
			((ArrayList<SequenceTemplate>)list.getInput()).remove(c);
			selectedMap.remove(c.getName());
			// ��ӵ���һ���б���
//			tree.add(((SequenceTemplate)select[i]).getParent(),select[i]);
		}
	}
	private void removeAll(ListViewer list,TreeViewer tree){
		list.setInput(new ArrayList<SequenceTemplate>());
		selectedMap.clear();
	}
	public void widgetSelected(SelectionEvent e) {
		Button b = (Button) e.widget;
		if (b.getText().equals(Messages.ExportDllSequenceSelectWizardPage_9)){// �����">"��ť
			add(((TreeSelection)treeViewer.getSelection()).toArray(), treeViewer, listViewer);
		}else if (b.getText().equals(Messages.ExportDllSequenceSelectWizardPage_10)){// �����"<"��ť
			remove(((StructuredSelection)listViewer.getSelection()).toArray(), listViewer, treeViewer);
		}else if (b.getText().equals(Messages.ExportDllSequenceSelectWizardPage_11)){// �����"<"��ť
			removeAll(listViewer, treeViewer);
		}
		updateComplete();
	}
	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	private void updateComplete() {
		selectedList  = (ArrayList<SequenceTemplate>)listViewer.getInput();
		this.setMessage(Messages.ExportDllSequenceSelectWizardPage_0, 1);
		this.setPageComplete(true);
	}

	public List<SequenceTemplate> getSelectedList() {
		return selectedList;
	}
	public void setSelectedList(List<SequenceTemplate> selectedList) {
		this.selectedList = selectedList;
	}

	public Map<String, SequenceTemplate> getSelectedMap() {
		return selectedMap;
	}

	public void setSelectedMap(Map<String, SequenceTemplate> selectedMap) {
		this.selectedMap = selectedMap;
	}
}
