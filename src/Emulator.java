import java.util.List;

public class Emulator {
    private final List<Instruction> instructions;
    private final int[] registers = new int[32];
    private final int[] memory = new int[8192];

    private int programCounter = 0;

    public Emulator(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public final int readRegister(final int registerIdx) {
        checkInBounds(registerIdx, 0, 31, "Tried to load invalid register.");
        return registers[registerIdx];
    }

    public final void writeRegister(final int registerIdx, final int value) {
        checkInBounds(registerIdx, 0, 31, "Tried to write invalid register.");
        checkNotEq(registerIdx, 0, "Cannot write to register 0.");
        registers[registerIdx] = value;
    }

    public final void dumpRegisters() {
        for (int i = 0; i < registers.length; i++) {
            System.out.printf("%02d %-2s\t%-10d\n", i, Register.getByRegisterNumber(i).registerName(), readRegister(i));
        }
    }

    public final int readMemory(final int memoryIdx) {
        checkInBounds(memoryIdx, 0, 8192, "Tried to read invalid memory address.");
        return memory[memoryIdx];
    }

    public final void writeMemory(final int memoryIdx, final int value) {
        checkInBounds(memoryIdx, 0, 8192, "Tried to write invalid memory address.");
        memory[memoryIdx] = value;
    }

    private void checkInBounds(final int value, final int low, final int high, final String msg) {
        if (value < low || value > high) {
            throw new IllegalArgumentException(msg);
        }
    }

    private void checkNotEq(final int value, final int other, final String msg) {
        if (value == other) {
            throw new IllegalArgumentException(msg);
        }
    }

    public void emulate() {
        for (; programCounter < instructions.size(); programCounter++) {
            final Instruction currentInstruction = instructions.get(programCounter);

            System.out.printf("Running: %s\n", currentInstruction);

            if (currentInstruction instanceof RFormatInstruction inst) {
                switch (inst.opcode()) {
                    case ADD -> writeRegister(inst.rd(), readRegister(inst.rs()) + readRegister(inst.rt()));
                    case AND -> writeRegister(inst.rd(), readRegister(inst.rs()) & readRegister(inst.rt()));
                    case OR -> writeRegister(inst.rd(), readRegister(inst.rs()) | readRegister(inst.rt()));
                    case SLL -> writeRegister(inst.rd(), readRegister(inst.rt()) << inst.shamt());
                    case SUB -> writeRegister(inst.rd(), readRegister(inst.rs()) - readRegister(inst.rt()));
                    case SLT -> writeRegister(inst.rd(), readRegister(inst.rs()) < readRegister(inst.rt()) ? 1 : 0);
                    case JR -> {
                        programCounter = readRegister(inst.rs());
                        continue;
                    }
                }
            } else if (currentInstruction instanceof IFormatInstruction inst) {
                switch (inst.opcode()) {
                    case ADDI -> writeRegister(inst.rt(), readRegister(inst.rs()) + inst.imm());
                    case BEQ -> {
                        if (readRegister(inst.rs()) == readRegister(inst.rt())) {
                            programCounter += 1 + inst.imm();
                            continue;
                        }
                    }
                    case BNE -> {
                        if (readRegister(inst.rs()) != readRegister(inst.rt())) {
                            programCounter += 1 + inst.imm();
                            continue;
                        }
                    }
                    case LW -> writeRegister(inst.rt(), readMemory(readRegister(inst.rs()) + inst.imm()));
                    case SW -> writeMemory(readRegister(inst.rs()) + inst.imm(), readRegister(inst.rt()));
                }
            } else if (currentInstruction instanceof JFormatInstruction inst) {
                switch (inst.opcode()) {
                    case J -> programCounter = inst.address();
                    case JAL -> {
                        writeRegister(31, programCounter + 1);
                        programCounter = inst.address();
                    }
                }
                continue;
            }

            dumpRegisters();
        }
    }
}
