package iain.aoc2024.day08;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class Day08 {

    private static final String INPUT_FILE_NAME = "day08/input.txt";
    private int maxX = 0;
    private int maxY = 0;
    private final List<Antenna> antennas = new ArrayList<>();
    private final HashSet<Character> frequencies = new HashSet<>();

    public Day08() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        HashSet<Point> antinodes = new HashSet<>();
        for (Character frequency : getFrequencies()) {
            List<Antenna> matchingAntennas = getAntennas().stream().
                    filter(x -> x.frequency() == frequency)
                    .toList();
            for (int i = 0; i < matchingAntennas.size() - 1; i++) {
                for (int j = i + 1; j < matchingAntennas.size(); j++) {
                    int xDiff = matchingAntennas.get(j).position().x - matchingAntennas.get(i).position().x;
                    int yDiff = matchingAntennas.get(j).position().y - matchingAntennas.get(i).position().y;
                    if (matchingAntennas.get(i).position().x + 2 * xDiff >= 0 &&
                            matchingAntennas.get(i).position().x + 2 * xDiff <= getMaxX() &&
                            matchingAntennas.get(i).position().y + 2 * yDiff >= 0 &&
                            matchingAntennas.get(i).position().y + 2 * yDiff <= getMaxY()
                    ) {
                        antinodes.add(new Point(
                                matchingAntennas.get(i).position().x + 2 * xDiff,
                                matchingAntennas.get(i).position().y + 2 * yDiff
                        ));
                    }
                    if (matchingAntennas.get(i).position().x - xDiff >= 0 &&
                            matchingAntennas.get(i).position().x - xDiff <= getMaxX() &&
                            matchingAntennas.get(i).position().y - yDiff >= 0 &&
                            matchingAntennas.get(i).position().y - yDiff <= getMaxY()
                    ) {
                        antinodes.add(new Point(
                                matchingAntennas.get(i).position().x - xDiff,
                                matchingAntennas.get(i).position().y - yDiff
                        ));
                    }
                }
            }
        }
        System.out.printf("The solution to part one is %s.%n", antinodes.size());
    }

    private void solvePartTwo() {
        HashSet<Point> antinodes = new HashSet<>();
        for (Character frequency : getFrequencies()) {
            List<Antenna> matchingAntennas = getAntennas().stream().
                    filter(x -> x.frequency() == frequency)
                    .toList();
            for (int i = 0; i < matchingAntennas.size() - 1; i++) {
                for (int j = i + 1; j < matchingAntennas.size(); j++) {
                    int xDiff = matchingAntennas.get(j).position().x - matchingAntennas.get(i).position().x;
                    int yDiff = matchingAntennas.get(j).position().y - matchingAntennas.get(i).position().y;
                    int distanceCounter = 0;
                    while (matchingAntennas.get(i).position().x + distanceCounter * xDiff >= 0 &&
                            matchingAntennas.get(i).position().x + distanceCounter * xDiff <= getMaxX() &&
                            matchingAntennas.get(i).position().y + distanceCounter * yDiff >= 0 &&
                            matchingAntennas.get(i).position().y + distanceCounter * yDiff <= getMaxY()
                    ) {
                        antinodes.add(new Point(
                                matchingAntennas.get(i).position().x + distanceCounter * xDiff,
                                matchingAntennas.get(i).position().y + distanceCounter * yDiff
                        ));
                        distanceCounter++;
                    }
                    distanceCounter = 1;
                    while (matchingAntennas.get(i).position().x - distanceCounter * xDiff >= 0 &&
                            matchingAntennas.get(i).position().x - distanceCounter * xDiff <= getMaxX() &&
                            matchingAntennas.get(i).position().y - distanceCounter * yDiff >= 0 &&
                            matchingAntennas.get(i).position().y - distanceCounter * yDiff <= getMaxY()
                    ) {
                        antinodes.add(new Point(
                                matchingAntennas.get(i).position().x - distanceCounter * xDiff,
                                matchingAntennas.get(i).position().y - distanceCounter * yDiff
                        ));
                        distanceCounter++;
                    }
                }
            }
        }
        System.out.printf("The solution to part two is %s.%n", antinodes.size());
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day08.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < line.length(); x++) {
                    setMaxX(Math.max(getMaxX(), x));
                    if (line.charAt(x) != '.') {
                        getAntennas().add(new Antenna(
                                new Point(x, y),
                                line.charAt(x)
                        ));
                        getFrequencies().add(line.charAt(x));
                    }
                }
                setMaxY(Math.max(getMaxY(), y));
                y++;
            }
        }
    }
}
