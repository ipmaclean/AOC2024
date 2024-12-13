package iain.aoc2024.day13;

import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ClawMachine {

    private final Point buttonA;
    private final Point buttonB;
    private final Point prize;

    public ClawMachine(List<String> input) {
        buttonA = getValues(input, 0);
        buttonB = getValues(input, 1);
        prize = getValues(input, 2);
    }

    private Point getValues(List<String> input, int index) {
        Pattern pattern = Pattern.compile("\\d+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input.get(index));
        List<Integer> values = new ArrayList<>();
        while (matcher.find()) {
            String match = matcher.group();
            values.add(Integer.parseInt(match));
        }
        return new Point(values.getFirst(), values.getLast());
    }

    public int solve() {
        int b = getButtonB().x * getButtonA().y - getButtonB().y * getButtonA().x;
        int bprize = getPrize().x * getButtonA().y - getPrize().y * getButtonA().x;
        if (bprize % b != 0) {
            return 0;
        }
        int bSolution = bprize / b;
        int aSolution = (getPrize().x - getButtonB().x * bSolution) / getButtonA().x;
        return 3 * aSolution + bSolution;
    }
}
