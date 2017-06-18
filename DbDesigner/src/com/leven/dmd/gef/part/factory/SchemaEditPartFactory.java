package com.leven.dmd.gef.part.factory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.ui.IEditorPart;

import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.part.ColumnPart;
import com.leven.dmd.gef.part.RelationshipPart;
import com.leven.dmd.gef.part.SchemaDiagramPart;
import com.leven.dmd.gef.part.TablePackagePart;
import com.leven.dmd.gef.part.TablePart;
/**
 * �����������Ĺ�����
 * @author lifeng
 * 2012-7-11 ����03:29:49
 */
public class SchemaEditPartFactory implements EditPartFactory {
	private IEditorPart editor;
	public SchemaEditPartFactory(IEditorPart editor) {
		this.editor = editor;
	}

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		if (model instanceof Schema)
			part = new SchemaDiagramPart();
		else if (model instanceof Table)
			part = new TablePart(editor);
		else if (model instanceof TablePackage)
			part = new TablePackagePart(editor);
		else if (model instanceof Relationship)
			part = new RelationshipPart(editor);
		else if (model instanceof Column)
			part = new ColumnPart(editor);
		part.setModel(model);
		return part;
	}
}