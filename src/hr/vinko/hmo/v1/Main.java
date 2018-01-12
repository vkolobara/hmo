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
		Parser parser = Parser.parse("data/bus_routing/sbr10.txt");
		System.out.println(parser.getMaxWalk());
		System.out.println(parser.getBusCapacity());
		System.out.println(parser.getStudents().size());
		System.out.println(parser.getBusStops().size());

		Population pop = Population.initPopulation(30, parser);

		Selection<Solution, Population> sel = new TournamentSelection(3);
		Crossover<Solution>[] crossovers = new Crossover[] { 
				new OrderedCrossover(), 
//				new PBCrossover() 
				};
		Mutation<Solution>[] mutations = new Mutation[] { 
				new ScrambleMutation(0.5), 
				new InversionMutation(0.2),
				new SwapMutation(0.3) 
				};

		for (int i = 0; i < 3_000_000; i++) {
			Solution[] selected = sel.select(pop);
			Crossover<Solution> crx = crossovers[rand.nextInt(crossovers.length)];
			Solution child = crx.mate(selected[0], selected[1]);
			Mutation<Solution> mut = mutations[rand.nextInt(mutations.length)];
			pop.replaceSolution(selected[2], mut.mutate(child));
			if (i % pop.getSize() == 0) {
				System.out.println("BEST AT " + (i / pop.getSize() + 1) + ": " + pop.getBest().getFitness());
			}
		}

		try (PrintWriter out = new PrintWriter("test1.txt")) {
			out.print(pop.getBest());
		}

	}

}
