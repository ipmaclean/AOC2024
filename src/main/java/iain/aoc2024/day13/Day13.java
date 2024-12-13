package iain.aoc2024.day13;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Day13 {

    private static final String INPUT_FILE_NAME = "day13/input.txt";
    private List<ClawMachine> clawMachines = new ArrayList<>();

    public Day13() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        long solution = 0;
        for (ClawMachine clawMachine : clawMachines) {
            solution += clawMachine.solve();
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        long solution = 0;
        System.out.printf("The solution to part two is %s.%n", solution);
    }


    private void getInput() throws IOException {
        InputStream inputStream = Day13.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> clawInput = new ArrayList<>();
                do {
                    clawInput.add(line);
                }
                while ((line = reader.readLine()) != null && !line.isEmpty());
                getClawMachines().add(new ClawMachine(clawInput));
            }
        }
    }
}
