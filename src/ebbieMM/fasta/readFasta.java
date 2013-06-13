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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/* 
 * Created by Denny Chen Dai 
 * readFasta.java
 * Utility function for parse in Fasta File 
 */
public class readFasta {
    
    private long pos;
    private String data = "";

    /* Creates a new instance of readFasta */
    public readFasta() {
    }
    
    /*
     * Parse in the fasta file and store all fasta sequences into Vector 
     */
    public Vector read(String filename){
    
        Vector sequence_list = new Vector();
        pos = 0; char tmp;
        data = loadfile(filename);
        tmp = getNextToken(); // ">"
        
        do{     
        String sequence = "";    
        String organName = "";
        //read header name
        do{
            organName+= tmp;
            tmp = getNextToken();
        }while(tmp != '\n');       
        //read in sequence
        do{
            tmp = getNextToken();
            if(tmp!='\n' && tmp!='#' && tmp!='>')
                sequence += tmp;    
        }while(tmp!='#' && tmp!='>');
        
        Fasta fasta = new Fasta(organName,sequence);
        sequence_list.add(fasta); 
       
        }while(tmp != '#');    
        return sequence_list;
    }
    
    /*
     * String parsing utility function 
     */
    public char getNextToken(){
        char tmp_char;
        do{
            tmp_char = data.charAt((int)pos++);
        }while(tmp_char == ' ' ||  tmp_char == '\t');
        return tmp_char;
    }
    
    /**
     * Load in a file into memory 
     */
    public String loadfile(String file_name){
        
        StringBuffer  m_data = new StringBuffer();
        try{
            File f = new File(file_name);
            FileReader in = new FileReader(f);   
            int len;
            char[] buffer = new char[4096];
            while((len = in.read(buffer)) != -1){
                String s = new String(buffer,0,len);
                m_data.append(s);
            }
        }catch(IOException e){
            System.err.println("No Input File!");
            return "";
        }
        m_data.append('#');
        return m_data.toString();
    }
    
}
