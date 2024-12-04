package iain.aoc2024.day01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day01 {

    private static final String INPUT_FILE_NAME = "day01/input.txt";

    private Day01() {
        throw new IllegalStateException("Utility class");
    }

    public static void solve() throws IOException {
        solvePartOne();
        solvePartTwo();
    }

    private static List<List<Long>> getLists() throws IOException {
        InputStream inputStream = Day01.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);
        List<List<Long>> lists = new ArrayList<>(2);
        lists.add(new ArrayList<>());
        lists.add(new ArrayList<>());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                long[] locationIds = Arrays.stream(line.split(" {3}"))
                        .mapToLong(Long::parseLong)
                        .toArray();
                for (int i = 0; i <= 1; i++) {
                    lists.get(i).add(locationIds[i]);
                }
            }
        }
        return lists;
    }

    private static void solvePartOne() throws IOException {
        long solution = 0;
        List<List<Long>> lists = getLists();
        List<List<Long>> orderedLists = new ArrayList<>();
        orderedLists.add(lists.getFirst().stream().sorted().toList());
        orderedLists.add(lists.getLast().stream().sorted().toList());
        for (int i = 0; i < orderedLists.getFirst().size(); i++) {
            solution += Math.abs(orderedLists.getFirst().get(i) - orderedLists.getLast().get(i));
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private static void solvePartTwo() throws IOException {
        long solution = 0;
        List<List<Long>> lists = getLists();
        for (int i = 0; i < lists.getFirst().size(); i++) {
            long leftElement = lists.getFirst().get(i);
            solution += leftElement * lists.getLast().stream().filter(x -> x == leftElement).count();
        }
        System.out.printf("The solution to part two is %s.%n", solution);
    }
}
