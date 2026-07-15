package com.mycompany.gsgui;
import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

/**
 * <p>
 * An image with a set of operations applied to it.
 * </p>
 *
 * <p>
 * The EditableImage represents an image with a series of operations applied to
 * it. It is fairly core to the ANDIE program, being the central data structure.
 * The operations are applied to a copy of the original image so that they can
 * be undone. THis is what is meant by "A Non-Destructive Image Editor" - you
 * can always undo back to the original image.
 * </p>
 *
 * <p>
 * Internally the EditableImage has two {@link BufferedImage}s - the original
 * image and the result of applying the current set of operations to it. The
 * operations themselves are stored on a {@link Stack}, with a second
 * {@link Stack} being used to allow undone operations to be redone.
 * </p>
 *
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>

 */
class EditableImage {

    /**
     * The original image. This should never be altered by ANDIE.
     */
    private BufferedImage original;

    /**
     * The current image, the result of applying {@link ops} to
     * {@link original}.
     */
    private BufferedImage current;

    /**
     * The sequence of operations currently applied to the image.
     */
    private Stack<ImageOperation> ops;

    /**
     * A memory of 'undone' operations to support 'redo'.
     */
    private Stack<ImageOperation> redoOps;

    /**
     * recorded ops that are opened or just recorded will be here
     */
    private Stack<ImageOperation> recordedOps;

    /**
     * the ops file name of last recorded file
     */
    private String recordedOpsName;
    /**
     * The file where the original image is stored/
     */
    private String imageFilename;

    /**
     * The file where the operation sequence is stored.
     */
    private String opsFilename;

    /**
     * a boolean to check if the image has been saved before an op
     */
    public static boolean isSavedAfterOp;

    /**
     * a boolean to determine if the program is recording
     */
    private static boolean isRecording;

    /**
     * <p>
     * Create a new EditableImage.
     * </p>
     *
     * <p>
     * A new EditableImage has no image (it is a null reference), and an empty
     * stack of operations.
     * </p>
     */
    public EditableImage() {
        original = null;
        current = null;
        ops = new Stack<>();
        redoOps = new Stack<>();
        recordedOps = new Stack<>();
        recordedOpsName = null;
        imageFilename = null;
        opsFilename = null;
        isSavedAfterOp = false;
        isRecording = false;
    }

    /**
     * <p>
     * Check if there is an image loaded.
     * </p>
     *
     * @return True if there is an image, false otherwise.
     */
    public boolean hasImage() {
        return current != null;
    }

    /**
     * <p>
     * Make a 'deep' copy of a BufferedImage.
     * </p>
     *
     * <p>
     * Object instances in Java are accessed via references, which means that
     * assignment does not copy an object, it merely makes another reference to
     * the original. In order to make an independent copy, the {@code clone()}
     * method is generally used. {@link BufferedImage} does not implement
     * {@link Cloneable} interface, and so the {@code clone()} method is not
     * accessible.
     * </p>
     *
     * <p>
     * This method makes a cloned copy of a BufferedImage. This requires
     * knowledge of some details about the internals of the BufferedImage, but
     * essentially comes down to making a new BufferedImage made up of copies of
     * the internal parts of the input.
     * </p>
     *
     * <p>
     * This code is taken from StackOverflow:
     * <a href="https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
     * in response to
     * <a href="https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
     * Code by Klark used under the CC BY-SA 2.5 license.
     * </p>
     *
     * </p>
     * Updated by MG 2025 to fix an error that occurs when copying a sub-image.
     * </p>
     *
     * <p>
     * This method (only) is released under
     * <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA
     * 2.5</a>
     * </p>
     *
     * @param bi The BufferedImage to copy.
     * @return A deep copy of the input.
     */
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * <p>
     * Open an image from a file.
     * </p>
     *
     * <p>
     * Opens an image from the specified file. Also tries to open a set of
     * operations from the file with <code>.ops</code> added. So if you open
     * <code>some/path/to/image.png</code>, this method will also try to read
     * the operations from <code>some/path/to/image.png.ops</code>.
     * </p>
     *
     * @param filePath The file to open the image from.
     * @throws Exception If something goes wrong.
     */
    public void open(String filePath) throws Exception {
        imageFilename = filePath;
        opsFilename = imageFilename + ".ops";
        File imageFile = new File(imageFilename);
        original = ImageIO.read(imageFile);
         if (original == null) {
        throw new IOException("ImageIO returned null for: " + filePath);
    }
        current = deepCopy(original);

        try (
                FileInputStream fileIn = new FileInputStream(this.opsFilename); ObjectInputStream objIn = new ObjectInputStream(fileIn);) {
            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
            ops = opsFromFile;
            redoOps.clear();
            objIn.close();
            fileIn.close();
        } catch (Exception ex) {
            // Could be no file or something else. Carry on for now.
            ops.clear();
            redoOps.clear();
        }
        this.refresh();
        isSavedAfterOp = true;
    }

