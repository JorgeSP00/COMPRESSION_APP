import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class Compressor {

    // Method to compress an image file with lossy compression (JPEG format)
    public static void compressLossy(String inputImagePath, String outputImagePath, float quality) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage image = ImageIO.read(inputFile);

        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();

        if (jpgWriteParam.canWriteCompressed()) {
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(quality);  // Quality range from 0.0 to 1.0
        }

        File outputFile = new File(outputImagePath);
        try (ImageOutputStream outputStream = ImageIO.createImageOutputStream(outputFile)) {
            jpgWriter.setOutput(outputStream);
            jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);
        } finally {
            jpgWriter.dispose();
        }
    }

    // Method to compress a file or directory into a ZIP file (Lossless)
    public static void compressLossless(String sourcePath, String zipFilePath) throws IOException {
        Path zipPath = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Path source = Paths.get(sourcePath);

            if (Files.isDirectory(source)) {
                Files.walk(source)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(source.relativize(path).toString());
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            Files.copy(path, zipOutputStream);
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Error compressing file: " + path + " - " + e.getMessage());
                        }
                    });
            } else if (Files.isRegularFile(source)) {
                ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
                try {
                    zipOutputStream.putNextEntry(zipEntry);
                    Files.copy(source, zipOutputStream);
                    zipOutputStream.closeEntry();
                } catch (IOException e) {
                    System.err.println("Error compressing file: " + source + " - " + e.getMessage());
                }
            } else {
                System.err.println("The specified source is neither a file nor a directory.");
            }
        }
    }
}