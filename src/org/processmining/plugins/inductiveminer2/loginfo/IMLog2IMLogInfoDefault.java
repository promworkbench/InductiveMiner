package org.processmining.plugins.inductiveminer2.loginfo;

import org.processmining.plugins.inductiveminer2.helperclasses.MultiIntSet;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormalisedIntDfg;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormalisedIntDfgImpl;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormaliserIntImpl;
import org.processmining.plugins.inductiveminer2.logs.IMEventIterator;
import org.processmining.plugins.inductiveminer2.logs.IMLog;
import org.processmining.plugins.inductiveminer2.logs.IMTrace;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntIntHashMap;

public class IMLog2IMLogInfoDefault implements IMLog2IMLogInfo {

	public IMLogInfo createLogInfo(IMLog log) {
		return log2logInfo(log);
	}

	public static IMLogInfo log2logInfo(IMLog log) {
		//initialise, read the log
		NormaliserIntImpl normaliser = new NormaliserIntImpl();
		NormalisedIntDfg dfg = new NormalisedIntDfgImpl();
		MultiIntSet activities = new MultiIntSet();
		TIntIntHashMap minimumSelfDistances = IMLogInfo.createEmptyMinimumSelfDistancesMap();
		TIntObjectMap<MultiIntSet> minimumSelfDistancesBetween = IMLogInfo.createEmptyMinimumSelfDistancesBetweenMap();
		long numberOfEvents = 0;
		long numberOfActivityInstances = 0;

		//walk trough the log
		for (IMTrace trace : log) {

			int toEventClass = -1;
			int fromEventClass = -1;

			int traceSize = 0;
			TIntIntHashMap eventSeenAt = new TIntIntHashMap();
			TIntList readTrace = new TIntArrayList();

			for (IMEventIterator it = trace.iterator(); it.hasNext();) {
				it.nextFast();
				int ec = normaliser.add(it.getActivityIndex());

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

							minimumSelfDistancesBetween.put(toEventClass, new MultiIntSet());
						}

						//store the minimum self-distance activities
						MultiIntSet mb = minimumSelfDistancesBetween.get(toEventClass);
						mb.addAll(readTrace.subList(eventSeenAt.get(toEventClass) + 1, traceSize));
					}
				}
				eventSeenAt.put(toEventClass, traceSize);
				{
					if (fromEventClass != -1) {
						//add edge to directly follows graph
						dfg.addDirectlyFollowsEdge(fromEventClass, toEventClass, 1);
					} else {
						//add edge to start activities
						dfg.addStartActivity(toEventClass, 1);
					}
				}

				traceSize += 1;
			}

			numberOfEvents += trace.size();
			numberOfActivityInstances += trace.size();

			if (toEventClass != -1) {
				dfg.addEndActivity(toEventClass, 1);
			}

			if (traceSize == 0) {
				dfg.addEmptyTraces(1);
			}
		}

		return new IMLogInfo(normaliser, dfg, activities, minimumSelfDistancesBetween, minimumSelfDistances, numberOfEvents,
				numberOfActivityInstances, log.size());
	}

}
