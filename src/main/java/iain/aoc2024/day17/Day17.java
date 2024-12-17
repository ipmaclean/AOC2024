package iain.aoc2024.day17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 {

    private static final String INPUT_FILE_NAME = "day17/input.txt";

    private final HashMap<Character, Long> inputRegisters = new HashMap<>();
    private HashMap<Character, Long> registers = new HashMap<>();
    private final List<Long> program = new ArrayList<>();

    public Day17() throws IOException {
        getInput();
    }

    public void solve() {
        solvePartOne();
        solvePartTwo();
    }

    private void solvePartOne() {
        System.out.printf("The solution to part one is %s.%n", runProgram(inputRegisters));
        System.out.printf("The input specific solution to part one is %s.%n", runRealInputProgramStreamlined(inputRegisters.get('A')));
    }

    private void solvePartTwo() {
        ArrayDeque<Long> desired = new ArrayDeque<>();
        for (long entry : program) {
            desired.add(entry);
        }
        System.out.printf("The solution to part two is %s.%n", findRegisterA(1, 8, desired));
    }

    private long findRegisterA(long registerA, long endRegisterCheck, Deque<Long> desiredProgram) {
        long currentDesired = desiredProgram.removeLast();
        while (registerA < endRegisterCheck) {
            long value = (registerA % 8) ^ (registerA / (long) Math.pow(2, (double) 7 - (registerA % 8))) % 8;
            if (value == currentDesired) {
                if (desiredProgram.isEmpty()) {
                    return registerA;
                }
                long nextA = findRegisterA(registerA * 8, registerA * 8 + 8, new ArrayDeque<>(desiredProgram));
                if (nextA != -1) {
                    return nextA;
                }
            }
            registerA++;
        }
        return -1;
    }

    private static String runRealInputProgramStreamlined(long aRegister) {
        List<String> output = new ArrayList<>();
        while (aRegister > 0) {
            output.add(String.valueOf((aRegister % 8) ^ (aRegister / (long) Math.pow(2, 7 - (aRegister % 8))) % 8));
            aRegister = aRegister / 8;
        }
        return String.join(",", output);
    }

    private String runProgram(HashMap<Character, Long> startRegisters) {
        List<String> output = new ArrayList<>();
        int instructionPointer = 0;
        registers = new HashMap<>(startRegisters);
        while (instructionPointer < program.size()) {
            long opcode = program.get(instructionPointer);
            long operand = program.get(instructionPointer + 1);
            switch ((int) opcode) {
                case 0:
                    aDivide(operand, 'A');
                    instructionPointer += 2;
                    break;
                case 1:
                    bitwiseXOR(operand, null);
                    instructionPointer += 2;
                    break;
                case 2:
                    mod8(operand);
                    instructionPointer += 2;
                    break;
                case 3:
                    long aRegister = registers.get('A');
                    instructionPointer = aRegister != 0 ? (int) operand : instructionPointer + 2;
                    break;
                case 4:
                    bitwiseXOR(operand, 'C');
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
        return String.join(",", output);
    }

    private void mod8(long operand) {
        registers.put('B', getComboOperand(operand) % 8);
    }

    private String out(long operand) {
        return String.valueOf(getComboOperand(operand) % 8);
    }

    private void aDivide(long operand, char registerKey) {
        long quotient = registers.get('A') / (int) Math.pow(2, getComboOperand(operand));
        registers.put(registerKey, quotient);
    }

    private void bitwiseXOR(long operand, Character optionalRegisterKey) {
        long variable = optionalRegisterKey != null ? registers.get(optionalRegisterKey) : operand;
        long value = registers.get('B') ^ variable;
        registers.put('B', value);
    }

    private long getComboOperand(long operand) {
        return switch ((int) operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> registers.get('A');
            case 5 -> registers.get('B');
            case 6 -> registers.get('C');
            default -> throw new IllegalArgumentException("Operand not recognised");
        };
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
                inputRegisters.put(registerChar, Long.parseLong(matcher.group()));
            }

            reader.readLine();
            line = reader.readLine();
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                program.add(Long.parseLong(matcher.group()));
            }
        }
    }
}
