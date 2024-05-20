import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import java.io.IOException;

public class Gemini {
    private String projectId;
    private String location;
    private String modelName;

    public Gemini(String projectId, String location) {
        this.modelName = "gemini-1.0-pro-002";
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
}
