package iain.aoc2024.day22;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day22 {

    private static final String INPUT_FILE_NAME = "day22/input.txt";
    private final List<Long> secretNumbers = new ArrayList<>();

    public Day22() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        long solution = 0;
        for (long secretNumber : secretNumbers) {
            for (int i = 0; i < 2000; i++) {
                secretNumber = getNextSecretNumber(secretNumber);
            }
            solution += secretNumber;
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        long solution = 0;
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private long getNextSecretNumber(long secretNumber) {
        long nextSecretNumber = mix(secretNumber, secretNumber * 64);
        nextSecretNumber = prune(nextSecretNumber);
        nextSecretNumber = mix(nextSecretNumber, nextSecretNumber / 32);
        nextSecretNumber = prune(nextSecretNumber);
        nextSecretNumber = mix(nextSecretNumber, nextSecretNumber * 2048);
        return prune(nextSecretNumber);
    }

    private long mix(long left, long right) {
        return left ^ right;
    }

    private long prune(long input) {
        return input % 16777216;
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day22.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                secretNumbers.add(Long.parseLong(line));
            }
        }
    }
}
