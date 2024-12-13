package iain.aoc2024.day13;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ClawMachine {

    private final LongPoint buttonA;
    private final LongPoint buttonB;
    private final LongPoint prize;

    public ClawMachine(List<String> input) {
        buttonA = getValues(input, 0);
        buttonB = getValues(input, 1);
        prize = getValues(input, 2);
    }

    private LongPoint getValues(List<String> input, int index) {
        Pattern pattern = Pattern.compile("\\d+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input.get(index));
        List<Long> values = new ArrayList<>();
        while (matcher.find()) {
            String match = matcher.group();
            values.add(Long.parseLong(match));
        }
        return new LongPoint(values.getFirst(), values.getLast());
    }

    public long solve(boolean isPartTwo) {
        long prizeX = isPartTwo ? getPrize().x() + 10_000_000_000_000L : getPrize().x();
        long prizeY = isPartTwo ? getPrize().y() + 10_000_000_000_000L : getPrize().y();
        long b = getButtonB().x() * getButtonA().y() - getButtonB().y() * getButtonA().x();
        long bPrize = prizeX * getButtonA().y() - prizeY * getButtonA().x();
        if (bPrize % b != 0) {
            return 0;
        }
        long bSolution = bPrize / b;
        if ((prizeX - getButtonB().x() * bSolution) % getButtonA().x() != 0) {
            return 0;
        }
        long aSolution = (prizeX - getButtonB().x() * bSolution) / getButtonA().x();
        return 3 * aSolution + bSolution;
    }
}
