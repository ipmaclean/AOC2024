package iain.aoc2024.day16;

import iain.aoc2024.coordinates.Coordinate2D;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

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
        System.out.printf("The solution to part two is %s.%n", getCountOfGoodSeats(getLowestScore()));
    }

    private Object getCountOfGoodSeats(long lowestScore) {
        HashSet<Coordinate2D> bestSeats = new HashSet<>();
        bestSeats.add(getEndingPosition());

        Queue<ReindeerStateWithScoreAndVisited> statesToVisit = new ArrayDeque<>();
        HashSet<ReindeerState> startingVisited = new HashSet<>();
        startingVisited.add(new ReindeerState(getStartingPosition(), 0));

        HashMap<ReindeerState, Long> visitedStatesToScore = new HashMap<>();
        visitedStatesToScore.put(new ReindeerState(getStartingPosition(), 0), 0L);

        statesToVisit.add(new ReindeerStateWithScoreAndVisited(getStartingPosition(), 0, 0L, startingVisited));
        // BFS, with each state tracking its previously visited states.
        // Discard any paths where:
        // you have already seen the same position and direction within the current state's history OR
        // lowestScore is exceeded OR
        // the current score for the position exceeds the minimum seen score for that position in any state
        while (!statesToVisit.isEmpty()) {
            ReindeerStateWithScoreAndVisited currentState = statesToVisit.poll();
            Coordinate2D forwardPosition = new Coordinate2D(
                    currentState.position().getX() + DIRECTIONS[currentState.directionIndex()].getX(),
                    currentState.position().getY() + DIRECTIONS[currentState.directionIndex()].getY()
            );
            if (forwardPosition.equals(getEndingPosition())) {
                bestSeats.addAll(currentState.visited().stream().map(ReindeerState::position).collect(Collectors.toSet()));
                continue;
            }

            char forwardTile = getMaze().get((int) forwardPosition.getY()).charAt((int) forwardPosition.getX());
            ReindeerState forwardState = new ReindeerState(
                    forwardPosition,
                    currentState.directionIndex()
            );
            if (forwardTile != '#' &&
                    !currentState.visited().contains(forwardState) &&
                    visitedStatesToScore.getOrDefault(forwardState, Long.MAX_VALUE) >= currentState.score() + 1 &&
                    currentState.score() + 1 < lowestScore) {
                HashSet<ReindeerState> newVisited = new HashSet<>(currentState.visited());
                newVisited.add(forwardState);
                statesToVisit.add(new ReindeerStateWithScoreAndVisited(
                        forwardPosition,
                        currentState.directionIndex(),
                        currentState.score() + 1,
                        newVisited
                ));
                visitedStatesToScore.put(forwardState, currentState.score() + 1);
            }

            for (int i = 1; i < DIRECTIONS.length; i += DIRECTIONS.length / 2) {
                ReindeerState turnState = new ReindeerState(
                        currentState.position(),
                        (currentState.directionIndex() + i) % DIRECTIONS.length
                );
                if (!currentState.visited().contains(turnState) &&
                        visitedStatesToScore.getOrDefault(turnState, Long.MAX_VALUE) >= currentState.score() + 1000 &&
                        currentState.score() + 1000 < lowestScore) {
                    HashSet<ReindeerState> newVisited = new HashSet<>(currentState.visited());
                    newVisited.add(turnState);
                    statesToVisit.add(new ReindeerStateWithScoreAndVisited(
                            currentState.position(),
                            (currentState.directionIndex() + i) % DIRECTIONS.length,
                            currentState.score() + 1000,
                            newVisited)
                    );
                    visitedStatesToScore.put(turnState, currentState.score() + 1000);
                }
            }
        }
        return bestSeats.size();
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
        throw new IllegalStateException("Could not find lowest score.");
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
