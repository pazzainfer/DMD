package com.leven.dmd.gef.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.dnd.DataElementFactory;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

/**
 * 调色板工具栏的内容创造者
 * @author lifeng
 * 2012-7-11 上午03:13:58
 */
public class PaletteViewerCreator {

	/** the palette root */
	private PaletteRoot paletteRoot;

	/**
	 * 获取调色板
	 * @author lifeng
	 * @return
	 * PaletteRoot
	 * @datetime 2012-7-11 上午03:14:34
	 */
	public PaletteRoot createPaletteRoot() {
		// create root
		paletteRoot = new PaletteRoot();
		paletteRoot.setLabel(Messages.PaletteViewerCreator_0);

		// a group of default control tools
		PaletteGroup controls = new PaletteGroup("Controls");  //$NON-NLS-1$
		paletteRoot.add(controls);

		ToolEntry tool = new SelectionToolEntry();
		tool.setLabel("");  //$NON-NLS-1$
		paletteRoot.setDefaultEntry(tool);

		controls.add(tool);
		PaletteSeparator separator = new PaletteSeparator(
				Activator.PLUGIN_ID + ".palette.seperator");  //$NON-NLS-1$
		separator.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		
		controls.add(separator);
		/*CombinedTemplateCreationEntry packageEntry = new CombinedTemplateCreationEntry(
				"", Messages.PaletteViewerCreator_5, TablePackage.class,  //$NON-NLS-1$
				new DataElementFactory(TablePackage.class),
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.PACKAGE),
						AbstractUIPlugin.imageDescriptorFromPlugin(
								Activator.PLUGIN_ID, ImageKeys.PACKAGE));

		CombinedTemplateCreationEntry tableEntry = new CombinedTemplateCreationEntry(
				"", Messages.PaletteViewerCreator_7, Table.class,  //$NON-NLS-1$
				new DataElementFactory(Table.class),
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.ADD_TABLE),
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.ADD_TABLE));

		CombinedTemplateCreationEntry columnEntry = new CombinedTemplateCreationEntry(
				Messages.PaletteViewerCreator_8, Messages.PaletteViewerCreator_9, Column.class,
				new DataElementFactory(Column.class),
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.LABEL_COLUMN),
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.LABEL_COLUMN));
*/
		CustomConnectionCreationToolEntry conn = new CustomConnectionCreationToolEntry("",  //$NON-NLS-1$
				Messages.PaletteViewerCreator_11, null, AbstractUIPlugin
						.imageDescriptorFromPlugin(
								Activator.PLUGIN_ID,
								ImageKeys.ARROW_PURPLE), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								Activator.PLUGIN_ID,
								ImageKeys.ARROW_PURPLE));
//		controls.add(packageEntry);
//		controls.add(tableEntry);
//		controls.add(columnEntry);
		controls.add(separator);
		controls.add(conn);
		
