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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
    
/*
 * RString.java
 * Author: Denny Chen 
 * A generic string parsing function class 
 * Created : Oct , 2004, 1:30 PM
 * Last edited: Mar 31, 2005, 2:30pm
 * (I add '.' as the standard token...)
 * @version 2.0  $Date: 2008/10/01 23:18:23 $
*/
public class RString{
	
	public int pos;
	public String m_data;

	public RString(String origin){
		m_data = origin;
		pos = 0;
	}
        
        public int LookNextTokenInLine(){
            //Post: return relative pos of next token to previous last \n symbol 
            
                char tmp_char;
                int tmp_pos = pos; //next token pos                                                                          
		do{
                        tmp_char = m_data.charAt(tmp_pos++);
                }while(tmp_char ==' ' || tmp_char == '\n'  || tmp_char == '\t');
    
                
            int tmp = tmp_pos - 1; // previous \n position 
            while(tmp >=0 ){
                if(m_data.charAt(tmp) == '\n')
                    break;
                tmp--;
            }
            
            return (tmp_pos - 1 - tmp);   // starting 1, the nth position in a line 
        }
    
	public String GetNextWord(){
                //Pre:  fetch next word from data,pos set to the one next of the word
                //Post:   including number is also fetch as word
		StringBuffer tmp=new StringBuffer();
                char tmp_char;                                                                                                             
                do{
			tmp_char = m_data.charAt(pos++);
                }while(tmp_char ==' ' || tmp_char == '\n' || tmp_char == '\t' || tmp_char == '\r');
                                                                                                                             
                if(!IsStandardToken(tmp_char)){         //read in a none standard char
                        tmp.append(tmp_char);
                        return tmp.toString();
                }//if
                                                                                                                             
                                                                                                                             
                do{
                        tmp.append(tmp_char);
                                                                                                                             
                        if(!IsStandardToken(m_data.charAt(pos)))
                                break;
                                                                                                                             
                        tmp_char = m_data.charAt(pos++);
                 }while(true);
                                                                                                                             
                return tmp.toString();
                                                                                                                             
        }

	//============================
	public String GetNextToken(){
                //Pre: fetch next char (except blank,space,and ctrl)
                //Post:
		StringBuffer tmp =new StringBuffer();
                char tmp_char;
		
		do{
                        tmp_char = m_data.charAt(pos++);
                }while(tmp_char ==' ' || tmp_char == '\n'  || tmp_char == '\t');
	                                                                                 
                                                                                                                             
                tmp.append(tmp_char);
                                                                                                                             
                return tmp.toString();
                                                                                                                             
        }
         
        /**
         * look ahead next token, including \n 
         */
        public String SeeNextTokenEx(){
                //Pre: look ahead for one token,do not change pos, ignoring \n,\t ' ''
                //Post:
                StringBuffer tmp = new StringBuffer();
                char tmp_char;
                int tmp_pos = pos;                                                                           
		do{
                        tmp_char = m_data.charAt(tmp_pos++);
                }while(tmp_char ==' ' || tmp_char == '\t');

                                                                                                                             
                tmp.append(tmp_char);
                                                                                                                             
                return tmp.toString();
        }
        
        public String SeeNextToken(){
                //Pre: look ahead for one token,do not change pos, ignoring \n,\t ' ''
                //Post:
                StringBuffer tmp = new StringBuffer();
                char tmp_char;
                int tmp_pos = pos;                                                                           
		do{
                        tmp_char = m_data.charAt(tmp_pos++);
                }while(tmp_char ==' ' || tmp_char == '\n'  || tmp_char == '\t');

                                                                                                                             
                tmp.append(tmp_char);
                                                                                                                             
                return tmp.toString();
        }
	
	boolean  IsStandardToken(char data){
	        //Pre:
		//Post:
		 if ( data >='a' && data <= 'z' ||
        	        data >='A' && data <= 'Z' ||
                	data >= '0' && data <='9' ||
	                data == '_' ||
			data == '.'  ||
                         data == '-')
        	        return true;
        	else
                	return false;
                                                                                                                             
	}//

          
    	/**
        *  Load a specified file into string
        *  @param file_name The specified file to be load in...
        */     
    public static String loadfile(String file_name){

         StringBuffer  data = new StringBuffer();

         try{
                File f = new File(file_name);
                FileReader in = new FileReader(f);


                int len;
                char[] buffer = new char[4096];
                while((len = in.read(buffer)) != -1){
                        String s = new String(buffer,0,len);
                        data.append(s);
                }//while


          }catch(IOException e){
                        System.err.println("Error, No Input File Found while loading " + file_name  + " !! ");
                        return "";
           }//catch

                return data.toString();

        }//loadfle
 
}
