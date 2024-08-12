package com.christophertbarrerasconsulting.studyjarvis.command;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    private class DummyCommand extends Command {
        public boolean runCalled = false;
        @Override
        public void run(List<String> args) throws IOException {
            runCalled = true;
        }
    }

    @org.junit.jupiter.api.Test
    void commandsHaveNoDuplicateShortcuts() {
        for (Command command: CommandParser.getInstance().commands){
            for (Command c: CommandParser.getInstance().commands) {
                if (c != command) {
                    Assertions.assertNotEquals(command.shortCut, c.shortCut);
                }
            }
        }
    }

    @org.junit.jupiter.api.Test
    void runRunsCommandWhenCommandTextIsFound() throws IOException {
        DummyCommand command = new DummyCommand();
        command.commandText = "foo";

        CommandParser commandParser = new CommandParser(List.of(new Command[]{command}));
        commandParser.run("foo bar");

        Assertions.assertTrue(command.runCalled);
    }

    @org.junit.jupiter.api.Test
    void runDoesntRunCommandWhenCommandTextIsNotFound() throws IOException {
        DummyCommand command = new DummyCommand();
        command.commandText = "goo";

        CommandParser commandParser = new CommandParser(List.of(new Command[]{command}));
        commandParser.run("foo bar");

        Assertions.assertFalse(command.runCalled);
    }
}