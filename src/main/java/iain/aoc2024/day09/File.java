package iain.aoc2024.day09;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class File {
    @Setter
    private Long index;
    private Long length;
}
