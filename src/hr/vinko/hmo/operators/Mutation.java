package hr.vinko.hmo.operators;

import java.util.Random;

public interface Mutation<T> {
	public static Random rand = new Random();
	public T mutate(T child);

}
