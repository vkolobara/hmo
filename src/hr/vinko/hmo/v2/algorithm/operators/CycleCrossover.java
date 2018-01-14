package hr.vinko.hmo.v2.algorithm.operators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hr.vinko.hmo.operators.Crossover;
import hr.vinko.hmo.v2.algorithm.Solution;

public class CycleCrossover implements Crossover<Solution> {
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

		Set<Integer> unvisited = new HashSet<>();

		Map<Integer, Integer> m2 = new HashMap<>();

		for (int i = 0; i < routes.length; i++) {
			m2.put(routes[i], i);
			unvisited.add(routes[i]);
		}
				
		unvisited.remove(routes[0]);
		Set<Integer> visited = new HashSet<>();
		visited.add(0);
		
		int i = 0;
		while (true) {
			int next = parent2.routes[i];
			if (unvisited.contains(next)) {
				routes[m2.get(next)] = next;
				unvisited.remove(next);
				i = m2.get(next);
				visited.add(i);
			} else {
				for (int j=0; j < routes.length; j++) {
					if (!visited.contains(j)) routes[j] = parent2.routes[j];
				}
				break;
			}
		}

		return new Solution(routes, assignment, busStopCount, parent1.getParser(), parent1.getStudentAssignment());
	}

}
