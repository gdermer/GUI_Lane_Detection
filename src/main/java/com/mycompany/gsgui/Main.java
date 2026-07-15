package com.mycompany.gsgui;

import com.mycompany.gsgui.CSVActions.LabelLane1;
import com.mycompany.gsgui.CSVActions.LabelLane2;
import com.mycompany.gsgui.CSVActions.LabelLane3;
import com.mycompany.gsgui.CSVActions.LabelLane4;
import com.mycompany.gsgui.CSVActions.MinusOneLane;
import com.mycompany.gsgui.CSVActions.plusOneLane;
import static com.mycompany.gsgui.ImageAction.target;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.imageio.*;

/**
 * <p>
 * Main class for A Non-Destructive Image Editor (ANDIE).
 * </p>
 *
 * <p>
 * This class is the entry point for the program. It creates a Graphical User
 * Interface (GUI) that provides access to various image editing and processing
 * operations.
 * </p>
 *
 * <p>
 *
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 *
 * @author Steven Mills
 * @version 1.0
 */
public class Main {
public static ImagePanel im;
public static CSVActions csvActions;
    //private static final ResourceBundle bundle = LanguageUtil.getBundle();
    /**
     * <p>
     * Launches the main GUI for the ANDIE program.
     * </p>
     *
     * <p>
     * This method sets up an interface consisting of an active image (an
     * {@code ImagePanel}) and various menus which can be used to trigger
     * operations to load, save, edit, etc. These operations are implemented
     * {@link ImageOperation}s and triggered via {@code ImageAction}s grouped by
     * their general purpose into menus.
     * </p>
     *
     * @see ImagePanel
     * @see ImageAction
     * @see ImageOperation
     * @see FileActions
     * @see EditActions
     * @see ViewActions
     * @see FilterActions
     * @see ColourActions
     * @see PictureAction
     * @see InternationalAction
     *
     * @throws Exception if something goes wrong.
     */
    private static void createAndShowGUI() throws Exception {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // Set up the main GUI frame
        JFrame frame = new JFrame("Main");

        Image image = ImageIO.read(Main.class.getClassLoader().getResource("imageGS.png"));
        //frame.setIconImage(image);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // The main content area is an ImagePanel
        im = new ImagePanel();
        im.setTool(new SelectionTool(im.getImage(), im));
        ImageAction.setTarget(im);
        JScrollPane scrollPane = new JScrollPane(im);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add in menus for various types of action the user may perform.
        JMenuBar menuBar = new JMenuBar();

        // File menus are pretty standard, so things that usually go in File menus go here.
        FileActions fileActions = new FileActions();
        menuBar.add(fileActions.createMenu());
        csvActions = new CSVActions();
        menuBar.add(csvActions.createMenu());
        addToolButtons(
                csvActions.minus1Lane,
                csvActions.lane1l,
                csvActions.lane2l,
                csvActions.lane3l,
                csvActions.lane4l,
                csvActions.plus1Lane
        //csvActions.changeLane
        );
        JButton laneLabel = new JButton(csvActions.getCurrlabel());
        laneLabel.setPreferredSize(new Dimension(100, 50));
        // 3. Add an ActionListener for standard clicks
        laneLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                laneLabel.setLabel(csvActions.getCurrlabel());
            }
        });

       
        im.add(laneLabel);
    
    
    

        // Likewise Edit menus are very common, so should be clear what might go here.
//        EditActions editActions = new EditActions();
//        menuBar.add(editActions.createMenu());
        // View actions control how the image is displayed, but do not alter its actual content
//        ViewActions viewActions = new ViewActions();
//        menuBar.add(viewActions.createMenu());
        // Filters apply a per-pixel operation to the image, generally based on a local window
//        FilterActions filterActions = new FilterActions();
//        menuBar.add(filterActions.createMenu());
        // Actions that affect the representation of colour in the image
//        ColourActions colourActions = new ColourActions();
//        menuBar.add(colourActions.createMenu());
//        InternationalAction internationalAction = new InternationalAction();
//        menuBar.add(internationalAction.createMenu());
        // Actions that affect the image orientation and scale