		return paletteRoot;

	}
	
	private PaletteRoot createPaletteRoot(List<Table> list) {
		// create root
		paletteRoot = new PaletteRoot();
		paletteRoot.setLabel(Messages.PaletteViewerCreator_12);

		// a group of default control tools
		PaletteGroup controls = new PaletteGroup("Controls");  //$NON-NLS-1$
		paletteRoot.add(controls);

		ToolEntry tool = new SelectionToolEntry();
		tool.setLabel(Messages.PaletteViewerCreator_13);
		paletteRoot.setDefaultEntry(tool);

		controls.add(tool);
		
		PaletteDrawer drawer = new PaletteDrawer(Messages.PaletteViewerCreator_14,
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.TOOLBOX));
		List entries = new ArrayList();
		
		CombinedTemplateCreationEntry packageEntry = new CombinedTemplateCreationEntry(
				Messages.PaletteViewerCreator_15, Messages.PaletteViewerCreator_1, TablePackage.class, //$NON-NLS-2$
				new DataElementFactory(TablePackage.class),
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.PACKAGE),
						AbstractUIPlugin.imageDescriptorFromPlugin(
								Activator.PLUGIN_ID, ImageKeys.PACKAGE));

		CombinedTemplateCreationEntry tableEntry = new CombinedTemplateCreationEntry(
				Messages.PaletteViewerCreator_16, Messages.PaletteViewerCreator_2, Table.class, //$NON-NLS-2$
				new DataElementFactory(Table.class),
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.ADD_TABLE),
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.ADD_TABLE));

		CombinedTemplateCreationEntry columnEntry = new CombinedTemplateCreationEntry(
				Messages.PaletteViewerCreator_17, Messages.PaletteViewerCreator_3, Column.class, //$NON-NLS-2$
				new DataElementFactory(Column.class),
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.LABEL_COLUMN),
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.LABEL_COLUMN));
		// a separator
		PaletteSeparator separator = new PaletteSeparator(
				Activator.PLUGIN_ID + ".palette.seperator");  //$NON-NLS-1$
		separator.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);

		CustomConnectionCreationToolEntry conn = new CustomConnectionCreationToolEntry(Messages.PaletteViewerCreator_18,
				Messages.PaletteViewerCreator_26, null, AbstractUIPlugin 
						.imageDescriptorFromPlugin(
								Activator.PLUGIN_ID,
								ImageKeys.ARROW_PURPLE), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								Activator.PLUGIN_ID,
								ImageKeys.ARROW_PURPLE));
		entries.add(packageEntry);
		entries.add(tableEntry);
		entries.add(columnEntry);
		entries.add(separator);
		entries.add(conn);

		drawer.addAll(entries);

		paletteRoot.add(drawer);
		
		//paletteRoot = addTableList(paletteRoot, list);
		
		return paletteRoot;

	}
	public PaletteRoot createPaletteRoot(Schema schema){
		if(schema==null){
			paletteRoot = this.createPaletteRoot(new ArrayList<Table>());
		} else {
			if(!schema.isPackageOpen()){
				paletteRoot = this.createPaletteRoot(schema.getTables());
				paletteRoot = addTablePackageList(paletteRoot, schema.getTablePackages());
			}else {
				TablePackage package1 = schema.getOpenPackage();
				if(package1!=null){
					paletteRoot = this.createPaletteRoot(package1.getTables());
					paletteRoot = addTablePackageList(paletteRoot, package1.getTablePackages());
				}else {
					paletteRoot = this.createPaletteRoot(new ArrayList<Table>());
				}
			}
		}
		
		return paletteRoot;
	}

	/**
	 * �򹤾����������ݱ��б�
	 * 
	 * @author lifeng
	 * @param paletteRoot
	 * @return PaletteRoot
	 * @datetime 2012-7-10 ����09:29:13
	 */
	public PaletteRoot addTableList(PaletteRoot paletteRoot, List<Table> list) {
		PaletteRoot root = paletteRoot;
		PaletteDrawer tableDrawer = new PaletteDrawer(Messages.PaletteViewerCreator_19,
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.TABLE_LIST));
		if (list != null && list.size() > 0) {
			Table table;
			CombinedTemplateCreationEntry tableEntry;
			for (Iterator<Table> it = list.iterator(); it.hasNext();) {
				table = it.next();
				tableEntry = new CombinedTemplateCreationEntry(table.getCnName()+"("+table.getName()+")",   //$NON-NLS-1$//$NON-NLS-2$
						Messages.PaletteViewerCreator_28, table, new DataElementFactory(table),
						AbstractUIPlugin.imageDescriptorFromPlugin(
								Activator.PLUGIN_ID,
								ImageKeys.TABLE_MODEL),
						AbstractUIPlugin.imageDescriptorFromPlugin(
								Activator.PLUGIN_ID,
								ImageKeys.TABLE_MODEL));
				tableDrawer.add(tableEntry);
			}

		}

		paletteRoot.add(tableDrawer);
		return root;
	}
	/**
	 * �򹤾����������ݱ��б�
	 * 
	 * @author lifeng
	 * @param paletteRoot
	 * @return PaletteRoot
	 * @datetime 2012-7-10 ����09:29:13
	 */
	public PaletteRoot addTablePackageList(PaletteRoot paletteRoot, List<TablePackage> list) {
		PaletteRoot root = paletteRoot;
		PaletteDrawer tableDrawer = new PaletteDrawer(Messages.PaletteViewerCreator_20,
				AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.PACKAGE));
		if (list != null && list.size() > 0) {
			TablePackage table;
			CombinedTemplateCreationEntry tableEntry;
			for (Iterator<TablePackage> it = list.iterator(); it.hasNext();) {
				table = it.next();
				tableEntry = new CombinedTemplateCreationEntry(table.getName(),
						Messages.PaletteViewerCreator_28, table, new DataElementFactory(table),
						AbstractUIPlugin.imageDescriptorFromPlugin(
								Activator.PLUGIN_ID,
								ImageKeys.PACKAGE),
								AbstractUIPlugin.imageDescriptorFromPlugin(
										Activator.PLUGIN_ID,
										ImageKeys.PACKAGE));
				tableDrawer.add(tableEntry);
			}
			
		}
		
		paletteRoot.add(tableDrawer);
		return root;
	}

	public PaletteRoot getPaletteRoot() {
		return paletteRoot;
	}
}