package org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.MultiSet;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLogImpl;
import org.processmining.plugins.InductiveMiner.mining.logs.IMTrace;
import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier;

import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public class Log2DfgMsd {
	public static DfgMsd convert(XLog xLog, XEventClassifier activityClassifier,
			XLifeCycleClassifier lifeCycleClassifier) {

		IMLog log = new IMLogImpl(xLog, activityClassifier, lifeCycleClassifier);

		//initialise, read the log
		DfgMsdImpl dfg = new DfgMsdImpl();
		MultiSet<XEventClass> activities = new MultiSet<XEventClass>();
		TObjectIntHashMap<XEventClass> minimumSelfDistances = new TObjectIntHashMap<>();
		THashMap<XEventClass, MultiSet<XEventClass>> minimumSelfDistancesBetween = new THashMap<XEventClass, MultiSet<XEventClass>>();

		XEventClass fromEventClass;
		XEventClass toEventClass;

		//walk trough the log
		Map<XEventClass, Integer> eventSeenAt;
		List<XEventClass> readTrace;

		for (IMTrace trace : log) {

			toEventClass = null;
			fromEventClass = null;

			int traceSize = 0;
			eventSeenAt = new THashMap<XEventClass, Integer>();
			readTrace = new ArrayList<XEventClass>();

			for (XEvent e : trace) {
				XEventClass ec = log.classify(trace, e);
				activities.add(ec);
				dfg.addActivity(ec);

				fromEventClass = toEventClass;
				toEventClass = ec;

				readTrace.add(toEventClass);

				if (eventSeenAt.containsKey(toEventClass)) {
					//we have detected an activity for the second time
					//check whether this is shorter than what we had already seen
					int oldDistance = Integer.MAX_VALUE;
					if (minimumSelfDistances.containsKey(toEventClass)) {
						oldDistance = minimumSelfDistances.get(toEventClass);
					}

					if (!minimumSelfDistances.containsKey(toEventClass)
							|| traceSize - eventSeenAt.get(toEventClass) <= oldDistance) {
						//keep the new minimum self distance
						int newDistance = traceSize - eventSeenAt.get(toEventClass);
						if (oldDistance > newDistance) {
							//we found a shorter minimum self distance, record and restart with a new multiset
							minimumSelfDistances.put(toEventClass, newDistance);

							minimumSelfDistancesBetween.put(toEventClass, new MultiSet<XEventClass>());
						}

						//store the minimum self-distance activities
						MultiSet<XEventClass> mb = minimumSelfDistancesBetween.get(toEventClass);
						mb.addAll(readTrace.subList(eventSeenAt.get(toEventClass) + 1, traceSize));
					}
				}
				eventSeenAt.put(toEventClass, traceSize);
				{
					if (fromEventClass != null) {
						//add edge to directly follows graph
						dfg.addDirectlyFollowsEdge(fromEventClass, toEventClass, 1);
					} else {
						//add edge to start activities
						dfg.addStartActivity(toEventClass, 1);
					}
				}

				traceSize += 1;
			}

			if (toEventClass != null) {
				dfg.addEndActivity(toEventClass, 1);
			}

			if (traceSize == 0) {
				dfg.addEmptyTraces(1);
			}
		}

		//transfer the minimum self-distances
		for (Entry<XEventClass, MultiSet<XEventClass>> entry : minimumSelfDistancesBetween.entrySet()) {
			MultiSet<XEventClass> t = entry.getValue();
			for (Iterator<XEventClass> it = t.iterator(); it.hasNext();) {
				XEventClass target = it.next();
				dfg.addMinimumSelfDistanceEdge(entry.getKey(), target, t.getCardinalityOf(target));
			}
		}

		return dfg;
	}
}
