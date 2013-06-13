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


package rnadv;

import java.applet.*;
 import java.awt.*;
 import java.io.*;
 import java.net.*;
 
 public class TestApplet extends Applet{
   String fileToRead = "test1.txt";
   StringBuffer strBuff;
   TextArea txtArea;
   Graphics g;
 
   public void init(){
     txtArea = new TextArea(100, 100);
     txtArea.setEditable(false);
     add(txtArea, "center");
     String prHtml = this.getParameter("fileToRead");
     if (prHtml != null) fileToRead = new String(prHtml);
     readFile();
   }
 
   public void readFile(){
     String line;
     URL url = null;
     String dir = getDocumentBase().toString();
     
     
     System.out.println("dir is :: " + dir + " --- file sperator " + File.separator);
     
     int idx = dir.lastIndexOf("/") + 1;
     
     System.out.println("index is  :: " + idx);
     
     String file = dir.substring(0, idx) + fileToRead;
     
     System.out.println("file is :: " + file);
     
     
     try{
       url = new URL(file);
     }
     catch(MalformedURLException e){}
          
     try{
         
         
         
       InputStream in = url.openStream();
       BufferedReader bf = new BufferedReader(new InputStreamReader(in));
       strBuff = new StringBuffer();
       while((line = bf.readLine()) != null){
         strBuff.append(line + "\n");
       }
     txtArea.append("File Name : " + fileToRead + "\n");
     txtArea.append(strBuff.toString());
     }
     catch(IOException e){
       e.printStackTrace();
     }
 
     
   }
 }
 
 
 
 
 
 
 
 
 
 
 
 
 
 