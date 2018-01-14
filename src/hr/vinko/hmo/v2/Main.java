package hr.vinko.hmo.v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Random;

import hr.vinko.hmo.operators.Crossover;
import hr.vinko.hmo.operators.Mutation;
import hr.vinko.hmo.operators.Selection;
import hr.vinko.hmo.parser.Parser;
import hr.vinko.hmo.v2.algorithm.Population;
import hr.vinko.hmo.v2.algorithm.Solution;
import hr.vinko.hmo.v2.algorithm.StudentAssignment;
import hr.vinko.hmo.v2.algorithm.operators.AssignmentCrossover;
import hr.vinko.hmo.v2.algorithm.operators.CycleCrossover;
import hr.vinko.hmo.v2.algorithm.operators.InversionRoutesMutation;
import hr.vinko.hmo.v2.algorithm.operators.OrderedCrossover;
import hr.vinko.hmo.v2.algorithm.operators.ScrambleAssignmentMutation;
import hr.vinko.hmo.v2.algorithm.operators.ScrambleRoutesMutation;
import hr.vinko.hmo.v2.algorithm.operators.StopsAssignmentMutation;
import hr.vinko.hmo.v2.algorithm.operators.TournamentSelection;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Two arguments expected: CONFIG PATH, INSTANCE PATH");
			System.exit(-1);
		}
		try (InputStream input = new FileInputStream(args[0])) {
			String instance = Paths.get(args[1]).getFileName().toString().replaceFirst("[.][^.]+$", "");;
			Properties prop = new Properties();
			prop.load(input);

			int popSize = Integer.parseInt((String) prop.get("pop_size"));
			double crxRate = Double.parseDouble((String) prop.get("crx_rate"));
			double mutRate = Double.parseDouble((String) prop.get("mut_rate"));
			int maxNoProgress = Integer.parseInt((String) prop.get("max_no_progress"));
			String outDir =  (String) prop.get("out_dir");
			long termSec =  Long.parseLong((String) prop.get("term_sec"));
			boolean genInfo = Boolean.parseBoolean((String) prop.get("gen_info"));

			try {
				File file = new File(outDir + File.separator + instance);
				file.mkdirs();
			} catch (Exception ingorable){};
			
			String dataPath = args[1];
			Random rand = new Random();
			
			
			long startTime = System.nanoTime();
			Parser parser = Parser.parse(dataPath);
			StudentAssignment assignment = StudentAssignment.assign(parser);
			
			Population pop = Population.initPopulation(popSize, parser, assignment, false);

			Selection<Solution, Population> sel = new TournamentSelection(3);

			Crossover<Solution>[] crossovers = new Crossover[] { new OrderedCrossover(), new AssignmentCrossover(),
					new CycleCrossover(), };

			Mutation<Solution>[] mutations = new Mutation[] { new ScrambleRoutesMutation(),
					new ScrambleAssignmentMutation(0.01), new InversionRoutesMutation(),
					new StopsAssignmentMutation() };

			
			double prevBest = pop.getBest().getFitness();
			long evaluations = popSize;
			
			boolean min1 = false;
			boolean min5 = false;
			boolean minInf = false;
			int noProgress = 0;
			long noProgressStartTime = System.nanoTime();
			
			for (int i = 0; ; i++) {
				Solution[] selected = sel.select(pop);
				if (i % 1_000 == 0) {
					pop.replaceSolution(selected[2], Solution.initGRASP(parser, assignment, false));
				} else {
					Solution child = selected[2];
					if (rand.nextDouble() <= crxRate) {
						Crossover<Solution> crx = crossovers[rand.nextInt(crossovers.length)];
						child = crx.mate(selected[0], selected[1]);
					}
					if (rand.nextDouble() <= mutRate) {
						Mutation<Solution> mut = mutations[rand.nextInt(mutations.length)];
						child = mut.mutate(child);
					}
					child.setFitness(child.calculateFitness());
					pop.replaceSolution(selected[2], child);
				}
				evaluations++;
				Solution best = pop.getBest();
				if (best.getFitness() < prevBest) {
					noProgress = 0;
					prevBest = best.getFitness();
					noProgressStartTime = System.nanoTime();
				} else {
					noProgress++;
				}

				/*
				 * If no progress for maxNoProgress, reset the population but
				 * retain the current best.
				 */
				if (noProgress >= maxNoProgress) {
					Population newPop = Population.initPopulation(popSize, parser, assignment, true);
					newPop.replaceSolution(sel.select(newPop)[2], pop.getBest());
					pop = newPop;
					noProgress = 0;
					evaluations += popSize;
				}
								
				if (genInfo && i % pop.getSize() == 0) {
					System.out.println(
							"BEST AT " + (i / pop.getSize() + 1) + ": " + best.getFitness() + " AVG: " + pop.getAvg());
				}
				
				long currTime = System.nanoTime();
				double seconds = (currTime - startTime) / 1_000_000_000.0;

				double progressTime = (currTime - noProgressStartTime) / 1_000_000_000.0;
				if (!min1 && seconds >= 60) {
					String time = "1m";
					writeToFile(pop.getBest(), outDir, instance, time, evaluations, seconds);
					min1=true;
				} else if (!min5 && seconds >= 300) {
					String time = "5m";
					writeToFile(pop.getBest(), outDir, instance, time, evaluations, seconds);
					min5=true;
				} else if (!minInf && progressTime >= termSec) {
					String time = "inf";
					writeToFile(pop.getBest(), outDir, instance, time, evaluations, seconds);
					minInf=true;
					break;
				}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	private static void writeToFile(Solution sol, String outDir, String instance, String time, long evaluations, double totalTime) throws FileNotFoundException {
		String outSol = getSolOutputFilePath(outDir, instance, time);
		String outEval = getEvalOutputFilePath(outDir, instance, time);
		
		writeSolutionToFile(outSol, sol);
		writeEvaluationsToFile(outEval, evaluations, sol.getFitness(), totalTime);
	}

	private static void writeSolutionToFile(String filePath, Solution sol) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(filePath)) {
			out.write(sol.toString());
		}
	}
	
	private static void writeEvaluationsToFile(String filePath, long evaluations, double fitness, double totalTime) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(filePath)) {
			out.write(totalTime + "s\n" + evaluations + "\n" + fitness);
		}
	}
	
	private static String getSolOutputFilePath(String outDir, String instance, String time) {
		return outDir + File.separator + instance + File.separator + "res-" + time + "-" + instance + ".txt";
	}
	
	private static String getEvalOutputFilePath(String outDir, String instance, String time) {
		return outDir + File.separator + instance + File.separator + "eval-" + time + "-" + instance + ".txt";
	}

}
