package hr.vinko.hmo;

import java.io.IOException;

import hr.vinko.hmo.algorithm.Population;
import hr.vinko.hmo.algorithm.Solution;
import hr.vinko.hmo.algorithm.operators.Selection;
import hr.vinko.hmo.algorithm.operators.TournamentSelection;
import hr.vinko.hmo.parser.Parser;

public class Main {

	public static void main(String[] args) throws IOException {
		Parser parser = Parser.parse("data/bus_routing/sbr1.txt");
		System.out.println(parser.getMaxWalk());
		System.out.println(parser.getBusCapacity());
		System.out.println(parser.getStudents().size());
		System.out.println(parser.getBusStops().size());

		Population pop = Population.initPopulation(100, parser);
		
		for (Solution sol : pop.getPopulation()) {
			System.out.println(sol.getSolution().size() + " " + sol.getFitness());
		}
		
		Selection sel = new TournamentSelection(4);
		Solution[] selected = sel.select(pop);
		System.out.println(selected[0].getFitness() + " " + selected[1].getFitness() + " " + selected[2].getFitness());
	}
	

}
