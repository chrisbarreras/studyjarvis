package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.google.api.gax.rpc.ResourceExhaustedException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.cloud.vertexai.generativeai.PartMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Supplier;

public class Gemini implements AutoCloseable{
    private static final Logger logger = LoggerFactory.getLogger(Gemini.class);

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
        logger.info("Gemini multi-modal input: {} URI(s)", uris.length);
        for (String uri : uris){
            String mime = FileHandler.mimeTypeFromUri(uri);
            if ("text/plain".equals(mime)) {
                String text = downloadText(uri);
                logger.info("  part: mime={} inlined-text={}chars uri={}", mime, text.length(), uri);
                parts.add(text);
            } else {
                logger.info("  part: mime={} uri={}", mime, uri);
                parts.add(PartMaker.fromMimeTypeAndData(mime, uri));
            }
        }
    }

    // Vertex AI rejects text/plain via fileData on Gemini 2.x; it expects text
    // as an inline string part. Fetch the object's bytes from GCS and return
    // them as a UTF-8 string so the caller can add it as a text part.
    private static String downloadText(String gsUri) {
        String stripped = gsUri.substring("gs://".length());
        int slash = stripped.indexOf('/');
        String bucketName = stripped.substring(0, slash);
        String blobName = stripped.substring(slash + 1);
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob blob = storage.get(bucketName, blobName);
        return new String(blob.getContent(), StandardCharsets.UTF_8);
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
