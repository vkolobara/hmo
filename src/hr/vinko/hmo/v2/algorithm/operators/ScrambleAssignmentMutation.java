package hr.vinko.hmo.v2.algorithm.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import hr.vinko.hmo.operators.Mutation;
import hr.vinko.hmo.v2.algorithm.Solution;

public class ScrambleAssignmentMutation implements Mutation<Solution> {

	private double mutRate;
	
	public ScrambleAssignmentMutation(double mutRate) {
		this.mutRate = mutRate;
	}
	
	@Override
	public Solution mutate(Solution child) {
		int busCapacity = child.getParser().getBusCapacity();
		int[] routes = Arrays.copyOf(child.routes, child.routes.length);
		int[] assignment = Arrays.copyOf(child.assignment, child.assignment.length);
		int[] busStopCount = Arrays.copyOf(child.busStopCount, child.busStopCount.length);
		
		Map<Integer, List<Integer>> availableMap = child.getStudentAssignment().possibilities;
		
		for (int i = 0; i < assignment.length; i++) {
			if (rand.nextDouble() <= mutRate) {
				List<Integer> possibilites = new ArrayList<>(availableMap.get(i));
				int alpha = Math.max(1, possibilites.size()/5);

				int ind = rand.nextInt(alpha);
				busStopCount[assignment[i]]--;

				while (true) {
					int sel = possibilites.get(ind);

					if (busStopCount[sel] + 1 < busCapacity) {
						assignment[i] = sel;
						break;
					} else {
						possibilites.remove(ind);
						ind = ind - 1 > 0 ? ind - 1 : 0;
					}
				}

				busStopCount[assignment[i]]++;

			}
		}
		
		return new Solution(routes, assignment, busStopCount, child.getParser(), child.getStudentAssignment());

	}

}
