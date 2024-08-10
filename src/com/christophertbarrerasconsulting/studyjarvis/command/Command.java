package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    public abstract void run(List<String> args) throws IOException;
    public String commandText;
    public String shortCut;
    public String helpText;
}
