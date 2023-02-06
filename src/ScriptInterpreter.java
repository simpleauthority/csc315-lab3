import java.util.List;

public class ScriptInterpreter {
    private final Emulator emulator;
    private final List<String> commands;

    public ScriptInterpreter(final Emulator emulator, final List<String> commands) {
        this.emulator = emulator;
        this.commands = commands;
    }

    public final void interpret() {

    }
}
