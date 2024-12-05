package iain.aoc2024.day05;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Day05 {

    private static final String INPUT_FILE_NAME = "day05/input.txt";
    private final List<int[]> updates = new ArrayList<>();
    @Setter
    private UpdateComparator updateComparator;

    public Day05() throws IOException {
        getInput();
    }

    public void solve() {
        long solutionPartOne = 0;
        long solutionPartTwo = 0;
        for (int[] update : getUpdates()) {
            int[] orderedUpdate = orderUpdate(update);
            if (Arrays.equals(orderedUpdate, update)) {
                solutionPartOne += orderedUpdate[orderedUpdate.length / 2];
            } else {
                solutionPartTwo += orderedUpdate[orderedUpdate.length / 2];
            }
        }
        System.out.printf("The solution to part one is %s.%n", solutionPartOne);
        System.out.printf("The solution to part two is %s.%n", solutionPartTwo);
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day05.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            List<int[]> rules = new ArrayList<>();
            while (!(line = reader.readLine()).isEmpty()) {
                rules.add(Arrays.stream(line.split("\\|")).mapToInt(Integer::parseInt).toArray());
            }
            setUpdateComparator(new UpdateComparator(rules));
            while ((line = reader.readLine()) != null) {
                updates.add(Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray());
            }
        }
    }

    private int[] orderUpdate(int[] update) {
        return Arrays.stream(update.clone())
                .boxed()
                .sorted(updateComparator)
                .mapToInt(Integer::intValue)
                .toArray();
    }
}
