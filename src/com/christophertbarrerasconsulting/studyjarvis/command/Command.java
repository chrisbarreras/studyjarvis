package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    public abstract void run() throws IOException;
    private List<String> args = new ArrayList<>();
    public String commandText;
    public String shortCut;
    public String helpText;

    public void setArgs (List<String> args) {
        this.args.clear();
        this.args.addAll(args);
    }
}
