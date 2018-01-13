package hr.vinko.hmo.v2.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import hr.vinko.hmo.parser.Coordinate;
import hr.vinko.hmo.parser.Parser;

public class Solution {

	public int[] routes;
	public int[] assignment;
	public int[] busStopCount;

	private Parser parser;
	private StudentAssignment studentAssignment;
	private static Random rand = new Random();

	public Solution(int[] routes, int[] assignment, int[] busStopCount, Parser parser,
			StudentAssignment studentAssignment) {
		super();
		this.routes = Arrays.copyOf(routes, routes.length);
		this.assignment = Arrays.copyOf(assignment, assignment.length);
		this.busStopCount = Arrays.copyOf(busStopCount, busStopCount.length);
		this.parser = parser;
		this.studentAssignment = studentAssignment;
	}

	public double getFitness() {
		double fitness = 0;

		double[] busStopDistances = parser.getStopToSchoolDistance();
		List<Coordinate> busStops = parser.getBusStops();

		int maxCapacity = parser.getBusCapacity();

		int currSum = 0;
		int i = 0;

		do {
			currSum = busStopCount[routes[i++]];
		} while (currSum == 0);

		fitness = busStopDistances[routes[i - 1]];

		int last = i - 1;
		for (; i < routes.length; i++) {
			if (busStopCount[routes[i]] == 0)
				continue;

			currSum += busStopCount[routes[i]];

			if (currSum > maxCapacity) {
				fitness += busStopDistances[routes[last]];
				fitness += busStopDistances[routes[i]];
				currSum = busStopCount[routes[i]];
			} else {
				fitness += Coordinate.euclideanDistance(busStops.get(routes[i]), busStops.get(routes[last]));
			}

			last = i;
		}

		fitness += busStopDistances[routes[last]];

		return fitness;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(assignment);
		result = prime * result + Arrays.hashCode(routes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Solution other = (Solution) obj;
		if (!Arrays.equals(assignment, other.assignment))
			return false;
		if (!Arrays.equals(routes, other.routes))
			return false;
		return true;
	}

	public static Solution initGRASP(Parser parser, StudentAssignment studentAssignment, boolean greedy) {
		int busCapacity = parser.getBusCapacity();

		int[] busStopCount = new int[parser.getBusStops().size()];
		int[] assignment = new int[parser.getStudents().size()];

		/*
		 * *********************************************************************
		 * *********** ASSIGN STUDENTS TO STOPS
		 * *********************************************************************
		 * ***********
		 */

		int studList[] = IntStream.range(0, parser.getStudents().size()).toArray();

		outer: while (true) {
			busStopCount = new int[parser.getBusStops().size()];

			for (int i = 0; i < studList.length; i++) {
				int randomPosition = rand.nextInt(studList.length);
				int temp = studList[i];
				studList[i] = studList[randomPosition];
				studList[randomPosition] = temp;
			}

			for (int i = 0; i < studList.length; i++) {
				int index = studList[i];
				List<Integer> queue = new ArrayList<>(studentAssignment.possibilities.get(index));
				int ind = 0;

				if (greedy) {
					ind = rand.nextInt(Math.min(5, queue.size()));
				} else {
					ind = rand.nextInt(queue.size());
				}

				while (true) {
					if (queue.size() == 0)
						continue outer;
					int sel = queue.get(ind);

					if (busStopCount[sel] < busCapacity) {
						assignment[index] = sel;
						break;
					} else {
						queue.remove(ind);
						ind = ind - 1 > 0 ? ind - 1 : 0;
					}
				}

				busStopCount[assignment[index]]++;
			}
			break;
		}

		/*
		 * *********************************************************************
		 * ***********
		 */

		/*
		 * *********************************************************************
		 * *********** CREATE ROUTES
		 * *********************************************************************
		 * ***********
		 */

		int[] finalSolution = new int[busStopCount.length];
		for (int i = 0; i < finalSolution.length; i++) {
			finalSolution[i] = i;
		}

		for (int i = 0; i < finalSolution.length; i++) {
			int randomPosition = rand.nextInt(finalSolution.length);
			int temp = finalSolution[i];
			finalSolution[i] = finalSolution[randomPosition];
			finalSolution[randomPosition] = temp;
		}
		/*
		 * *********************************************************************
		 * ***********
		 * *********************************************************************
		 * ***********
		 */

		return new Solution(finalSolution, assignment, busStopCount, parser, studentAssignment);

	}

	public Parser getParser() {
		return parser;
	}

	public StudentAssignment getStudentAssignment() {
		return studentAssignment;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		int maxCapacity = parser.getBusCapacity();

		int currSum = 0;
		for (int i = 0; i < routes.length; i++) {
			if (busStopCount[routes[i]] == 0)
				continue;
			currSum += busStopCount[routes[i]];

			String wrt = (routes[i] + 1) + "";

			if (currSum > maxCapacity) {
				sb.append("\n" + wrt + " ");
				currSum = busStopCount[routes[i]];
			} else {
				sb.append(wrt + " ");
			}
		}
		sb.append("\n\n");

		for (int i = 0; i < assignment.length; i++) {
			sb.append((i + 1) + " " + (assignment[i] + 1) + "\n");
		}

		return sb.toString();
	}

}
