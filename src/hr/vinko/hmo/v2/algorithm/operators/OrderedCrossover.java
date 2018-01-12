package hr.vinko.hmo.v2.algorithm.operators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import hr.vinko.hmo.operators.Crossover;
import hr.vinko.hmo.v2.algorithm.Solution;

public class OrderedCrossover implements Crossover<Solution>{

	@Override
	public Solution mate(Solution parent1, Solution parent2) {
		Solution child = null;
		if (rand.nextBoolean()) {
			child = crx(parent1, parent2);
		} else {
			child = crx(parent2, parent1);
		}
		
		return child;
	}

	private Solution crx(Solution parent1, Solution parent2) {
		int[] routes = Arrays.copyOf(parent1.routes, parent1.routes.length);
		int[] assignment = Arrays.copyOf(parent1.assignment, parent1.assignment.length);
		int[] busStopCount = Arrays.copyOf(parent1.busStopCount, parent1.busStopCount.length);
		
		int number1, number2;
        do {
            number1 = rand.nextInt(routes.length);
            number2 = rand.nextInt(routes.length);
        } while (number1 == number2);

        int start = Math.min(number1, number2);
        int end = Math.max(number1, number2);

        Set<Integer> existing = new HashSet<>();
        
        for (int i=start; i<=end; i++) existing.add(routes[i]);
        
        for (int i=0, index=0; i<routes.length; i++) {
        	if (i >= start && i <= end) continue;
        	
        	if (existing.contains(parent2.routes[index])) {
        		i--;
        	} else {
        		routes[i] = parent2.routes[index];
        	}
        	
    		index++;

        }
        		
		
		return new Solution(routes, assignment, busStopCount, parent1.getParser(), parent1.getStudentAssignment());
	}

}
