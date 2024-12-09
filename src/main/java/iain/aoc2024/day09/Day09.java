package iain.aoc2024.day09;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@Getter
public class Day09 {

    private static final String INPUT_FILE_NAME = "day09/input.txt";

    private final LinkedHashMap<Long, File> files = new LinkedHashMap<>();
    private final LinkedHashMap<Long, Long> filesToLength = new LinkedHashMap<>();
    private final TreeMap<Long, Long> indicesToFreeSpace = new TreeMap<>();

    public Day09() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        long solution = 0;
        LinkedHashMap<Long, Long> filesToLengthLocal = new LinkedHashMap<>(getFilesToLength());
        LinkedHashMap<Long, Long> indexToFreeSpaceLocal = new LinkedHashMap<>(getIndicesToFreeSpace());

        long fileIndex = 0;
        while (!filesToLengthLocal.isEmpty()) {
            Map.Entry<Long, Long> firstFile = filesToLengthLocal.pollFirstEntry();
            if (firstFile == null) {
                break;
            }
            long newFileIndex = fileIndex + firstFile.getValue();
            // sum up file positions and ids
            solution += firstFile.getKey() * (newFileIndex - fileIndex) * (newFileIndex + fileIndex - 1) / 2;
            fileIndex = newFileIndex;

            Map.Entry<Long, Long> currentFreeSpaceEntry = indexToFreeSpaceLocal.pollFirstEntry();
            Long currentFreeSpace = currentFreeSpaceEntry.getValue();
            while (currentFreeSpace != null && currentFreeSpace > 0) {
                Map.Entry<Long, Long> lastFile = filesToLengthLocal.lastEntry();
                if (lastFile == null) {
                    break;
                }
                if (lastFile.getValue() > currentFreeSpace) {
                    newFileIndex = fileIndex + currentFreeSpace;
                    solution += lastFile.getKey() * (newFileIndex - fileIndex) * (newFileIndex + fileIndex - 1) / 2;
                    fileIndex = newFileIndex;
                    filesToLengthLocal.put(lastFile.getKey(), lastFile.getValue() - currentFreeSpace);
                    currentFreeSpace = 0L;
                } else {
                    newFileIndex = fileIndex + lastFile.getValue();
                    solution += lastFile.getKey() * (newFileIndex - fileIndex) * (newFileIndex + fileIndex - 1) / 2;
                    fileIndex = newFileIndex;
                    // remove the last entry
                    filesToLengthLocal.pollLastEntry();
                    currentFreeSpace -= lastFile.getValue();
                }
            }
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        LinkedHashMap<Long, File> filesLocal = new LinkedHashMap<>(getFiles());
        TreeMap<Long, Long> indicesToFreeSpaceLocal = new TreeMap<>(getIndicesToFreeSpace());

        for (Map.Entry<Long, File> file : filesLocal.reversed().entrySet()) {
            for (Map.Entry<Long, Long> indexToFreeSpace : indicesToFreeSpaceLocal.entrySet()) {
                Long fileIndex = file.getValue().getIndex();
                Long fileLength = file.getValue().getLength();
                if (indexToFreeSpace.getKey() < fileIndex && indexToFreeSpace.getValue() >= fileLength) {
                    // add free space where file was
                    indicesToFreeSpaceLocal.put(fileIndex, fileLength);
                    //stitch free space either side if needed
                    Map.Entry<Long, Long> nextFreeSpace = indicesToFreeSpaceLocal.higherEntry(fileIndex);
                    if (nextFreeSpace != null && fileIndex + indicesToFreeSpaceLocal.get(fileIndex) == nextFreeSpace.getKey()) {
                        indicesToFreeSpaceLocal.put(fileIndex, indicesToFreeSpaceLocal.get(fileIndex) + nextFreeSpace.getValue());
                        indicesToFreeSpaceLocal.remove(nextFreeSpace.getKey());
                    }
                    Map.Entry<Long, Long> previousFreeSpace = indicesToFreeSpaceLocal.lowerEntry(fileIndex);
                    if (previousFreeSpace != null && previousFreeSpace.getKey() + previousFreeSpace.getValue() == fileIndex) {
                        indicesToFreeSpaceLocal.put(previousFreeSpace.getKey(), previousFreeSpace.getValue() + indicesToFreeSpaceLocal.get(fileIndex));
                        indicesToFreeSpaceLocal.remove(fileIndex);
                    }

                    // move the file
                    file.getValue().setIndex(indexToFreeSpace.getKey());

                    // remove free space that has been overwritten
                    if (indexToFreeSpace.getKey() + fileLength > 0) {
                        indicesToFreeSpaceLocal.put(
                                indexToFreeSpace.getKey() + fileLength,
                                indexToFreeSpace.getValue() - fileLength
                        );
                    }
                    indicesToFreeSpaceLocal.remove(indexToFreeSpace.getKey());
                    break;
                }
            }
        }
        long solution = 0;
        for (Map.Entry<Long, File> file : getFiles().entrySet()) {
            solution += file.getKey() *
                    file.getValue().getLength() *
                    (2 * file.getValue().getIndex() + file.getValue().getLength() - 1) / 2;
        }
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day09.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine();
            long fileCounter = 0;
            long indexCounter = 0;
            for (int i = 0; i < line.length(); i++) {
                long fileLength = Character.getNumericValue(line.charAt(i));
                if (i % 2 == 0) {
                    getFiles().put(fileCounter, new File(indexCounter, fileLength));
                    getFilesToLength().put(fileCounter++, fileLength);
                } else {
                    getIndicesToFreeSpace().put(indexCounter, fileLength);
                }
                indexCounter += fileLength;
            }
        }
    }
}
