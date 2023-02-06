import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Repl {
    private final Emulator emulator;

    public Repl(final Emulator emulator) {
        this.emulator = emulator;
    }

    public final void start() {
        try (final Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("mips> ");

                String commandLine = scanner.nextLine();
                if (commandLine.isEmpty()) {
                    continue;
                }

                if (!readAndProcessCommand(commandLine)) break;
            }
        }
    }

    public final void startScript(File file) {
        if (!file.exists()) {
            throw new RuntimeException("Cannot start script interpreter. File does not exist.");
        } else if (!file.canRead()) {
            throw new RuntimeException("Cannot start script interpreter. File does not allow reading.");
        }

        try (final Scanner scanner = new Scanner(new FileInputStream(file))) {
            while (true) {
                String commandLine = scanner.nextLine();
                if (commandLine.isEmpty()) {
                    continue;
                }

                System.out.printf("mips> %s%n", commandLine);
                if (!readAndProcessCommand(commandLine)) break;
            }
        } catch (IOException ex) {
            System.err.println("IOException encountered while reading script file.");
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private boolean readAndProcessCommand(String commandLine) {
        final char command = commandLine.charAt(0);
        commandLine = commandLine.substring(1).trim();

        switch (command) {
            case 'h' -> displayHelp();

            case 'd' -> emulator.dumpRegisters();

            case 's' -> {
                int n = 1;
                if (!commandLine.isEmpty()) {
                    try {
                        n = Integer.parseInt(commandLine);
                    } catch (NumberFormatException ignored) {}
                }

                emulator.emulateNInstructions(n);
                System.out.printf("\t%d instruction(s) executed%n", n);
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

                emulator.dumpMemory(lower, upper);
            }

            case 'c' -> {
                emulator.reset();
                System.out.println("\tSimulator reset");
            }

            case 'q' -> {
                return false;
            }
        }

        return true;
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
