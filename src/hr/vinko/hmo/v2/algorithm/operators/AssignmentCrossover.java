package hr.vinko.hmo.v2.algorithm.operators;

import java.util.Arrays;

import hr.vinko.hmo.operators.Crossover;
import hr.vinko.hmo.v2.algorithm.Solution;

public class AssignmentCrossover implements Crossover<Solution> {
	
	@Override
	public Solution mate(Solution parent1, Solution parent2) {
		Solution child = null;
		if (parent1.getFitness() > parent2.getFitness()) {
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
		int busCapacity = parent1.getParser().getBusCapacity();

//		Map<Integer, List<Integer>> available = parent1.getStudentAssignment().possibilities;

		for (int i = 0; i < assignment.length; i++) {
			if (rand.nextDouble()<=0.1) {
				if (busStopCount[parent2.assignment[i]] < busCapacity) {
					busStopCount[assignment[i]]--;
					assignment[i] = parent2.assignment[i];
					busStopCount[assignment[i]]++;
				}
			}
		}

		return new Solution(routes, assignment, busStopCount, parent1.getParser(), parent1.getStudentAssignment());
	}

}
