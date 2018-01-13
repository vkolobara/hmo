package hr.vinko.hmo.v1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import hr.vinko.hmo.operators.Crossover;
import hr.vinko.hmo.operators.Mutation;
import hr.vinko.hmo.operators.Selection;
import hr.vinko.hmo.parser.Parser;
import hr.vinko.hmo.v1.algorithm.Population;
import hr.vinko.hmo.v1.algorithm.Solution;
import hr.vinko.hmo.v1.algorithm.StudentAssignment;
import hr.vinko.hmo.v1.algorithm.operators.InversionMutation;
import hr.vinko.hmo.v1.algorithm.operators.OrderedCrossover;
import hr.vinko.hmo.v1.algorithm.operators.PBCrossover;
import hr.vinko.hmo.v1.algorithm.operators.ScrambleMutation;
import hr.vinko.hmo.v1.algorithm.operators.SwapMutation;
import hr.vinko.hmo.v1.algorithm.operators.TournamentSelection;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		Random rand = new Random();
		Parser parser = Parser.parse("data/bus_routing/sbr4.txt");
//		Parser parser = Parser.parse("data/instances/inst89-5s40-800-c25-w5.coord");
		System.out.println(parser.getMaxWalk());
		System.out.println(parser.getBusCapacity());
		System.out.println(parser.getStudents().size());
		System.out.println(parser.getBusStops().size());

		StudentAssignment assignment = null;

		do {
			try {
				assignment = StudentAssignment.assign(parser, true);
			} catch (Exception ignorable){};
			
		} while (assignment == null);

		Population pop = Population.initPopulation(50, parser, assignment);

		double mutRate = 0.3;

		Selection<Solution, Population> sel = new TournamentSelection(4);
		Crossover<Solution>[] crossovers = new Crossover[] { new OrderedCrossover(), new PBCrossover() };
		Mutation<Solution>[] mutations = new Mutation[] { 
				new ScrambleMutation(), 
				new InversionMutation(),
				new SwapMutation() };

		long startTime = System.nanoTime();
		long iterTime = startTime;
		for (int i = 0; i < pop.getSize() * 100_000; i++) {
			Solution[] selected = sel.select(pop);
			Crossover<Solution> crx = crossovers[rand.nextInt(crossovers.length)];
			Solution child = crx.mate(selected[0], selected[1]);
			if (rand.nextDouble() <= mutRate) {
				Mutation<Solution> mut = mutations[rand.nextInt(mutations.length)];
				child = mut.mutate(child);
			}
			pop.replaceSolution(selected[2], child);
			double bestFit = pop.getBest().getFitness();
			if (i % pop.getSize() == 0) {
				System.out.println("(" + (System.nanoTime() - iterTime) / 1_000_000.0 + "ms) BEST AT "
						+ (i / pop.getSize() + 1) + ": " + bestFit + " AVG: " + pop.getAvg());
				iterTime = System.nanoTime();
			}

			if (i % 1 == 0) {
				StudentAssignment newAssignment = null;

				do {
					try {
						newAssignment = StudentAssignment.assign(parser, true);
					} catch (Exception ignorable) {};
					
				} while (newAssignment == null);

				for (int j = 0; j < pop.getSize(); j++) {
					pop.getPopulation().get(j).setAssignment(newAssignment);
				}

				if (pop.getBest().getFitness() <= bestFit) {
					assignment = newAssignment;
				} else {
					for (int j = 0; j < pop.getSize(); j++) {
						pop.getPopulation().get(j).setAssignment(assignment);
					}
				}
			}
		}
		System.out.println("TOOK: " + ((System.nanoTime() - startTime) / 1_000_000_000.0) + "s");

		try (

				PrintWriter out = new PrintWriter("test1.txt")) {
			out.print(pop.getBest());
		}

	}

}
