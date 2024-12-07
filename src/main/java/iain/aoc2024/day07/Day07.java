package iain.aoc2024.day07;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Day07 {

    private static final String INPUT_FILE_NAME = "day07/input.txt";
    private final List<Calibration> calibrations = new ArrayList<>();

    public Day07() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        long solution = 0;
        for (Calibration calibration : getCalibrations()) {
            if (calibration.canInsertOperators(false)) {
                solution += calibration.getTestValue();
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        long solution = 0;
        for (Calibration calibration : getCalibrations()) {
            if (calibration.canInsertOperators(true)) {
                solution += calibration.getTestValue();
            }
        }
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private void getInput() throws IOException {
        InputStream inputStream = iain.aoc2024.day07.Day07.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                getCalibrations().add(new Calibration(line));
            }
        }
    }
}
