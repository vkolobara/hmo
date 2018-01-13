package hr.vinko.hmo.v1.algorithm;

import java.util.ArrayList;
import java.util.stream.Stream;

import hr.vinko.hmo.v1.algorithm.StudentAssignment;
import hr.vinko.hmo.parser.Parser;

public class Population {

	private ArrayList<Solution> population;
	private int capacity;
	
	public Population(int size) {
		this.capacity = size;
		population = new ArrayList<>(size);
	}
	
	public static Population initPopulation(int size, Parser parser, StudentAssignment assignment) {
		Population pop = new Population(size);
		
		while (pop.population.size() < size) {
			pop.population.add(Solution.initGRASP(parser, assignment));
		}
		
		return pop;
		
	}
	
	public void replaceSolution(Solution old, Solution newSol) {
		population.set(population.indexOf(old), newSol);
	}
	
	public Solution getBest() {
		double bestFit = Double.MAX_VALUE;
		Solution best = null;
		
		for (Solution sol : population) {
			double fit = sol.getFitness();
			if (fit <= bestFit) {
				bestFit=fit;
				best = sol;
			}
		}
		
		return best;
	}

	public ArrayList<Solution> getPopulation() {
		return population;
	}
	public boolean isFull () {
		return population.size() == capacity;
	}
	
	public void add(Solution sol) {
		 if (!isFull()) population.add(sol);
	}
	
	public double getAvg() {
		return population.stream().map(x -> x.getFitness()).reduce(Double::sum).get()/population.size();
	}

	public void remove(Solution sol) {
		population.remove(sol);
	}
	
	public int getSize() {
		return population.size();
	}
	
	public int getCapacity() {
		return capacity;
	}

	
}
