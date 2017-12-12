package org.processmining.plugins.InductiveMiner.reduceacceptingpetrinet;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.framework.packages.PackageManager.Canceller;

public class ReducePetriNetKeepLanguage {

	public static void reduce(AcceptingPetriNet petriNet, Canceller canceller) {

		boolean reduced = false;
		do {
			reduced = false;
			reduced |= MurataFSPkeepLanguage.reduce(petriNet, canceller);
			reduced |= MurataFPPkeepLanguage.reduce(petriNet, canceller);
			reduced |= MurataESTkeepLanguage.reduce(petriNet, canceller);
			reduced |= MurataFSTkeepLanguage.reduce(petriNet, canceller);
			reduced |= MurataFPTkeepLanguage.reduce(petriNet, canceller);

			if (canceller.isCancelled()) {
				return;
			}
		} while (reduced);
	}
}
