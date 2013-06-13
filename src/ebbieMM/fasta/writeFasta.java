/********************************************************************************************
*  Ebbie-MM Version 4.0
*  Copyright (c)  2008 April  Denny Chen Dai, Herbert H. Tsang, H. Alexander Ebhardt.
*  Permission is granted to copy, distribute and/or modify this document
*  under the terms of the GNU Free Documentation License, Version 1.2
*  or any later version published by the Free Software Foundation;
*  with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
*  Texts.  A copy of the license is included in the section entitled "GNU
*  Free Documentation License".
*********************************************************************************************/
package ebbieMM.fasta;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/*
 * Created by Denny Chen Dai 
 * writeFasta.java
 * Output a sequence in fasta format into file 
 * Updated: output a series of sequences in fasta format into file
 */
public class writeFasta {
    
    //private PrintStream out = null;
    private BufferedWriter out = null; 
    /** Creates a new instance of writeFasta */
    public writeFasta() {
    }
    
    /**
     * Write a series of sequencs to file
     * @filename the file name to write to 
     * @data sequences data 
     */
    public void write(String filename, Vector data){
        String file_name = filename;
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(file_name));
            for(int i=0;i<data.size();i++){
                Fasta seq = (Fasta)data.get(i);         
                //out = new PrintStream(file_name);
                out.write( seq.head + "\t" + seq.energy + "\t" + seq.ss + "\n" );
                out.write(seq.sequence + "\n\n"); 
                out.flush();
            } 
            out.close();
        }
        catch (IOException e) {;}
    }
    
    /*
     * Write fasta sequence into file
     * @filename  the file name to write 
     * @data  sequence data in fasta format 
     */
    public void write(String filename, Fasta data){
        String file_name = filename;
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(file_name));         
            //out = new PrintStream(file_name);
            out.write( data.head + "\n" );
            out.write(data.sequence + "\n");  //maybe too big to read all in memory
            out.close();
        }
        catch (IOException e) {;}
    }
}
