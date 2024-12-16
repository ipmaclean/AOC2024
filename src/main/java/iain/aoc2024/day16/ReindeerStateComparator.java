package iain.aoc2024.day16;

import java.util.Comparator;

public class ReindeerStateComparator implements Comparator<ReindeerStateWithScore> {
    @Override
    public int compare(ReindeerStateWithScore left, ReindeerStateWithScore right) {
        if (left.score() < right.score()) {
            return -1;
        }
        if (left.score() > right.score()) {
            return 1;
        }

        if (left.directionIndex() < right.directionIndex()) {
            return -1;
        }
        if (left.directionIndex() > right.directionIndex()) {
            return 1;
        }

        if (left.position().getX() < right.position().getX()) {
            return -1;
        }
        if (left.position().getX() > right.position().getX()) {
            return -1;
        }
        if (left.position().getY() < right.position().getY()) {
            return -1;
        }
        if (left.position().getY() > right.position().getY()) {
            return -1;
        }

        return 0;
    }
}
