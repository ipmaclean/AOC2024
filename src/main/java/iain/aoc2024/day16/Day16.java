package iain.aoc2024.day16;

import iain.aoc2024.coordinates.Coordinate2D;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

@Getter
@Setter
public class Day16 {

    private static final String INPUT_FILE_NAME = "day16/input.txt";
    private static final Coordinate2D[] DIRECTIONS = new Coordinate2D[]
            {
                    new Coordinate2D(1, 0),
                    new Coordinate2D(0, 1),
                    new Coordinate2D(-1, 0),
                    new Coordinate2D(0, -1),
            };

    private Coordinate2D startingPosition = new Coordinate2D(-1, -1);
    private Coordinate2D endingPosition = new Coordinate2D(-1, -1);
    private List<String> maze = new ArrayList<>();

    public Day16() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        System.out.printf("The solution to part one is %s.%n", getLowestScore());
    }

    private void solvePartTwo() {
        long solution = 0;
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private long getLowestScore() {
        TreeSet<ReindeerStateWithScore> statesToVisit = new TreeSet<>(new ReindeerStateComparator());
        HashSet<ReindeerState> visitedStates = new HashSet<>();
        statesToVisit.add(new ReindeerStateWithScore(getStartingPosition(), 0, 0L));
        visitedStates.add(new ReindeerState(getStartingPosition(), 0));
        // BFS ordered by lowest score, discard any paths
        // where you have already seen the same position
        // and direction with a lower score.
        while (!statesToVisit.isEmpty()) {
            ReindeerStateWithScore currentState = statesToVisit.getFirst();
            statesToVisit.remove(currentState);
            Coordinate2D forwardPosition = new Coordinate2D(
                    currentState.position().getX() + DIRECTIONS[currentState.directionIndex()].getX(),
                    currentState.position().getY() + DIRECTIONS[currentState.directionIndex()].getY()
            );
            if (forwardPosition.equals(getEndingPosition())) {
                return currentState.score() + 1;
            }
            char forwardTile = getMaze().get((int) forwardPosition.getY()).charAt((int) forwardPosition.getX());
            ReindeerState forwardState = new ReindeerState(
                    forwardPosition,
                    currentState.directionIndex()
            );
            if (forwardTile != '#' && !visitedStates.contains(forwardState)) {
                statesToVisit.add(new ReindeerStateWithScore(
                        forwardPosition,
                        currentState.directionIndex(),
                        currentState.score() + 1
                ));
                visitedStates.add(forwardState);
            }
            for (int i = 1; i < DIRECTIONS.length; i += DIRECTIONS.length / 2) {
                ReindeerState turnState = new ReindeerState(
                        currentState.position(),
                        (currentState.directionIndex() + i) % DIRECTIONS.length
                );
                if (!visitedStates.contains(turnState)) {
                    statesToVisit.add(new ReindeerStateWithScore(
                            currentState.position(),
                            (currentState.directionIndex() + i) % DIRECTIONS.length,
                            currentState.score() + 1000
                    ));
                    visitedStates.add(turnState);
                }
            }
        }
        return -1;
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day16.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            long y = 0;
            while ((line = reader.readLine()) != null) {
                getMaze().add(line);
                if (line.contains("S")) {
                    setStartingPosition(new Coordinate2D(line.indexOf("S"), y));
                }
                if (line.contains("E")) {
                    setEndingPosition(new Coordinate2D(line.indexOf("E"), y));
                }
                y++;
            }
        }
    }
}
