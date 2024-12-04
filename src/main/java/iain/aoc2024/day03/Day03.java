package iain.aoc2024.day03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {

    private static final String INPUT_FILE_NAME = "day03/input.txt";

    private Day03() {
        throw new IllegalStateException("Utility class");
    }

    public static void solve() throws IOException {
        solvePartOne();
        solvePartTwo();
    }

    private static List<String> getMemory() throws IOException {
        InputStream inputStream = Day03.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);
        List<String> listOfMemory = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                listOfMemory.add(line);
            }
        }
        return listOfMemory;
    }

    private static void solvePartOne() throws IOException {
        long solution = 0;
        List<String> listOfMemory = getMemory();
        for (String memory : listOfMemory) {
            Pattern pattern = Pattern.compile("mul\\(\\d+,\\d+\\)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(memory);
            while (matcher.find()) {
                String match = matcher.group();
                match = match.replace("mul(", "").replace(")", "");
                long[] values = Arrays.stream(match.split(",")).mapToLong(Long::parseLong).toArray();
                solution += values[0] * values[1];
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private static void solvePartTwo() throws IOException {
        long solution = 0;
        List<String> listOfMemory = getMemory();
        boolean mulEnabled = true;
        for (String memory : listOfMemory) {
            Pattern pattern = Pattern.compile("(mul\\(\\d+,\\d+\\))|(do\\(\\))|(don't\\(\\))", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(memory);
            while (matcher.find()) {
                String match = matcher.group();
                if (match.equals("do()")) {
                    mulEnabled = true;
                } else if (match.equals("don't()")) {
                    mulEnabled = false;
                } else if (mulEnabled) {
                    match = match.replace("mul(", "").replace(")", "");
                    long[] values = Arrays.stream(match.split(",")).mapToLong(Long::parseLong).toArray();
                    solution += values[0] * values[1];
                }
            }
        }
        System.out.printf("The solution to part two is %s.%n", solution);
    }
}
