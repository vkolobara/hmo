package hr.vinko.hmo.v1.algorithm.operators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import hr.vinko.hmo.operators.Crossover;
import hr.vinko.hmo.v1.algorithm.Solution;

public class OrderedCrossover implements Crossover<Solution> {

	@Override
	public Solution mate(Solution parent1, Solution parent2) {
		
		
		int[] child;
		if (rand.nextBoolean()) {
			child = ordcrx(parent1.getSolution(), parent2.getSolution());
		} else {
			child = ordcrx(parent2.getSolution(), parent1.getSolution());
		}
		
		return new Solution(child, parent1.getParser(), parent1.getAssignment());
	}
	
	private int[] ordcrx(int[] p1, int[] p2) {
		int[] child = Arrays.copyOf(p1, p1.length);
		
		int number1, number2;
        do {
            number1 = rand.nextInt(child.length);
            number2 = rand.nextInt(child.length);
        } while (number1 == number2);

        int start = Math.min(number1, number2);
        int end = Math.max(number1, number2);

        Set<Integer> existing = new HashSet<>();
        
        for (int i=start; i<=end; i++) existing.add(child[i]);
        
        for (int i=0, index=0; i<child.length; i++) {
        	if (i >= start && i <= end) continue;
        	
        	if (existing.contains(p2[index])) {
        		i--;
        	} else {
        		child[i] = p2[index];
        	}
        	
    		index++;

        }
        
		return child;
	}

}
