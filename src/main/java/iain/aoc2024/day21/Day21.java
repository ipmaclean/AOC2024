package iain.aoc2024.day21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static java.util.Map.entry;

public class Day21 {
    private static final String INPUT_FILE_NAME = "day21/input.txt";
    private static final HashMap<AbstractMap.SimpleImmutableEntry<Character, Character>, String> ARROW_MAP = new HashMap<>(
            Map.ofEntries(
                    entry(new AbstractMap.SimpleImmutableEntry<>('<', '<'), "A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('<', '^'), ">^A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('<', 'v'), ">A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('<', '>'), ">>A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('<', 'A'), ">>^A"),

                    entry(new AbstractMap.SimpleImmutableEntry<>('^', '<'), "v<A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('^', '^'), "A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('^', 'v'), "vA"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('^', '>'), "v>A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('^', 'A'), ">A"),

                    entry(new AbstractMap.SimpleImmutableEntry<>('v', '<'), "<A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('v', '^'), "^A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('v', 'v'), "A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('v', '>'), ">A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('v', 'A'), "^>A"),

                    entry(new AbstractMap.SimpleImmutableEntry<>('>', '<'), "<<A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('>', '^'), "<^A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('>', 'v'), "<A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('>', '>'), "A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('>', 'A'), "^A"),

                    entry(new AbstractMap.SimpleImmutableEntry<>('A', '<'), "v<<A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('A', '^'), "<A"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('A', 'v'), "<vA"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('A', '>'), "vA"),
                    entry(new AbstractMap.SimpleImmutableEntry<>('A', 'A'), "A")
            )
    );

    private final List<Integer> values = new ArrayList<>();
    private final List<String> inputs = new ArrayList<>();

    public Day21() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        // Cheated on this one a bit and manually tweaked the input to
        // have 'directions' for typing on the numpad (see example).
        // Prefer to use < first, then v/^, then >/A, unless
        // you can chain together multiple of the same.
        int solution = 0;
        int counter = 0;
        for (String input : inputs) {
            StringBuilder sb;
            for (int i = 0; i < 2; i++) {
                char firstChar = 'A';
                sb = new StringBuilder();
                for (int j = 0; j < input.length(); j++) {
                    char secondChar = input.charAt(j);
                    sb.append(ARROW_MAP.get(new AbstractMap.SimpleImmutableEntry<>(firstChar, secondChar)));
                    firstChar = secondChar;
                }
                input = sb.toString();
            }
            solution += input.length() * values.get(counter++);
        }
        System.out.printf("The solution to part one is %s.%n", solution);
    }

    private void solvePartTwo() {
        // Store the count of 'instructions' ( [^><v]* terminating with A)
        // instead of generating the full string. Also cached the actual
        // instruction translation to the next robot down the chain.

        // I should neaten this up, but I've spent enough time on it already!
        long solution = 0;
        int counter = 0;
        HashMap<String, String> translations = new HashMap<>();
        for (String input : inputs) {
            StringBuilder sb;
            String[] slicedInputs = input.split("(?<=A)");
            HashMap<String, Long> instructionCounts = new HashMap<>();
            for (String slicedInput : slicedInputs) {
                long value = instructionCounts.getOrDefault(slicedInput, 0L);
                instructionCounts.put(slicedInput, value + 1);
            }
            for (int i = 0; i < 25; i++) {
                HashMap<String, Long> nextInstructionCounts = new HashMap<>();
                for (Map.Entry<String, Long> instructionToCount : instructionCounts.entrySet()) {
                    String translation = translations.getOrDefault(instructionToCount.getKey(), null);
                    if (translation == null) {
                        char firstChar = 'A';
                        sb = new StringBuilder();
                        for (int j = 0; j < instructionToCount.getKey().length(); j++) {
                            char secondChar = instructionToCount.getKey().charAt(j);
                            sb.append(ARROW_MAP.get(new AbstractMap.SimpleImmutableEntry<>(firstChar, secondChar)));
                            firstChar = secondChar;
                        }
                        translation = sb.toString();
                        translations.put(instructionToCount.getKey(), translation);
                    }
                    String[] slicedTranslations = translation.split("(?<=A)");
                    for (String slicedTranslation : slicedTranslations) {
                        long value = nextInstructionCounts.getOrDefault(slicedTranslation, 0L);
                        nextInstructionCounts.put(slicedTranslation, value + instructionToCount.getValue());
                    }
                }
                instructionCounts = nextInstructionCounts;
            }
            long sequenceLength = 0;
            for (Map.Entry<String, Long> instructionToCount : instructionCounts.entrySet()) {
                sequenceLength += instructionToCount.getKey().length() * instructionToCount.getValue();
            }
            solution += sequenceLength * values.get(counter++);
        }
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day21.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                values.add(Integer.parseInt(line));
            }
            while (!(line = reader.readLine()).isEmpty()) {
                inputs.add(line);
            }
        }
    }
}
