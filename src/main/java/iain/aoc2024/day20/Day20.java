package iain.aoc2024.day20;

import iain.aoc2024.coordinates.Coordinate2D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class Day20 {

    private static final String INPUT_FILE_NAME = "day20/input.txt";
    private static final Coordinate2D[] DIRECTIONS = new Coordinate2D[]
            {
                    new Coordinate2D(1, 0),
                    new Coordinate2D(0, 1),
                    new Coordinate2D(-1, 0),
                    new Coordinate2D(0, -1),
            };

    private final List<String> map = new ArrayList<>();
    private Coordinate2D startingCoordinate;
    private Coordinate2D endingCoordinate;
    private final HashMap<Coordinate2D, Tile> coordinateToTileMap = new HashMap<>();
    private final TreeSet<Tile> unvisitedTiles = new TreeSet<>(new TileComparator());
    private HashMap<Coordinate2D, Long> shortestPathsFromStart = new HashMap<>();
    private HashMap<Coordinate2D, Long> shortestPathsFromEnd = new HashMap<>();

    public Day20() throws IOException {
        getMap();
        setStartAndEndCoords();
    }

    public void solve() {
        shortestPathsFromStart = getShortestPath(startingCoordinate, endingCoordinate);
        shortestPathsFromEnd = getShortestPath(endingCoordinate, startingCoordinate);
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        System.out.printf("The solution to part one is %s.%n", getSolution(2));
    }

    private void solvePartTwo() {
        System.out.printf("The solution to part two is %s.%n", getSolution(20));
    }

    private long getSolution(long maxCheatDistance) {
        long solution = 0;
        for (Coordinate2D startingCoord : shortestPathsFromStart.keySet()) {
            solution += getCheatCounts(startingCoord, maxCheatDistance);
        }
        return solution;
    }

    private long getCheatCounts(
            Coordinate2D startingCheatCoord,
            long maxCheatDistance) {
        long minimumCheatSaveToCount = 100;
        long totalCheatSavings = 0;
        long shortestNonCheatPath = shortestPathsFromStart.get(endingCoordinate);
        for (long y = startingCheatCoord.getY() - maxCheatDistance; y <= startingCheatCoord.getY() + maxCheatDistance; y++) {
            for (long x = startingCheatCoord.getX() - maxCheatDistance; x <= startingCheatCoord.getX() + maxCheatDistance; x++) {
                long xDiff = Math.abs(startingCheatCoord.getX() - x);
                long yDiff = Math.abs(startingCheatCoord.getY() - y);
                if (xDiff + yDiff < 2 ||
                        xDiff + yDiff > maxCheatDistance ||
                        !shortestPathsFromEnd.containsKey(new Coordinate2D(x, y))
                ) {
                    continue;
                }
                long shortestPathWithCheat = shortestPathsFromStart.get(startingCheatCoord) +
                        xDiff + yDiff +
                        shortestPathsFromEnd.get(new Coordinate2D(x, y));
                if (shortestPathWithCheat < shortestNonCheatPath) {
                    long savingsWithCheat = shortestNonCheatPath - shortestPathWithCheat;
                    totalCheatSavings = savingsWithCheat >= minimumCheatSaveToCount ?
                            totalCheatSavings + 1 :
                            totalCheatSavings;
                }
            }
        }
        return totalCheatSavings;
    }


    private HashMap<Coordinate2D, Long> getShortestPath(Coordinate2D startingCoord, Coordinate2D endingCoord) {
        getInput();
        HashMap<Coordinate2D, Long> coordsToShortestPaths = new HashMap<>();

        Tile startingTile = coordinateToTileMap.get(startingCoord);
        unvisitedTiles.remove(startingTile);
        startingTile.setShortestPath(0);
        unvisitedTiles.add(startingTile);
        while (!unvisitedTiles.isEmpty()) {
            Tile currentSpace = unvisitedTiles.pollFirst();
            if (currentSpace == null || currentSpace.getShortestPath() == Long.MAX_VALUE) {
                // no path to end
                return coordsToShortestPaths;
            }
            coordsToShortestPaths.put(currentSpace.getCoordinates(), currentSpace.getShortestPath());
            coordinateToTileMap.remove(currentSpace.getCoordinates());
            if (currentSpace.getCoordinates().equals(endingCoord)) {
                return coordsToShortestPaths;
            }
            for (Coordinate2D direction : DIRECTIONS) {
                Coordinate2D nextCoord = new Coordinate2D(
                        currentSpace.getCoordinates().getX() + direction.getX(),
                        currentSpace.getCoordinates().getY() + direction.getY()
                );
                Tile nextTile = coordinateToTileMap.getOrDefault(nextCoord, null);
                if (nextTile == null) {
                    continue;
                }
                long shortestPath = Math.min(
                        nextTile.getShortestPath(),
                        currentSpace.getShortestPath() + 1);
                unvisitedTiles.remove(nextTile);
                nextTile.setShortestPath(shortestPath);
                unvisitedTiles.add(nextTile);
            }
        }
        // no path to end
        return coordsToShortestPaths;
    }

    private void getMap() throws IOException {
        InputStream inputStream = Day20.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                map.add(line);
            }
        }
    }

    private void getInput() {
        coordinateToTileMap.clear();
        unvisitedTiles.clear();
        int y = 0;
        for (String line : map) {
            for (int x = 0; x < line.length(); x++) {
                char tileChar = line.charAt(x);
                if (tileChar == '#') {
                    continue;
                }
                Coordinate2D coord = new Coordinate2D(x, y);
                Tile tile = new Tile(coord);
                coordinateToTileMap.put(coord, tile);
                unvisitedTiles.add(tile);
            }
            y++;
        }
    }

    private void setStartAndEndCoords() {
        int y = 0;
        for (String line : map) {
            int xStart = line.indexOf('S');
            int xEnd = line.indexOf('E');
            if (xStart != -1) {
                startingCoordinate = new Coordinate2D(xStart, y);
            }
            if (xEnd != -1) {
                endingCoordinate = new Coordinate2D(xEnd, y);
            }
            y++;
        }
    }
}
