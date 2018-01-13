package hr.vinko.hmo.v1.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.IntStream;

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

	public static StudentAssignment assign(Parser parser, boolean greedy) {

		int busCapacity = parser.getBusCapacity();
		double maxWalk = parser.getMaxWalk();
		List<Coordinate> busStops = parser.getBusStops();
		List<Coordinate> students = parser.getStudents();
		// Coordinate school = parser.getSchool();
		double[] stopDistances = parser.getStopToSchoolDistance();
		double[][] studentDistances = parser.getStudentToStopDistance();

		StudentAssignment assignment = new StudentAssignment(busStops.size());

		int alpha;
		if (greedy) {
			alpha = Math.max(1, busStops.size()/5);
		} else {
			alpha = busStops.size();
		}
		
		int studList[] = IntStream.range(0, students.size()).toArray();

		for (int i = 0; i < studList.length; i++) {
			int randomPosition = rand.nextInt(studList.length);
			int temp = studList[i];
			studList[i] = studList[randomPosition];
			studList[randomPosition] = temp;
		}

		for (int i = 0; i < studList.length; i++) {
			int index = studList[i];
			List<Integer> queue = new ArrayList<>();

			for (int j = 0; j < busStops.size(); j++) {
				if (maxWalk >= studentDistances[index][j])
					queue.add(j);
			}

			if (queue.size() == 1)
				assignment.assignment.put(index, queue.get(0));
			else {
				queue.sort(new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return Double.compare(stopDistances[o1], stopDistances[o2]);
					}
				});
				int ind = 0;

				ind = rand.nextInt(Math.min(alpha, queue.size()));

				while (true) {
					int sel = queue.get(ind);

					if (assignment.busStopCount[sel] < busCapacity) {
						assignment.assignment.put(index, sel);
						break;
					} else {
						queue.remove(ind);
						ind = ind - 1 > 0 ? ind - 1 : 0;
					}
				}

			}

			assignment.busStopCount[assignment.assignment.get(index)]++;
		}

		return assignment;

	}

	public Map<Integer, Integer> getAssignment() {
		return assignment;
	}

	public int[] getBusStopCount() {
		return busStopCount;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Entry<Integer, Integer> entry : assignment.entrySet()) {
			sb.append((entry.getKey() + 1) + " " + (entry.getValue() + 1) + "\n");
		}

		return sb.toString();

	}

}
