package org.processmining.plugins.InductiveMiner.mining.filteredLog;

import java.util.List;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.MultiSet;

public class Filteredlog extends IMLog {

	public IMLog internalLog;

	public Filteredlog(XLog log, XEventClassifier classifier) {
		internalLog = new IMLog(log, classifier);
	}

	public Filteredlog(MultiSet<List<XEventClass>> log, Set<XEventClass> eventClasses, int eventSize) {
		for (List<XEventClass> trace : log) {
			IMTrace t = new IMTrace();
			t.addAll(trace);
			add(t, log.getCardinalityOf(trace));
		}
	}

	//private void debug(String x) {
	//	System.out.println(x);
	//}
}
