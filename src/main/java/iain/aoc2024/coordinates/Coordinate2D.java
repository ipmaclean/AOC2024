package iain.aoc2024.coordinates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coordinate2D {
    private long x;
    private long y;


    public Coordinate2D(Coordinate2D coordinate2D) {
        x = coordinate2D.getX();
        y = coordinate2D.getY();
    }

    public void set(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Coordinate2D other = (Coordinate2D) obj;

        if (this.x != other.x) {
            return false;
        }

        if (this.y != other.y) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (53 + longHashCode(x)) * 53 + longHashCode(y);
    }

    private static int longHashCode(long value) {
        return (int) (value ^ (value >>> 32));
    }
}
