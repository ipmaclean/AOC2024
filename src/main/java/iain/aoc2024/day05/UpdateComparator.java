package iain.aoc2024.day05;

import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Getter
public class UpdateComparator implements Comparator<Integer> {

    private final List<int[]> rules;

    public UpdateComparator(List<int[]> rules) {
        this.rules = rules;
    }

    @Override
    public int compare(Integer left, Integer right) {
        if (left.equals(right)) {
            return 0;
        }
        // Will throw if anything other than exactly one rule is returned
        int[] matchingRule = getRules().stream()
                .filter(x -> IntStream.of(x).anyMatch(y -> y == left) && IntStream.of(x).anyMatch(y -> y == right))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Multiple elements: " + a + ", " + b);
                })
                .get();
        if (matchingRule[0] == right) {
            return 1;
        }
        return -1;
    }
}
