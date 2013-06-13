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
package rnaedit.util;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/*
 * ResultLog.java
 * Created by Denny Chen Dai
 * Logging class that outputs info into files 
 *
 */
public class ResultLog {
    
    private PrintStream out = null;  //
    /** Creates a new instance of ResultLog */
    public ResultLog() {
    }
    
    /** Creates a new instance of SolutionLog */
    public ResultLog(String filename) {
    
        String file_name = filename;
        try{out = new PrintStream(new FileOutputStream(file_name));
        }
        catch (FileNotFoundException e) {;}
    }
    
    public void println(String data){
        if(out==null)
            return; 
        out.println(data);
        out.flush();
    } 
}
