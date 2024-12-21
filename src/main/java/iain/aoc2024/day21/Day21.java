package iain.aoc2024.day21;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static java.util.Map.entry;

@Getter
@Setter
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
        System.out.printf("The solution to part two is %s.%n", 0);
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
