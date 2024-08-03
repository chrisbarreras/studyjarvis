import java.io.IOException;

public class Jarvis {
    Gemini gemini;
    GoogleBucket bucket;

    public Jarvis(String bucketName, String geminiProjectID, String geminiModelName, String geminiLocation) {
        gemini = new Gemini(geminiProjectID, geminiModelName, geminiLocation);
        bucket = new GoogleBucket(bucketName);
        gemini.initializeMultiModalInput(bucket.getURIs().toArray(new String[0]));
    }

    public String createQuiz(int numberOfQuestions) throws IOException {
        return gemini.respond("Create a quiz with this many questions and put answers at the bottom: " + numberOfQuestions);
    }
}
