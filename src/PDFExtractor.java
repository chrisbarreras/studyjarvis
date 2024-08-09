//import org.apache.pdfbox.Loader;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.commons.logging.LogFactory;
import org.apache.fontbox.ttf.TTFParser;

public class PDFExtractor {

    public static void extract(String inputPdfPath, String outputDir) {
        int nextFileNumber = FileHandler.getNextFileNumber(outputDir);

        try (PDDocument document = Loader.loadPDF(new File(inputPdfPath))) {
            extractPagesAsImages(inputPdfPath, outputDir, nextFileNumber);
            extractPagesAsText(inputPdfPath, outputDir, nextFileNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractPagesAsImages(String inputPdfPath, String outputDir, int nextFileNumber) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(inputPdfPath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            Files.createDirectories(Paths.get(outputDir)); // Ensure output directory exists
            for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(pageNumber, 300); // 300 DPI for better quality
                String filePath = FileHandler.getNextFilePath(outputDir, inputPdfPath, nextFileNumber, pageNumber + 1, ".png");
                ImageIO.write(bim, "png", new File(filePath));
                System.out.println("Saved image: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractPagesAsText(String inputPdfPath, String outputDir, int nextFileNumber) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(inputPdfPath))) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            Files.createDirectories(Paths.get(outputDir)); // Ensure output directory exists
            for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
                pdfTextStripper.setStartPage(pageNumber + 1);
                pdfTextStripper.setEndPage(pageNumber + 1);
                String text = pdfTextStripper.getText(document);
                String filePath = FileHandler.getNextFilePath(outputDir, inputPdfPath, nextFileNumber, pageNumber + 1, ".txt");
                Files.write(Paths.get(filePath), text.getBytes());
                System.out.println("Saved text: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
