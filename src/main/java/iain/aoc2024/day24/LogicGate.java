package iain.aoc2024.day24;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LogicGate {
    private final String left;
    private final String right;
    private final String output;
    private final String operation;

}
