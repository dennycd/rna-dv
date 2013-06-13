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


package rnaedit.ct;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Denny Chen Dai
 */
public class writeCT {
    
    private BufferedWriter out = null; 
  
    public writeCT() {
    }
    
    /**
     * Write to file in dot-bracket (dp) file format 
     * 
     * #comments
     * sequence
     * structure
     * 
     * @param rna_ct
     * @param filename
     */
    public void writeDP(CT rna_ct, String filename){
        
        System.out.print("writing DP files to " + filename);
        
        String data = "";
        String file_name = filename;
        
        rna_ct.header = "#" + rna_ct.header;
        
        /*let's dp file content*/
        data = rna_ct.header + "\n" ; //header info       
        data += rna_ct.generateSequence() + "\n";
        data += rna_ct.generateStruct();
 
        /*now flush out to the specified file*/
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(file_name));         
            out.write(data);
            out.close();
        }
        catch (IOException e) {;}
        
        System.out.print("...done \n");
    }
    
    

    /*
     * Write CT structure data into CT file format
     * @filename  the file name to write to disk
     * @rna_ct the RNA ct structure data 
     */
    public void write(CT rna_ct, String filename ){
        
        System.out.println("writing CT files...");
        
        String data = "";
        String file_name = filename;
        
        /*let's generate the ct file string info*/
        //data += rna_ct.sequence.size() + " " + rna_ct.header + "\n" ; //header info
        data += rna_ct.sequence.size() + " ENERGY " + rna_ct.fitness  + "\n" ; 
        
        /*format:  index base    pos    index+2   pair    index */
        for(int i=0;i<rna_ct.sequence.size();i++){
           CT_base base = (CT_base)rna_ct.sequence.get(i);
           data+= base.index 
                   + "\t" + base.neo.toUpperCase() 
                   + "\t" + i 
                   + "\t" +  ( i==rna_ct.sequence.size()-1 ? 0 : i+2)   //last one shall be 0, other start with 2
                   + "\t" + base.pair
                   + "\t" + base.index
                   + "\n";
        }
        
        /*now flush out to the specified file*/
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(file_name));         
            out.write(data);
            out.close();
        }
        catch (IOException e) {;}
    }
    
        /**
     * Write CT structure data into RNAML format
     * 
     * @param rna_ct The CT structure data
     * @param filename The abosolute filename to store to
     */
    public void writeRNAML(CT rna_ct, String filename){
        System.out.print("\nwriting RNAML files...");
        
        String head = " <?xml version=\"1.0\"?>\n" +
                "<!DOCTYPE rnaml SYSTEM \"rnaml.dtd\">\n" +
                "<rnaml version=\"1.0\">\n" +
                "<molecule id=\"1\">\n" +
                "<sequence>\n" +
                "<numbering-system id=\"1\" used-in-file=\"false\">\n";
        
        String numbering_range = "<numbering-range>\n" +
                            "<start>" + 1 + "</start>\n" +
                            "<end>" + rna_ct.sequence.size() + "</end>\n" +
                            "</numbering-range>\n";
        
        String seq_data = "<seq-data>" + rna_ct.sequence() + "</seq-data>\n";
        
        head = head + numbering_range + "</numbering-system>\n" 
                + seq_data 
                + "</sequence>\n" +
                "<structure>\n" +
                "<model id=\"?\">\n" +
                "<str-annotation>\n";
        

        String body = "";
        for(int i=0;i<rna_ct.sequence.size();i++){
            
            CT_base base = (CT_base)rna_ct.sequence.get(i);
            if(base.pair <= 0) //free base. skip
                continue;
            if(base.index > base.pair)  //reaching right half of a pair, skip
                continue;
            
            body += "<base-pair comment=\"?\">\n" +
                    "<base-id-5p>\n" +
                    "<base-id><position>" + base.index + "</position></base-id>\n" +
                    "</base-id-5p>\n" +
                    "<base-id-3p>\n" +
                    "<base-id><position>" + base.pair + "</position></base-id>\n" +
                    "</base-id-3p>\n" + 
                    "</base-pair>\n";
        }
        
                    
        String tail = "</str-annotation>\n" +
                "</model>\n" +
                "</structure>\n" +
                "</molecule>\n" +
                "</rnaml>";
    
        String data = head + body + tail;
        
        /*now flush out to the specified file*/
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));         
            out.write(data);
            out.close();
            System.out.print("done\n");
        }
        catch (IOException e) {;}
    }
    
    
}
