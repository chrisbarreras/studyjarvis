package com.christophertbarrerasconsulting.studyjarvis.command;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringSplitterTest {

    @org.junit.jupiter.api.Test
    void splitStringBySpaceIgnoringQuotesSplitsStringWithNoSpaces()
    {
        Assertions.assertEquals(List.of("foo"), StringSplitter.splitStringBySpaceIgnoringQuotes("foo"));
    }
}