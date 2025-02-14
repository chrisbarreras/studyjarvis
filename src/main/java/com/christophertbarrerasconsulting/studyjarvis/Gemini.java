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
import java.util.function.Function;
import java.util.function.Supplier;

public class Gemini implements AutoCloseable{
    private final String projectId;
    private final String location;
    private final String modelName;
    VertexAI vertexAI;
    GenerativeModel generativeModel;
    ArrayList<Object> parts;

    public Gemini(String projectId, String modelName, String location) {
        this.modelName = modelName; // "gemini-1.0-pro-002" or "gemini-1.0-pro-001";
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

//    public String imageInput(String imageUri, String mimeType, String textPrompt)
//            throws IOException {
//        // Initialize client that will be used to send requests. This client only needs
//        // to be created once, and can be reused for multiple requests.
//        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
//            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
//            GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
//                    PartMaker.fromMimeTypeAndData(mimeType, imageUri),
//                    textPrompt
//            ));
//
//            return response.toString();
//        }
//    }
//
//    public String multiModalInput(String[] imageUris, String [] textPrompts, String textPrompt)
//            throws IOException {
//        // Initialize client that will be used to send requests. This client only needs
//        // to be created once, and can be reused for multiple requests.
//        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
//            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
//
//            Object[] parts = new Object[imageUris.length + textPrompts.length + 1];
//            for (int i = 0; i < imageUris.length; i++) {
////                String mimeType = com.christophertbarrerasconsulting.studyjarvis.file.ImageHandler.mimeTypeFromImageUri(imageUris[i]);
////                byte[] imageBytes = com.christophertbarrerasconsulting.studyjarvis.file.ImageHandler.readImageFile(imageUris[i]);
////                parts[i] = PartMaker.fromMimeTypeAndData(mimeType, imageBytes);
//                parts[i] = imageUris[i];
//            }
//            for (int i = 0; i < textPrompts.length; i++) {
//                parts[i + imageUris.length] = textPrompts[i];
//            }
//            parts[parts.length-1] = textPrompt;
//
//            Content content = ContentMaker.fromMultiModalData(parts);
//            GenerateContentResponse response = model.generateContent(content);
//
//            return response.toString();
//        }
//    }

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
