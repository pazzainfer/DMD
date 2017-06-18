package com.leven.dmd.gef.layout;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;

public class NodeJoiningDirectedGraphLayout extends DirectedGraphLayout {

	public void visit(DirectedGraph graph) {

		// System.out.println("Before Populate: Graph nodes: " + graph.nodes);
		// System.out.println("Before Populate: Graph edges: " + graph.edges);
		new DummyEdgeCreator().visit(graph);

		// create edges to join any isolated clusters
		new ClusterEdgeCreator().visit(graph);

		// System.out.println("After Populate: Graph nodes: " + graph.nodes);
		// System.out.println("After Populate: Graph edges: " + graph.edges);

		super.visit(graph);
	}
}