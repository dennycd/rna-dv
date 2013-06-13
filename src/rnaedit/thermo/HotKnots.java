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
 * 
 * HotKnots energy function class
 * 
 */

package rnaedit.thermo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import rnaedit.ct.CT;
import rnaedit.ct.CT_base;

/**
 *
 * @author Denny Chen Dai
 */
public class HotKnots {
    
    
    private String cmd = ""; 
    private String dir = "";
    private static String RNALIB = "RnaDVLib";        
            
    /** Creates a new instance of HotKnots */
    public HotKnots() {
                
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")){
            dir = RNALIB + File.separator +"hotknot\\bin\\";
            cmd = "computeEnergy.exe";
        }
        else{      
            cmd = "./computeEnergy";;
            dir = RNALIB + File.separator +"hotknot/bin/";
        }
        
    }
    
    /**
     * Convert a given ct file into a corresponding bp sequence file
     * 
     * use a tmp file "tmp.bpseq"
     * @return the Absolute file path
     */
    private String convertCT_BPSEQ(CT ct_rna){
        
        String data = "";
        for(int i=0; i<ct_rna.sequence.size();i++){
            CT_base base = (CT_base)ct_rna.sequence.get(i);
            data = data + base.index  + "\t" + base.neo + "\t" + base.pair + "\n";
        }
        
             
        String file_name = RNALIB + File.separator +"tmp" + File.separator + "tmp.bpseq";       
        /*now flush out to the specified file*/
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(file_name));         
            out.write(data);
            out.close();
        }
        catch (IOException e) {;}
        
        return (new File(file_name)).getAbsolutePath();

    }
      
    /**
     * execute the ct-energy program to compute the energy for specified CT
     *@ct_file The read in CT file 
     */
    public String fitness(CT ct_rna){
        
        String file = this.convertCT_BPSEQ(ct_rna);
        
        System.out.print("Calling  " + cmd + "  \"" + file + "\" "); 
        String energy ="";       
        
        //respawning the process
        ProcessBuilder pb = new ProcessBuilder(cmd, file);
        pb.directory(new File(dir));
        
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
            while((line = bufferedReader.readLine()) != null){
                energy += line;
            }//end while
        }catch(Exception e){
            e.printStackTrace();  
        }//end catch
        
        
        
        System.out.print("finished \n");             
        return Float.toString(this.parse_energy(energy));
    }
    
    /**
     * 
     * 
     * @param output The output string from hotknot energy program
     * @return The energy value 
     */
    private float parse_energy(String output){
        float energy = 0;
        
        int pos = output.indexOf("energy is");
        int end = output.indexOf("Arc", pos);
        
        if(pos==-1 || end==-1)
            return 0;
        
        try{
            energy = Float.parseFloat(output.substring(pos+10, end-2));
        }catch(NumberFormatException e){
            //System.out.println("");
            return 0;
        }
        //System.out.println(output);
        return energy/(float)(-1000);
    }

 /*     
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
*/
}
