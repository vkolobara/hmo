package hr.vinko.hmo.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hr.vinko.hmo.parser.Coordinate;
import hr.vinko.hmo.parser.Parser;

public class Solution {
	
	private List<List<Integer>> solution;
	private Parser parser;
	private StudentAssignment assignment;
	private static Random rand = new Random();

	public Solution(List<List<Integer>> solution, Parser parser, StudentAssignment assignment) {
		super();
		this.solution = solution;
		this.parser = parser;
		this.assignment = assignment;
	}
	
	public double getFitness() {
		double fitness = 0;
		
		double[] busStopDistances = parser.getStopToSchoolDistance();
		List<Coordinate> busStops = parser.getBusStops();
		
		for (List<Integer> sol : solution) {
			fitness += busStopDistances[sol.get(0)] + busStopDistances[sol.get(sol.size()-1)];
			for (int i=1; i<sol.size(); i++) {
				fitness += Coordinate.euclideanDistance(busStops.get(sol.get(i)), busStops.get(sol.get(i-1)));
			}
		}
		
		return fitness;
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
		if (solution == null) {
			if (other.solution != null)
				return false;
		} else if (!solution.equals(other.solution))
			return false;
		return true;
	}

	public static Solution initGRASP(Parser parser, StudentAssignment assignment) {
		int busCapacity = parser.getBusCapacity();
		double maxWalk = parser.getMaxWalk();
		List<Coordinate> busStops = parser.getBusStops();
		List<Coordinate> students = parser.getStudents();
		Coordinate school = parser.getSchool();
		double[] stopDistances = parser.getStopToSchoolDistance();
		double[][] studentDistances = parser.getStudentToStopDistance();
		
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
		
		List<List<Integer>> finalSolution = new ArrayList<>();
		
		for (int i=0; i<solution.size(); i++) {
			List<Integer> route = solution.get(i);
			if (studentsPerRoute.get(i) > 0) {
				finalSolution.add(route);
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

	public List<List<Integer>> getSolution() {
		return solution;
	}

	public Parser getParser() {
		return parser;
	}

	public StudentAssignment getAssignment() {
		return assignment;
	}
	
	

}
