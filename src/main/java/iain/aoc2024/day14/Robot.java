package iain.aoc2024.day14;

import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Robot {
    private final Point max;
    private final Point position;
    private final Point velocity;

    public Robot(String input, Point max) {
        this.max = max;
        Pattern pattern = Pattern.compile("-?\\d+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        List<Integer> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(Integer.parseInt(matcher.group()));
        }
        position = new Point(matches.get(0), matches.get(1));
        velocity = new Point(matches.get(2), matches.get(3));
    }

    public Point getPositionAfterTime(int time) {
        return new Point(
                modulo(getPosition().x + time * getVelocity().x, getMax().x),
                modulo(getPosition().y + time * getVelocity().y, getMax().y)
        );
    }

    public Integer getQuadrantAfterTime(int time) {
        Point newPosition = getPositionAfterTime(time);
        if (newPosition.x < getMax().x / 2 && newPosition.y < getMax().y / 2) {
            return 0;
        }
        if (newPosition.x > getMax().x / 2 && newPosition.y < getMax().y / 2) {
            return 1;
        }
        if (newPosition.x < getMax().x / 2 && newPosition.y > getMax().y / 2) {
            return 2;
        }
        if (newPosition.x > getMax().x / 2 && newPosition.y > getMax().y / 2) {
            return 3;
        }
        return null;
    }

    private int modulo(int x, int y) {
        int result = x % y;
        if (result < 0) {
            result += y;
        }
        return result;
    }
}
