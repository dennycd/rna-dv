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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rnaedit.thermo;

import ebbieMM.fasta.Fasta;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import rnaedit.ct.CT;

/**
 *
 * @author cda18
 */
public class RNAeval {

    private String cmd = "";
    private String dir = "";
     private static String RNALIB = "RnaDVLib";   
 
    public RNAeval() {

        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            cmd = "RNAeval.exe";
        } else {
            cmd = "./RNAeval";
        }
         dir = RNALIB + File.separator + "vienna";

    }

    public String fitness(CT ct_rna) {

        String sequence = ct_rna.generateSequence();
        String structure = ct_rna.generateStruct();

        System.out.print("Calling  " + cmd + "  \"" + ct_rna.ct_file + "\" ");

        //respawning the process
        ProcessBuilder pb = new ProcessBuilder(dir + File.separator + cmd, "");
        pb.redirectErrorStream(true);
        Process p = null;


        BufferedWriter bw = null;
        try {
            p = pb.start();
            /*pump output to process*/
            OutputStream os = p.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw, 4096/* buffsize in chars */);
            bw.write(sequence + "\n" + structure + "\n");
            bw.flush();
        } catch (IOException e) {
            System.err.println("spawning problem " + e.getMessage());
            return "";
        }



        String lseq = "", lss = "";
        /*read in the output of the process*/
        try {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(p.getInputStream());
            BufferedReader br =
                    new BufferedReader(inputStreamReader);
            String line = null;
            String buffer = "";

            do {
            
                
                line = br.readLine();
                buffer += line + "\n";
            
                if(buffer.indexOf(".")!= -1)
                    break;
            
            } while (true);

            if (!buffer.equals("")) {
                /*extract correct info*/
                String[] buf = buffer.split("\n");
                lseq = buf[buf.length - 2];
                lss = buf[buf.length - 1];
            }

            /*sequence info*/
            /*         while ((line = br.readLine()) != null) {
            buf += line;
            
            lseq = line;
            break;
            }
             */         /*structure body info*/
            /*          while ((line = br.readLine()) != null) {
            lss = line;
            break;
            }
            
             */
            bw.write("@\n"); /*end of process for RNAdistance*/
            bw.close(); /*once all folded close writer IO*/

        } catch (Exception e) {
            e.printStackTrace();
        }//end

        System.out.print("...done \n");
        return (new Fasta()).parse_ss(lss);
    }
}
