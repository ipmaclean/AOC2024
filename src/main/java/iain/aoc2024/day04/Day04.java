package iain.aoc2024.day04;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day04 {

    private static final String INPUT_FILE_NAME = "day04/input.txt";
    private static final Point[] DIRECTIONS = new Point[]
            {
                    new Point(-1, -1),
                    new Point(0, -1),
                    new Point(1, -1),
                    new Point(-1, 0),
                    new Point(1, 0),
                    new Point(-1, 1),
                    new Point(0, 1),
                    new Point(1, 1),
            };

    private Day04() {
        throw new IllegalStateException("Utility class");
    }

    public static void solve() throws IOException {
        solvePartOne();
        solvePartTwo();
    }

    private static List<String> getWordSearch() throws IOException {
        InputStream inputStream = Day04.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);
        List<String> wordSearch = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordSearch.add(line);
            }
        }
        return wordSearch;
    }

    private static void solvePartOne() throws IOException {
        long solution = 0;
        List<String> wordSearch = getWordSearch();

        for (int y = 0; y < wordSearch.size(); y++) {
            for (int x = 0; x < wordSearch.getFirst().length(); x++) {
                for (Point direction : DIRECTIONS) {
                    if (searchInDirection(wordSearch, new Point(x, y), direction, "XMAS")) {
                        solution++;
                    }
                }
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private static boolean searchInDirection(
            List<String> wordSearch,
            Point position,
            Point direction,
            String wordToSearch
    ) {
        if (position.y < 0 || position.y >= wordSearch.size() ||
                position.x < 0 || position.x >= wordSearch.getFirst().length()) {
            return false;
        }
        if (wordSearch.get(position.y).charAt(position.x) != wordToSearch.charAt(0)) {
            return false;
        }
        if (wordToSearch.length() > 1) {
            return searchInDirection(
                    wordSearch,
                    new Point(position.x + direction.x, position.y + direction.y),
                    direction,
                    wordToSearch.substring(1)
            );
        } else {
            return true;
        }
    }

    private static void solvePartTwo() throws IOException {
        long solution = 0;
        List<String> wordSearch = getWordSearch();

        for (int y = 0; y < wordSearch.size(); y++) {
            for (int x = 0; x < wordSearch.getFirst().length(); x++) {
                if ((searchInDirection(wordSearch, new Point(x, y), new Point(1, 1), "MAS") ||
                        searchInDirection(wordSearch, new Point(x, y), new Point(1, 1), "SAM")) &&
                        (searchInDirection(wordSearch, new Point(x + 2, y), new Point(-1, 1), "MAS") ||
                                searchInDirection(wordSearch, new Point(x + 2, y), new Point(-1, 1), "SAM"))) {
                    solution++;
                }
            }
        }
        System.out.printf("The solution to part two is %s.%n", solution);
    }
}
