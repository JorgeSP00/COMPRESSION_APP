import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class Main {

    // Main method
    public static void main(String[] args) {
        // Check if the correct arguments are provided
        if (args.length < 2) {
            System.err.println("Usage: java Main <file or directory to compress> <output zip file>");
            System.exit(1);
        }

        String sourcePath = args[0]; // Could be a file or directory
        String zipFile = args[1];

        try {
            compressToZip(sourcePath, zipFile);
            System.out.println("Compression completed successfully.");
        } catch (IOException e) {
            System.err.println("Error during compression: " + e.getMessage());
        }
    }

    // Method to compress a file or directory into a ZIP file
    public static void compressToZip(String sourcePath, String zipFilePath) throws IOException {
        Path zipPath = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Path source = Paths.get(sourcePath);

            // Check if the source is a file or a directory
            if (Files.isDirectory(source)) {
                // If it's a directory, walk the directory tree
                Files.walk(source)
                    .filter(path -> !Files.isDirectory(path)) // Ignore directories
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(source.relativize(path).toString());
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            Files.copy(path, zipOutputStream); // Copy file content to ZIP
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Error compressing file: " + path + " - " + e.getMessage());
                        }
                    });
            } else if (Files.isRegularFile(source)) {
                // If it's a regular file, compress it directly
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