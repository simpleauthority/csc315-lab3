public abstract class Instruction {
    private final int opcode;

    public Instruction(final int opcode) {
        this.opcode = opcode;
    }

    public int opcode() {
        return opcode;
    }

    public abstract String assemble();
}
