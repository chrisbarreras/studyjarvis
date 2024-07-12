import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.sl.usermodel.PictureData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class PowerPointHandler {

    public static void main(String[] args) {
        String pptxFilePath = "path/to/your/presentation.pptx"; // Change this to your PPTX file path
        String outputDir = "output/images/"; // Directory to save images

        try {
            extractImagesFromPptx(pptxFilePath, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void extractImagesFromPptx(String pptxFilePath, String outputDir) throws IOException {
        FileInputStream inputStream = new FileInputStream(pptxFilePath);
        XMLSlideShow ppt = new XMLSlideShow(inputStream);

        int imageCounter = 1;
        for (PictureData pictureData : ppt.getPictureData()) {
            String ext = pictureData.getType().extension;
            String imageFilePath = outputDir + "image" + imageCounter + "." + ext;

            try (FileOutputStream fos = new FileOutputStream(imageFilePath)) {
                fos.write(pictureData.getData());
                System.out.println("Extracted: " + imageFilePath);
                imageCounter++;
            }
        }

        ppt.close();
        inputStream.close();
    }

    public static void extractTextFromSlides(String pptxFilePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(pptxFilePath));
        XMLSlideShow ppt = new XMLSlideShow(inputStream);

        StringBuilder textContent = new StringBuilder();
        int slideCounter = 1;
        for (XSLFSlide slide : ppt.getSlides()) {
            textContent.append("Slide ").append(slideCounter).append(":\n");

            for (XSLFTextShape shape : slide.getPlaceholders()) {
                textContent.append(shape.getText()).append("\n");
            }

            slideCounter++;
        }

        // Optionally print or save the text content
        System.out.println("Extracted Text:\n" + textContent.toString());

        ppt.close();
        inputStream.close();
    }

    public static void convertSlidesToImages(String pptxFilePath, String outputDir) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(pptxFilePath));
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
            String imageFilePath = outputDir + "slide" + slideCounter + ".png";
            ImageIO.write(img, "png", new File(imageFilePath));
            System.out.println("Converted slide to image: " + imageFilePath);
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