    /**
     * <p>
     * Open an operations file.
     * </p>
     *
     * <p>
     * Opens a set of operations from the specified file. The file should be an
     * <code>.ops</code> file. If a plain image path is given, it will append
     * <code>.ops</code> to the path automatically.
     * </p>
     *
     * @param filePath The file to open the operations from.
     * @throws Exception If something goes wrong.
     */
    public void openOps(String filePath) throws Exception {

        this.recordedOpsName = filePath.endsWith(".ops") ? filePath : filePath + ".ops";

        try (
                FileInputStream fileIn = new FileInputStream(this.recordedOpsName); ObjectInputStream objIn = new ObjectInputStream(fileIn);) {
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
            recordedOps.clear();
            recordedOps = opsFromFile;
        } catch (Exception ex) {
            // Could be no file or something else. Carry on for now.
            recordedOps.clear();
        }

        isSavedAfterOp = true;
    }

    /**
     * <p>
     * Save an image to file.
     * </p>
     *
     * <p>
     * Saves an image to the file it was opened from, or the most recent file
     * saved as. Also saves a set of operations from the file with
     * <code>.ops</code> added. So if you save to
     * <code>some/path/to/image.png</code>, this method will also save the
     * current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     *
     * @throws Exception If something goes wrong.
     */
    public void save() throws Exception {
        if (this.opsFilename == null) {
            this.opsFilename = this.imageFilename + ".ops";
        }
        // Write image file based on file extension
        String extension = imageFilename.substring(1 + imageFilename.lastIndexOf(".")).toLowerCase();
        ImageIO.write(original, extension, new File(imageFilename));
        try ( // Write operations file
                FileOutputStream fileOut = new FileOutputStream(this.opsFilename); ObjectOutputStream objOut = new ObjectOutputStream(fileOut);) {
            objOut.writeObject(this.ops);
        }
        isSavedAfterOp = true;

    }

    /**
     * <p>
     * Export the current edited image to a specified file path.
     * </p>
     *
     * <p>
     * Writes the current image (with all operations applied) to the file at the
     * given path. The file format is determined by the file extension in the
     * path. Unlike save(), this does not write an .ops file alongside the
     * image, and does not alter the stored image or operations filenames.
     * </p>
     *
     * @param path The file path to export the image to, including extension.
     * @throws Exception If something goes wrong writing the file.
     */
    public void export(String path) throws Exception {

        String extension = path.substring(1 + path.lastIndexOf(".")).toLowerCase();

        ImageIO.write(getCurrentImage(), extension, new File(path));

        isSavedAfterOp = true;

    }

    /**
     * <p>
     * Save an image to a specified file.
     * </p>
     *
     * <p>
     * Saves an image to the file provided as a parameter. Also saves a set of
     * operations from the file with <code>.ops</code> added. So if you save to
     * <code>some/path/to/image.png</code>, this method will also save the
     * current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     *
     * @param imageFilename The file location to save the image to.
     * @throws Exception If something goes wrong.
     */
    public void saveAs(String imageFilename) throws Exception {
        this.imageFilename = imageFilename;
        this.opsFilename = imageFilename + ".ops";
        save();
        isSavedAfterOp = true;
    }

