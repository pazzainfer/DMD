package com.leven.dmd.pro.util.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.pro.util.ImageHelper;
import com.leven.dmd.pro.util.ImageKeys;

public class TableSelectTreeViewer extends TreeViewer {
	/**
	 * ��ģ��ݱ��������
	 * @param parent ���ؼ�
	 * @param schema ��ģ����
	 * @param multi �Ƿ��ѡ
	 */
	public TableSelectTreeViewer(Composite parent,Schema schema,boolean multi) {
		super(parent,(multi?SWT.MULTI:SWT.SINGLE) | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		this.setLabelProvider(new ILabelProvider() {
			public void addListener(ILabelProviderListener listener) {}
			public void dispose() {}
			public boolean isLabelProperty(Object element, String property) {return false;}
			public void removeListener(ILabelProviderListener listener) {}
			public Image getImage(Object element) {
				if(element instanceof Column){
					return ImageHelper.getImage(ImageKeys.COLUMN);
				} else if(element instanceof Table){
					return ImageHelper.getImage(ImageKeys.TABLE);
				} else if(element instanceof TablePackage){
					return ImageHelper.getImage(ImageKeys.PACKAGE);
				} else if(element instanceof Schema){
					return ImageHelper.getImage(ImageKeys.SCHEMA);
				} else {
					return null;
				}
			}
			public String getText(Object element) {
				if(element instanceof Column){
					return ((Column)element).getName();
				} else if(element instanceof Table){
					return ((Table)element).getName();
				} else if(element instanceof TablePackage){
					return ((TablePackage)element).getName();
				} else if(element instanceof Schema){
					return ((Schema)element).getName();
				} else {
					return null;
				}
			}
		});
		this.setContentProvider(new ITreeContentProvider(){
			public void dispose() {}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
			public Object[] getElements(Object inputElement) {
				return (Object[])inputElement;
			}
			public Object[] getChildren(Object parentElement) {
				if(parentElement instanceof Schema){
					List tables = ((Schema)parentElement).getTables();
					List packages = ((Schema)parentElement).getTablePackages();
					List elements = new ArrayList();
					for(int i=0;i<tables.size();i++){
						elements.add(tables.get(i));
					}
					for(int i=0;i<packages.size();i++){
						elements.add(packages.get(i));
					}
					return elements.toArray();
				} else if(parentElement instanceof TablePackage){
					List elements = new ArrayList();
					elements.addAll(((TablePackage)parentElement).getTablePackages());
					elements.addAll(((TablePackage)parentElement).getTables());
					return elements.toArray();
				} else {
					return null;
				}
			}
			public Object getParent(Object element) {
				if(element instanceof Table){
					Object obj = ((Table)element).getParent();
					return obj!=null?obj:((Table)element).getSchema();
				} else if(element instanceof TablePackage){
					return ((TablePackage)element).getParent();
				} else {
					return null;
				}
			}
			public boolean hasChildren(Object element) {
				if(element instanceof Table){
					return false;
				} else {
					return true;
				}
			}
		});
		this.setInput(new Object[]{schema});
	}

}
