package org.processmining.plugins.InductiveMiner.model.conversion;

import java.util.Iterator;
import java.util.UUID;

import org.processmining.plugins.InductiveMiner.model.EventClass;
import org.processmining.plugins.InductiveMiner.model.ExclusiveChoice;
import org.processmining.plugins.InductiveMiner.model.Loop;
import org.processmining.plugins.InductiveMiner.model.Node;
import org.processmining.plugins.InductiveMiner.model.Parallel;
import org.processmining.plugins.InductiveMiner.model.Sequence;
import org.processmining.plugins.InductiveMiner.model.Tau;

@Deprecated
public class ProcessTreeModel2Dot {
	
	public abstract class dotNode {
		public String id;
		public String style;
		
		public dotNode(String style) {
			this.id = UUID.randomUUID().toString();
			this.style = style;
			dot += "\"" + id + "\" [ "+style+" ];\n";
		}
	}
	
	public class dotPlace extends dotNode {
		public dotPlace(String label) {
			super("label=\"\", shape=\"circle\"");
		}
		public dotPlace(String label, String style) {
			super("label=\"\", shape=\"circle\", "+ style);
		}
	}
	
	public class dotTransition extends dotNode {
		//transition
		public dotTransition(String label) {
			super("label=\""+label+"\", shape=\"box\"");
		}
		//tau transition
		public dotTransition() {
			super("label=\"\", style=filled, fillcolor=\"#EEEEEE\", width=\"0.15\", shape=\"box\"");
		}
	}
	
	private String dot;
	
	public String convert2PetriNet(Node root) {
		dot = "digraph G {\n";
		dot += "rankdir=LR;\n";
		dotPlace Source = new dotPlace("source", "style=filled, fillcolor=\"green\"");
		dotPlace Sink = new dotPlace("sink", "style=filled, fillcolor=\"red\"");
		convertNode(Source, Sink, root, false, true);
		dot += "}\n";
		
		return dot;
	}
	
	private void convertNode(dotPlace source, 
			dotPlace sink, 
			Node node,
			boolean assureNoInputPlacementment,
			boolean assureNoOutputRemoval) {
		if (node instanceof Tau) {
			convertTau(source, sink, (Tau) node, assureNoInputPlacementment, assureNoOutputRemoval);
		} else if (node instanceof EventClass) {
			convertActivity(source, sink, (EventClass) node, assureNoInputPlacementment, assureNoOutputRemoval);
		} else if (node instanceof ExclusiveChoice) {
			beginCluster(true);
			convertExclusiveChoice(source, sink, (ExclusiveChoice) node, assureNoInputPlacementment, assureNoOutputRemoval);
			endCluster(true);
		} else if (node instanceof Sequence) {
			beginCluster(true);
			convertSequence(source, sink, (Sequence) node, assureNoInputPlacementment, assureNoOutputRemoval);
			endCluster(true);
		} else if (node instanceof Parallel) {
			beginCluster(true);
			convertParallel(source, sink, (Parallel) node, assureNoInputPlacementment, assureNoOutputRemoval);
			endCluster(true);
		} else if (node instanceof Loop) {
			beginCluster(true);
			convertLoop(source, sink, (Loop) node, assureNoInputPlacementment, assureNoOutputRemoval);
			endCluster(true);
		}
	}
	
	private void convertTau(dotPlace source, 
			dotPlace sink, 
			Tau node,
			boolean assureNoInputPlacementment,
			boolean assureNoOutputRemoval) {
		dotTransition t = new dotTransition();
		addArc(source, t);
		addArc(t, sink);
	}
	
	private void convertActivity(dotPlace source, 
			dotPlace sink, 
			EventClass node,
			boolean assureNoInputPlacementment,
			boolean assureNoOutputRemoval) {
		dotTransition t = new dotTransition(node.toString());
		addArc(source, t);
		addArc(t, sink);
	}
	
