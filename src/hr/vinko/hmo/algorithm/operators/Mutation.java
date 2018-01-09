package hr.vinko.hmo.algorithm.operators;

import hr.vinko.hmo.algorithm.Solution;

public interface Mutation {
	
	public Solution mutate(Solution child);

}
