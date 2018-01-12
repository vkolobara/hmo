package hr.vinko.hmo.operators;

import java.util.Random;

import hr.vinko.hmo.v1.algorithm.Solution;

public interface Mutation<T> {
	public static Random rand = new Random();
	public T mutate(T child);

}