	private void convertExclusiveChoice(dotPlace source, 
			dotPlace sink, 
			ExclusiveChoice node,
			boolean assureNoInputPlacementment,
			boolean assureNoOutputRemoval) {
		for (Node child : node.getChildren()) {
			beginCluster(false);
			convertNode(source, sink, child, true, true);
			endCluster(false);
		}
	}
	
	private void convertSequence(dotPlace source, 
			dotPlace sink, 
			Sequence node,
			boolean assureNoInputPlacementment,
			boolean assureNoOutputRemoval) {
		int last = node.getChildren().size();
		int i = 0;
		dotPlace lastSink = source;
		for (Node child : node.getChildren()) {
			beginCluster(false);
			dotPlace childSink;
			if (i == last - 1) {
				childSink = sink;
			} else {
				childSink = new dotPlace(i + " sink " + node.toString());
			}
			
			boolean assureBefore = (i == 0 && assureNoInputPlacementment);
			boolean assureAfter = (i == last - 1 && assureNoOutputRemoval) || (i < last - 1);
			
			convertNode(lastSink, childSink, child, assureBefore, assureAfter);
			endCluster(false);
			lastSink = childSink;
			i++;
		}
	}
	
	private void convertParallel(dotPlace source, 
			dotPlace sink, 
			Parallel node,
			boolean assureNoInputPlacementment,
			boolean assureNoOutputRemoval) {
		beginCluster(false);
		//add split tau
		dotTransition t1 = new dotTransition();
		addArc(source, t1);		
		
		//add join tau
		dotTransition t2 = new dotTransition();
		addArc(t2, sink);
		
		//add for each child a source and sink place
		int i = 0;
		for (Node child : node.getChildren()) {
			dotPlace childSource = new dotPlace("source " + i + " " + node.toString());
			addArc(t1, childSource);
			
			dotPlace childSink = new dotPlace("sink " + i + " " + node.toString());
			addArc(childSink, t2);
			
			convertNode(childSource, childSink, child, false, false);
			
			i++;
		}
		endCluster(false);
	}
	
	private void convertLoop(dotPlace source, 
			dotPlace sink, 
			Loop node,
			boolean assureNoInputPlacementment,
			boolean assureNoOutputRemoval) {
		
		dotPlace child1Source;
		if (assureNoInputPlacementment) {
			//create child 1 source place
			child1Source = new dotPlace("source 1 " + node.toString());
			
			//create transition from child 1 sink place to sink
			dotTransition t1 = new dotTransition();
			addArc(source, t1);
			addArc(t1, child1Source);
		} else {
			child1Source = source;
		}
		
		dotPlace child1Sink;
		if (assureNoOutputRemoval) {
			//create child 1 sink place
			child1Sink = new dotPlace("sink 1 " + node.toString());
			
			//create transition from child 1 sink place to sink
			dotTransition t2 = new dotTransition();
			addArc(child1Sink, t2);
			addArc(t2, sink);
		} else {
			child1Sink = sink;
		}
		
		//convert the first child
		Iterator<Node> i = node.getChildren().iterator();
		beginCluster(false);
		convertNode(child1Source, child1Sink, i.next(), assureNoInputPlacementment, true);
		endCluster(false);
		
		//convert the other children
		while (i.hasNext()) {
			beginCluster(false);
			convertNode(child1Sink, child1Source, i.next(), true, true);
			endCluster(false);
		}
	}	
	
	private void addArc(dotNode from, dotNode to) {
		 dot += "\"" + from.id + "\" -> \"" + to.id + "\";\n";
	}
	
	private void beginCluster(boolean border) {
		if (!border) {
			dot += "subgraph \"cluster_"+ UUID.randomUUID().toString() + "\" {\n";
		}
	}
	
	private void endCluster(boolean border) {
		//dot += "}\n";
		if (!border) {
			dot += "color=\"#E0E0FF\";}\n";
		}
	}
}
