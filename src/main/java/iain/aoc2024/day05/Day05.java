package iain.aoc2024.day05;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Getter
public class Day05 {

    private static final String INPUT_FILE_NAME = "day05/input.txt";
    private final List<int[]> rules = new ArrayList<>();
    private final List<int[]> updates = new ArrayList<>();

    public Day05() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day05.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                rules.add(Arrays.stream(line.split("\\|")).mapToInt(Integer::parseInt).toArray());
            }
            while ((line = reader.readLine()) != null) {
                updates.add(Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray());
            }
        }
    }

    private void solvePartOne() {
        long solution = 0;
        for (int[] update : getUpdates()) {
            int[] orderedUpdate = orderUpdate(update);
            if (Arrays.equals(orderedUpdate, update)) {
                solution += orderedUpdate[orderedUpdate.length / 2];
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        long solution = 0;
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private int[] orderUpdate(int[] update) {
        int[] orderedUpdate = update.clone();
        bubbleSort(orderedUpdate);
        return orderedUpdate;
    }

    private void bubbleSort(int[] arr) {
        int n = arr.length;
        int temp = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (shouldSwap(arr[j - 1], arr[j])) {
                    temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    private boolean shouldSwap(int left, int right) {
        int[] matchingRule = getRules().stream()
                .filter(x -> IntStream.of(x).anyMatch(y -> y == left) && IntStream.of(x).anyMatch(y -> y == right))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Multiple elements: " + a + ", " + b);
                })
                .get();
        return matchingRule[0] == right;
    }
}
