package com.christophertbarrerasconsulting.studyjarvis.extraction;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.apache.poi.sl.usermodel.TextShape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Extracts text and rendered slide images from PowerPoint files.
 *
 * Supports both legacy .ppt (OLE2 binary) and modern .pptx (Office Open XML)
 * formats — SlideShowFactory sniffs the magic bytes and dispatches to
 * HSLFSlideShow or XMLSlideShow accordingly.
 */
public class PowerPointExtractor {

    public static void extract(String powerPointFilePath, String outputFolderPath) throws IOException {
        int nextFileNumber = FileHandler.getNextFileNumber(outputFolderPath);

        extractSlidesAsImages(powerPointFilePath, outputFolderPath, nextFileNumber);
        extractSlidesAsText(powerPointFilePath, outputFolderPath, nextFileNumber);
    }

    private static void extractSlidesAsText(String powerPointFilePath, String outputFolderPath, int fileNumber) throws IOException {
        try (SlideShow<?, ?> ppt = openSlideShow(powerPointFilePath)) {
            int slideNumber = 1;
            for (Slide<?, ?> slide : ppt.getSlides()) {
                StringBuilder textContent = new StringBuilder();

                for (Shape<?, ?> shape : slide.getShapes()) {
                    if (shape instanceof TextShape<?, ?> textShape) {
                        textContent.append(textShape.getText()).append("\n");
                    }
                }

                if (textContent.length() > 0) {
                    String filePath = FileHandler.getNextFilePath(outputFolderPath, powerPointFilePath, fileNumber, slideNumber, ".txt");
                    FileHandler.writeTextToFile(textContent.toString(), filePath);
                }
                slideNumber++;
            }
        }
    }

    private static void extractSlidesAsImages(String powerPointFilePath, String outputFolderPath, int nextFileNumber) throws IOException {
        try (SlideShow<?, ?> ppt = openSlideShow(powerPointFilePath)) {
            Dimension pageSize = ppt.getPageSize();
            int slideCounter = 1;

            for (Slide<?, ?> slide : ppt.getSlides()) {
                BufferedImage img = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, img.getWidth(), img.getHeight());

                slide.draw(graphics);
                graphics.dispose();

                String imageFilePath = FileHandler.getNextFilePath(outputFolderPath, powerPointFilePath, nextFileNumber, slideCounter, ".png");
                ImageIO.write(img, "png", new File(imageFilePath));
                slideCounter++;
            }
        }
    }

    private static SlideShow<?, ?> openSlideShow(String powerPointFilePath) throws IOException {
        return SlideShowFactory.create(new File(powerPointFilePath));
    }
}
