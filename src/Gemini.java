import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.cloud.vertexai.generativeai.PartMaker;

import java.io.IOException;

public class Gemini {
    private String projectId;
    private String location;
    private String modelName;

    public Gemini(String projectId, String modelName, String location) {
        this.modelName = modelName; // "gemini-1.0-pro-002" or "gemini-1.0-pro-001";
        this.location = location;
        this.projectId = projectId;
    }

    public String textInput(String textPrompt) throws IOException {
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            String output;
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);

            GenerateContentResponse response = model.generateContent(textPrompt);
            output = ResponseHandler.getText(response);
            return output;
        }
    }

    public String imageInput(String imageUri, String mimeType, String textPrompt)
            throws IOException {
        // Initialize client that will be used to send requests. This client only needs
        // to be created once, and can be reused for multiple requests.
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
//            String imageUri = "gs://cloud-samples-data/vertex-ai/llm/prompts/landmark1.png";

            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
                    PartMaker.fromMimeTypeAndData(mimeType, imageUri),
                    textPrompt
            ));

            return response.toString();
        }
    }
}
