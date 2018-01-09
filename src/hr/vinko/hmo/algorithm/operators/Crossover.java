package hr.vinko.hmo.algorithm.operators;

import hr.vinko.hmo.algorithm.Solution;

public interface Crossover {
	public Solution mate(Solution parent1, Solution parent2);

}
