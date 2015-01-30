package org.processmining.plugins.InductiveMiner.mining.logSplitter;

import gnu.trove.map.hash.THashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.processmining.plugins.InductiveMiner.MultiSet;
import org.processmining.plugins.InductiveMiner.mining.IMLog;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.IMTrace;
import org.processmining.plugins.InductiveMiner.mining.MinerState;
import org.processmining.plugins.InductiveMiner.mining.cuts.Cut;

public class LogSplitterLoop implements LogSplitter {

	public LogSplitResult split(IMLog log, IMLogInfo logInfo, Cut cut, MinerState minerState) {
		return new LogSplitResult(split(log, cut.getPartition(), minerState), new MultiSet<XEventClass>());
	}

	public static List<IMLog> split(IMLog log, Collection<Set<XEventClass>> partition, MinerState minerState) {

		List<XLog> result = new ArrayList<>();
		Map<Set<XEventClass>, XLog> mapSigma2Sublog = new THashMap<>();
		Map<XEventClass, Set<XEventClass>> mapActivity2sigma = new THashMap<>();
		for (Set<XEventClass> sigma : partition) {
			XLog sublog = new XLogImpl(new XAttributeMapImpl());
			result.add(sublog);
			mapSigma2Sublog.put(sigma, sublog);
			for (XEventClass activity : sigma) {
				mapActivity2sigma.put(activity, sigma);
			}
		}
		
		//loop through the traces
		for (IMTrace trace : log) {
			XTrace partialTrace = new XTraceImpl(new XAttributeMapImpl());
			
			//keep track of the last sigma we were in
			Set<XEventClass> lastSigma = partition.iterator().next();
			
			for (XEvent event : trace) {
				XEventClass c = log.classify(event);
				if (!lastSigma.contains(c)) {
					mapSigma2Sublog.get(lastSigma).add(partialTrace);
					partialTrace = new XTraceImpl(new XAttributeMapImpl());
					lastSigma = mapActivity2sigma.get(c);
				}
				partialTrace.add(event);
			}
			mapSigma2Sublog.get(lastSigma).add(partialTrace);
			
			//add an empty trace if the last event was not of sigma_1
			if (lastSigma != partition.iterator().next()) {
				mapSigma2Sublog.get(lastSigma).add(new XTraceImpl(new XAttributeMapImpl()));
			}
		}

		//wrap in IMLog objects
		List<IMLog> result2 = new ArrayList<>();
		for (XLog xLog : result) {
			result2.add(new IMLog(xLog, minerState.parameters.getClassifier()));
		}
		return result2;
	}

}