package hr.vinko.hmo.v2.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private double fitness;

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
		return fitness;
	}

	public double calculateFitness() {
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
				int alpha;
				if (greedy) {
					alpha = Math.max(1, queue.size() / 5);
				} else {
					alpha = queue.size();
				}
				ind = rand.nextInt(alpha);
				ind = rand.nextInt(alpha);

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

		List<Coordinate> busStops = parser.getBusStops();
		double[] stopDistances = parser.getStopToSchoolDistance();

		List<List<Integer>> solution = new ArrayList<>();

		List<Integer> studentsPerRoute = new ArrayList<>();

		Map<Integer, Integer> busStopOnRoute = new HashMap<>();
		double[][] savingsMatrix = new double[busStops.size()][busStops.size()];

		List<Pair> queue = new ArrayList<>();

		for (int i = 0; i < busStopCount.length; i++) {
			int value = busStopCount[i];

			if (value > 0) {
				List<Integer> sol = new ArrayList<>();
				sol.add(i);
				solution.add(sol);
				studentsPerRoute.add(value);
				busStopOnRoute.put(i, solution.size() - 1);
			}

			for (int j = 0; j < i; j++) {
				savingsMatrix[i][j] = stopDistances[i] + stopDistances[j]
						- Coordinate.euclideanDistance(busStops.get(i), busStops.get(j));
				queue.add(new Solution.Pair(i, j));
			}
		}

		queue.sort(new Comparator<Pair>() {
			@Override
			public int compare(Pair o1, Pair o2) {
				return -Double.compare(savingsMatrix[o1.x][o1.y], savingsMatrix[o2.x][o2.y]);
			}
		});

		while (!queue.isEmpty()) {
			int alpha = Math.min(Math.max(queue.size() / 10, 5), queue.size());

			int index = rand.nextInt(alpha);

			Pair curr = queue.get(index);

			int i = curr.x;
			int j = curr.y;

			if (busStopOnRoute.get(i) != null && busStopOnRoute.get(j) != null
					&& (studentsPerRoute.get(busStopOnRoute.get(i))
							+ studentsPerRoute.get(busStopOnRoute.get(j))) <= busCapacity
					&& studentsPerRoute.get(busStopOnRoute.get(i)) > 0
					&& studentsPerRoute.get(busStopOnRoute.get(j)) > 0 && i != j) {

				if (solution.get(busStopOnRoute.get(i)).get(0) == i && solution.get(busStopOnRoute.get(j))
						.get(solution.get(busStopOnRoute.get(j)).size() - 1) == j) {

					studentsPerRoute.set(busStopOnRoute.get(i),
							studentsPerRoute.get(busStopOnRoute.get(i)) + studentsPerRoute.get(busStopOnRoute.get(j)));
					studentsPerRoute.set(busStopOnRoute.get(j), 0);

					for (Integer elem : solution.get(busStopOnRoute.get(j))) {
						busStopOnRoute.put(elem, busStopOnRoute.get(i));
						solution.get(busStopOnRoute.get(i)).add(elem);
					}

				} else if (solution.get(busStopOnRoute.get(j)).get(0) == j && solution.get(busStopOnRoute.get(i))
						.get(solution.get(busStopOnRoute.get(i)).size() - 1) == i) {

					studentsPerRoute.set(busStopOnRoute.get(j),
							studentsPerRoute.get(busStopOnRoute.get(j)) + studentsPerRoute.get(busStopOnRoute.get(i)));
					studentsPerRoute.set(busStopOnRoute.get(i), 0);

					for (Integer elem : solution.get(busStopOnRoute.get(i))) {
						busStopOnRoute.put(elem, busStopOnRoute.get(j));
						solution.get(busStopOnRoute.get(j)).add(elem);
					}

				}

			}

			queue.remove(index);
		}

		List<Integer> unvisited = new ArrayList<>();
		for (int i = 0; i < busStopCount.length; i++) {
			unvisited.add(i);
		}

		int index = 0;
		for (int i = 0; i < solution.size(); i++) {
			List<Integer> route = solution.get(i);
			if (studentsPerRoute.get(i) > 0) {
				for (int j = 0; j < route.size(); j++) {
					finalSolution[index++] = route.get(j);
					unvisited.remove(route.get(j));
				}
			}
		}

		for (; index < finalSolution.length; index++) {
			finalSolution[index] = unvisited.get(rand.nextInt(unvisited.size()));
			unvisited.remove(new Integer(finalSolution[index]));
		}
		/*
		 * *********************************************************************
		 * ***********
		 * *********************************************************************
		 * ***********
		 */
		Solution sol = new Solution(finalSolution, assignment, busStopCount, parser, studentAssignment);
		sol.fitness = sol.calculateFitness();
		return sol;

	}

	private static class Pair {
		public Pair(int i, int j) {
			x = i;
			y = j;
		}

		int x, y;
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

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

}
