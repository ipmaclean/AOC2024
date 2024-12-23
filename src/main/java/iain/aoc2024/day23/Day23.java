package iain.aoc2024.day23;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Day23 {

    private static final String INPUT_FILE_NAME = "day23/input.txt";
    private final List<Node> computers = new ArrayList<>();

    public Day23() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        Instant start = Instant.now();
        long solution = 0;
        for (Node computer : computers) {
            List<Node> connections = computer.getConnections();
            if (connections.size() < 2) {
                continue;
            }
            for (int i = 0; i < connections.size() - 1; i++) {
                for (int j = i + 1; j < connections.size(); j++) {
                    if (connections.get(i).getConnections().contains(connections.get(j)) &&
                            (computer.getName().startsWith("t") ||
                                    connections.get(i).getName().startsWith("t") ||
                                    connections.get(j).getName().startsWith("t"))) {
                        solution++;
                    }
                }
            }
        }
        solution /= 3;
        Instant finish = Instant.now();
        System.out.printf("The solution to part one is %s.%n", solution);

        long timeElapsed = Duration.between(start, finish).toMillis();
        DecimalFormat formatter = new DecimalFormat("#,###");
        System.out.printf("The solution to part one took %sms.%n", formatter.format(timeElapsed));
    }

    private void solvePartTwo() {
        Instant start = Instant.now();
        HashSet<String> passwords = new HashSet<>();
        HashSet<Node> investigatedNodes = new HashSet<>();
        for (Node computer : computers) {
            // skip any nodes we've already found as a connected group.
            // They will still be found in any new connected groups via
            // a currently un-investigated node
            if (investigatedNodes.contains(computer)) {
                continue;
            }
            List<HashSet<Node>> directNetworks = new ArrayList<>();
            HashSet<Node> connectionsWithSelf = new HashSet<>(computer.getConnections());
            connectionsWithSelf.add(computer);
            directNetworks.add(connectionsWithSelf);
            for (Node connection : computer.getConnections()) {
                // Split the direct connections to the current computer
                // into groups where everything is connected to each other.
                // Discard groups of 2 for efficiency.
                List<HashSet<Node>> nextDirectNetworks = new ArrayList<>();
                HashSet<Node> currentConnectionsWithSelf = new HashSet<>(connection.getConnections());
                currentConnectionsWithSelf.add(connection);
                for (HashSet<Node> directNetwork : directNetworks) {
                    //
                    HashSet<Node> intersection = new HashSet<>(directNetwork);
                    intersection.retainAll(currentConnectionsWithSelf);
                    if (intersection.size() > 2) {
                        nextDirectNetworks.add(intersection);
                    }

                    HashSet<Node> difference = new HashSet<>(directNetwork);
                    difference.removeAll(currentConnectionsWithSelf);
                    if (difference.size() > 1) {
                        difference.add(computer);
                        nextDirectNetworks.add(difference);
                    }
                }
                directNetworks = nextDirectNetworks;
            }
            // add the password based on the connection groups found
            for (HashSet<Node> directNetwork : directNetworks) {
                investigatedNodes.addAll(directNetwork);
                String password = directNetwork.stream()
                        .map(Node::getName)
                        .sorted()
                        .collect(Collectors.joining(","));
                passwords.add(password);
            }
        }
        String solution = passwords.stream()
                .max(Comparator.comparingInt(String::length))
                .get();
        Instant finish = Instant.now();
        System.out.printf("The solution to part two is %s.%n", solution);

        long timeElapsed = Duration.between(start, finish).toMillis();
        DecimalFormat formatter = new DecimalFormat("#,###");
        System.out.printf("The solution to part two took %sms.%n", formatter.format(timeElapsed));
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day23.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] computerNames = line.split("-");
                Optional<Node> computer0Optional = computers.stream()
                        .filter(x -> x.getName().equals(computerNames[0]))
                        .findFirst();
                Node computer0;
                if (computer0Optional.isPresent()) {
                    computer0 = computer0Optional.get();
                } else {
                    computer0 = new Node(computerNames[0]);
                    computers.add(computer0);
                }
                Optional<Node> computer1Optional = computers.stream()
                        .filter(x -> x.getName().equals(computerNames[1]))
                        .findFirst();
                Node computer1;
                if (computer1Optional.isPresent()) {
                    computer1 = computer1Optional.get();
                } else {
                    computer1 = new Node(computerNames[1]);
                    computers.add(computer1);
                }
                computer0.getConnections().add(computer1);
                computer1.getConnections().add(computer0);
            }
        }
    }
}
