package iain.aoc2024.day12;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Queue;
import java.util.*;

@Getter
@Setter
public class Day12 {

    private static final String INPUT_FILE_NAME = "day12/input.txt";
    private static final Point[] DIRECTIONS = new Point[]
            {
                    new Point(0, -1),
                    new Point(1, 0),
                    new Point(0, 1),
                    new Point(-1, 0),
            };

    private int maxX = 0;
    private int maxY = 0;
    private List<String> gardenMap = new ArrayList<>();
    private HashSet<Point> allPoints = new HashSet<>();

    public Day12() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        long solution = 0;
        LinkedHashSet<Point> allPointsLocal = new LinkedHashSet<>(getAllPoints());
        while (!allPointsLocal.isEmpty()) {
            Point gardenPlot = allPointsLocal.getFirst();
            solution += getGardenFenceCost(gardenPlot, allPointsLocal);
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        long solution = 0;
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private long getGardenFenceCost(Point startingPoint, HashSet<Point> allPointsLocal) {
        char plantId = getGardenMap().get(startingPoint.y).charAt(startingPoint.x);
        Queue<Point> pointsToVisit = new ArrayDeque<>();
        HashSet<Point> trackedPoints = new HashSet<>();
        pointsToVisit.add(startingPoint);
        trackedPoints.add(startingPoint);
        long perimeter = 0;
        // flood fill
        while (!pointsToVisit.isEmpty()) {
            Point currentPoint = pointsToVisit.poll();
            allPointsLocal.remove(currentPoint);
            for (Point direction : DIRECTIONS) {
                Point nextPoint = new Point(currentPoint.x + direction.x, currentPoint.y + direction.y);
                if (nextPoint.x < 0 || nextPoint.x > getMaxX() || nextPoint.y < 0 || nextPoint.y > getMaxY() ||
                        getGardenMap().get(nextPoint.y).charAt(nextPoint.x) != plantId) {
                    perimeter++;
                    continue;
                }
                if (trackedPoints.contains(nextPoint)) {
                    continue;
                }
                trackedPoints.add(nextPoint);
                pointsToVisit.add(nextPoint);
            }
        }
        return trackedPoints.size() * perimeter;
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day12.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                getGardenMap().add(line);
                setMaxX(Math.max(getMaxX(), line.length() - 1));
                setMaxY(Math.max(getMaxY(), y));
                for (int x = 0; x < line.length(); x++) {
                    getAllPoints().add(new Point(x, y));
                }
                y++;
            }
        }
    }
}
