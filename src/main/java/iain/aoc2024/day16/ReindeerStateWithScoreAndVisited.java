package iain.aoc2024.day16;

import iain.aoc2024.coordinates.Coordinate2D;

import java.util.HashSet;

public record ReindeerStateWithScoreAndVisited(Coordinate2D position, int directionIndex, Long score, HashSet<ReindeerState> visited) {
}
