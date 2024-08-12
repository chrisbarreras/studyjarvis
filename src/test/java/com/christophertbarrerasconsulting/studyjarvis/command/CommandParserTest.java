package com.christophertbarrerasconsulting.studyjarvis.command;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    private class DummyCommand extends Command {
        public boolean runCalled = false;
        public List<String> args;
        @Override
        public void run(List<String> args) throws IOException {
            runCalled = true;
            this.args = args;
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
        DummyCommand unrecognizedCommand = new DummyCommand();

        CommandParser commandParser = new CommandParser(List.of(new Command[]{command}), unrecognizedCommand);
        commandParser.run("foo bar");

        Assertions.assertTrue(command.runCalled);
        Assertions.assertFalse(unrecognizedCommand.runCalled);
    }

    @org.junit.jupiter.api.Test
    void runDoesntRunCommandWhenCommandTextIsNotFound() throws IOException {
        DummyCommand command = new DummyCommand();
        command.commandText = "goo";
        DummyCommand unrecognizedCommand = new DummyCommand();

        CommandParser commandParser = new CommandParser(List.of(new Command[]{command}), unrecognizedCommand);
        commandParser.run("foo bar");

        Assertions.assertFalse(command.runCalled);
        Assertions.assertTrue(unrecognizedCommand.runCalled);
    }

    @org.junit.jupiter.api.Test
    void runRunsCommandWhenShortCutIsFound() throws IOException {
        DummyCommand command = new DummyCommand();
        command.shortCut = "foo";
        DummyCommand unrecognizedCommand = new DummyCommand();

        CommandParser commandParser = new CommandParser(List.of(new Command[]{command}), unrecognizedCommand);
        commandParser.run("foo bar");

        Assertions.assertTrue(command.runCalled);
        Assertions.assertFalse(unrecognizedCommand.runCalled);
    }

    @org.junit.jupiter.api.Test
    void runDoesntRunCommandWhenShortCutIsNotFound() throws IOException {
        DummyCommand command = new DummyCommand();
        command.shortCut = "goo";
        DummyCommand unrecognizedCommand = new DummyCommand();

        CommandParser commandParser = new CommandParser(List.of(new Command[]{command}), unrecognizedCommand);
        commandParser.run("foo bar");

        Assertions.assertFalse(command.runCalled);
        Assertions.assertTrue(unrecognizedCommand.runCalled);
    }

    @org.junit.jupiter.api.Test
    void runPassesCorrectArgsToCommand() throws IOException {
        DummyCommand command = new DummyCommand();
        command.shortCut = "foo";

        CommandParser commandParser = new CommandParser(List.of(new Command[]{command}), new UnrecognizedCommand());
        commandParser.run("Foo bar");

        Assertions.assertEquals(List.of("bar"), command.args);
    }
}