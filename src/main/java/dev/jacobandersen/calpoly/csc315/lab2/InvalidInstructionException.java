//package dev.jacobandersen.calpoly.csc315.lab2;

public class InvalidInstructionException extends RuntimeException {
    public InvalidInstructionException(String message) {
        super(String.format("invalid instruction: %s", message));
    }
}
