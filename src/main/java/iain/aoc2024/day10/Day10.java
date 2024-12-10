package iain.aoc2024.day10;

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
public class Day10 {

    private static final String INPUT_FILE_NAME = "day10/input.txt";
    private static final Point[] DIRECTIONS = new Point[]
            {
                    new Point(0, -1),
                    new Point(1, 0),
                    new Point(0, 1),
                    new Point(-1, 0),
            };

    private int maxX = 0;
    private int maxY = 0;
    private List<String> topologicalMap = new ArrayList<>();

    public Day10() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        long solution = 0;
        for (int y = 0; y <= getMaxY(); y++) {
            for (int x = 0; x <= getMaxX(); x++) {
                if (getTopologicalMap().get(y).charAt(x) == '0') {
                    solution += getTrailheadScore(x, y);
                }
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        long solution = 0;
        for (int y = 0; y <= getMaxY(); y++) {
            for (int x = 0; x <= getMaxX(); x++) {
                if (getTopologicalMap().get(y).charAt(x) == '0') {
                    solution += getTrailheadRating(x, y);
                }
            }
        }
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private long getTrailheadScore(int x, int y) {
        return getTrailheadValue(x, y, true);
    }

    private long getTrailheadRating(int x, int y) {
        return getTrailheadValue(x, y, false);
    }

    private long getTrailheadValue(int x, int y, boolean isTrailheadScore) {
        Queue<Point> pointsToVisit = new ArrayDeque<>();
        HashSet<Point> trackedPoints = new HashSet<>();
        pointsToVisit.add(new Point(x, y));
        long trailheadValue = 0;
        while (!pointsToVisit.isEmpty()) {
            Point currentPoint = pointsToVisit.poll();
            int currentHeight = Character.getNumericValue(getTopologicalMap().get(currentPoint.y).charAt(currentPoint.x));
            for (Point direction : DIRECTIONS) {
                Point nextPoint = new Point(currentPoint.x + direction.x, currentPoint.y + direction.y);
                if (nextPoint.x < 0 || nextPoint.x > getMaxX() || nextPoint.y < 0 || nextPoint.y > getMaxY() ||
                        trackedPoints.contains(nextPoint)) {
                    continue;
                }
                int nextHeight = Character.getNumericValue(getTopologicalMap().get(nextPoint.y).charAt(nextPoint.x));
                if (nextHeight != currentHeight + 1) {
                    continue;
                }
                if (isTrailheadScore) {
                    trackedPoints.add(nextPoint);
                }
                if (nextHeight == 9) {
                    trailheadValue++;
                    continue;
                }
                pointsToVisit.add(nextPoint);
            }
        }
        return trailheadValue;
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day10.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                getTopologicalMap().add(line);
                setMaxX(Math.max(getMaxX(), line.length() - 1));
                setMaxY(Math.max(getMaxY(), y));
                y++;
            }
        }
    }
}
