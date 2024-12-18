package iain.aoc2024.day18;

import iain.aoc2024.coordinates.Coordinate2D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Day18 {

    private static final String INPUT_FILE_NAME = "day18/input.txt";
    private static final Coordinate2D MAX_COORDINATES = new Coordinate2D(70, 70); // 70 input 6 example
    private static final Coordinate2D[] DIRECTIONS = new Coordinate2D[]
            {
                    new Coordinate2D(0, -1),
                    new Coordinate2D(1, 0),
                    new Coordinate2D(0, 1),
                    new Coordinate2D(-1, 0),
            };

    private final HashMap<Coordinate2D, MemorySpace> coordinateToMemorySpaceMap = new HashMap<>();
    private final TreeSet<MemorySpace> unvisitedMemorySpaces = new TreeSet<>(new MemorySpaceComparator());
    private final List<String> fallingBytes = new ArrayList<>();

    public void solve() throws IOException {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() throws IOException {
        System.out.printf("The solution to part one is %s.%n", getShortestPath(1024L));
    }

    private void solvePartTwo() throws IOException {
        // binary search
        int low = 1025;
        int high = fallingBytes.size();
        while (high - low > 1) {
            int index = (low + high) / 2;
            if (getShortestPath(index) != -1) {
                low = index;
            }
            else {
                high = index;
            }
        }
        System.out.printf("The solution to part two is %s.%n", fallingBytes.get(high - 1));
    }

    private long getShortestPath(long wallTimerCutoff) throws IOException {
        getInput();
        MemorySpace startingMemorySpace = coordinateToMemorySpaceMap.get(new Coordinate2D(0, 0));
        unvisitedMemorySpaces.remove(startingMemorySpace);
        startingMemorySpace.setShortestPath(0);
        unvisitedMemorySpaces.add(startingMemorySpace);
        while (!unvisitedMemorySpaces.isEmpty()) {
            MemorySpace currentSpace = unvisitedMemorySpaces.pollFirst();
            if (currentSpace == null || currentSpace.getShortestPath() == Long.MAX_VALUE) {
                // no path to end
                return -1;
            }
            coordinateToMemorySpaceMap.remove(currentSpace.getCoordinates());
            if (currentSpace.getCoordinates().equals(MAX_COORDINATES)) {
                return currentSpace.getShortestPath();
            }
            for (Coordinate2D direction : DIRECTIONS) {
                Coordinate2D nextCoord = new Coordinate2D(
                        currentSpace.getCoordinates().getX() + direction.getX(),
                        currentSpace.getCoordinates().getY() + direction.getY()
                );
                MemorySpace nextMemorySpace = coordinateToMemorySpaceMap.getOrDefault(nextCoord, null);
                if (nextMemorySpace == null) {
                    continue;
                }
                if (nextMemorySpace.getWallTimer() > wallTimerCutoff) {
                    long shortestPath = Math.min(
                            nextMemorySpace.getShortestPath(),
                            currentSpace.getShortestPath() + 1);
                    unvisitedMemorySpaces.remove(nextMemorySpace);
                    nextMemorySpace.setShortestPath(shortestPath);
                    unvisitedMemorySpaces.add(nextMemorySpace);
                }
            }
        }
        // no path to end
        return -1;
    }

    private void getInput() throws IOException {
        coordinateToMemorySpaceMap.clear();
        unvisitedMemorySpaces.clear();
        fallingBytes.clear();
        for (long y = 0; y <= MAX_COORDINATES.getY(); y++) {
            for (long x = 0; x <= MAX_COORDINATES.getX(); x++) {
                Coordinate2D coordinates = new Coordinate2D(x, y);
                MemorySpace memorySpace = new MemorySpace(coordinates);
                coordinateToMemorySpaceMap.put(coordinates, memorySpace);
                unvisitedMemorySpaces.add(memorySpace);
            }
        }
        InputStream inputStream = Day18.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            long counter = 1L;
            while ((line = reader.readLine()) != null) {
                fallingBytes.add(line);
                long[] coordinates = Arrays.stream(line.split(",")).mapToLong(Long::parseLong).toArray();
                coordinateToMemorySpaceMap.get(new Coordinate2D(coordinates[0], coordinates[1])).setWallTimer(counter++);
            }
        }
    }
}

