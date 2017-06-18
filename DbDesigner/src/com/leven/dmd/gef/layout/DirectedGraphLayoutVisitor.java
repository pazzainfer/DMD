package com.leven.dmd.gef.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;

import com.leven.dmd.gef.figures.TableFigure;
import com.leven.dmd.gef.figures.TablePackageFigure;
import com.leven.dmd.gef.part.RelationshipPart;
import com.leven.dmd.gef.part.SchemaDiagramPart;
import com.leven.dmd.gef.part.TablePackagePart;
import com.leven.dmd.gef.part.TablePart;

public class DirectedGraphLayoutVisitor {

	Map partToNodesMap;
	DirectedGraph graph;

	/**
	 * Public method for reading graph nodes
	 */
	public void layoutDiagram(SchemaDiagramPart diagram) {

		partToNodesMap = new HashMap();

		graph = new DirectedGraph();
		addNodes(diagram);
		if (graph.nodes.size() > 0) {
			addEdges(diagram);
			new NodeJoiningDirectedGraphLayout().visit(graph);
			applyResults(diagram);
		}

	}

	// ******************* SchemaDiagramPart contribution methods **********/

	protected void addNodes(SchemaDiagramPart diagram) {
		GraphAnimation.recordInitialState(diagram.getFigure());
		IFigure fig = diagram.getFigure();
		for (int i = 0; i < diagram.getChildren().size(); i++) {
			Object obj = diagram.getChildren().get(i);
			if(obj instanceof TablePart){
				addNodes((TablePart) obj);
			}else if(obj instanceof TablePackagePart){
				addNodes((TablePackagePart) obj);
			}
		}
	}

	/**
	 * Adds nodes to the graph object for use by the GraphLayoutManager
	 */
	protected void addNodes(TablePart tablePart) {
		Node n = new Node(tablePart);
		n.width = tablePart.getFigure().getPreferredSize(400, 300).width;
		n.height = tablePart.getFigure().getPreferredSize(400, 300).height;
		n.setPadding(new Insets(10, 8, 10, 12));
		partToNodesMap.put(tablePart, n);
		graph.nodes.add(n);
	}
	protected void addNodes(TablePackagePart tablePart) {
		Node n = new Node(tablePart);
		n.width = tablePart.getFigure().getPreferredSize(400, 300).width;
		n.height = tablePart.getFigure().getPreferredSize(400, 300).height;
		n.setPadding(new Insets(10, 8, 10, 12));
		partToNodesMap.put(tablePart, n);
		graph.nodes.add(n);
	}

	protected void addEdges(SchemaDiagramPart diagram) {
		for (int i = 0; i < diagram.getChildren().size(); i++) {
			Object obj = diagram.getChildren().get(i);
			if(obj instanceof TablePart){
				addEdges((TablePart)obj);
			}
		}
	}

	// ******************* TablePart contribution methods **********/

	protected void addEdges(TablePart tablePart) {
		List outgoing = tablePart.getSourceConnections();
		for (int i = 0; i < outgoing.size(); i++) {
			RelationshipPart relationshipPart = (RelationshipPart) tablePart
					.getSourceConnections().get(i);
			addEdges(relationshipPart);
		}
	}

	// ******************* RelationshipPart contribution methods **********/

	protected void addEdges(RelationshipPart relationshipPart) {
		GraphAnimation.recordInitialState((Connection) relationshipPart
				.getFigure());
		Node source = (Node) partToNodesMap.get(relationshipPart.getSource());
		Node target = (Node) partToNodesMap.get(relationshipPart.getTarget());
		Edge e = new Edge(relationshipPart, source, target);
		e.weight = 2;
		graph.edges.add(e);
		partToNodesMap.put(relationshipPart, e);
	}

	// ******************* SchemaDiagramPart apply methods **********/

	protected void applyResults(SchemaDiagramPart diagram) {
		applyChildrenResults(diagram);
	}

	protected void applyChildrenResults(SchemaDiagramPart diagram) {
		for (int i = 0; i < diagram.getChildren().size(); i++) {
			Object obj = diagram.getChildren().get(i);
			if(obj instanceof TablePart){
				TablePart tablePart = (TablePart) diagram.getChildren().get(i);
				applyResults(tablePart);
			}else if(obj instanceof TablePackagePart){
				TablePackagePart tablePart = (TablePackagePart) diagram.getChildren().get(i);
				applyResults(tablePart);
			}
		}
	}

	protected void applyOwnResults(SchemaDiagramPart diagram) {
	}

	// ******************* TablePart apply methods **********/

	public void applyResults(TablePart tablePart) {

		Node n = (Node) partToNodesMap.get(tablePart);
		TableFigure tableFigure = (TableFigure) tablePart.getFigure();

		Rectangle bounds = new Rectangle(n.x, n.y,
				tableFigure.getPreferredSize().width,
				tableFigure.getPreferredSize().height);

		tableFigure.setBounds(bounds);

		for (int i = 0; i < tablePart.getSourceConnections().size(); i++) {
			RelationshipPart relationship = (RelationshipPart) tablePart
					.getSourceConnections().get(i);
			applyResults(relationship);
		}
	}
	public void applyResults(TablePackagePart tablePart) {
		
		Node n = (Node) partToNodesMap.get(tablePart);
		TablePackageFigure tableFigure = (TablePackageFigure) tablePart.getFigure();
		
		Rectangle bounds = new Rectangle(n.x, n.y,
				tableFigure.getPreferredSize().width,
				tableFigure.getPreferredSize().height);
		
		tableFigure.setBounds(bounds);
		
		for (int i = 0; i < tablePart.getSourceConnections().size(); i++) {
			RelationshipPart relationship = (RelationshipPart) tablePart
			.getSourceConnections().get(i);
			applyResults(relationship);
		}
	}

	// ******************* RelationshipPart apply methods **********/

	protected void applyResults(RelationshipPart relationshipPart) {

		Edge e = (Edge) partToNodesMap.get(relationshipPart);
		NodeList nodes = e.vNodes;

		PolylineConnection conn = (PolylineConnection) relationshipPart
				.getConnectionFigure();
		conn.setTargetDecoration(new PolygonDecoration());
		if (nodes != null) {
			List bends = new ArrayList();
			for (int i = 0; i < nodes.size(); i++) {
				Node vn = nodes.getNode(i);
				int x = vn.x;
				int y = vn.y;
				if (e.isFeedback) {
					bends.add(new AbsoluteBendpoint(x, y + vn.height));
					bends.add(new AbsoluteBendpoint(x, y));

				} else {
					bends.add(new AbsoluteBendpoint(x, y));
					bends.add(new AbsoluteBendpoint(x, y + vn.height));
				}
			}
			conn.setRoutingConstraint(bends);
		} else {
			conn.setRoutingConstraint(Collections.EMPTY_LIST);
		}

	}

}