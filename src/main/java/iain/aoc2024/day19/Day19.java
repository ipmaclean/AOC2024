package iain.aoc2024.day19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day19 {

    private static final String INPUT_FILE_NAME = "day19/input.txt";

    private String[] patterns = new String[0];
    private final List<String> designs = new ArrayList<>();
    private final HashMap<String, Long> designsToWaysToMake = new HashMap<>();

    public Day19() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        long solution = 0;
        for (String design : designs) {
            if (waysToMakeDesign(design) > 0) {
                solution++;
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        long solution = 0;
        for (String design : designs) {
            solution += waysToMakeDesign(design);
        }
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private long waysToMakeDesign(String design) {
        if (design.isEmpty()) {
            return 1;
        }
        if (designsToWaysToMake.containsKey(design)) {
            return designsToWaysToMake.get(design);
        }

        long waysToMake = 0;
        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                waysToMake += waysToMakeDesign(design.substring(pattern.length()));
            }
        }
        designsToWaysToMake.put(design, waysToMake);
        return waysToMake;
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day19.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            patterns = reader.readLine().split(", ");
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                designs.add(line);
            }
        }
    }
}
