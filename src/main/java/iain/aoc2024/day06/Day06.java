package iain.aoc2024.day06;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

@Getter
@Setter
public class Day06 {

    private static final String INPUT_FILE_NAME = "day06/input.txt";
    private static final Point[] DIRECTIONS = new Point[]
            {
                    new Point(0, -1),
                    new Point(1, 0),
                    new Point(0, 1),
                    new Point(-1, 0),
            };
    private int maxX = 0;
    private int maxY = 0;
    private Point startPosition = new Point(-1, -1);
    private final HashSet<Point> blockers = new HashSet<>();

    public Day06() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        int directionIndex = 0;
        Point currentPosition = new Point(startPosition.x, startPosition.y);
        HashSet<Point> visitedPositions = new HashSet<>();
        while (currentPosition.x >= 0 && currentPosition.x <= getMaxX() &&
                currentPosition.y >= 0 && currentPosition.y <= getMaxY()) {
            visitedPositions.add(new Point(currentPosition));
            Point nextPosition = new Point(
                    currentPosition.x + DIRECTIONS[directionIndex].x,
                    currentPosition.y + DIRECTIONS[directionIndex].y
            );
            if (blockers.contains(nextPosition)) {
                directionIndex = (directionIndex + 1) % 4;
            } else {
                currentPosition = nextPosition;
            }

        }
        System.out.printf("The solution to part one is %s.%n", visitedPositions.size());
    }

    private void solvePartTwo() {
        long solution = 0;
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private void getInput() throws IOException {
        InputStream inputStream = iain.aoc2024.day05.Day05.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < line.length(); x++) {
                    setMaxX(Math.max(getMaxX(), x));
                    if (line.charAt(x) == '#') {
                        blockers.add(new Point(x, y));
                    } else if (line.charAt(x) == '^') {
                        setStartPosition(new Point(x, y));
                    }
                }
                setMaxY(Math.max(getMaxY(), y));
                y++;
            }
        }
    }
}
