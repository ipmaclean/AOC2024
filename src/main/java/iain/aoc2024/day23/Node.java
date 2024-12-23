package iain.aoc2024.day23;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Node {
    private final String name;
    private final List<Node> connections = new ArrayList<>();
}
