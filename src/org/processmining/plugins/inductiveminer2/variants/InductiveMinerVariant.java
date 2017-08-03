package org.processmining.plugins.inductiveminer2.variants;

import org.processmining.plugins.inductiveminer2.mining.MiningParametersAbstract;

public interface InductiveMinerVariant {

	/**
	 * 
	 * @return the name of this variant.
	 */
	public String toString();

	/**
	 * 
	 * @return whether the noise threshold is relevant.
	 */
	public boolean hasNoise();

	/**
	 * 
	 * @return whether if the noise threshold is set to 0, fitness is
	 *         guaranteed.
	 */
	public boolean noNoiseImpliesFitness();

	/**
	 * 
	 * @return A mining parameters object to perform the discovery. Must return
	 *         the same object everytime called.
	 */
	public MiningParametersAbstract getMiningParameters();

	/**
	 * 
	 * @return Give a warning if there more than the returned number of
	 *         activities, or a negative number if such a warning is not
	 *         necessary.
	 */
	public int getWarningThreshold();

	/**
	 * 
	 * @return A doi for more information or null.
	 */
	public String getDoi();
}
