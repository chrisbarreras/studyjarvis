package com.christophertbarreras.studyjarvis.extraction;

import com.christophertbarrerasconsulting.studyjarvis.command.CommandParser;

import java.io.IOException;

public class GeminiFunctionalTest {
    @org.junit.jupiter.api.Test
    void imageInputReturns () throws IOException {
        CommandParser.getInstance().run("ls");

    }
}
