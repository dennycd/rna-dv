/********************************************************************************************
*  RNA-DV Version 1.0
*  Copyright (c)  2008 September  Denny Chen Dai, Herbert H. Tsang.
*  Permission is granted to copy, distribute and/or modify this document
*  under the terms of the GNU Free Documentation License, Version 1.2
*  or any later version published by the Free Software Foundation;
*  with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
*  Texts.  A copy of the license is included in the section entitled "GNU
*  Free Documentation License".
*********************************************************************************************/


/*
 * Efn.java
 *
 * the EFN energy model, provided by UNAFold program 
 *
 */

package rnaedit.thermo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import rnaedit.ct.CT;
import rnaedit.ct.writeCT;
import rnaedit.util.ChildDataHandler;

/**
 *
 * @author Denny Chen Dai
 */
public class Efn {
    
    private String cmd = ""; //assuming user installed the UNAFold package
    private String dir = "";
    private static String RNALIB = "RnaDVLib";        
    
    /** Creates a new instance of Efn */
    public Efn() {
                
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")){
            cmd = "ct-energy.exe";
            dir = RNALIB + File.separator + "unafold\\";
        }
        else{
            cmd = "./ct-energy";
            dir = RNALIB + File.separator +"unafold/";
        }
        
    }
    
    /**
     * execute the ct-energy program to compute the energy for specified CT
     *@ct_file The read in CT file 
     */
    public String fitness(CT ct_rna){
        
        /*output the structure into standard ct files*/
        if(ct_rna.ct_file.equals(""))
            try {
                File tmpFile = 
                        new File(RNALIB + File.separator + "tmp" + File.separator + "tmp.ct");
                tmpFile.createNewFile();
                ct_rna.ct_file = tmpFile.getAbsolutePath();
        } catch (IOException ex) {
            Logger.getLogger(Efn.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        (new writeCT()).write(ct_rna, ct_rna.ct_file);
        
        System.out.print("Calling  " + cmd + "  \"" + ct_rna.ct_file + "\" "); 
        String energy ="";       
    
        //respawning the process
        ProcessBuilder pb = new ProcessBuilder(dir + cmd, ct_rna.ct_file);
        
        Map map = pb.environment();
        String curP = (new File ("")).getAbsolutePath() + File.separator + dir;
        map.put("UNAFOLDDAT", curP );  // unafold need this env to locate energy data
        
        
        pb.redirectErrorStream(true);
        Process p = null;      
        try {
            p = pb.start();
        } catch (IOException e) {
            System.err.println("spawning problem " + e.getMessage());
            return "";
        }

        /*read in the output of the process*/    
        try{
            InputStreamReader inputStreamReader = 
                       new InputStreamReader(p.getInputStream());
            BufferedReader bufferedReader = 
                    new BufferedReader(inputStreamReader);
            String line=null;
            System.out.print("...reading...");
            while((line = bufferedReader.readLine()) != null){
                energy += line;
                //System.out.println(line + "...");
            }//end while
        }catch(Exception e){
            e.printStackTrace();  
        }//end catch
            
        System.out.println("finished " + energy);             
        return energy; 
    }

      
    void getChildOutput(Process proc){
        try{
         //Spawn a thread.
        ChildDataHandler outputHandler = 
                           new ChildDataHandler(
                           proc.getInputStream(),"OUT: ");         
        //Start them running
        outputHandler.start();
        }catch( Exception e ){
          e.printStackTrace();
        }//end catch
    }

}


