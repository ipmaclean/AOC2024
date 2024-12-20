package iain.aoc2024.day20;


import java.util.Comparator;

public class TileComparator implements Comparator<Tile> {
    @Override
    public int compare(Tile left, Tile right) {
        if (left.getShortestPath() < right.getShortestPath()) {
            return -1;
        }
        if (left.getShortestPath() > right.getShortestPath()) {
            return 1;
        }

        if (left.getCoordinates().getX() < right.getCoordinates().getX()) {
            return -1;
        }
        if (left.getCoordinates().getX() > right.getCoordinates().getX()) {
            return 1;
        }
        if (left.getCoordinates().getY() < right.getCoordinates().getY()) {
            return -1;
        }
        if (left.getCoordinates().getY() > right.getCoordinates().getY()) {
            return 1;
        }

        if (!left.isWall() && right.isWall()) {
            return -1;
        }
        if (left.isWall() && !right.isWall()) {
            return 1;
        }

        return 0;
    }
}
