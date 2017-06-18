package com.leven.dmd.gef.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Table;

/**
 * ������λ���ƶ�������ֻ��XYLayoutEditPolicy�з�����Ч
 * 
 * @author lifeng 2012-7-11 ����02:51:46
 */
public class TableMoveCommand extends Command {

	private Table table;
	private Rectangle oldBounds;
	private Rectangle newBounds;

	public TableMoveCommand(Table table, Rectangle oldBounds,
			Rectangle newBounds) {
		super(Messages.TableMoveCommand_0);
		this.table = table;
		this.oldBounds = oldBounds;
		this.newBounds = newBounds;
	}

	public void execute() {
		table.modifyBounds(newBounds);
	}

	public void undo() {
		table.modifyBounds(oldBounds);
	}

}