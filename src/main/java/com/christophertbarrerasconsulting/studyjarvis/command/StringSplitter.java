package com.christophertbarrerasconsulting.studyjarvis.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringSplitter {

    public static List<String> splitStringBySpaceIgnoringQuotes(String input) {
        List<String> result = new ArrayList<>();

        if (input == null || input.isEmpty()) {
            return result; // Return an empty list if the input is null or empty
        }

        // Regular expression to match words and quoted phrases
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\S+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Add the quoted string without the quotes
                result.add(matcher.group(1));
            } else {
                // Add the normal word
                result.add(matcher.group());
            }
        }

        return result;
    }
}
