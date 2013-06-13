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

package rnaedit.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ChildDataHandler extends Thread{
    InputStream inputStream;
    String type;
    
    public ChildDataHandler(InputStream inputStream,String type){
      this.inputStream = inputStream;
      this.type = type;
    }//end constructor
    
    public void run(){
      try{
        InputStreamReader inputStreamReader = 
                       new InputStreamReader(inputStream);
        BufferedReader bufferedReader = 
                    new BufferedReader(inputStreamReader);
        String line=null;
        while((line = bufferedReader.readLine()) != null){
          System.out.println(type + line);
        }//end while
      }catch(Exception e){
        e.printStackTrace();  
      }//end catch
    }//end run
  }//end class ChildDataHandler