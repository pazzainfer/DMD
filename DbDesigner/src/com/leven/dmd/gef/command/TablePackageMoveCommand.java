package com.leven.dmd.gef.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.TablePackage;

/**
 * ������λ���ƶ�������ֻ��XYLayoutEditPolicy�з�����Ч
 * 
 * @author lifeng 2012-7-11 ����02:51:46
 */
public class TablePackageMoveCommand extends Command {

	private TablePackage tablePackage;
	private Rectangle oldBounds;
	private Rectangle newBounds;

	public TablePackageMoveCommand(TablePackage tablePackage, Rectangle oldBounds,
			Rectangle newBounds) {
		super(Messages.TablePackageMoveCommand_0);
		this.tablePackage = tablePackage;
		this.oldBounds = oldBounds;
		this.newBounds = newBounds;
	}

	public void execute() {
		tablePackage.modifyBounds(newBounds);
	}

	public void undo() {
		tablePackage.modifyBounds(oldBounds);
	}

}