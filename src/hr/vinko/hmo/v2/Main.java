package hr.vinko.hmo.v2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import hr.vinko.hmo.operators.Crossover;
import hr.vinko.hmo.operators.Mutation;
import hr.vinko.hmo.operators.Selection;
import hr.vinko.hmo.parser.Parser;
import hr.vinko.hmo.v2.algorithm.Population;
import hr.vinko.hmo.v2.algorithm.Solution;
import hr.vinko.hmo.v2.algorithm.StudentAssignment;
import hr.vinko.hmo.v2.algorithm.operators.AssignmentCrossover;
import hr.vinko.hmo.v2.algorithm.operators.InversionRoutesMutation;
import hr.vinko.hmo.v2.algorithm.operators.ScrambleAssignmentMutation;
import hr.vinko.hmo.v2.algorithm.operators.ScrambleRoutesMutation;
import hr.vinko.hmo.v2.algorithm.operators.StopsAssignmentMutation;
import hr.vinko.hmo.v2.algorithm.operators.TournamentSelection;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		Random rand = new Random();
		Parser parser = Parser.parse("data/bus_routing/sbr4.txt");
		// Parser parser =
		// Parser.parse("data/instances/inst89-5s40-800-c25-w5.coord");
		System.out.println(parser.getMaxWalk());
		System.out.println(parser.getBusCapacity());
		System.out.println(parser.getStudents().size());
		System.out.println(parser.getBusStops().size());
		StudentAssignment assignment = StudentAssignment.assign(parser);
		long startTime = System.nanoTime();
		Population pop = Population.initPopulation(50, parser, assignment);
		System.out.println("INITIAL POPULATION TIME: (" + (System.nanoTime() - startTime) / 1_000_000_000.0 + "s)");

		Selection<Solution, Population> sel = new TournamentSelection(4);

		double crxRate = 0.9;
		double mutRate = 1;

		Crossover<Solution>[] crossovers = new Crossover[] {
				// new OrderedCrossover(),
				new AssignmentCrossover() };

		Mutation<Solution>[] mutations = new Mutation[] { new ScrambleRoutesMutation(),
				new ScrambleAssignmentMutation(0.05), new InversionRoutesMutation(), new StopsAssignmentMutation() };

		startTime = System.nanoTime();
		for (int i = 0; i < pop.getSize() * 100_000; i++) {
			Solution[] selected = sel.select(pop);
			if (i % 10_000 == 0) {
				if (rand.nextBoolean()) {
					Solution child = selected[0];
					Mutation<Solution> mut = mutations[rand.nextInt(mutations.length)];
					child = mut.mutate(child);

				} else {
					pop.replaceSolution(selected[2], Solution.initGRASP(parser, assignment, true));
				}
			} else {
				Solution child = selected[2];
				if (rand.nextDouble() <= crxRate) {
					Crossover<Solution> crx = crossovers[rand.nextInt(crossovers.length)];
					child = crx.mate(selected[0], selected[1]);
				}
				if (rand.nextDouble() <= mutRate) {
					for (int j = 0; j < mutations.length; j++) {
						Mutation<Solution> mut = mutations[rand.nextInt(mutations.length)];
						child = mut.mutate(child);
					}
				}
				pop.replaceSolution(selected[2], child);
			}
			if (i % pop.getSize() == 0) {
				System.out.println("(" + (System.nanoTime() - startTime) / 1_000_000.0 + "ms) BEST AT "
						+ (i / pop.getSize() + 1) + ": " + pop.getBest().getFitness() + " AVG: " + pop.getAvg());
				startTime = System.nanoTime();
			}
		}

		try (

				PrintWriter out = new PrintWriter("test.txt")) {
			out.write(pop.getBest().toString());
		}
	}

}
