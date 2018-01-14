package hr.vinko.hmo.v2.algorithm.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import hr.vinko.hmo.operators.Mutation;
import hr.vinko.hmo.v2.algorithm.Solution;

public class StopsAssignmentMutation implements Mutation<Solution> {

	@Override
	public Solution mutate(Solution child) {
		int busCapacity = child.getParser().getBusCapacity();
		int[] routes = Arrays.copyOf(child.routes, child.routes.length);
		int[] assignment = Arrays.copyOf(child.assignment, child.assignment.length);
		int[] busStopCount = Arrays.copyOf(child.busStopCount, child.busStopCount.length);

		Map<Integer, List<Integer>> availableMap = child.getStudentAssignment().possibilities;
		
		int capacity = 0;
		int numTries = 0;
		int index = 0;
		do {
			index = rand.nextInt(busStopCount.length);
			capacity = busStopCount[index];
		} while (capacity < busCapacity / 2 && numTries++ < 10);

		while (capacity == 0) {
			index = rand.nextInt(busStopCount.length);
			capacity = busStopCount[index];
		}

		int nRemove = rand.nextInt(capacity);
		
		for (int i = 0; i < assignment.length && nRemove > 0; i++) {
			if (assignment[i] == index && rand.nextBoolean()) {
				List<Integer> possibilites = new ArrayList<>(availableMap.get(i));

				int ind = rand.nextInt(possibilites.size());

				while (true) {
					if (possibilites.size()==0) {
						break;
					}
					int sel = possibilites.get(ind);

					if (busStopCount[sel] + 1 < busCapacity) {
						assignment[i] = sel;
						break;
					} else {
						possibilites.remove(ind);
						ind = ind - 1 > 0 ? ind - 1 : 0;
					}
				}
				
				busStopCount[index]--;
				busStopCount[assignment[i]]++;
				nRemove--;

			} 

			if (i==assignment.length-1) i = 0;

		}

		return new Solution(routes, assignment, busStopCount, child.getParser(), child.getStudentAssignment());

	}

}
