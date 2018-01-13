package hr.vinko.hmo.v1.algorithm.operators;

import java.util.Arrays;

import hr.vinko.hmo.operators.Mutation;
import hr.vinko.hmo.v1.algorithm.Solution;

public class ScrambleMutation implements Mutation<Solution> {
	@Override
	public Solution mutate(Solution child) {
		int[] mut = Arrays.copyOf(child.getSolution(), child.getSolution().length);

		int number1, number2;
		do {
			number1 = rand.nextInt(mut.length);
			number2 = rand.nextInt(mut.length);
		} while (number1 == number2);

		int start = Math.min(number1, number2);
		int end = Math.max(number1, number2);

		for (int i = start; i <= end; i++) {
			int randomPosition = rand.nextInt(end - start) + start;
			int temp = mut[i];
			mut[i] = mut[randomPosition];
			mut[randomPosition] = temp;
		}

		return new Solution(mut, child.getParser(), child.getAssignment());
	}

}
