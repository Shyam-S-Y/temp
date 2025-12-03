package com.idcard.photo;

import net.coobird.thumbnailator.Thumbnails;
import java.io.File;
import java.io.IOException;

public class PhotoResizer {

    // Resize photo to a specific width (height adjusts automatically)
    public File resizeToWidth(File inputFile, int targetWidth, String outputPath) {
        try {
            File outputFile = new File(outputPath);

            Thumbnails.of(inputFile)
                    .width(targetWidth)
                    .toFile(outputFile);

            System.out.println("✓ Photo resized successfully to width: " + targetWidth + "px");
            return outputFile;

        } catch (IOException e) {
            System.out.println("✗ Error resizing photo: " + e.getMessage());
            return null;
        }
    }

    // Resize to exact dimensions (might distort if ratio is different)
    public File resizeExact(File inputFile, int width, int height, String outputPath) {
        try {
            File outputFile = new File(outputPath);

            Thumbnails.of(inputFile)
                    .size(width, height)
                    .toFile(outputFile);

            System.out.println("✓ Photo resized to exact size: " + width + "x" + height + "px");
            return outputFile;

        } catch (IOException e) {
            System.out.println("✗ Error resizing photo: " + e.getMessage());
            return null;
        }
    }

    // Resize to fit within max dimensions (keeps aspect ratio)
    public File resizeToFit(File inputFile, int maxWidth, int maxHeight, String outputPath) {
        try {
            File outputFile = new File(outputPath);

            Thumbnails.of(inputFile)
                    .size(maxWidth, maxHeight)
                    .keepAspectRatio(true)
                    .toFile(outputFile);

            System.out.println("✓ Photo resized to fit within: " + maxWidth + "x" + maxHeight + "px");
            return outputFile;

        } catch (IOException e) {
            System.out.println("✗ Error resizing photo: " + e.getMessage());
            return null;
        }
    }
}