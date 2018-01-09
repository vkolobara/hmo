package hr.vinko.hmo.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hr.vinko.hmo.parser.Coordinate;
import hr.vinko.hmo.parser.Parser;

public class StudentAssignment {
	private Map<Integer, Integer> assignment;
	private int[] busStopCount;

	private static Random rand = new Random();

	public StudentAssignment(int nStops) {
		assignment = new HashMap<>();
		busStopCount = new int[nStops];
	}

	public static StudentAssignment assign(Parser parser) {

		int busCapacity = parser.getBusCapacity();
		double maxWalk = parser.getMaxWalk();
		List<Coordinate> busStops = parser.getBusStops();
		List<Coordinate> students = parser.getStudents();
		// Coordinate school = parser.getSchool();
		double[] stopDistances = parser.getStopToSchoolDistance();
		double[][] studentDistances = parser.getStudentToStopDistance();

		StudentAssignment assignment = new StudentAssignment(busStops.size());

		int alpha = busStops.size()/2;

		for (int i = 0; i < students.size(); i++) {
			List<Integer> queue = new ArrayList<>();

			for (int j = 0; j < busStops.size(); j++) {
				if (maxWalk >= studentDistances[i][j])
					queue.add(j);
			}

			if (queue.size() == 1)
				assignment.assignment.put(i, queue.get(0));
			else {
				queue.sort(new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return Double.compare(stopDistances[o1], stopDistances[o2]);
					}
				});
				

				int ind = rand.nextInt(Math.min(alpha, queue.size()));

				while (true) {
					int sel = queue.get(ind);

					if (assignment.busStopCount[sel] < busCapacity) {
						assignment.assignment.put(i, sel);
						break;
					} else {
						queue.remove(ind);
						ind = ind - 1 > 0 ? ind - 1 : 0;
					}
				}
			}

			assignment.busStopCount[assignment.assignment.get(i)]++;
		}

		return assignment;

	}

	public Map<Integer, Integer> getAssignment() {
		return assignment;
	}

	public int[] getBusStopCount() {
		return busStopCount;
	}

}
