package org.processmining.plugins.InductiveMiner.graphs;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.procedure.TObjectIntProcedure;
import gnu.trove.set.hash.THashSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Components<V> {

	private final int[] components;
	private int numberOfComponents;
	private final TObjectIntHashMap<V> node2index;

	public Components(V[] nodes) {
		components = new int[nodes.length];
		numberOfComponents = nodes.length;
		for (int i = 0; i < components.length; i++) {
			components[i] = i;
		}

		node2index = new TObjectIntHashMap<>();
		{
			int i = 0;
			for (V node : nodes) {
				node2index.put(node, i);
				i++;
			}
		}
	}

	public Components(Collection<? extends Collection<V>> partition) {
		numberOfComponents = partition.size();
		node2index = new TObjectIntHashMap<>();
		int nodeNumber = 0;
		for (Collection<V> component : partition) {
			for (V node : component) {
				node2index.put(node, nodeNumber);
				nodeNumber++;
			}
		}
		components = new int[nodeNumber];

		int componentNumber = 0;
		nodeNumber = 0;
		for (Collection<V> component : partition) {
			for (V node : component) {
				components[nodeNumber] = componentNumber;
				nodeNumber++;
			}
			componentNumber++;
		}
	}

	/**
	 * Merge the components of the two nodes. If they are in the same component,
	 * runs in O(1). If they are not, runs in O(n) (n = number of nodes).
	 * 
	 * @param indexA
	 * @param indexB
	 */
	public void mergeComponentsOf(int indexA, int indexB) {
		int source = components[indexA];
		int target = components[indexB];

		mergeComponents(source, target);
	}

	/**
	 * Merge the components of the two nodes. If they are in the same component,
	 * runs in O(1). If they are not, runs in O(n) (n = number of nodes). Use
	 * the integer variant if possible.
	 * 
	 * @param indexA
	 * @param indexB
	 */
	public void mergeComponentsOf(V nodeA, V nodeB) {
		mergeComponentsOf(node2index.get(nodeA), node2index.get(nodeB));
	}

	public void mergeComponents(int componentA, int componentB) {
		if (componentA != componentB) {
			numberOfComponents--;
			for (int i = 0; i < components.length; i++) {
				if (components[i] == componentA) {
					components[i] = componentB;
				}
			}
		}
	}

	public boolean areInSameComponent(int nodeIndexA, int nodeIndexB) {
		return components[nodeIndexA] == components[nodeIndexB];
	}

	/**
	 * Preferably use the integer variant.
	 * 
	 * @param nodeA
	 * @param nodeB
	 * @return
	 */
	public boolean areInSameComponent(V nodeA, V nodeB) {
		return areInSameComponent(node2index.get(nodeA), node2index.get(nodeB));
	}

	public int getComponentOf(int node) {
		return components[node];
	}

	public int getComponentOf(V node) {
		return getComponentOf(node2index.get(node));
	}

	public int getNumberOfComponents() {
		return numberOfComponents;
	}

	public List<Set<V>> getComponents() {
		final List<Set<V>> result = new ArrayList<Set<V>>(numberOfComponents);

		//prepare a hashmap of components
		final TIntIntHashMap component2componentIndex = new TIntIntHashMap();
		int highestComponentIndex = 0;
		for (int node = 0; node < components.length; node++) {
			int component = components[node];
			if (!component2componentIndex.contains(component)) {
				component2componentIndex.put(component, highestComponentIndex);
				highestComponentIndex++;
				result.add(new THashSet<V>());
			}
		}

		//put each node in its component
		node2index.forEachEntry(new TObjectIntProcedure<V>() {
			public boolean execute(V node, int nodeIndex) {
				int component = components[nodeIndex];
				int componentIndex = component2componentIndex.get(component);
				result.get(componentIndex).add(node);
				return true;
			}
		});

		return result;
	}

	public Iterable<Integer> getNodeIndicesOfComponent(final int componentIndex) {
		return new Iterable<Integer>() {
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {
					int now = -1;

					public Integer next() {
						for (int i = now + 1; i < components.length; i++) {
							if (components[i] == componentIndex) {
								now = i;
								return now;
							}
						}
						return null;
					}

					public boolean hasNext() {
						for (int i = now + 1; i < components.length; i++) {
							if (components[i] == componentIndex) {
								return true;
							}
						}
						return false;
					}
					
					public void remove() {
						
					}
				};
			}
		};

	}
}