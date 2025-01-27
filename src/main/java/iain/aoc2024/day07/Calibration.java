package iain.aoc2024.day07;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

@Getter
public class Calibration {

    private final Deque<Long> expression;
    private final Long testValue;

    public Calibration(String input) {
        String[] equation = input.split(":");
        testValue = Long.parseLong(equation[0]);
        List<Long> expressionArray = Arrays.stream(
                        equation[1].substring(1).split(" ")
                ).mapToLong(Long::parseLong)
                .boxed()
                .toList();
        expression = new ArrayDeque<>(expressionArray);
    }

    public boolean canInsertOperators(boolean isPartTwo) {
        Long currentTestValue = getTestValue();
        Deque<Long> localExpression = new ArrayDeque<>(expression);
        int successCount = canInsertOperatorsLocal(currentTestValue, localExpression, isPartTwo);
        return successCount > 0;
    }

    private int canInsertOperatorsLocal(
            Long currentTestValue,
            Deque<Long> localExpression,
            boolean isPartTwo
    ) {
        if (localExpression.isEmpty()) {
            throw new IllegalArgumentException("Cannot pass empty deque");
        }
        if (localExpression.size() == 1) {
            return localExpression.pollLast().equals(currentTestValue) ?
                    1 :
                    0;
        }
        Long lastValue = localExpression.pollLast();
        if (currentTestValue < lastValue) {
            return 0;
        }
        int multSuccessCount = 0;
        if (currentTestValue % lastValue == 0) {
            multSuccessCount = canInsertOperatorsLocal(
                    currentTestValue / lastValue,
                    new ArrayDeque<>(localExpression),
                    isPartTwo
            );
        }
        int addSuccessCount = canInsertOperatorsLocal(
                currentTestValue - lastValue,
                new ArrayDeque<>(localExpression),
                isPartTwo
        );
        int concatSuccessCount = 0;
        if (isPartTwo && canDeconcatenate(currentTestValue, lastValue)) {

            concatSuccessCount = canInsertOperatorsLocal(
                    Long.parseLong(currentTestValue.toString().substring(
                            0,
                            currentTestValue.toString().length() - lastValue.toString().length()
                    )),
                    new ArrayDeque<>(localExpression),
                    isPartTwo
            );
        }
        return multSuccessCount + addSuccessCount + concatSuccessCount;
    }

    private boolean canDeconcatenate(
            Long currentTestValue,
            Long lastValue
    ) {
        return currentTestValue.toString().length() > lastValue.toString().length() &&
                currentTestValue.toString().endsWith(lastValue.toString());
    }
}
