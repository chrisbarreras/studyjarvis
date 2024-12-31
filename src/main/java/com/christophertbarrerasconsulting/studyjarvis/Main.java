package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.command.Command;
import com.christophertbarrerasconsulting.studyjarvis.command.CommandSession;
import com.christophertbarrerasconsulting.studyjarvis.command.LoadLocalSettingsCommand;
import org.apache.poi.openxml4j.util.ZipSecureFile;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        // TODO:

//        Display code responses
//        Web UI
//        Multi User
//        Android App

//        String slidesDir = "C:\\slides";

        ZipSecureFile.setMinInflateRatio(0.002);
        Command command = new LoadLocalSettingsCommand();
        command.run(new ArrayList<>());

        CommandSession.start();
    }
}