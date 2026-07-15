package com.mycompany.gsgui;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class CSVOutput {
    private String csvFileName;
    private FileWriter writer;
    
    
    public CSVOutput(String csvFileName) throws IOException{
        this.csvFileName = csvFileName;
        this.writer = new FileWriter(new File(csvFileName), true);
        
         writer.append("Image_path, lane\n");
    }
    
    
    
    public void outputcsv(String newPic, String laneNum) throws IOException{
        this.writer.append(newPic).append(", ").append(laneNum).append("\n");

    }
    
    
    public String getcsvFIleName(){
        return this.csvFileName;
    }
}
