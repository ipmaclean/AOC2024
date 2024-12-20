package iain.aoc2024.day20;

import iain.aoc2024.coordinates.Coordinate2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tile {
    private final Coordinate2D coordinates;
    private boolean isWall;
    private long shortestPath = Long.MAX_VALUE;

    public Tile(Coordinate2D coordinates, boolean isWall) {
        this.coordinates = coordinates;
        this.isWall = isWall;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Tile other = (Tile) obj;

        if (this.coordinates != other.coordinates) {
            return false;
        }

        if (this.isWall != other.isWall) {
            return false;
        }

        if (this.shortestPath != other.shortestPath) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return ((53 + coordinates.hashCode()) * 53 + Boolean.hashCode(isWall) * 53 + longHashCode(shortestPath));
    }

    private static int longHashCode(long value) {
        return (int) (value ^ (value >>> 32));
    }
}
