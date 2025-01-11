package com.christophertbarrerasconsulting.studyjarvis.extraction;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import org.apache.poi.xslf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class PowerPointExtractor {

    public static void extract(String powerPointFilePath, String outputFolderPath) throws IOException {
        int nextFileNumber = FileHandler.getNextFileNumber(outputFolderPath);

        extractSlidesAsImages(powerPointFilePath, outputFolderPath, nextFileNumber);
        extractSlidesAsText(powerPointFilePath, outputFolderPath, nextFileNumber);
    }

    private static void extractSlidesAsText(String powerPointFilePath, String outputFolderPath, int fileNumber) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(powerPointFilePath);
             XMLSlideShow ppt = new XMLSlideShow(inputStream)) {

            int slideNumber = 1;
            for (XSLFSlide slide : ppt.getSlides()) {
                StringBuilder textContent = new StringBuilder();

                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof XSLFTextShape textShape) {
                        textContent.append(textShape.getText()).append("\n");
                    }
                }

                if (!textContent.isEmpty()) {
                    String filePath = FileHandler.getNextFilePath(outputFolderPath, powerPointFilePath, fileNumber, slideNumber, ".txt");
                    FileHandler.writeTextToFile(textContent.toString(), filePath);
                    // logger.info("Extracted text from slide " + slideNumber + " to " + filePath);
                }
                slideNumber++;
            }
        }
    }


    private static void extractSlidesAsImages(String powerPointFilePath, String outputFolderPath, int nextFileNumber) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(new File(powerPointFilePath));
             XMLSlideShow ppt = new XMLSlideShow(inputStream)) {

            int slideCounter = 1;
            for (XSLFSlide slide : ppt.getSlides()) {
                Dimension pageSize = ppt.getPageSize();
                BufferedImage img = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                // Set background color and fill
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, img.getWidth(), img.getHeight());

                // Render the slide into the image
                slide.draw(graphics);
                graphics.dispose();

                // Save the image
                String imageFilePath = FileHandler.getNextFilePath(outputFolderPath, powerPointFilePath, nextFileNumber, slideCounter, ".png");
                ImageIO.write(img, "png", new File(imageFilePath));
//            logger.info("Converted slide to image: " + imageFilePath);
                slideCounter++;
            }
        }
    }
}

