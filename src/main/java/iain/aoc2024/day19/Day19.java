package iain.aoc2024.day19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day19 {

    private static final String INPUT_FILE_NAME = "day19/input.txt";

    private String[] patterns = new String[0];
    private final List<String> designs = new ArrayList<>();
    private final HashSet<String> possibleDesigns = new HashSet<>();
    private final HashSet<String> impossibleDesigns = new HashSet<>();

    public Day19() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        possibleDesigns.clear();
        impossibleDesigns.clear();
        int solution = 0;
        for (String design : designs) {
            if (canMakeDesign(design)) {
                solution++;
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        System.out.printf("The solution to part two is %s.%n", 0);
    }

    private boolean canMakeDesign(String design) {
        if (design.isEmpty() || possibleDesigns.contains(design)) {
            return true;
        }
        if (impossibleDesigns.contains(design)) {
            return false;
        }
        boolean canMakeDesign = false;
        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                canMakeDesign = canMakeDesign(design.substring(pattern.length())) || canMakeDesign;
            }
            if (canMakeDesign) {
                possibleDesigns.add(design);
                break;
            }
        }
        if (!canMakeDesign) {
            impossibleDesigns.add(design);
        }
        return canMakeDesign;
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
