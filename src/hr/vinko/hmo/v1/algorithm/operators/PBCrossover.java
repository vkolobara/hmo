package hr.vinko.hmo.v1.algorithm.operators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import hr.vinko.hmo.operators.Crossover;
import hr.vinko.hmo.v1.algorithm.Solution;

public class PBCrossover implements Crossover<Solution>{

	@Override
	public Solution mate(Solution parent1, Solution parent2) {
		int[] child;
		if (rand.nextBoolean()) {
			child = pbx(parent1.getSolution(), parent2.getSolution());
		} else {
			child = pbx(parent2.getSolution(), parent1.getSolution());
		}
		
		return new Solution(child, parent1.getParser(), parent1.getAssignment());
	}
	
	private int[] pbx(int[] p1, int[] p2) {
		int[] child = Arrays.copyOf(p1, p1.length);
		
		Set<Integer> indice = new HashSet<>();
		Set<Integer> taken = new HashSet<>();
		
		for (int i=0; i<child.length; i++) {
			if (rand.nextBoolean()) {
				indice.add(i);
				taken.add(child[i]);
			}
		}
		
		for (int i=0, index=0; i<child.length; i++) {
			if (indice.contains(i)) continue;
			if (taken.contains(p2[index])) i--;
			else child[i] = p2[index];
			index++;
		}

		
		return child;
	}

}
