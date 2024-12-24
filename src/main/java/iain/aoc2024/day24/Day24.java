package iain.aoc2024.day24;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 {

    private static final String INPUT_FILE_NAME = "day24/input.txt";
    private final HashMap<String, Wire> wires = new HashMap<>();
    private final List<LogicGate> logicGates = new ArrayList<>();


    public Day24() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        Instant start = Instant.now();
        HashMap<String, Wire> wiresLocal = new HashMap<>(wires);
        List<LogicGate> logicGatesLocal = new ArrayList<>(logicGates);
        while (!logicGatesLocal.isEmpty()) {
            for (int i = logicGatesLocal.size() - 1; i >= 0; i--) {
                LogicGate logicGate = logicGatesLocal.get(i);
                if (!wiresLocal.containsKey(logicGate.getLeft()) || !wiresLocal.containsKey(logicGate.getRight())) {
                    continue;
                }
                switch (logicGate.getOperation()) {
                    case "AND":
                        wiresLocal.put(
                                logicGate.getOutput(),
                                new Wire(
                                        logicGate.getOutput(),
                                        wiresLocal.get(logicGate.getLeft()).getValue() & wiresLocal.get(logicGate.getRight()).getValue()
                                )
                        );
                        break;
                    case "OR":
                        wiresLocal.put(
                                logicGate.getOutput(),
                                new Wire(
                                        logicGate.getOutput(),
                                        wiresLocal.get(logicGate.getLeft()).getValue() | wiresLocal.get(logicGate.getRight()).getValue()
                                )
                        );
                        break;
                    case "XOR":
                        wiresLocal.put(
                                logicGate.getOutput(),
                                new Wire(
                                        logicGate.getOutput(),
                                        wiresLocal.get(logicGate.getLeft()).getValue() ^ wiresLocal.get(logicGate.getRight()).getValue()
                                )
                        );
                        break;
                    default:
                        throw new IllegalArgumentException("Operation not recognised");
                }
                logicGatesLocal.remove(i);
            }
        }

        String[] zWires = wiresLocal.keySet().stream()
                .filter(x -> x.startsWith("z"))
                .sorted(Comparator.comparingInt(x -> Integer.parseInt(x.substring(1))))
                .toArray(String[]::new);
        long solution = 0;
        long binary = 1;
        for (String zWire : zWires) {
            solution += wiresLocal.get(zWire).getValue() * binary;
            binary *= 2;
        }

        Instant finish = Instant.now();
        System.out.printf("The solution to part one is %s.%n", solution);

        long timeElapsed = Duration.between(start, finish).toMillis();
        DecimalFormat formatter = new DecimalFormat("#,###");
        System.out.printf("The solution to part one took %sms.%n", formatter.format(timeElapsed));
    }

    private void solvePartTwo() {
        Instant start = Instant.now();


        Instant finish = Instant.now();
        System.out.printf("The solution to part two is %s.%n", 0);

        long timeElapsed = Duration.between(start, finish).toMillis();
        DecimalFormat formatter = new DecimalFormat("#,###");
        System.out.printf("The solution to part two took %sms.%n", formatter.format(timeElapsed));
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day24.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                wires.put(line.substring(0, 3), new Wire(line.substring(0, 3), Long.parseLong(line.substring(5, 6))));
            }
            while ((line = reader.readLine()) != null) {
                Pattern lowerPattern = Pattern.compile("([a-z0-9]+)");
                Matcher lowerMatcher = lowerPattern.matcher(line);
                String[] wireNames = new String[3];
                int counter = 0;
                while (lowerMatcher.find()) {
                    wireNames[counter++] = lowerMatcher.group();
                }
                Pattern upperPattern = Pattern.compile("([A-Z]+)");
                Matcher upperMatcher = upperPattern.matcher(line);
                String operation = "";
                while (upperMatcher.find()) {
                    operation = upperMatcher.group();
                }
                logicGates.add(new LogicGate(wireNames[0], wireNames[1], wireNames[2], operation));
            }
        }
    }
}

