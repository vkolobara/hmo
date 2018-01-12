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
import hr.vinko.hmo.v2.algorithm.operators.AssignmentCrossover;
import hr.vinko.hmo.v2.algorithm.operators.InversionRoutesMutation;
import hr.vinko.hmo.v2.algorithm.operators.OrderedCrossover;
import hr.vinko.hmo.v2.algorithm.operators.ScrambleAssignmentMutation;
import hr.vinko.hmo.v2.algorithm.operators.ScrambleRoutesMutation;
import hr.vinko.hmo.v2.algorithm.operators.StopsAssignmentMutation;
import hr.vinko.hmo.v2.algorithm.operators.TournamentSelection;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		Random rand = new Random();
		Parser parser = Parser.parse("data/bus_routing/sbr10.txt");
		System.out.println(parser.getMaxWalk());
		System.out.println(parser.getBusCapacity());
		System.out.println(parser.getStudents().size());
		System.out.println(parser.getBusStops().size());

		Population pop = Population.initPopulation(100, parser);
		Selection<Solution, Population> sel = new TournamentSelection(4);

		Crossover<Solution>[] crossovers = new Crossover[]{
				new OrderedCrossover(),
				new AssignmentCrossover()
		};
		Mutation<Solution>[] mutations = new Mutation[]{
				new ScrambleRoutesMutation(0.5),
				new ScrambleAssignmentMutation(0.2, 2),
				new InversionRoutesMutation(1),
				new StopsAssignmentMutation(0.3)
		};
		
		long startTime = System.nanoTime();
		for (int i = 0; i < 10_000_000; i++) {
			Solution[] selected = sel.select(pop);
			Crossover<Solution> crx = crossovers[rand.nextInt(crossovers.length)];
			Solution child = crx.mate(selected[0], selected[1]);
			Mutation<Solution> mut = mutations[rand.nextInt(mutations.length)];
			pop.replaceSolution(selected[2], mut.mutate(child));
			if (i % pop.getSize() == 0) {
				System.out.println("(" + (System.nanoTime()-startTime)/1_000_000.0 + "ms) BEST AT " + (i / pop.getSize() + 1) + ": " + pop.getBest().getFitness());
				startTime=System.nanoTime();
			}
		}

		try (PrintWriter out = new PrintWriter("test.txt")) {
			out.write(pop.getBest().toString());
		}
	}

}
