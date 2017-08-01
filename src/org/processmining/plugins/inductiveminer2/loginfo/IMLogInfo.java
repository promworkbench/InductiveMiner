package org.processmining.plugins.inductiveminer2.loginfo;

import java.util.concurrent.atomic.AtomicInteger;

import org.processmining.plugins.inductiveminer2.helperclasses.MultiIntSet;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormalisedIntDfg;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormaliserInt;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormaliserIntImpl;
import org.processmining.plugins.inductiveminer2.logs.IMLog;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;

public class IMLogInfo {

	private NormaliserIntImpl normaliser;

	protected final NormalisedIntDfg dfg;

	protected final MultiIntSet activities;

	protected final TIntObjectMap<MultiIntSet> minimumSelfDistancesBetween; //index -> (index^2)
	protected final TIntIntHashMap minimumSelfDistances; //index -> minimum self distance

	protected final long numberOfEvents;
	protected final long numberOfActivityInstances;
	protected final long numberOfTraces;

	public static TIntObjectMap<MultiIntSet> createEmptyMinimumSelfDistancesBetweenMap() {
		return new TIntObjectHashMap<>(10, 0.5f, Integer.MIN_VALUE);
	}

	public static TIntIntHashMap createEmptyMinimumSelfDistancesMap() {
		return new TIntIntHashMap(10, 0.5f, Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	/**
	 * Construct a log-info object. Please use the provided "createEmpty..."
	 * functions to initialise the required hash maps.
	 * 
	 * @param normaliser
	 * 
	 * @param directlyFollowsGraph
	 * @param activities
	 * @param minimumSelfDistancesBetween
	 * @param minimumSelfDistances
	 * @param numberOfEvents
	 * @param numberOfActivityInstances
	 * @param numberOfTraces
	 */
	public IMLogInfo(NormaliserIntImpl normaliser, NormalisedIntDfg directlyFollowsGraph, MultiIntSet activities,
			TIntObjectMap<MultiIntSet> minimumSelfDistancesBetween, TIntIntHashMap minimumSelfDistances,
			long numberOfEvents, long numberOfActivityInstances, long numberOfTraces) {
		this.normaliser = normaliser;
		this.dfg = directlyFollowsGraph;
		this.activities = activities;
		this.minimumSelfDistancesBetween = minimumSelfDistancesBetween;
		this.minimumSelfDistances = minimumSelfDistances;
		this.numberOfEvents = numberOfEvents;
		this.numberOfActivityInstances = numberOfActivityInstances;
		this.numberOfTraces = numberOfTraces;
	}
	
	public NormaliserInt getNormaliser() {
		return normaliser;
	}

	public NormalisedIntDfg getDfg() {
		return dfg;
	}

	public MultiIntSet getNormalisedActivities() {
		return activities;
	}

	public int[] getActivities() {
		int[] result = new int[activities.toSet().size()];
		TIntIterator it = activities.toSet().iterator();
		for (int i = 0; i < activities.toSet().size(); i++) {
			result[i] = normaliser.deNormalise(it.next());
		}
		return result;
	}

	public TIntObjectMap<MultiIntSet> getMinimumSelfDistancesBetween() {
		return minimumSelfDistancesBetween;
	}

	/**
	 * 
	 * @param activityIndex
	 * @return A multset of activity indices that have a minimum self-distance
	 *         relation from the given activityIndex.
	 */
	public MultiIntSet getMinimumSelfDistanceBetween(int activityIndex) {
		if (!minimumSelfDistances.containsKey(activityIndex)) {
			return new MultiIntSet();
		}
		return minimumSelfDistancesBetween.get(activityIndex);
	}

	/**
	 * 
	 * @return A map activity index -> minimum self-distance
	 */
	public TIntIntHashMap getMinimumSelfDistances() {
		return minimumSelfDistances;
	}

	public long getMinimumSelfDistance(int activityIndex) {
		if (minimumSelfDistances.containsKey(activityIndex)) {
			return minimumSelfDistances.get(activityIndex);
		}
		return 0;
	}

	public long getNumberOfEvents() {
		return numberOfEvents;
	}

	public long getNumberOfActivityInstances() {
		return numberOfActivityInstances;
	}

	public long getNumberOfTraces() {
		return numberOfTraces;
	}

	public String[] getActivityNames(final IMLog log) {
		TIntSet set = activities.toSet();
		final String[] result = new String[set.size()];
		final AtomicInteger i = new AtomicInteger(0);
		set.forEach(new TIntProcedure() {
			public boolean execute(int value) {
				result[i.getAndIncrement()] = log.getActivity(value);
				return true;
			}
		});
		return result;
	}

}
