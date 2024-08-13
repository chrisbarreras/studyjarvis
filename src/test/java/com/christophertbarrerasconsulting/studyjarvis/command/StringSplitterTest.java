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

    @org.junit.jupiter.api.Test
    void splitStringBySpaceIgnoringQuotesSplitsStringWithSpaces()
    {
        Assertions.assertEquals(List.of("foo", "bar"), StringSplitter.splitStringBySpaceIgnoringQuotes("foo bar"));
    }

    @org.junit.jupiter.api.Test
    void splitStringBySpaceIgnoringQuotesSplitsStringWithQuotationMarks()
    {
        Assertions.assertEquals(List.of("foo", "bar", "goo bar"), StringSplitter.splitStringBySpaceIgnoringQuotes("foo bar \"goo bar\""));
    }
}