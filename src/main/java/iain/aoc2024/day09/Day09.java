package iain.aoc2024.day09;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class Day09 {

    private static final String INPUT_FILE_NAME = "day09/input.txt";

    private final LinkedHashMap<Long, Long> files = new LinkedHashMap<>();
    private final Deque<Long> freeSpace = new ArrayDeque<>();

    public Day09() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        long solution = 0;
        LinkedHashMap<Long, Long> filesLocal = new LinkedHashMap<>(getFiles());
        Deque<Long> freeSpaceLocal = new ArrayDeque<>(getFreeSpace());

        long fileIndex = 0;
        while (!filesLocal.isEmpty()) {
            Map.Entry<Long, Long> firstFile = filesLocal.pollFirstEntry();
            if (firstFile == null) {
                break;
            }
            long newFileIndex = fileIndex + firstFile.getValue();
            // sum up file positions and ids
            solution += firstFile.getKey() * (newFileIndex - fileIndex) * (newFileIndex + fileIndex - 1) / 2;
            fileIndex = newFileIndex;

            Long currentFreeSpace = freeSpaceLocal.pollFirst();
            while (currentFreeSpace != null && currentFreeSpace > 0) {
                Map.Entry<Long, Long> lastFile = filesLocal.lastEntry();
                if (lastFile == null) {
                    break;
                }
                if (lastFile.getValue() > currentFreeSpace) {
                    newFileIndex = fileIndex + currentFreeSpace;
                    solution += lastFile.getKey() * (newFileIndex - fileIndex) * (newFileIndex + fileIndex - 1) / 2;
                    fileIndex = newFileIndex;
                    filesLocal.put(lastFile.getKey(), lastFile.getValue() - currentFreeSpace);
                    currentFreeSpace = 0L;
                } else {
                    newFileIndex = fileIndex + lastFile.getValue();
                    solution += lastFile.getKey() * (newFileIndex - fileIndex) * (newFileIndex + fileIndex - 1) / 2;
                    fileIndex = newFileIndex;
                    // remove the last entry
                    filesLocal.pollLastEntry();
                    currentFreeSpace -= lastFile.getValue();
                }
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        System.out.printf("The solution to part two is %s.%n", 0);
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day09.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine();
            long fileCounter = 0;
            for (int i = 0; i < line.length(); i += 2) {
                long fileLength = Character.getNumericValue(line.charAt(i));
                getFiles().put(fileCounter++, fileLength);
            }
            for (int i = 1; i < line.length(); i += 2) {
                long fileLength = Character.getNumericValue(line.charAt(i));
                getFreeSpace().addLast(fileLength);
            }
        }
    }
}
