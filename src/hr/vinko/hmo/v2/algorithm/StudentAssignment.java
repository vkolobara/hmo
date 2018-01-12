package hr.vinko.hmo.v2.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.vinko.hmo.parser.Coordinate;
import hr.vinko.hmo.parser.Parser;

public class StudentAssignment {
	public Map<Integer, List<Integer>> possibilities;

	public StudentAssignment(int nStops) {
		possibilities = new HashMap<>();
	}

	public static StudentAssignment assign(Parser parser) {

		double maxWalk = parser.getMaxWalk();
		List<Coordinate> busStops = parser.getBusStops();
		List<Coordinate> students = parser.getStudents();
		// Coordinate school = parser.getSchool();
		double[] stopDistances = parser.getStopToSchoolDistance();
		double[][] studentDistances = parser.getStudentToStopDistance();

		StudentAssignment assignment = new StudentAssignment(busStops.size());


		for (int i = 0; i < students.size(); i++) {
			List<Integer> queue = new ArrayList<>();

			for (int j = 0; j < busStops.size(); j++) {
				if (maxWalk >= studentDistances[i][j])
					queue.add(j);
			}
			queue.sort(new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return Double.compare(stopDistances[o1], stopDistances[o2]);
				}
			});
			
			assignment.possibilities.put(i, queue);
			
		}

		return assignment;

	}
}
