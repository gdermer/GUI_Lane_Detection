package com.mycompany.gsgui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author gd
 */
public class CSVActions {

    ArrayList<Action> actions = new ArrayList<Action>();
    public int i = 1;
    public String path;
    public FileCSVOpen fo;
    public CSVOutput output;
public plusOneLane plus1Lane;
    public MinusOneLane minus1Lane;
    public LabelLane1 lane1l;
    public LabelLane2 lane2l;
    public LabelLane3 lane3l;
    public LabelLane4 lane4l;
    //public changeLaneLabel changeLane;
    public String currLaneLabel;
    public String currPath;
    
    //** constrctor **/
    public CSVActions() {
        OpenCSVPic csvAction = new OpenCSVPic("OPEN a csv", new ImageIcon(), "OPEN A FILE", KeyEvent.VK_O, 1); //change this back to i when sorted to avdoi default 

        csvAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        actions.add(csvAction);

//        plusOneLane plus1Lane = new plusOneLane("right", new ImageIcon(), "OPEN A FILE", KeyEvent.VK_O, i); //change this back to i when sorted to avdoi default 
//
//        plus1Lane.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
//        actions.add(plus1Lane);
//        
//        MinusOneLane minus1Lane = new MinusOneLane("left", new ImageIcon(), "OPEN A FILE", KeyEvent.VK_O, i); //change this back to i when sorted to avdoi default 
//
//        minus1Lane.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
//        actions.add(minus1Lane);
        plus1Lane = new plusOneLane("right", new ImageIcon(), "NEXT IMAGE", KeyEvent.VK_O, i);
        minus1Lane = new MinusOneLane("left", new ImageIcon(), "PREVIOUS IMAGE", KeyEvent.VK_O, i);
        lane1l = new LabelLane1("lane 1", new ImageIcon(), "LABEL LANE 1", KeyEvent.VK_O, i);
        lane2l = new LabelLane2("lane 2", new ImageIcon(), "LABEL LANE 2", KeyEvent.VK_O, i);
        lane3l = new LabelLane3("lane 3", new ImageIcon(), "LABEL LANE 3", KeyEvent.VK_O, i);
        lane4l = new LabelLane4("lane 4", new ImageIcon(), "LABEL LANE 4", KeyEvent.VK_O, i);
        //changeLane = new changeLaneLabel(currLaneLabel, new ImageIcon(), currLaneLabel, KeyEvent.VK_O, i);
//            LabelLane1 lane1l = new LabelLane1("lane 1", new ImageIcon(), "OPEN A FILE", KeyEvent.VK_O, i); //change this back to i when sorted to avdoi default 
//
//        lane1l.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
//        actions.add(lane1l);
//
//              LabelLane2 lane2l = new LabelLane2("lane 2", new ImageIcon(), "OPEN A FILE", KeyEvent.VK_O, i); //change this back to i when sorted to avdoi default
//
//        lane2l.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
//        actions.add(lane2l);
//
//          LabelLane3 lane3l = new LabelLane3("lane 3", new ImageIcon(), "OPEN A FILE", KeyEvent.VK_O, i); //change this back to i when sorted to avdoi default
//
//        lane3l.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
//        actions.add(lane3l);
//          LabelLane4 lane4l = new LabelLane4("lane 2", new ImageIcon(), "OPEN A FILE", KeyEvent.VK_O, i); //change this back to i when sorted to avdoi default 
//
//        lane4l.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
//        actions.add(lane4l);
    }
public String getCurrlabel(){
    return currLaneLabel;
}


    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("csv");
        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));
        }
        return fileMenu;
    }

    public class OpenCSVPic extends ImageAction {

        public OpenCSVPic(String name, ImageIcon icon, String desc, Integer mnemonic, int i) {
            super(name, icon, desc, mnemonic);
            i = i;
        }

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
                            //String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                            //target.getImage().open(imageFilepath);
                            path = imageFilepath;
                            fo = new FileCSVOpen(imageFilepath);
                           
                            fo.readCSV();
                            System.out.println("opened csv");

                            String imagePath = fo.getPathsArray().get(1).getKey(); // open the first image 
                            currPath = fo.getPathsArray().get(1).getKey();
                            currLaneLabel = fo.getPathsArray().get(1).getValue();
                            //    System.out.println("open path image ");

                            if (imagePath.contains(".png")
                                    || imagePath.contains(".jpeg")
                                    || imagePath.contains(".jpg")
                                    || imagePath.contains(".webp")
                                    || imagePath.contains(".svg")
                                    && !target.getImage().hasTransparency()) {
                                //target.getImage().open(imagePath);
                                target.getImage().export(imagePath);
                            } else {
                                imagePath = imagePath.concat(".png");
                                target.getImage().export(imagePath);
                            }
                            if (!imagePath.contains(".png") && target.getImage().hasTransparency()) {
                                if (!imagePath.contains(".webp") && target.getImage().hasTransparency()) {
                                    JOptionPane.showMessageDialog(null, "THIS FILE TYPE DOES NOT SUPPORT TRANSPARENCY. EXPORTED AS '.PNG'");
                                    target.getImage().export(imagePath.concat(".png"));
                                }
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "CANNOT EXPORT IMAGE, PLEASE TRY AGAIN", "ERROR " + ex, JOptionPane.ERROR_MESSAGE);
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
                    System.out.println("read csv");
                    path = imageFilepath;
                    fo = new FileCSVOpen(imageFilepath);
                     output = new CSVOutput(imageFilepath+"_output");
                     
                    System.out.println(output.getcsvFIleName());
                    fo.readCSV();
                    System.out.println("opened csv");

                     String imagePath = fo.getPathsArray().get(1).getKey(); // open the first image 
                            currPath = fo.getPathsArray().get(1).getKey();
                            currLaneLabel = fo.getPathsArray().get(1).getValue();
                            output.outputcsv(imagePath, getCurrlabel());
                    System.out.println("open path image ");
                    target.getImage().open(imagePath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "COULDNT OPEN FILE " + ex, "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }

            target.repaint();
            target.getParent().revalidate();
        }

    }

    public class plusOneLane extends ImageAction {

        public plusOneLane(String name, ImageIcon icon, String desc, Integer mnemonic, int i) {
            super(name, icon, desc, mnemonic);
            i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            i++;
           
                       
            try {

                String imagePath = fo.getPathsArray().get(i).getKey(); // open the first image 

                target.getImage().open(imagePath);
                currLaneLabel = fo.getPathsArray().get(i).getValue();
                 output.outputcsv(imagePath, getCurrlabel());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "COULDNT OPEN FILE " + ex, "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);

            }
            

            target.repaint();
            target.getParent().revalidate();
        }

    }

    
     public class MinusOneLane extends ImageAction {

        public MinusOneLane(String name, ImageIcon icon, String desc, Integer mnemonic, int i) {
            super(name, icon, desc, mnemonic);
            i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            i--;
           
                       
            try {

                String imagePath = fo.getPathsArray().get(i).getKey(); // open the first image 

                target.getImage().open(imagePath);
                currLaneLabel = fo.getPathsArray().get(i).getValue();
                 output.outputcsv(imagePath, getCurrlabel());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "COULDNT OPEN FILE " + ex, "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);

            }

            target.repaint();
            target.getParent().revalidate();
        }

    }
     
     public class LabelLane1 extends ImageAction {

        public LabelLane1(String name, ImageIcon icon, String desc, Integer mnemonic, int i) {
            super(name, icon, desc, mnemonic);
            i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
       
            i++;
           
                       
            try {
                String imagePath1 = fo.getPathsArray().get(i-1).getKey();
               output.outputcsv(imagePath1, "1");
               fo.getPathsArray().get(i).setValue("1");
               
               
                String imagePath = fo.getPathsArray().get(i).getKey(); // open the first image 

                target.getImage().open(imagePath);
                currLaneLabel = "1";
                 output.outputcsv(imagePath, getCurrlabel());
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "COULDNT OPEN FILE " + ex, "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
                System.out.println("if you see it the problem is the output ");

            }
    
            target.repaint();
            target.getParent().revalidate();
        }

    }
     
     public class LabelLane2 extends ImageAction {

        public LabelLane2(String name, ImageIcon icon, String desc, Integer mnemonic, int i) {
            super(name, icon, desc, mnemonic);
            i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
       
            i++;
           
                       
            try {
                String imagePath1 = fo.getPathsArray().get(i-1).getKey();
               output.outputcsv(imagePath1, "2");
               fo.getPathsArray().get(i).setValue("2");
               
                String imagePath = fo.getPathsArray().get(i).getKey(); // open the first image 

                target.getImage().open(imagePath);
                  currLaneLabel = "2";
     output.outputcsv(imagePath, getCurrlabel());
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "COULDNT OPEN FILE " + ex, "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
                System.out.println("if you see it the problem is the output ");

            }
  
            target.repaint();
            target.getParent().revalidate();
        }

    }
     
     public class LabelLane3 extends ImageAction {

        public LabelLane3(String name, ImageIcon icon, String desc, Integer mnemonic, int i) {
            super(name, icon, desc, mnemonic);
            i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
       
            i++;
           
                       
            try {
                String imagePath1 = fo.getPathsArray().get(i-1).getKey();
               output.outputcsv(imagePath1, "3");
               fo.getPathsArray().get(i).setValue("3");
               
                String imagePath = fo.getPathsArray().get(i).getKey(); // open the first image 

                target.getImage().open(imagePath);
                  currLaneLabel = "3";
     output.outputcsv(imagePath, getCurrlabel());
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "COULDNT OPEN FILE " + ex, "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
                System.out.println("if you see it the problem is the output ");

            }
    
            target.repaint();
            target.getParent().revalidate();
        }

    }
     
     public class LabelLane4 extends ImageAction {

        public LabelLane4(String name, ImageIcon icon, String desc, Integer mnemonic, int i) {
            super(name, icon, desc, mnemonic);
            i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
       
            i++;
           
                       
            try {
                String imagePath1 = fo.getPathsArray().get(i-1).getKey();
               output.outputcsv(imagePath1, "4");
               fo.getPathsArray().get(i).setValue("4");
               
                String imagePath = fo.getPathsArray().get(i).getKey(); // open the first image 

                target.getImage().open(imagePath);
                  currLaneLabel = "4";
     output.outputcsv(imagePath, getCurrlabel());
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "COULDNT OPEN FILE " + ex, "YOU GOT AN ERROR", JOptionPane.ERROR_MESSAGE);
                System.out.println("if you see it the problem is the output ");

            }
    
            target.repaint();
            target.getParent().revalidate();
        }

    }
     
    
}
