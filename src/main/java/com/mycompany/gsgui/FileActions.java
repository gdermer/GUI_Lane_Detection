package com.mycompany.gsgui;

import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class FileActions {


    /**
     * A list of actions for the File menu.
     */
    public ArrayList<Action> actions = new ArrayList<>();

    /**
     * <p>
     * Create a set of File menu actions.
     * </p>
     */
    public FileActions() {

        //actions.add(new FileOpenAction(bundle.getString("OPEN"), new ImageIcon(getClass().getClassLoader().getResource("icons-open3.png")), bundle.getString("OPEN A FILE") +" (CTRL O)", KeyEvent.VK_A));
        FileOpenAction openAction = new FileOpenAction("OPEN", new ImageIcon(), "OPEN A FILE", KeyEvent.VK_O);
        openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        actions.add(openAction);

        FileSaveAction saveAction = new FileSaveAction("SAVE", new ImageIcon(), "SAVE THE FILE", KeyEvent.VK_S);
        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        actions.add(saveAction);

        FileSaveAsAction saveAsAction = new FileSaveAsAction("SAVE AS", new ImageIcon(), "SAVE A COPY", KeyEvent.VK_W);
        saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        actions.add(saveAsAction);

        FileExitAction exitAction = new FileExitAction("EXIT", new ImageIcon(), "EXIT THE PROGRAM", KeyEvent.VK_Q);
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        actions.add(exitAction);

        FileExportAction exportAction = new FileExportAction("EXPORT", new ImageIcon(), "EXPORT THE FILE", KeyEvent.VK_E);
        exportAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        actions.add(exportAction);

    }

    /**
     * <p>
     * Create a menu containing the list of File actions.
     * </p>
     *
     * @return The File menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("FILE");

        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to open an image from file.
     * </p>
     *
     * @see EditableImage#open(String)
     */
    public class FileOpenAction extends ImageAction {

        /**
         * <p>
         * Create a new file-open action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        FileOpenAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-open action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the FileOpenAction is triggered. It
         * prompts the user to select a file and opens it as an image.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
               
            if (target.getImage().hasImage() && !(EditableImage.isSavedAfterOp)) {
                int confirm = JOptionPane.showOptionDialog(null,
                        "EXPORT BEFORE OPENING?",
                        "UNSAVED CHANGES", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
            
                if (confirm == JOptionPane.OK_OPTION) {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showDialog(target, "EXPORT");
                    if (result == JFileChooser.APPROVE_OPTION) {
                        try {
                            String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                            if (imageFilepath.contains(".png")
                                    || imageFilepath.contains(".jpeg")
                                    || imageFilepath.contains(".jpg")
                                    || imageFilepath.contains(".webp")
                                    || imageFilepath.contains(".svg")
                                    && !target.getImage().hasTransparency()) {
                                target.getImage().export(imageFilepath);
                            } else {
                                imageFilepath = imageFilepath.concat(".png");
                                target.getImage().export(imageFilepath);
                            }
                            if (!imageFilepath.contains(".png") && target.getImage().hasTransparency()) {
                                if (!imageFilepath.contains(".webp") && target.getImage().hasTransparency()) {
                                    JOptionPane.showMessageDialog(null, "THIS FILE TYPE DOES NOT SUPPORT TRANSPARENCY. EXPORTED AS '.PNG'");
                                    target.getImage().export(imageFilepath.concat(".png"));
                                }
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "CANNOT EXPORT IMAGE, PLEASE TRY AGAIN", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }
                if (confirm == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }

            JFileChooser fileChooser = new JFileChooser();
            //JOptionPane.showMessageDialog(null, "Please open an image", "Open a file", JOptionPane.PLAIN_MESSAGE);
            int result = fileChooser.showOpenDialog(target);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().open(imageFilepath);
                } catch (Exception ex) {
                    //JOptionPane.showMessageDialog(null, "COULDNT OPEN FILE", "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
                        JOptionPane.showMessageDialog(null, "COULDNT OPEN FILE: " + ex, "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
}
                }
            

            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to save an image to its current file location.
     * </p>
     *
     * @see EditableImage#save()
     */
    public class FileSaveAction extends ImageAction {

        /**
         * <p>
         * Create a new file-save action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        FileSaveAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-save action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the FileSaveAction is triggered. It
         * saves the image to its original filepath.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(null, "NEED TO LOAD IMAGE FIRST", "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (EditableImage.isRecording()) {
                JOptionPane.showMessageDialog(null,
                        "PLEASE FINISH RECORDING FIRST",
                        "YOU GOT AN ERROR",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                target.getImage().save();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "COULDNT SAVE FILE", "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * <p>
     * Action to save an image to a new file location.
     * </p>
     *
     * @see EditableImage#saveAs(String)
     */
    public class FileSaveAsAction extends ImageAction {

        /**
         * <p>
         * Create a new file-save-as action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        FileSaveAsAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-save-as action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the FileSaveAsAction is triggered. It
         * prompts the user to select a file and saves the image to it.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(null, "NEED TO LOAD IMAGE FIRST", "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (EditableImage.isRecording()) {
                JOptionPane.showMessageDialog(null,
                       "PLEASE FINISH RECORDING FIRST",
                        "YOU GOT AN ERROR",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();

                    // If no extension specified, default to .png silently
                    if (!imageFilepath.contains(".")) {
                        imageFilepath = imageFilepath + ".png";
// If extension specified but not recognised, prompt the user
                    } else if (!imageFilepath.endsWith(".png") && !imageFilepath.endsWith(".jpg")
                            && !imageFilepath.endsWith(".jpeg") && !imageFilepath.endsWith(".webp")) {

                        Object[] formatButtons = {"Yes", "No"};
                        int formatChoice = JOptionPane.showOptionDialog(null,
                                "UNRECOGNISED FILE FORMAT. SAVE AS PNG INSTEAD?",
                                "INVALID FILE FORMAT",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                formatButtons,
                                formatButtons[0]);

                        if (formatChoice == 0) {
                            imageFilepath = imageFilepath + ".png";
                        } else {
                            return;
                        }
                    }

                    boolean isTransparencyFormat = imageFilepath.endsWith(".png")
                            || imageFilepath.endsWith(".webp");

                    if (!isTransparencyFormat) {
                        Object[] transparencyButtons = {"Yes", "No", "Cancel"};
                        int choice = JOptionPane.showOptionDialog(null,
                              "THIS FILE TYPE DOES NOT SUPPORT TRANSPARENCY. EXPORT AS PNG INSTEAD?",
                                "TRANSPARENCY WARNING",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                transparencyButtons,
                                transparencyButtons[0]);

                        if (choice == 0) {
                            int dotIndex = imageFilepath.lastIndexOf('.');
                            if (dotIndex > 0) {
                                imageFilepath = imageFilepath.substring(0, dotIndex);
                            }
                            imageFilepath = imageFilepath + ".png";
                            target.getImage().saveAs(imageFilepath);
                        } else if (choice == 1) {
                            target.getImage().saveAs(imageFilepath);
                        } else {
                            return;
                        }
                    } else {
                        target.getImage().saveAs(imageFilepath);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "COULDNT SAVE AS FILE", "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    /**
     * <p>
     * Action to quit the ANDIE application.
     * </p>
     */
    public class FileExitAction extends AbstractAction {

        /**
         * <p>
         * Create a new file-exit action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        FileExitAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-exit action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the FileExitAction is triggered. It
         * quits the program.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showOptionDialog(null,
                    "ARE YOU SURE YOU WANT TO CLOSE?",
                    "EXIT CONFIRMATION", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == JOptionPane.OK_OPTION) {
                System.exit(0);
            }

        }

    }

    /**
     * <p>
     * Action to export the image.
     * </p>
     */
    public class FileExportAction extends ImageAction {

        /**
         * <p>
         * Create a new file-export action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        FileExportAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-Export action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the FileExportAction is triggered. It
         * export the image to its chosen path.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(null, "NEED TO LOAD IMAGE FIRST", "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (EditableImage.isRecording()) {
                JOptionPane.showMessageDialog(null,
                        "PLEASE FINISH RECORDING FIRST",
                        "YOU GOT AN ERROR",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();

                    // If no extension specified, default to .png silently
                    if (!imageFilepath.contains(".")) {
                        imageFilepath = imageFilepath + ".png";
// If extension specified but not recognised, prompt the user
                    } else if (!imageFilepath.endsWith(".png") && !imageFilepath.endsWith(".jpg")
                            && !imageFilepath.endsWith(".jpeg") && !imageFilepath.endsWith(".webp")) {

                        Object[] formatButtons = {"Yes", "No"};
                        int formatChoice = JOptionPane.showOptionDialog(null,
                                "UNRECOGNISED FILE FORMAT. SAVE AS PNG INSTEAD?",
                                "INVALID FILE FORMAT",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                formatButtons,
                                formatButtons[0]);

                        if (formatChoice == 0) {
                            imageFilepath = imageFilepath + ".png";
                        } else {
                            return;
                        }
                    }

                    boolean isTransparencyFormat = imageFilepath.endsWith(".png")
                            || imageFilepath.endsWith(".webp");

                    if (!isTransparencyFormat) {
                        Object[] transparencyButtons = {"Yes", "No", "Cancel"};
                        int choice = JOptionPane.showOptionDialog(null,
                                "THIS FILE TYPE DOES NOT SUPPORT TRANSPARENCY. EXPORT AS PNG INSTEAD?",
                                "TRANSPARENCY WARNING",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                transparencyButtons,
                                transparencyButtons[0]);

                        if (choice == 0) {
                            int dotIndex = imageFilepath.lastIndexOf('.');
                            if (dotIndex > 0) {
                                imageFilepath = imageFilepath.substring(0, dotIndex);
                            }
                            imageFilepath = imageFilepath + ".png";
                            target.getImage().export(imageFilepath);
                        } else if (choice == 1) {
                            target.getImage().export(imageFilepath);
                        } else {
                            return;
                        }
                    } else {
                        target.getImage().export(imageFilepath);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            java.text.MessageFormat.format("CANT EXPORT THE FILE {0}",
                                    new Object[]{ex.getMessage()}),
                            "YOU GOT AN ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

}
