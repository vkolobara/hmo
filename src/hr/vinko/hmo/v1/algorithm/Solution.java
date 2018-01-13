package hr.vinko.hmo.v1.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hr.vinko.hmo.parser.Coordinate;
import hr.vinko.hmo.parser.Parser;

public class Solution {
	
	private int[] solution;
	private Parser parser;
	private StudentAssignment assignment;
	private static Random rand = new Random();

	public Solution(int[] solution, Parser parser, StudentAssignment assignment) {
		super();
		this.solution = Arrays.copyOf(solution, solution.length);
		this.parser = parser;
		this.assignment = assignment;
	}
		
	public double getFitness() {
		double fitness = 0;
		
		double[] busStopDistances = parser.getStopToSchoolDistance();
		List<Coordinate> busStops = parser.getBusStops();
		
		int maxCapacity = parser.getBusCapacity();
		int[] busStopCount = assignment.getBusStopCount();
		
		int currSum = busStopCount[solution[0]];
		fitness = busStopDistances[solution[0]];
		
		for (int i=1; i<solution.length; i++) {
			currSum += busStopCount[solution[i]];
			
			if (currSum > maxCapacity) {
				fitness += busStopDistances[solution[i-1]];
				fitness += busStopDistances[solution[i]];
				currSum = busStopCount[solution[i]];
			} else {
				fitness += Coordinate.euclideanDistance(busStops.get(solution[i]), busStops.get(solution[i-1]));
			}
		}
		
		fitness += busStopDistances[solution[solution.length-1]];
				
		return fitness;
	}
	
	public void setAssignment(StudentAssignment assignment) {
		this.assignment = assignment;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((solution == null) ? 0 : solution.hashCode());
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
		return Arrays.equals(solution, other.solution);
	}

	public static Solution initGRASP(Parser parser, StudentAssignment assignment) {
		int busCapacity = parser.getBusCapacity();
		List<Coordinate> busStops = parser.getBusStops();
		double[] stopDistances = parser.getStopToSchoolDistance();
		
		int[] busStopCount = assignment.getBusStopCount();
		
		List<List<Integer>> solution = new ArrayList<>();
		
		List<Integer> studentsPerRoute = new ArrayList<>();
		
		Map<Integer, Integer> busStopOnRoute = new HashMap<>();
		double[][] savingsMatrix = new double[busStops.size()][busStops.size()];
			
		List<Pair> queue = new ArrayList<>();
		
		for (int i=0; i<busStopCount.length; i++) {
			int value = busStopCount[i];
			
			if (value > 0) {
				List<Integer> sol = new ArrayList<>();
				sol.add(i);
				solution.add(sol);
				studentsPerRoute.add(value);
				busStopOnRoute.put(i, solution.size()-1);
			}
			
			for (int j=0; j<i; j++) {
				savingsMatrix[i][j] = stopDistances[i] + stopDistances[j] - Coordinate.euclideanDistance(busStops.get(i), busStops.get(j));
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
			int alpha = Math.min(Math.max(queue.size()/10, 25),  queue.size());
			
			int index = rand.nextInt(alpha);
			
			Pair curr = queue.get(index);
			
			int i = curr.x;
			int j = curr.y;
			
			if (busStopOnRoute.get(i) != null && busStopOnRoute.get(j) != null
				&& (studentsPerRoute.get(busStopOnRoute.get(i)) + studentsPerRoute.get(busStopOnRoute.get(j))) <= busCapacity
		        && studentsPerRoute.get(busStopOnRoute.get(i)) > 0
		        && studentsPerRoute.get(busStopOnRoute.get(j)) > 0
		        && i != j) {
				
		            if (solution.get(busStopOnRoute.get(i)).get(0) == i && 
		            	solution.get(busStopOnRoute.get(j)).get(solution.get(busStopOnRoute.get(j)).size()-1) == j) {

		                studentsPerRoute.set(busStopOnRoute.get(i), studentsPerRoute.get(busStopOnRoute.get(i)) + studentsPerRoute.get(busStopOnRoute.get(j)));
		                studentsPerRoute.set(busStopOnRoute.get(j), 0);

		                for (Integer elem : solution.get(busStopOnRoute.get(j))) {
		                    busStopOnRoute.put(elem, busStopOnRoute.get(i));
		                    solution.get(busStopOnRoute.get(i)).add(elem);
		                }

		            } else if (solution.get(busStopOnRoute.get(j)).get(0) == j && 
			            	solution.get(busStopOnRoute.get(i)).get(solution.get(busStopOnRoute.get(i)).size()-1) == i)  {
		            	
		                studentsPerRoute.set(busStopOnRoute.get(j), studentsPerRoute.get(busStopOnRoute.get(j)) + studentsPerRoute.get(busStopOnRoute.get(i)));
		                studentsPerRoute.set(busStopOnRoute.get(i), 0);

		                for (Integer elem : solution.get(busStopOnRoute.get(i))) {
		                    busStopOnRoute.put(elem, busStopOnRoute.get(j));
		                    solution.get(busStopOnRoute.get(j)).add(elem);
		                }

		            }

		        }
			
			queue.remove(index);
		}
		
		int[] finalSolution = new int[new HashSet<>(assignment.getAssignment().values()).size()];
		int index = 0;
		for (int i=0; i<solution.size(); i++) {
			List<Integer> route = solution.get(i);
			if (studentsPerRoute.get(i) > 0) {
				for (int j =0; j<route.size(); j++)
				finalSolution[index++] = route.get(j);
			}
		}
		
		return new Solution(finalSolution, parser, assignment);
		
	}
	
	private static class Pair {
		public Pair(int i, int j) {
			x=i;
			y=j;
		}
		int x, y;
	}

	public int[] getSolution() {
		return solution;
	}

	public Parser getParser() {
		return parser;
	}

	public StudentAssignment getAssignment() {
		return assignment;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		int maxCapacity = parser.getBusCapacity();
		int[] busStopCount = assignment.getBusStopCount();
		
		int currSum = 0;
		for (int i=0; i<solution.length; i++) {
			
			currSum += busStopCount[solution[i]];

			String wrt = (solution[i]+1) + "";
			
			if (currSum > maxCapacity) {
				sb.append("\n" + wrt + " ");
				currSum = busStopCount[solution[i]];
			} else {
				sb.append(wrt + " ");
			}
		}
		
		return sb.toString() + "\n\n" + assignment.toString();
	}
	

}
