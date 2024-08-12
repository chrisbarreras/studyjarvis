package com.christophertbarrerasconsulting.studyjarvis.extraction;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import org.apache.poi.xslf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class PowerPointExtractor {

    public static void extract(String powerPointFilePath, String outputFolderPath) throws IOException {
        int nextFileNumber = FileHandler.getNextFileNumber(outputFolderPath);

        extractSlidesAsImages(powerPointFilePath, outputFolderPath, nextFileNumber);
        extractSlidesAsText(powerPointFilePath, outputFolderPath, nextFileNumber);
    }

    public static void extractSlidesAsText(String powerPointFilePath, String outputFolderPath, int fileNumber) throws IOException {
        FileInputStream inputStream = new FileInputStream(powerPointFilePath);
        XMLSlideShow ppt = new XMLSlideShow(inputStream);


        int slideNumber = 1;
        for (XSLFSlide slide : ppt.getSlides()) {
            StringBuilder textContent = new StringBuilder();
            for (XSLFTextShape shape : slide.getPlaceholders()) {
                textContent.append(shape.getText()).append("\n");
            }
            if (!textContent.isEmpty()) {
                FileHandler.writeTextToFile(textContent.toString(), FileHandler.getNextFilePath(outputFolderPath, powerPointFilePath, fileNumber, slideNumber, ".txt"));
            }
            slideNumber++;
        }

        ppt.close();
        inputStream.close();
    }

    public static void extractSlidesAsImages(String powerPointFilePath, String outputFolderPath, int nextFileNumber) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(powerPointFilePath));
        XMLSlideShow ppt = new XMLSlideShow(inputStream);

        int slideCounter = 1;
        for (XSLFSlide slide : ppt.getSlides()) {
            BufferedImage img = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();

            // Set background color and fill
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, img.getWidth(), img.getHeight());

            // Render the slide into the image
            slide.draw(graphics);

            // Save the image
            String imageFilePath = FileHandler.getNextFilePath(outputFolderPath, powerPointFilePath, nextFileNumber, slideCounter, ".png");
            ImageIO.write(img, "png", new File(imageFilePath));
//            System.out.println("Converted slide to image: " + imageFilePath);
            slideCounter++;
        }

        ppt.close();
        inputStream.close();
    }

    public static void Test() throws IOException {
        XMLSlideShow ppt = new XMLSlideShow();
        ppt.close();
    }
}

