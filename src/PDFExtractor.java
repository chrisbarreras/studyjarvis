//import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PDFExtractor {

    public static void extract(String inputPdfPath, String outputDir) {
        try (PDDocument document = PDDocument.load(new File(inputPdfPath))) {
            extractPagesAsImages(document, outputDir);
            extractPagesAsText(document, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractPagesAsImages(PDDocument document, String outputDir) throws IOException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        Files.createDirectories(Paths.get(outputDir)); // Ensure output directory exists
        for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(pageNumber, 300); // 300 DPI for better quality
            String fileName = String.format("%s/page_%d.png", outputDir, pageNumber + 1);
            ImageIO.write(bim, "png", new File(fileName));
            System.out.println("Saved image: " + fileName);
        }
    }

    private static void extractPagesAsText(PDDocument document, String outputDir) throws IOException {
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        Files.createDirectories(Paths.get(outputDir)); // Ensure output directory exists
        for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
            pdfTextStripper.setStartPage(pageNumber + 1);
            pdfTextStripper.setEndPage(pageNumber + 1);
            String text = pdfTextStripper.getText(document);
            String fileName = String.format("%s/page_%d.txt", outputDir, pageNumber + 1);
            Files.write(Paths.get(fileName), text.getBytes());
            System.out.println("Saved text: " + fileName);
        }
    }
}
