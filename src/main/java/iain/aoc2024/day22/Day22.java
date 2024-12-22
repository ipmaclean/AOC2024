package iain.aoc2024.day22;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
        System.out.printf("The solution to part two is %s.%n", getMaxBananas());
    }

    private long getMaxBananas() {
        HashMap<List<Long>, Long> sequencesToBananas = new HashMap<>();
        for (long secretNumber : secretNumbers) {
            HashSet<List<Long>> seenSequences = new HashSet<>();
            long lastPrice = secretNumber % 10;

            secretNumber = getNextSecretNumber(secretNumber);
            long currentPrice = secretNumber % 10;
            long diff1 = currentPrice - lastPrice;
            lastPrice = currentPrice;

            secretNumber = getNextSecretNumber(secretNumber);
            currentPrice = secretNumber % 10;
            long diff2 = currentPrice - lastPrice;
            lastPrice = currentPrice;

            secretNumber = getNextSecretNumber(secretNumber);
            currentPrice = secretNumber % 10;
            long diff3 = currentPrice - lastPrice;
            lastPrice = currentPrice;

            for (int i = 0; i < 2000 - 3; i++) {
                secretNumber = getNextSecretNumber(secretNumber);
                currentPrice = secretNumber % 10;
                long diff4 = currentPrice - lastPrice;

                List<Long> diffList = List.of(diff1, diff2, diff3, diff4);
                if (!seenSequences.contains(diffList)) {
                    long runningBananaTotal = sequencesToBananas.getOrDefault(diffList, 0L);
                    sequencesToBananas.put(diffList, runningBananaTotal + currentPrice);
                }
                seenSequences.add(diffList);
                lastPrice = currentPrice;
                diff1 = diff2;
                diff2 = diff3;
                diff3 = diff4;
            }
        }
        return sequencesToBananas.values().stream().max(Long::compare).get();
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
