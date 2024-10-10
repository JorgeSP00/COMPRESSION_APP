File Compressor Application
Overview
The File Compressor Application is a Java-based tool that provides an easy-to-use graphical interface for compressing files and directories. Users can choose between two types of compression: lossless and lossy. The application allows you to select files or directories to compress, specify the output file name, and choose the desired compression type.

How It Works
Select File/Directory: Users can click the "Select File/Directory" button to open a file chooser dialog. Here, you can select a single file or an entire directory for compression.

Set Output Name: Enter the desired name for the output compressed file in the provided text field. The application will suggest a default name based on the selected file.

Choose Compression Type:

Lossless (ZIP): This option preserves all original data and allows for complete restoration of the original files.
Lossy (JPEG): This option reduces file size by removing some data, suitable for images where some loss of quality is acceptable.
Compress: After making your selections, click the "Compress" button to initiate the compression process. A message will confirm the completion of the compression.

Compression Types
Lossless Compression
Definition: Lossless compression preserves all the original data without any loss. This means that the original file can be perfectly reconstructed from the compressed file.
Use Cases: Ideal for documents, executable files, and any data where integrity is crucial, such as text files or software applications.
Lossy Compression
Definition: Lossy compression reduces file size by permanently removing some data. This method sacrifices some quality for a smaller file size.
Use Cases: Commonly used for images (like JPEGs) and audio files, where a slight loss of quality is acceptable in exchange for significantly reduced file sizes.