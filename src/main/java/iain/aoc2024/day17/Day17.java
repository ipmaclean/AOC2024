package iain.aoc2024.day17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 {

    private static final String INPUT_FILE_NAME = "day17/input.txt";

    private final HashMap<Character, Integer> startRegisters = new HashMap<>();
    private HashMap<Character, Integer> registers = new HashMap<>();
    private final List<Integer> program = new ArrayList<>();

    public Day17() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        List<String> output = new ArrayList<>();
        int instructionPointer = 0;
        registers = new HashMap<>(startRegisters);
        while (instructionPointer < program.size()) {
            int opcode = program.get(instructionPointer);
            int operand = program.get(instructionPointer + 1);
            switch (opcode) {
                case 0:
                    aDivide(operand, 'A');
                    instructionPointer += 2;
                    break;
                case 1:
                    bitwiseXOR(operand, 'B', null);
                    instructionPointer += 2;
                    break;
                case 2:
                    mod8(operand, 'B');
                    instructionPointer += 2;
                    break;
                case 3:
                    int aRegister = registers.get('A');
                    instructionPointer = aRegister != 0 ? operand : instructionPointer + 2;
                    break;
                case 4:
                    bitwiseXOR(operand, 'B', 'C');
                    instructionPointer += 2;
                    break;
                case 5:
                    output.add(out(operand));
                    instructionPointer += 2;
                    break;
                case 6:
                    aDivide(operand, 'B');
                    instructionPointer += 2;
                    break;
                case 7:
                    aDivide(operand, 'C');
                    instructionPointer += 2;
                    break;
                default:
                    throw new IllegalArgumentException("Opcode not recognised");
            }
        }
        System.out.printf("The solution to part one is %s.%n", String.join(",", output));
    }

    private void mod8(int operand, char registerKey) {
        registers.put(registerKey, getComboOperand(operand) % 8);
    }

    private String out(int operand) {
        return String.valueOf(getComboOperand(operand) % 8);
    }

    private void aDivide(int operand, char registerKey) {
        int quotient = registers.get('A') / (int) Math.pow(2, getComboOperand(operand));
        registers.put(registerKey, quotient);
    }

    private void bitwiseXOR(int operand, char registerKey, Character optionalRegisterKey) {
        int variable = optionalRegisterKey != null ? registers.get(optionalRegisterKey) : operand;
        int value = registers.get(registerKey) ^ variable;
        registers.put(registerKey, value);
    }

    private int getComboOperand(int operand) {
        switch (operand) {
            case 0, 1, 2, 3:
                return operand;
            case 4:
                return registers.get('A');
            case 5:
                return registers.get('B');
            case 6:
                return registers.get('C');
            case 7:
            default:
                throw new IllegalArgumentException("Operand not recognised");
        }
    }

    private void solvePartTwo() {
        long solution = 0;
        System.out.printf("The solution to part two is %s.%n", solution);
    }

    private void getInput() throws IOException {
        InputStream inputStream = Day17.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            Pattern pattern = Pattern.compile("\\d+", Pattern.CASE_INSENSITIVE);
            char[] registerChars = new char[]{'A', 'B', 'C'};
            for (char registerChar : registerChars) {
                line = reader.readLine();
                Matcher matcher = pattern.matcher(line);
                matcher.find();
                startRegisters.put(registerChar, Integer.parseInt(matcher.group()));
            }

            line = reader.readLine();
            line = reader.readLine();
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                program.add(Integer.parseInt(matcher.group()));
            }
        }
    }
}