//        PictureAction pictureActions = new PictureAction();
//        menuBar.add(pictureActions.createMenu());
        // Actions for drawing shapes
//        DrawActions drawActions = new DrawActions();
//        menuBar.add(drawActions.createMenu());
//        MacroActions macroActions = new MacroActions();
//        menuBar.add(macroActions.createMenu());
//        ToolBar toolBar = new ToolBar();
//        frame.add(toolBar.createToolBar(
//                fileActions,
//                editActions,
//                viewActions,
//                filterActions,
//                colourActions,
//                pictureActions, macroActions), BorderLayout.NORTH);
//        
//        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        ActionMap actionMap = frame.getRootPane().getActionMap();
//        
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK), "Open");
//        actionMap.put("Open", fileActions.actions.get(0));
        //prompts the user to confirm on exit and to save if there is an image
        frame.addWindowListener(new WindowAdapter() {
            /**
             * A method that scans the event of the window closing if the user
             * has an image on the frame, it will open a prompt to export the
             * image, if yes, there will be another pane if no then the program
             * will close if cancel then nothing will happen
             */
            @Override
            public void windowClosing(WindowEvent e) {
                if (EditableImage.isRecording()) {
                    JOptionPane.showMessageDialog(null,
                            "PLEASE FINISH RECORDING FIRST",
                            "YOU GOT AN ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (target.getImage().hasImage() && !(EditableImage.isSavedAfterOp)) {
                    int confirm = JOptionPane.showOptionDialog(null,
                            "EXPORT BEFORE EXITING?",
                            "UNSAVED CHANGES", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (confirm == JOptionPane.OK_OPTION) {
                        JFileChooser fileChooser = new JFileChooser();
                        int result = fileChooser.showDialog(target, "EXPORT");

                        if (result == JFileChooser.APPROVE_OPTION) {
                            try {
                                String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();

                                if (!imageFilepath.contains(".")) {
                                    imageFilepath = imageFilepath + ".png";
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
                                System.exit(0);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null,
                                        "CANNOT EXPORT IMAGE, PLEASE TRY AGAIN",
                                        "ERROR",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else if (confirm == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    }
                } else {
                    System.exit(0);
                }
            }
        });

        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }
    public static void addToolButtons(Action... buttonActions) {
    for (Action a : buttonActions) {
        JButton button = new JButton(a);
        button.setPreferredSize(new Dimension(100,50));
        
        if(a instanceof LabelLane1 lane1l){
            
        
            button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                im.laneLabel.setLabel(csvActions.currLaneLabel);
            }
            
        });
                      }
        
        if(a instanceof LabelLane2 lane2l){
            
        
            button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                im.laneLabel.setLabel(csvActions.currLaneLabel);
            }
            
        });
                      }
        
        if(a instanceof LabelLane3 lane3l){
            
        
            button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                im.laneLabel.setLabel(csvActions.currLaneLabel);
            }
            
        });
                      }
        
        if(a instanceof LabelLane4 lane4l){
            
        
            button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                im.laneLabel.setLabel(csvActions.currLaneLabel);
            }
            
        });
                      }
        
        if(a instanceof MinusOneLane minus1Lane){
            
        
            button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                im.laneLabel.setLabel(csvActions.currLaneLabel);
            }
            
        });
                      }
        
        if(a instanceof plusOneLane plus1Lane){
            
        
            button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                im.laneLabel.setLabel(csvActions.currLaneLabel);
            }
            
        });
                      }
                      
     
        im.add(button);
        
    }
    }

    /**
     * <p>
     * Main entry point to the ANDIE program.
     * </p>
     *
     * <p>
     * Creates and launches the main GUI in a separate thread. As a result, this
     * is essentially a wrapper around {@code createAndShowGUI()}.
     * </p>
     *
     * @param args Command line arguments, not currently used
     * @throws Exception If something goes awry
     * @see #createAndShowGUI()
     */
    public static void main(String[] args) throws Exception {

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        });
    }
}
