package hr.vinko.hmo.algorithm.operators;

import hr.vinko.hmo.algorithm.Population;
import hr.vinko.hmo.algorithm.Solution;

public interface Selection {

	public Solution[] select(Population pop);
}
