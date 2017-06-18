package com.leven.dmd.gef.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;

/**
 * ɾ����Ĳ�������
 * 
 * @author lifeng 2012-7-11 ����02:38:48
 */
public class DeleteTableCommand extends Command {

	private Table table;
	private Schema schema;
	private int index = -1;
	private List foreignKeyRelationships = new ArrayList();
	private List primaryKeyRelationships = new ArrayList();
	private Rectangle bounds;

	public DeleteTableCommand() {
		super();
		this.setLabel(Messages.DeleteTableCommand_0);
	}

	public DeleteTableCommand(Schema schema,Table table) {
		super();
		this.setLabel(Messages.DeleteTableCommand_1);
		this.schema = schema;
		this.table = table;
	}
	
	private void deleteRelationships(Table t) {

		this.foreignKeyRelationships.addAll(t.getForeignKeyRelationships());

		// ѭ�������Ե�ǰ��Ϊ����ı��ϵ
		for (int i = 0; i < foreignKeyRelationships.size(); i++) {
			Relationship r = (Relationship) foreignKeyRelationships.get(i);
			r.getPrimaryKeyTable().removePrimaryKeyRelationship(r);
			t.removeForeignKeyRelationship(r);
		}

		// ѭ�������Ե�ǰ��Ϊ�����ı��ϵ
		this.primaryKeyRelationships.addAll(t.getPrimaryKeyRelationships());
		for (int i = 0; i < primaryKeyRelationships.size(); i++) {
			Relationship r = (Relationship) primaryKeyRelationships.get(i);
			r.getForeignKeyTable().removeForeignKeyRelationship(r);
			t.removePrimaryKeyRelationship(r);
		}
	}

	public void execute() {
		primExecute();
	}

	protected void primExecute() {
		deleteRelationships(table);
		index = schema.getTables().indexOf(table);
		schema.removeTable(table);
	}

	public void redo() {
		primExecute();
	}

	private void restoreRelationships() {
		for (int i = 0; i < foreignKeyRelationships.size(); i++) {
			Relationship r = (Relationship) foreignKeyRelationships.get(i);
			r.getForeignKeyTable().addForeignKeyRelationship(r);
			r.getPrimaryKeyTable().addPrimaryKeyRelationship(r);
		}
		foreignKeyRelationships.clear();
		for (int i = 0; i < primaryKeyRelationships.size(); i++) {
			Relationship r = (Relationship) primaryKeyRelationships.get(i);
			r.getForeignKeyTable().addForeignKeyRelationship(r);
			r.getPrimaryKeyTable().addPrimaryKeyRelationship(r);
		}
		primaryKeyRelationships.clear();
	}

	public void setTable(Table a) {
		table = a;
	}

	public void setSchema(Schema sa) {
		schema = sa;
	}

	public void undo() {
		schema.addTable(table, index);
		restoreRelationships();
		table.modifyBounds(bounds);
	}

	public void setOriginalBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

}