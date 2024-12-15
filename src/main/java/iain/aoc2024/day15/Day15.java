package iain.aoc2024.day15;

import iain.aoc2024.coordinates.Coordinate2D;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static java.util.Map.entry;

@Getter
public class Day15 {

    private static final String INPUT_FILE_NAME = "day15/input.txt";
    private static final HashMap<Character, Coordinate2D> DIRECTIONS = new HashMap<>(
            Map.ofEntries(
                    entry('^', new Coordinate2D(0, -1)),
                    entry('>', new Coordinate2D(1, 0)),
                    entry('v', new Coordinate2D(0, 1)),
                    entry('<', new Coordinate2D(-1, 0))
            )
    );

    private final LinkedHashMap<Coordinate2D, Tile> warehouseMap = new LinkedHashMap<>();
    private final List<String> movements = new ArrayList<>();
    @Setter
    private Coordinate2D startingPosition = new Coordinate2D(-1, -1);

    public Day15() {
        // getInput is called in the solve methods to ensure a clean start
    }

    public void solve() throws IOException {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() throws IOException {
        getInput();
        Coordinate2D currentPosition = new Coordinate2D(getStartingPosition());
        for (String line : getMovements()) {
            for (int i = 0; i < line.length(); i++) {
                move(currentPosition, DIRECTIONS.get(line.charAt(i)));
            }
        }
        long solution = 0;
        for (Map.Entry<Coordinate2D, Tile> coordinate2DTileEntry : getWarehouseMap().entrySet()) {
            if (coordinate2DTileEntry.getValue().isBox()) {
                solution += 100 * coordinate2DTileEntry.getKey().getY() + coordinate2DTileEntry.getKey().getX();
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void move(Coordinate2D currentPosition, Coordinate2D direction) {
        Tile nextTile = getWarehouseMap().get(new Coordinate2D(
                currentPosition.getX() + direction.getX(),
                currentPosition.getY() + direction.getY()
        ));
        if (nextTile.isWall()) {
            return;
        }
        boolean canMove = false;
        if (!nextTile.isBox()) {
            canMove = true;
        } else {
            canMove = tryMoveBox(new Coordinate2D(
                            currentPosition.getX() + direction.getX(),
                            currentPosition.getY() + direction.getY()
                    ),
                    direction,
                    nextTile);
        }
        if (canMove) {
            currentPosition.set(
                    currentPosition.getX() + direction.getX(),
                    currentPosition.getY() + direction.getY()
            );
        }
    }

    private boolean tryMoveBox(Coordinate2D currentPosition, Coordinate2D direction, Tile currentTile) {
        Tile nextTile = getWarehouseMap().get(new Coordinate2D(
                currentPosition.getX() + direction.getX(),
                currentPosition.getY() + direction.getY()
        ));
        if (nextTile.isBox()) {
            tryMoveBox(new Coordinate2D(
                            currentPosition.getX() + direction.getX(),
                            currentPosition.getY() + direction.getY()
                    ),
                    direction,
                    nextTile);
        }
        if (nextTile.isWall()) {
            return false;
        }
        if (!nextTile.isBox()) {
            currentTile.setBox(false);
            nextTile.setBox(true);
            return true;
        }
        return false;
    }

    private void solvePartTwo() throws IOException {
        getInput();
        long solution = 0;
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private void getInput() throws IOException {
        getWarehouseMap().clear();
        getMovements().clear();
        InputStream inputStream = Day15.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int y = 0;
            while (!(line = reader.readLine()).isEmpty()) {
                for (int x = 0; x < line.length(); x++) {
                    char tileChar = line.charAt(x);
                    getWarehouseMap().put(new Coordinate2D(x, y), new Tile(tileChar == '#', tileChar == 'O'));
                    if (tileChar == '@') {
                        setStartingPosition(new Coordinate2D(x, y));
                    }
                }
                y++;
            }
            while ((line = reader.readLine()) != null) {
                getMovements().add(line);
            }
        }
    }
}
