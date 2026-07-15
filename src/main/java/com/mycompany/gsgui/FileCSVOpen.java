package com.mycompany.gsgui;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

    /**
     *
     * @author gd
     */
    public class FileCSVOpen {
        private String inputPath;
        //TreeMap<String, String> paths = new TreeMap<String,String>();
        ArrayList<Pair> paths = new ArrayList<Pair>();

        public FileCSVOpen(String inputPath) {
            this.inputPath = inputPath;

        }

    public void readCSV() {
         ArrayList<Pair> sv = new ArrayList<Pair>();
        String line = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(this.inputPath));
            
            while ((line = br.readLine()) != null) {
                String [] values = line.split(","); 
                sv.add(new Pair(values[7], values[5] ));
                System.out.println(values[7]+", "+values[5]);
                
            }
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        } catch (IOException e) {
           
            e.printStackTrace();
        }
        this.paths =  sv;
    
    }
    
    public ArrayList<Pair> getPathsArray(){
        return paths;
    }
    
    
    public String getInputPath(){
        return inputPath;
    }
    
    }
    
    
