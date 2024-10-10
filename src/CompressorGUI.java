import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CompressorGUI {
    private JFrame frame;
    private JButton dirButton;
    private JTextField zipNameField;
    private JLabel selectedFileLabel;
    private JButton compressButton;
    private JFileChooser fileChooser;
    private String sourceDir;
    private String zipFile;
    private JRadioButton losslessButton;
    private JRadioButton lossyButton;
    private ButtonGroup compressionTypeGroup;

    public CompressorGUI() {
        // Create the main frame
        frame = new JFrame("Compressor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new GridLayout(5, 1, 10, 10)); // Using GridLayout for better structure
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Create a panel for file selection
        JPanel filePanel = new JPanel(new FlowLayout());
        dirButton = new JButton("Select File/Directory");
        selectedFileLabel = new JLabel("No file or directory selected");
        selectedFileLabel.setPreferredSize(new Dimension(300, 30));

        filePanel.add(dirButton);
        filePanel.add(selectedFileLabel);

        // Create a panel for output file name
        JPanel outputPanel = new JPanel(new FlowLayout());
        JLabel zipNameLabel = new JLabel("Output Name:");
        zipNameField = new JTextField();
        zipNameField.setPreferredSize(new Dimension(200, 30));
        outputPanel.add(zipNameLabel);
        outputPanel.add(zipNameField);

        // Create a panel for compression options
        JPanel compressionPanel = new JPanel(new FlowLayout());
        compressionPanel.setBorder(BorderFactory.createTitledBorder("Compression Type"));

        losslessButton = new JRadioButton("Lossless (ZIP)", true);
        lossyButton = new JRadioButton("Lossy (JPEG)");
        JButton losslessInfoButton = new JButton("ℹ");
        JButton lossyInfoButton = new JButton("ℹ");

        compressionPanel.add(losslessButton);
        compressionPanel.add(losslessInfoButton);
        compressionPanel.add(lossyButton);
        compressionPanel.add(lossyInfoButton);

        // Group radio buttons
        compressionTypeGroup = new ButtonGroup();
        compressionTypeGroup.add(losslessButton);
        compressionTypeGroup.add(lossyButton);

        // Add compress button
        compressButton = new JButton("Compress");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(compressButton);

        // Add components to the main frame in GridLayout order
        frame.add(filePanel); // Step 1: Select File
        frame.add(outputPanel); // Step 2: Input Zip Name
        frame.add(compressionPanel); // Step 3: Choose Compression Type
        frame.add(buttonPanel); // Step 4: Compress Button

        // File chooser for selecting file or directory
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        // Action listener for selecting the file or directory
        dirButton.addActionListener(e -> {
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                sourceDir = selectedFile.getAbsolutePath();
                selectedFileLabel.setText("Selected: " + selectedFile.getName());

                // Set the default output name based on the selected file
                String defaultOutputName = selectedFile.getName();

                // Remove the file extension if present
                if (defaultOutputName.contains(".")) {
                    defaultOutputName = defaultOutputName.substring(0, defaultOutputName.lastIndexOf('.'));
                }

                // Set the default extension based on compression type
                if (losslessButton.isSelected()) {
                    zipNameField.setText(defaultOutputName);
                } else if (lossyButton.isSelected()) {
                    zipNameField.setText(defaultOutputName);
                }
            }
        });


        // Action listener for compress button
        compressButton.addActionListener(e -> {
            String outputName = zipNameField.getText();

            if (sourceDir == null) {
                JOptionPane.showMessageDialog(frame, "Please select a file or directory.");
            } else if (outputName == null || outputName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a name for the output.");
            } else {
                File sourceFile = new File(sourceDir);

                // Lossless ZIP Compression
                if (losslessButton.isSelected()) {
                    if (!outputName.endsWith(".zip")) {
                        outputName += ".zip";
                    }
                    zipFile = sourceFile.getParent() + File.separator + outputName;

                    try {
                        Compressor.compressLossless(sourceDir, zipFile);
                        JOptionPane.showMessageDialog(frame, "Lossless ZIP compression completed.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    }

                // Lossy JPEG Compression
                } else if (lossyButton.isSelected()) {
                    if (!isImageFile(sourceFile)) {
                        JOptionPane.showMessageDialog(frame, "The selected file is not a valid image for lossy compression.");
                        return;
                    }

                    if (!outputName.endsWith(".jpg")) {
                        outputName += ".jpg";
                    }

                    String outputPath = sourceFile.getParent() + File.separator + outputName;

                    try {
                        Compressor.compressLossy(sourceDir, outputPath, 0.5f); // 50% quality
                        JOptionPane.showMessageDialog(frame, "Lossy JPEG compression completed.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    }
                }
            }
        });

        // Action listener for info button (Lossless)
        losslessInfoButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "Lossless compression preserves all original data.\n" +
                    "Use this for files where exact data restoration is required, like documents or executables.",
                    "Lossless Compression", JOptionPane.INFORMATION_MESSAGE);
        });

        // Action listener for info button (Lossy)
        lossyInfoButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "Lossy compression reduces file size by removing some data.\n" +
                    "Use this for images where some loss of quality is acceptable, like JPEG images.",
                    "Lossy Compression", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setVisible(true);
    }

    // Helper method to check if the file is an image (JPEG)
    private boolean isImageFile(File file) {
        String[] validExtensions = { ".jpg", ".jpeg" };
        String fileName = file.getName().toLowerCase();

        for (String ext : validExtensions) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        new CompressorGUI();
    }
}