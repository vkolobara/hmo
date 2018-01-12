package hr.vinko.hmo.v2.algorithm.operators;

import java.util.Arrays;

import hr.vinko.hmo.operators.Mutation;
import hr.vinko.hmo.v2.algorithm.Solution;

public class ScrambleRoutesMutation implements Mutation<Solution>{
	
	private double mutRate;
	
	public ScrambleRoutesMutation(double mutRate) {
		super();
		this.mutRate = mutRate;
	}

	@Override
	public Solution mutate(Solution child) {
		int[] routes = Arrays.copyOf(child.routes, child.routes.length);
		int[] assignment = Arrays.copyOf(child.assignment, child.assignment.length);
		int[] busStopCount = Arrays.copyOf(child.busStopCount, child.busStopCount.length);
				
		// SCRAMBLE ROUTES
		if (rand.nextDouble() <= mutRate) {
			int number1, number2;
	        do {
	            number1 = rand.nextInt(routes.length);
	            number2 = rand.nextInt(routes.length);
	        } while (number1 == number2);

	        int start = Math.min(number1, number2);
	        int end = Math.max(number1, number2);

			for (int i = start; i <= end; i++) {
				int randomPosition = rand.nextInt(end-start) + start;
				int temp = routes[i];
				routes[i] = routes[randomPosition];
				routes[randomPosition] = temp;
			}
		}
		
		return new Solution(routes, assignment, busStopCount, child.getParser(), child.getStudentAssignment());
	}

}
