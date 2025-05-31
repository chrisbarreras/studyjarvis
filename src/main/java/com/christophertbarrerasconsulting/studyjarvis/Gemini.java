package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.google.api.gax.rpc.ResourceExhaustedException;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.cloud.vertexai.generativeai.PartMaker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

public class Gemini implements AutoCloseable{
    private final String projectId;
    private final String location;
    private final String modelName;
    VertexAI vertexAI;
    GenerativeModel generativeModel;
    ArrayList<Object> parts;

    public Gemini(String projectId, String modelName, String location) {
        this.modelName = modelName;
        this.location = location;
        this.projectId = projectId;
        vertexAI = new VertexAI(projectId, location);
        generativeModel = new GenerativeModel(modelName, vertexAI);
        parts = new ArrayList<>();
    }

    @Override public void close(){
        vertexAI.close();
    }

    public String textInput(String textPrompt) throws IOException {
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            String output;
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);

            GenerateContentResponse response = generateContent(model, textPrompt);
            output = ResponseHandler.getText(response);
            return output;
        }
    }

    public void initializeMultiModalInput(String[] uris) {
        parts.clear();
        for (String uri : uris){
            parts.add(PartMaker.fromMimeTypeAndData(FileHandler.mimeTypeFromUri(uri), uri));
        }
    }

    public String respond(String prompt) throws IOException {
        ArrayList<Object> partsPlusPrompt = new ArrayList<>(parts);
        partsPlusPrompt.add(prompt);

        Content content = ContentMaker.fromMultiModalData(partsPlusPrompt.toArray());
        GenerateContentResponse response = generateContent(generativeModel, content);

        String responseText = ResponseHandler.getText(response);
        parts.add("Prompt:\n\"" + prompt + "\"\n\n" + "Response:\n\"" + responseText + "\"");
        return responseText;
    }

    private GenerateContentResponse generateContent (GenerativeModel model, String textInput) throws IOException {
        return tryGenerateContentResponse(()-> {
            try {
                return model.generateContent(textInput);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 5);
    }

    private GenerateContentResponse generateContent (GenerativeModel model, Content content) throws IOException {
        return tryGenerateContentResponse(()-> {
            try {
                return model.generateContent(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 5);
    }

    private GenerateContentResponse tryGenerateContentResponse (Supplier<GenerateContentResponse> supplier, int numberOfAttempts) throws IOException {
        int attemptCount = 1;
        do {
            try {
                return supplier.get();
            } catch (ResourceExhaustedException e) {
                try {
                    System.out.println("Retrying after attempt number " + attemptCount);
                    if (attemptCount++ <= numberOfAttempts) {
                        Thread.sleep(2000);
                    }
                    else {
                        throw e;
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
        } while (true);
    }
}
