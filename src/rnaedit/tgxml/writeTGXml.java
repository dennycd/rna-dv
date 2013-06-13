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


package rnaedit.tgxml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Denny Chen Dai
 */
public class writeTGXml {
    
    private BufferedWriter out = null; 
    /** Creates a new instance of writeTGXml */
    public writeTGXml() {
    }
    
    /*
     * Write XML Data string to xml file 
     * @filename  the file name to write 
     * @data  Touch Graph xml string data 
     */
    public void write(String data, String filename ){
        System.out.println("writing XML files...");
        String file_name = filename;
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(file_name));         
            //out = new PrintStream(file_name);
            out.write(data);
            out.close();
        }
        catch (IOException e) {;}
    }
    
}
