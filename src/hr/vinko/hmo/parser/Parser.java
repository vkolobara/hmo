package hr.vinko.hmo.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
	private Coordinate school;
	private List<Coordinate> busStops;
	private List<Coordinate> students;

	private int busCapacity;
	private double maxWalk;

	private double[][] studentToStopDistance;
	private double[] stopToSchoolDistance;
	
	public Parser(int nStops, int nStudents) {
		busStops = new ArrayList<>(nStops);
		students = new ArrayList<>(nStudents);
		studentToStopDistance = new double[nStudents][nStops];
		stopToSchoolDistance = new double[nStops-1];
	}

	public static Parser parse(String filePath) throws IOException {
		try (Scanner scanner = new Scanner(new File(filePath))) {
			String[] line = scanner.nextLine().split("\\s+");
			int nStops = Integer.parseInt(line[0]);
			int nStudents = Integer.parseInt(line[2]);
			
			Parser parser = new Parser(nStops, nStudents);
			parser.maxWalk = Double.parseDouble(line[4]);
			parser.busCapacity = Integer.parseInt(line[7]);
			
			scanner.nextLine();
			
			line = scanner.nextLine().split("\\s+");
			parser.school = new Coordinate(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
			
			
			for (int i=0; i<nStops-1; i++) {
				line = scanner.nextLine().split("\\s+");
				Coordinate busStop = new Coordinate(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
				parser.busStops.add(busStop);
				parser.stopToSchoolDistance[i] = Coordinate.euclideanDistance(busStop, parser.school);
			}
			
			scanner.nextLine();
			scanner.nextLine();
			
	
			
			for (int i=0; i<nStudents; i++) {
				line = scanner.nextLine().split("\\s+");
				Coordinate student = new Coordinate(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
				parser.students.add(student);
				
				for (int j=0; j<nStops-1; j++) {
					parser.studentToStopDistance[i][j] = Coordinate.euclideanDistance(student, parser.busStops.get(j));
				}
			}
			
			return parser;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Coordinate getSchool() {
		return school;
	}

	public List<Coordinate> getBusStops() {
		return busStops;
	}

	public List<Coordinate> getStudents() {
		return students;
	}

	public int getBusCapacity() {
		return busCapacity;
	}

	public double getMaxWalk() {
		return maxWalk;
	}

	public double[][] getStudentToStopDistance() {
		return studentToStopDistance;
	}

	public double[] getStopToSchoolDistance() {
		return stopToSchoolDistance;
	}

	

}
