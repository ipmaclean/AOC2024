package iain.aoc2024.day18;

import java.util.Comparator;

public class MemorySpaceComparator implements Comparator<MemorySpace> {
    @Override
    public int compare(MemorySpace left, MemorySpace right) {
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

        if (left.getWallTimer() < right.getWallTimer()) {
            return -1;
        }
        if (left.getWallTimer() > right.getWallTimer()) {
            return 1;
        }

        return 0;
    }
}
