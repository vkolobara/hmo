package hr.vinko.hmo.v1.algorithm.operators;

import java.util.Arrays;

import hr.vinko.hmo.operators.Mutation;
import hr.vinko.hmo.v1.algorithm.Solution;

public class SwapMutation implements Mutation<Solution> {
	private double mutRate;

	public SwapMutation(double mutRate) {
		this.mutRate = mutRate;
	}

	@Override
	public Solution mutate(Solution child) {
		int[] mut = Arrays.copyOf(child.getSolution(), child.getSolution().length);

		if (rand.nextDouble() <= mutRate) {
			int number1, number2;
			do {
				number1 = rand.nextInt(mut.length);
				number2 = rand.nextInt(mut.length);
			} while (number1 == number2);
			int tmp = mut[number1];
			mut[number1] = mut[number2];
			mut[number2] = tmp;
		}

		return new Solution(mut, child.getParser(), child.getAssignment());
	}

}
