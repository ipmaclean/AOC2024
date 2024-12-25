package iain.aoc2024.day25;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Day25 {

    private static final String INPUT_FILE_NAME = "day25/input.txt";
    private final List<int[]> locks = new ArrayList<>();
    private final List<int[]> keys = new ArrayList<>();


    public Day25() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        Instant start = Instant.now();
        long solution = 0;
        for (int[] lock : locks) {
            for (int[] key : keys) {
                boolean keyFits = true;
                for (int i = 0; i < 5; i++) {
                    if (key[i] + lock[i] > 5) {
                        keyFits = false;
                        break;
                    }
                }
                if (keyFits) {
                    solution++;
                }
            }
        }
        Instant finish = Instant.now();
        System.out.printf("The solution to part one is %s.%n", solution);

        long timeElapsed = Duration.between(start, finish).toMillis();
        DecimalFormat formatter = new DecimalFormat("#,###");
        System.out.printf("The solution to part one took %sms.%n", formatter.format(timeElapsed));
    }

    private void solvePartTwo() {
        System.out.println("Merry Christmas!");
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day25.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int[] heights = new int[]{-1, -1, -1, -1, -1};
                boolean isLock = line.charAt(0) == '#';
                addHeights(line, heights);
                for (int i = 0; i < 6; i++) {
                    addHeights(reader.readLine(), heights);
                }
                if (isLock) {
                    locks.add(heights);
                } else {
                    keys.add(heights);
                }
                reader.readLine();
            }
        }
    }

    private void addHeights(String line, int[] heights) {
        for (int i = 0; i < 5; i++) {
            if (line.charAt(i) == '#') {
                heights[i]++;
            }
        }
    }
}
