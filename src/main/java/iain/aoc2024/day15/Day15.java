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

    public void solve() throws IOException {
        // Not proud of this one! Bit of a hot mess - but it works!
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() throws IOException {
        getInputPartOne();
        Coordinate2D currentPosition = new Coordinate2D(getStartingPosition());
        for (String line : getMovements()) {
            for (int i = 0; i < line.length(); i++) {
                movePartOne(currentPosition, DIRECTIONS.get(line.charAt(i)));
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

    private void movePartOne(Coordinate2D currentPosition, Coordinate2D direction) {
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
            canMove = tryMoveBoxPartOne(new Coordinate2D(
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

    private boolean tryMoveBoxPartOne(Coordinate2D currentPosition, Coordinate2D direction, Tile currentTile) {
        Tile nextTile = getWarehouseMap().get(new Coordinate2D(
                currentPosition.getX() + direction.getX(),
                currentPosition.getY() + direction.getY()
        ));
        if (nextTile.isBox()) {
            tryMoveBoxPartOne(new Coordinate2D(
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
        getInputPartTwo();
        Coordinate2D currentPosition = new Coordinate2D(getStartingPosition());
        for (String line : getMovements()) {
            for (int i = 0; i < line.length(); i++) {
                movePartTwo(currentPosition, DIRECTIONS.get(line.charAt(i)));
                //printPretty(currentPosition, line.charAt(i));
            }
        }
        long solution = 0;
        for (Map.Entry<Coordinate2D, Tile> coordinate2DTileEntry : getWarehouseMap().entrySet()) {
            if (coordinate2DTileEntry.getValue().isBoxLeft()) {
                solution += 100 * coordinate2DTileEntry.getKey().getY() + coordinate2DTileEntry.getKey().getX();
            }
        }
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private void printPretty(Coordinate2D currentPosition, char direction) {
        StringBuilder sb = new StringBuilder();
        Map.Entry<Coordinate2D, Tile> previousCoordinate2DTileEntry = null;
        sb.append(direction);
        sb.append(System.lineSeparator());
        for (Map.Entry<Coordinate2D, Tile> coordinate2DTileEntry : getWarehouseMap().entrySet()) {
            if (previousCoordinate2DTileEntry != null &&
                    previousCoordinate2DTileEntry.getKey().getY() != coordinate2DTileEntry.getKey().getY()) {
                sb.append(System.lineSeparator());
            }
            Tile tile = coordinate2DTileEntry.getValue();
            char tileCharacter;
            if (tile.isWall()) {
                tileCharacter = '#';
            } else if (tile.isBox()) {
                tileCharacter = 'O';
            } else if (tile.isBoxLeft()) {
                tileCharacter = '[';
            } else if (tile.isBoxRight()) {
                tileCharacter = ']';
            } else if (coordinate2DTileEntry.getKey().equals(currentPosition)) {
                tileCharacter = '@';
            } else {
                tileCharacter = '.';
            }
            sb.append(tileCharacter);
            previousCoordinate2DTileEntry = coordinate2DTileEntry;
        }
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());

        System.out.print(sb);
    }

    private void movePartTwo(Coordinate2D currentPosition, Coordinate2D direction) {
        Tile nextTile = getWarehouseMap().get(new Coordinate2D(
                currentPosition.getX() + direction.getX(),
                currentPosition.getY() + direction.getY()
        ));
        if (nextTile.isWall()) {
            return;
        }
        boolean canMove;
        if (!nextTile.isBoxLeft() && !nextTile.isBoxRight()) {
            canMove = true;
        } else {
            if (direction.getX() != 0) {
                canMove = tryMoveBoxPartTwoHorizontal(new Coordinate2D(
                                currentPosition.getX() + direction.getX(),
                                currentPosition.getY()
                        ),
                        direction,
                        nextTile);
            } else {
                canMove = checkVerticalBoxCanMove(new Coordinate2D(
                                currentPosition.getX(),
                                currentPosition.getY() + direction.getY()
                        ),
                        direction,
                        nextTile);
                if (canMove) {
                    moveBoxPartTwoVertical(new Coordinate2D(
                                    currentPosition.getX(),
                                    currentPosition.getY() + direction.getY()
                            ),
                            direction,
                            nextTile);
                }
            }
        }
        if (canMove) {
            currentPosition.set(
                    currentPosition.getX() + direction.getX(),
                    currentPosition.getY() + direction.getY()
            );
        }
    }

    private boolean tryMoveBoxPartTwoHorizontal(Coordinate2D currentPosition, Coordinate2D direction, Tile currentTile) {
        Tile nextTile = getWarehouseMap().get(new Coordinate2D(
                currentPosition.getX() + 2L * direction.getX(),
                currentPosition.getY()
        ));
        if (nextTile.isBoxLeft() || nextTile.isBoxRight()) {
            tryMoveBoxPartTwoHorizontal(new Coordinate2D(
                            currentPosition.getX() + 2L * direction.getX(),
                            currentPosition.getY()
                    ),
                    direction,
                    nextTile);
        }
        if (nextTile.isWall()) {
            return false;
        }
        if (!nextTile.isBoxLeft() && !nextTile.isBoxRight()) {
            Tile otherBoxTile = getWarehouseMap().get(new Coordinate2D(
                    currentPosition.getX() + direction.getX(),
                    currentPosition.getY()
            ));
            otherBoxTile.setBoxLeft(currentTile.isBoxLeft());
            otherBoxTile.setBoxRight(currentTile.isBoxRight());
            nextTile.setBoxLeft(!currentTile.isBoxLeft());
            nextTile.setBoxRight(!currentTile.isBoxRight());
            currentTile.setBoxLeft(false);
            currentTile.setBoxRight(false);
            return true;
        }
        return false;
    }

    private boolean checkVerticalBoxCanMove(Coordinate2D currentPosition, Coordinate2D direction, Tile currentTile) {
        if (!currentTile.isBoxLeft() && !currentTile.isBoxRight()) {
            return true;
        }
        Coordinate2D nextPosition = new Coordinate2D(
                currentPosition.getX(),
                currentPosition.getY() + direction.getY()
        );
        Tile nextTile = getWarehouseMap().get(nextPosition);
        Coordinate2D otherNextPosition = new Coordinate2D(
                currentPosition.getX() + (currentTile.isBoxLeft() ? 1L : -1L),
                currentPosition.getY() + direction.getY()
        );
        Tile otherNextTile = getWarehouseMap().get(otherNextPosition);
        if (nextTile.isWall() || otherNextTile.isWall()) {
            return false;
        }
        if (nextTile.isBoxLeft() || nextTile.isBoxRight() ||
                otherNextTile.isBoxLeft() || otherNextTile.isBoxRight()) {
            return checkVerticalBoxCanMove(
                    nextPosition,
                    direction,
                    nextTile
            ) &&
                    checkVerticalBoxCanMove(
                            otherNextPosition,
                            direction,
                            otherNextTile
                    );

        }
        return true;
    }


    private void moveBoxPartTwoVertical(Coordinate2D currentPosition, Coordinate2D direction, Tile currentTile) {
        Coordinate2D nextPosition = new Coordinate2D(
                currentPosition.getX(),
                currentPosition.getY() + direction.getY()
        );
        Tile nextTile = getWarehouseMap().get(nextPosition);
        Coordinate2D otherNextPosition = new Coordinate2D(
                currentPosition.getX() + (currentTile.isBoxLeft() ? 1L : -1L),
                currentPosition.getY() + direction.getY()
        );
        Tile otherNextTile = getWarehouseMap().get(otherNextPosition);
        if (nextTile.isBoxLeft() || nextTile.isBoxRight()) {
            moveBoxPartTwoVertical(nextPosition, direction, nextTile);
        }
        if (otherNextTile.isBoxLeft() || otherNextTile.isBoxRight()) {
            moveBoxPartTwoVertical(otherNextPosition, direction, otherNextTile);
        }
        Tile otherCurrentBoxTile = getWarehouseMap().get(new Coordinate2D(
                currentPosition.getX() + (currentTile.isBoxLeft() ? 1L : -1L),
                currentPosition.getY()
        ));

        nextTile.setBoxLeft(currentTile.isBoxLeft());
        nextTile.setBoxRight(currentTile.isBoxRight());
        otherNextTile.setBoxLeft(!currentTile.isBoxLeft());
        otherNextTile.setBoxRight(!currentTile.isBoxRight());

        otherCurrentBoxTile.setBoxLeft(false);
        otherCurrentBoxTile.setBoxRight(false);
        currentTile.setBoxLeft(false);
        currentTile.setBoxRight(false);
    }

    private void getInputPartOne() throws IOException {
        getWarehouseMap().clear();
        getMovements().clear();
        InputStream inputStream = Day15.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int y = 0;
            while (!(line = reader.readLine()).isEmpty()) {
                for (int x = 0; x < line.length(); x++) {
                    char tileChar = line.charAt(x);
                    getWarehouseMap().put(new Coordinate2D(x, y), new Tile(
                            tileChar == '#', tileChar == 'O', false, false)
                    );
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

    private void getInputPartTwo() throws IOException {
        getWarehouseMap().clear();
        getMovements().clear();
        InputStream inputStream = Day15.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int y = 0;
            while (!(line = reader.readLine()).isEmpty()) {
                for (int x = 0; x < line.length(); x++) {
                    char tileChar = line.charAt(x);
                    getWarehouseMap().put(new Coordinate2D(2L * x, y), new Tile(
                            tileChar == '#', false, tileChar == 'O', false
                    ));
                    getWarehouseMap().put(new Coordinate2D(2L * x + 1L, y), new Tile(
                            tileChar == '#', false, false, tileChar == 'O'
                    ));
                    if (tileChar == '@') {
                        setStartingPosition(new Coordinate2D(2L * x, y));
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
