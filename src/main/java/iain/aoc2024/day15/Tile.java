package iain.aoc2024.day15;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Tile {
    private final boolean isWall;
    @Setter
    private boolean isBox;
}
