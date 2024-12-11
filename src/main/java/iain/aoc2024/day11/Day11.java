package iain.aoc2024.day11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Day11 {

    private static final String INPUT_FILE_NAME = "day11/input.txt";

    private Day11() {
        throw new IllegalStateException("Utility class");
    }

    public static void solve() throws IOException {
        solvePartOne();
        solvePartTwo();
    }

    private static LinkedHashMap<Long, Long> getInput() throws IOException {
        InputStream inputStream = Day11.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);
        LinkedHashMap<Long, Long> stonesMap = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            long[] stones = Arrays.stream(reader
                            .readLine()
                            .split(" "))
                    .mapToLong(Long::parseLong)
                    .toArray();
            for (Long stone : stones) {
                Long currentValue = stonesMap.putIfAbsent(stone, 1L);
                if (currentValue != null) {
                    stonesMap.put(stone, currentValue + 1);
                }
            }
        }
        return stonesMap;
    }

    private static void solvePartOne() throws IOException {
        System.out.printf("The solution to part one is %s.%n", getSolution(25));
    }

    private static void solvePartTwo() throws IOException {
        System.out.printf("The solution to part two is %s.%n", getSolution(75));
    }

    private static long getSolution(int blinksToComplete) throws IOException {
        LinkedHashMap<Long, Long> stonesMap = getInput();
        for (int i = 0; i < blinksToComplete; i++) {
            stonesMap = blink(stonesMap);
        }
        return stonesMap.values().stream().mapToLong(Long::valueOf).sum();
    }

    private static LinkedHashMap<Long, Long> blink(LinkedHashMap<Long, Long> stonesMap) {
        LinkedHashMap<Long, Long> nextStonesMap = new LinkedHashMap<>();
        for (Map.Entry<Long, Long> stoneEntry : stonesMap.entrySet()) {
            List<Long> nextStones = blinkSingleStone(stoneEntry.getKey());
            for (Long nextStone : nextStones) {
                Long currentValue = nextStonesMap.putIfAbsent(nextStone, stoneEntry.getValue());
                if (currentValue != null) {
                    nextStonesMap.put(nextStone, currentValue + stoneEntry.getValue());
                }
            }
        }
        return nextStonesMap;
    }

    private static List<Long> blinkSingleStone(Long stone) {
        List<Long> nextStones = new ArrayList<>();
        String stoneString = stone.toString();
        if (stone.equals(0L)) {
            nextStones.add(1L);
        } else if (stoneString.length() % 2 == 0) {
            nextStones.add(
                    Long.parseLong(
                            stoneString.substring(0, stoneString.length() / 2)
                    )
            );
            nextStones.add(
                    Long.parseLong(
                            stoneString.substring(stoneString.length() / 2)
                    )
            );
        } else {
            nextStones.add(stone * 2024);
        }
        return nextStones;
    }
}
