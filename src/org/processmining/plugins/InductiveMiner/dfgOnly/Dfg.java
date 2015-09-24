package org.processmining.plugins.InductiveMiner.dfgOnly;

import java.util.Iterator;

import org.apache.commons.collections15.iterators.ArrayIterator;
import org.deckfour.xes.classification.XEventClass;
import org.processmining.plugins.InductiveMiner.MultiSet;
import org.processmining.plugins.InductiveMiner.graphs.Graph;
import org.processmining.plugins.InductiveMiner.graphs.GraphFactory;

public class Dfg {
	private Graph<XEventClass> directlyFollowsGraph;
	private Graph<XEventClass> concurrencyGraph;

	private final MultiSet<XEventClass> startActivities;
	private final MultiSet<XEventClass> endActivities;

	public Dfg() {
		this(1);
	}

	public Dfg(int initialSize) {
		directlyFollowsGraph = GraphFactory.create(XEventClass.class, initialSize);
		concurrencyGraph = GraphFactory.create(XEventClass.class, initialSize);

		startActivities = new MultiSet<>();
		endActivities = new MultiSet<>();
	}

	private Dfg(Graph<XEventClass> directlyFollowsGraph, Graph<XEventClass> concurrencyGraph) {
		this.directlyFollowsGraph = directlyFollowsGraph;
		this.concurrencyGraph = concurrencyGraph;
		startActivities = new MultiSet<>();
		endActivities = new MultiSet<>();
	}

	public static Dfg createTimeOptimised(int initialSize) {
		return new Dfg(GraphFactory.createTimeOptimised(XEventClass.class, initialSize),
				GraphFactory.createTimeOptimised(XEventClass.class, initialSize));
	}

	public Dfg(final Graph<XEventClass> directlyFollowsGraph, final Graph<XEventClass> concurrencyGraph,
			final MultiSet<XEventClass> startActivities, final MultiSet<XEventClass> endActivities) {
		this.directlyFollowsGraph = directlyFollowsGraph;
		this.concurrencyGraph = concurrencyGraph;

		this.startActivities = startActivities;
		this.endActivities = endActivities;
	}

	public void addActivity(XEventClass activity) {
		directlyFollowsGraph.addVertex(activity);
		concurrencyGraph.addVertex(activity);
	}

	public Graph<XEventClass> getDirectlyFollowsGraph() {
		return directlyFollowsGraph;
	}

	public void setDirectlyFollowsGraph(Graph<XEventClass> directlyFollowsGraph) {
		this.directlyFollowsGraph = directlyFollowsGraph;
	}

	public Iterable<XEventClass> getActivities() {
		return new Iterable<XEventClass>() {
			public Iterator<XEventClass> iterator() {
				return new ArrayIterator<XEventClass>(directlyFollowsGraph.getVertices());
			}
		};

	}

	public Graph<XEventClass> getConcurrencyGraph() {
		return concurrencyGraph;
	}

	public MultiSet<XEventClass> getStartActivities() {
		return startActivities;
	}

	public MultiSet<XEventClass> getEndActivities() {
		return endActivities;
	}

	public void addDirectlyFollowsEdge(final XEventClass source, final XEventClass target, final long cardinality) {
		addActivity(source);
		addActivity(target);
		directlyFollowsGraph.addEdge(source, target, cardinality);
	}

	public void addParallelEdge(final XEventClass a, final XEventClass b, final long cardinality) {
		addActivity(a);
		addActivity(b);
		concurrencyGraph.addEdge(a, b, cardinality);
	}

	public void addStartActivity(XEventClass activity, long cardinality) {
		addActivity(activity);
		startActivities.add(activity, cardinality);
	}

	public void addEndActivity(XEventClass activity, long cardinality) {
		addActivity(activity);
		endActivities.add(activity, cardinality);
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		for (long edgeIndex : directlyFollowsGraph.getEdges()) {
			result.append(directlyFollowsGraph.getEdgeSource(edgeIndex));
			result.append("->");
			result.append(directlyFollowsGraph.getEdgeTargetIndex(edgeIndex));
			result.append(", ");
		}
		return result.toString();
	}

	/**
	 * Adds a directly-follows graph edge (in each direction) for each parallel
	 * edge.
	 */
	public void collapseParallelIntoDirectly() {
		for (long edgeIndex : concurrencyGraph.getEdges()) {
			directlyFollowsGraph.addEdge(concurrencyGraph.getEdgeSource(edgeIndex),
					concurrencyGraph.getEdgeTarget(edgeIndex), concurrencyGraph.getEdgeWeight(edgeIndex));
			directlyFollowsGraph.addEdge(concurrencyGraph.getEdgeTarget(edgeIndex),
					concurrencyGraph.getEdgeSource(edgeIndex), concurrencyGraph.getEdgeWeight(edgeIndex));
		}
	}
}
