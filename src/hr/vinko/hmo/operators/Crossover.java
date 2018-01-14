package hr.vinko.hmo.operators;

import java.util.Random;

public interface Crossover<T> {
	public static Random rand = new Random();
	public T mate(T parent1, T parent2);

}
