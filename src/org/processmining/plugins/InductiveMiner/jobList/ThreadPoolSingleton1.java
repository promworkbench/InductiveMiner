package org.processmining.plugins.InductiveMiner.jobList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolSingleton1 {

	//singleton
	private static ExecutorService instance = null;

	//constructor
	public static ExecutorService getInstance() {
		if (instance == null) {
			instance = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		}
		return instance;
	}
	
	
}