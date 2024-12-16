package iain.aoc2024.day16;

import iain.aoc2024.coordinates.Coordinate2D;

public record ReindeerStateWithScore(Coordinate2D position, int directionIndex, Long score) {
}