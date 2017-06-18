package com.leven.dmd.gef.policy;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import com.leven.dmd.gef.command.TableAddCommand;
import com.leven.dmd.gef.command.TablePackageAddCommand;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.part.SchemaDiagramPart;

public class SchemaContainerEditPolicy extends ContainerEditPolicy {

	protected Command getAddCommand(GroupRequest request) {
		return null;
	}

	protected Command getCreateCommand(CreateRequest request) {

		Object newObject = request.getNewObject();
		if (newObject instanceof Table) {
			Point location = request.getLocation();
			SchemaDiagramPart schemaPart = (SchemaDiagramPart) getHost();
			Schema schema = schemaPart.getSchema();
			Table table = (Table) newObject;
			table.setLocation(location);
			TableAddCommand tableAddCommand = new TableAddCommand();
			tableAddCommand.setSchema(schema);
			tableAddCommand.setTable(table);
			return tableAddCommand;
		}else if(newObject instanceof TablePackage){
			Point location = request.getLocation();
			SchemaDiagramPart schemaPart = (SchemaDiagramPart) getHost();
			Schema schema = schemaPart.getSchema();
			TablePackage table = (TablePackage) newObject;
			if(table.getName()==null || table.getName().equals("")){
				table.setName("package" + (schema.getTablePackages().size() + 1));
			}
			table.setLocation(location);
			TablePackageAddCommand tableAddCommand = new TablePackageAddCommand();
			tableAddCommand.setSchema(schema);
			tableAddCommand.setTablePackage(table);
			return tableAddCommand;
		}else {
			return null;
		}
	}

	public EditPart getTargetEditPart(Request request) {
		if (REQ_CREATE.equals(request.getType()))
			return getHost();
		if (REQ_ADD.equals(request.getType()))
			return getHost();
		if (REQ_MOVE.equals(request.getType()))
			return getHost();
		return super.getTargetEditPart(request);
	}

}