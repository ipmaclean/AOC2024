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
    private Coordinate2D startingCoordinate = new Coordinate2D(-1, -1);
    private Coordinate2D endingCoordinate = new Coordinate2D(-1, -1);
    private final HashMap<Coordinate2D, Tile> coordinateToTileMap = new HashMap<>();
    private final TreeSet<Tile> unvisitedTiles = new TreeSet<>(new TileComparator());

    public Day20() throws IOException {
        getMap();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        long shortestPathNoCheat = getShortestPath(null, 0);
        List<Long> shortestPathsWithCheats = new ArrayList<>();
        for (int y = 0; y < map.size() - 1; y++) {
            for (int x = 0; x < map.getFirst().length() - 1; x++) {
                for (int direction = 0; direction < 4; direction++) {
                    long shortestPathWithCheat = getShortestPath(new Coordinate2D(x, y), direction);
                    if (shortestPathWithCheat != -1) {
                        shortestPathsWithCheats.add(shortestPathWithCheat);
                    }
                }
            }
        }
        long cheatsThatSaveAtLeast100 = shortestPathsWithCheats.stream()
                .filter(x -> x < shortestPathNoCheat)
                .map(x -> shortestPathNoCheat - x)
                .filter(x -> x >= 100)
                .count();
        System.out.printf("The solution to part one is %s.%n", cheatsThatSaveAtLeast100);
    }


    private void solvePartTwo() {
        System.out.printf("The solution to part two is %s.%n", 0);
    }

    private long getShortestPath(Coordinate2D cheatStart, int cheatDirectionIndex) {
        Coordinate2D cheatMiddle;
        if (cheatStart != null) {
            cheatMiddle = new Coordinate2D(
                    cheatStart.getX() + DIRECTIONS[cheatDirectionIndex].getX(),
                    cheatStart.getY() + DIRECTIONS[cheatDirectionIndex].getY()
            );
            Coordinate2D cheatEnd = new Coordinate2D(
                    cheatStart.getX() + 2 * DIRECTIONS[cheatDirectionIndex].getX(),
                    cheatStart.getY() + 2 * DIRECTIONS[cheatDirectionIndex].getY()
            );
            if (cheatEnd.getY() < 0 || cheatEnd.getY() >= map.size() ||
                    cheatEnd.getX() < 0 || cheatEnd.getX() >= map.getFirst().length() ||
                    map.get((int) cheatStart.getY()).charAt((int) cheatStart.getX()) == '#' ||
                    map.get((int) cheatEnd.getY()).charAt((int) cheatEnd.getX()) == '#' ||
                    map.get((int) cheatMiddle.getY()).charAt((int) cheatMiddle.getX()) == '.'
            ) {
                return -1;
            }
        }
        getInput();
        Tile startingTile = coordinateToTileMap.get(startingCoordinate);
        unvisitedTiles.remove(startingTile);
        startingTile.setShortestPath(0);
        unvisitedTiles.add(startingTile);
        while (!unvisitedTiles.isEmpty()) {
            Tile currentSpace = unvisitedTiles.pollFirst();
            if (currentSpace == null || currentSpace.getShortestPath() == Long.MAX_VALUE) {
                // no path to end
                return -1;
            }
            coordinateToTileMap.remove(currentSpace.getCoordinates());
            if (currentSpace.getCoordinates().equals(endingCoordinate)) {
                return currentSpace.getShortestPath();
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
                if (!nextTile.isWall()) {
                    long shortestPath = Math.min(
                            nextTile.getShortestPath(),
                            currentSpace.getShortestPath() + 1);
                    unvisitedTiles.remove(nextTile);
                    nextTile.setShortestPath(shortestPath);
                    unvisitedTiles.add(nextTile);
                }
            }
            if (currentSpace.getCoordinates().equals(cheatStart)) {
                Coordinate2D cheatEnd = new Coordinate2D(
                        cheatStart.getX() + 2 * DIRECTIONS[cheatDirectionIndex].getX(),
                        cheatStart.getY() + 2 * DIRECTIONS[cheatDirectionIndex].getY()
                );
                Tile nextTile = coordinateToTileMap.getOrDefault(cheatEnd, null);
                if (nextTile != null) {
                    long shortestPath = Math.min(
                            nextTile.getShortestPath(),
                            currentSpace.getShortestPath() + 2);
                    unvisitedTiles.remove(nextTile);
                    nextTile.setShortestPath(shortestPath);
                    unvisitedTiles.add(nextTile);
                }
            }
        }
        // no path to end
        return -1;
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
                Coordinate2D coord = new Coordinate2D(x, y);
                Tile tile = new Tile(coord, tileChar == '#');
                coordinateToTileMap.put(coord, tile);
                unvisitedTiles.add(tile);
                if (tileChar == 'S') {
                    startingCoordinate = new Coordinate2D(x, y);
                }
                if (tileChar == 'E') {
                    endingCoordinate = new Coordinate2D(x, y);
                }
            }
            y++;
        }
    }
}
