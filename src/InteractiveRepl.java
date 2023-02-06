import java.util.Scanner;

public class InteractiveRepl {
    private final Emulator emulator;

    public InteractiveRepl(final Emulator emulator) {
        this.emulator = emulator;
    }

    public final void start() {
        try (final Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("mips> ");

                String commandLine = scanner.nextLine();
                final char command = commandLine.charAt(0);
                commandLine = commandLine.substring(1).trim();

                switch (command) {
                    case 'h' -> displayHelp();

                    case 'd' -> {
                        System.out.println();
                        emulator.dumpRegisters();
                        System.out.println();
                    }

                    case 's' -> {
                        int n = 1;
                        if (!commandLine.isEmpty()) {
                            try {
                                n = Integer.parseInt(commandLine);
                            } catch (NumberFormatException ignored) {}
                        }

                        emulator.emulateNInstructions(n);
                        System.out.printf("\t\t%d instruction(s) executed.\n", n);
                    }

                    case 'r' -> emulator.emulate();

                    case 'm' -> {
                        if (commandLine.isEmpty()) break;
                        String[] bounds = commandLine.split(" ");

                        int lower, upper;
                        try {
                            lower = Integer.parseInt(bounds[0]);
                            upper = Integer.parseInt(bounds[1]);
                        } catch (NumberFormatException ex) {
                            lower = 0;
                            upper = 1;

                        }

                        System.out.println();
                        emulator.dumpMemory(lower, upper);
                        System.out.println();
                    }

                    case 'c' -> {
                        emulator.reset();
                        System.out.println("\t\tSimulator reset");
                    }

                    case 'q' -> {
                        return;
                    }
                }
            }
        }
    }

    private void displayHelp() {
        System.out.println("""
                h = show help
                d = dump register state
                s = single step through the program (i.e. execute 1 instruction and stop)
                s num = step through num instructions of the program
                r = run until the program ends
                m num1 num2 = display data memory from location num1 to num2
                c = clear all registers, memory, and the program counter to 0
                q = exit the program
                """);
    }
}