    /**
     * <p>
     * Save the recorded operations to a named file in the given directory.
     * </p>
     *
     * <p>
     * Saves the operations currently recorded. Does not stop an active
     * recording session, and does not clear the recorded operations.
     * </p>
     *
     * @param opsFilename the name of the file to save (without extension)
     * @param opsDir the directory to save the file in
     * @throws Exception if something goes wrong writing the file
     */
    public void opsSaveAs(String opsFilename, String opsDir) throws Exception {
        this.recordedOpsName = opsFilename + ".ops";

        if (this.recordedOpsName == null) {
            this.recordedOpsName = this.imageFilename + ".ops";
        }
        // Write image file based on file extension
        String newpath = opsDir + "/" + this.recordedOpsName;
        try ( // Write operations file

                FileOutputStream fileOut = new FileOutputStream(newpath); ObjectOutputStream objOut = new ObjectOutputStream(fileOut);) {
            objOut.writeObject(recordedOps);

        }
    }

    /**
     * <p>
     * Apply an {@link ImageOperation} to this image.
     * </p>
     *
     * @param op The operation to apply.
     */
    public void apply(ImageOperation op) {
        current = op.apply(current);
        ops.add(op);
        if (isRecording) {
            recordedOps.add(op);
        }
        isSavedAfterOp = false;
    }

    /**
     * <p>
     * Apply all recorded ops to this image.
     * </p>
     *
     */
    public void applyRecording() {
        if (!isRecording) {
            for (ImageOperation op : recordedOps) {
                current = op.apply(current);
                ops.add(op);
            }
            isSavedAfterOp = false;

        } else {
            System.out.println("Program is still recording");
            throw new RuntimeException("Program is still recording");

        }

    }

    /**
     * <p>
     * Stops the current macro recording session.
     * </p>
     *
     * <p>
     * Sets the recording flag to false, preventing any further image operations
     * from being added to the recorded operations list.
     * </p>
     */
    public void stopRecordingOps() {
        isRecording = false;
    }

    /**
     * <p>
     * Begins a new macro recording session.
     * </p>
     *
     * <p>
     * Clears any previously recorded operations and sets the recording flag to
     * true, so that all subsequent image operations are captured.
     * </p>
     */
    public void recordOps() {
        // Clear any existing recorded operations before starting a fresh recording
        if (!recordedOps.isEmpty()) {
            recordedOps.clear();
        }

        isRecording = true;
    }

    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * </p>
     */
    public void undo() {
        try {
            redoOps.push(ops.pop());
            refresh();
        } catch (java.util.EmptyStackException ex) {
            System.out.println("no more undo operations");
        }
    }

    /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the
     * image.
     * </p>
     */
    public void redo() {
        try {
            apply(redoOps.pop());
        } catch (java.util.EmptyStackException ex) {
            System.out.println("no more redo operations");
        }
    }

    /**
     * <p>
     * Get the current image after the operations have been applied.
     * </p>
     *
     * @return The result of applying all of the current operations to the
     * {@link original} image.
     */
    public BufferedImage getCurrentImage() {
        return current;
    }

    /**
     * <p>
     * Get a copy of current image
     * </p>
     *
     * @return The current image
     */
    public BufferedImage getCopyCurrentImage() {
        return deepCopy(current);
    }

    /**
     * <p>
     * Reapply the current list of operations to the original.
     * </p>
     *
     * <p>
     * While the latest version of the image is stored in {@link current}, this
     * method makes a fresh copy of the original and applies the operations to
     * it in sequence. This is useful when undoing changes to the image, or in
     * any other case where {@link current} cannot be easily incrementally
     * updated.
     * </p>
     */
    private void refresh() {
        current = deepCopy(original);
        for (ImageOperation op : ops) {
            current = op.apply(current);

        }
    }

    /**
     * <p>
     * Check whether the current image contains any transparent pixels.
     * </p>
     *
     * <p>
     * Iterates over every pixel in the current image and checks the alpha
     * channel value. If any pixel has an alpha value less than 255, the image
     * is considered to have transparency and true is returned. This is used to
     * determine whether the image can be safely exported to formats that do not
     * support transparency, such as JPEG.
     * </p>
     *
     * @return true if the image contains at least one transparent pixel, false
     * otherwise.
     */
    public boolean hasTransparency() {
        for (int y = 0; y < current.getHeight(); ++y) {
            for (int x = 0; x < current.getWidth(); ++x) {
                int argb = current.getRGB(x, y);
                if (((argb & 0xFF000000) >>> 24) < 255) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * <p>
     * checks whether the recording is currently active 
     * <p>
     * 
     * @return isRecording if recording is active 
     */
    public static boolean isRecording() {
    return isRecording;
}

}
