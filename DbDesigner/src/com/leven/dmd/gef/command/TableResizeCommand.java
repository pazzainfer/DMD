package com.leven.dmd.gef.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Table;

public class TableResizeCommand extends Command {
	private Table table;
	private int oldHeight;
	private int oldWidth;
	private int newHeight;
	private int newWidth;
	
	public TableResizeCommand(Table table){
		super(Messages.TableResizeCommand_0);
		this.table = table;
	}

	@Override
	public void execute() {
		Rectangle rec = table.getBounds().getCopy();
		oldHeight = rec.height;
		oldWidth = rec.width;
		rec.width = (newWidth);
		rec.height = (newHeight);
		table.modifyBounds(rec);
	}
	
	@Override
	public void undo() {
		Rectangle rec = table.getBounds().getCopy();
		newHeight = rec.height;
		newWidth = rec.width;
		rec.width = (oldWidth);
		rec.height = (oldHeight);
		table.modifyBounds(rec);
	}
	
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}

	public int getOldHeight() {
		return oldHeight;
	}

	public void setOldHeight(int oldHeight) {
		this.oldHeight = oldHeight;
	}

	public int getOldWidth() {
		return oldWidth;
	}

	public void setOldWidth(int oldWidth) {
		this.oldWidth = oldWidth;
	}

	public int getNewHeight() {
		return newHeight;
	}

	public void setNewHeight(int newHeight) {
		this.newHeight = newHeight;
	}

	public int getNewWidth() {
		return newWidth;
	}

	public void setNewWidth(int newWidth) {
		this.newWidth = newWidth;
	}
}