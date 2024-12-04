package iain.aoc2024.day02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day02 {

    private static final String INPUT_FILE_NAME = "day02/input.txt";

    private Day02() {
        throw new IllegalStateException("Utility class");
    }

    public static void solve() throws IOException {
        solvePartOne();
//        solvePartTwo();
    }

    private static List<List<Long>> getListOfLevels() throws IOException {
        InputStream inputStream = Day02.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);
        List<List<Long>> listOfLevels = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<Long> levels = Arrays.stream(line.split(" "))
                        .mapToLong(Long::parseLong)
                        .boxed()
                        .toList();
                listOfLevels.add(levels);
            }
        }
        return listOfLevels;
    }

    private static void solvePartOne() throws IOException {
        long solution = 0;
        List<List<Long>> listOfLevels = getListOfLevels();
        for (int i = 0; i < listOfLevels.size(); i++) {
            if (isSafe(listOfLevels.get(i)) || isSafe(listOfLevels.get(i).reversed())) {
                solution++;
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private static boolean isSafe(List<Long> levels) {
        if (!levels.equals(levels.stream().sorted().toList())) {
            return false;
        }
        for (int i = 0; i < levels.size() - 1; i++) {
            if (levels.get(i + 1) - levels.get(i) > 3 || levels.get(i + 1) - levels.get(i) == 0) {
                return false;
            }
        }
        return true;
    }

//    private static void solvePartTwo() throws IOException {
//        long solution = 0;
//        List<List<Long>> lists = getListOfLevels();
//        for (int i = 0; i < lists.getFirst().size(); i++) {
//            long leftElement = lists.getFirst().get(i);
//            solution += leftElement * lists.getLast().stream().filter(x -> x == leftElement).count();
//        }
//        System.out.printf("The solution to part two is %s.%n", solution);
//    }
}
