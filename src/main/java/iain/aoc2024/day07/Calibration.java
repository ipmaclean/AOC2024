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
        Deque<Long> localExpression = new ArrayDeque<>(expression);
        Long currentValue = localExpression.pollFirst();
        int successCount;
        successCount = canInsertOperatorsLocal(currentValue, localExpression, isPartTwo);
        return successCount > 0;
    }

    private int canInsertOperatorsLocal(
            Long currentValue,
            Deque<Long> localExpression,
            boolean isPartTwo
    ) {
        if (localExpression.isEmpty()) {
            return getTestValue().equals(currentValue) ?
                    1 :
                    0;
        }
        if (currentValue > getTestValue()) {
            return 0;
        }
        Long firstValue = localExpression.pollFirst();
        int multSuccessCount = canInsertOperatorsLocal(
                currentValue * firstValue,
                new ArrayDeque<>(localExpression),
                isPartTwo
        );
        int addSuccessCount = canInsertOperatorsLocal(
                currentValue + firstValue,
                new ArrayDeque<>(localExpression),
                isPartTwo
        );
        int concatSuccessCount = 0;
        if (isPartTwo) {
            concatSuccessCount = canInsertOperatorsLocal(
                    Long.parseLong(currentValue.toString() + firstValue),
                    new ArrayDeque<>(localExpression),
                    isPartTwo
            );
        }
        return multSuccessCount + addSuccessCount + concatSuccessCount;
    }
}
