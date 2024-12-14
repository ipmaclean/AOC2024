package iain.aoc2024.day14;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Day14 {

    private static final String INPUT_FILE_NAME = "day14/input.txt";
    private static final Point MAX = new Point(101, 103);

    private List<Robot> robots = new ArrayList<>();

    public Day14() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        int[] quadrants = new int[]{0, 0, 0, 0};
        for (Robot robot : getRobots()) {
            Integer quadrant = robot.getQuadrantAfterTime(100);
            if (quadrant != null) {
                quadrants[quadrant]++;
            }
        }
        System.out.printf(
                "The solution to part one is %s.%n",
                Arrays.stream(quadrants).reduce(1, (subtotal, element) -> subtotal * element)
        );
    }

    private void solvePartTwo() {
        System.out.printf("The solution to part two is %s.%n", 0);
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day14.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                getRobots().add(new Robot(line, MAX));
            }
        }
    }
}
