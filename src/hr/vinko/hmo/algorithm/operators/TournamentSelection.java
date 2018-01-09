package hr.vinko.hmo.algorithm.operators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import hr.vinko.hmo.algorithm.Population;
import hr.vinko.hmo.algorithm.Solution;

public class TournamentSelection implements Selection {
	
	private int size;
	private static Random rand = new Random();
	
	public TournamentSelection(int size) {
		this.size = size;
	}

	@Override
	public Solution[] select(Population pop) {
		ArrayList<Solution> lst = pop.getPopulation();
		Set<Integer> visited = new HashSet<>();
		
		Population tournament = new Population(size);
		while (!tournament.isFull()) {
			int index = rand.nextInt(lst.size());
			if (!visited.contains(index)) {
				tournament.add(lst.get(index));
				visited.add(index);
			}
		}
		
		Solution[] ret = new Solution[3];
		
		for (int i=0; i<2; i++) {
			Solution best = tournament.getBest();
			ret[i] = best;
			tournament.remove(best);
		}
		
		while (tournament.getSize() > 1) tournament.remove(tournament.getBest());
		
		ret[2] = tournament.getBest();
		
		return ret;
	}

	
	
}
