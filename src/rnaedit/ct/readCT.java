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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import rnaedit.util.RString;

/**
 * Read in the CT file and store into an intermidiate prepresentation 
 * @author Denny Chen Dai
 */
public class readCT {

    /** Creates a new instance of readCT */
    public readCT() {
    }
    
    
    /**
     * Parse in one xml chunck containg one base pair information
     *         
     * <base-pair comment="?">
            <base-id-5p>
               <base-id>
                  <molecule-id ref="1"><position>1</position>
               </base-id>
            </base-id-5p>
            <base-id-3p>
               <base-id>
                  <molecule-id ref="2"><position>16</position>
               </base-id>
            </base-id-3p>
         </base-pair>
     * 
     * @param data  The string data trunck containing one pairing info
     * @return pairing info   " left_base_index&right_base_index"
     *
     */
    private String parseBasePair(String data){

        String left="",right="";  
        int start=0,end=0;
           
        start = data.indexOf("<position>", 0) + "<position>".length();
        end = data.indexOf("</position>", start);
        left = data.substring(start, end);
        
        start = data.indexOf("<position>", end) + "<position>".length();
        end = data.indexOf("</position>", start);
        right = data.substring(start, end);

        return left + "&" + right;
    }
    
    
    /**
     * 
     * Parse in RNAML files and store it in CT format
     * 
     * @param filename
     * @return
     */
    public CT parseRNAML(String filename){
        CT rna = new CT(); 
        String data = this.loadfile(filename);
        
        
        int start=0,end=0;
        /*obtain sequence content*/
        String seq="";
        int pos = 0;
        do{
            start = data.indexOf("<seq-data>", pos);
            end = data.indexOf("</seq-data>", start);
            if(start!=-1 && end!=-1)
                seq += data.substring(start+ "<seq-data>".length(), end); 
            else
                break;
            pos = end;
        }while(true);
        
        /*populate CT bases using a primary sequence content*/
        rna.populate(seq);
        
        /*obtain each pairing*/
        pos = 0;
        do{
            start = data.indexOf("<base-pair", pos);
            end = data.indexOf("</base-pair>", start);
            if(start!=-1 && end!=-1){
                /*extract pairing info*/
                String[] pair = parseBasePair(data.substring(start, 
                        end + "</base-pair>".length())).split("&");
                /*add pairing info*/
                rna.addBond(pair[0], pair[1]);
                rna.addBond(pair[1], pair[0]);
            }
            else
                break;
            pos = end;
        }while(true);
        
        
        return rna;
    }
    
    /**
     * Parse in a CT file and return the CT class instance 
     */
    public CT parseCT(String filename){
        
        //System.out.println("parsing in CT files...");
        
        CT rna = new CT(); 
        int pos = 0; String tmp;
        
        //skip header begin with #
        String[] ct_data = this.loadfile(filename).split("\n");    
        String raw_ct = "";
        for(int i=0; i< ct_data.length;i++){
            if(ct_data[i].charAt(0) != '#')
                raw_ct += ct_data[i] + "\n";
        }
        
        //rebuild the raw ct string data
        //RString data = new RString(raw_ct + "#");
    
        
        /*
        String header = data.GetNextWord();
        int length = Integer.parseInt(header); //sequence length
        
        while( ! data.SeeNextTokenEx().equals("\n"))
        { 
            tmp = data.GetNextToken();
            header = header + tmp; 
        };
        rna.header = header;
       */
        ct_data = raw_ct.split("\n");
        rna.header = ct_data[0]; //first line as header
        RString size = new RString(ct_data[0] + "#");
        int length = Integer.parseInt(size.GetNextWord());
        
        /*read in all bases*/
        for(int i=1; i <= length; i++){
            
            RString data = new RString(ct_data[i] + "#");
            
            try{
                
                int index = Integer.parseInt(data.GetNextWord()); //index value 
                String value = data.GetNextWord(); // nucleotide value           
                data.GetNextWord(); //the third column 
                data.GetNextWord(); // the fourth column
                int pair = Integer.parseInt(data.GetNextWord()); //paired index
            
                CT_base base = new CT_base(index, value, pair);
                /*add into rna sequence*/
                rna.add(base);
                
            }catch(NumberFormatException e){
                System.err.println("number: " +i);
            }
            
        }
        
        return rna; 
    }
    /**
     * 
     * Parse in dotbracket structure and sequence content separately
     * 
     * @param structure Secondary structure in dot bracket format
     * @param seq Sequence content 
     * @return CT object populated with structreu & sequence content
     */
    public CT parseBracketString(String structure, String sequence){
       
        CT rna = new CT(); 
   
        /*construct header*/
        rna.header = structure.length() + "  ENERGY = 0";
                  
        int n = structure.length();
        char[] seq = structure.toCharArray();
        Stack stk = new Stack();
        
        for(int i=0;i<n;i++){
            
            char tmp = structure.charAt(i);
            if(sequence==null || sequence.equals(""))
                rna.add(new CT_base(i+1,"N",0));
            else
                rna.add(new CT_base(i+1,String.valueOf(sequence.charAt(i)),0));
            
            if(tmp == '(')
                stk.push(Integer.valueOf(i));   //put in left pair inside
            else
            if(tmp == ')'){   
                
                int index_L = ((Integer)stk.pop()).intValue();
                
                CT_base baseL = (CT_base)rna.sequence.get(index_L);
                CT_base baseR = (CT_base)rna.sequence.get(i);
                
                baseL.pair = baseR.index;
                baseR.pair = baseL.index;
            } 
        }
 
        return rna;
        
        
    }
    
           
    /**
     * Parse in a CT file from dot_bracket format source  *.dp file
     * 
     * Assum each structure is contained in one line in dot_bracket format
     * 
     * skip lines begin with # format
     * 
     * #comment line
     * sequence content
     * structure content
     * 
     * 
     * @param filename
     * @return
     */      
    public CT parseDP(String filename){
        
                
        CT rna = new CT(); 
       
        //skip header begin with #
        String[] ct_data = this.loadfile(filename).split("\n");          
        String sequence = "",structure = "";
        
        
        for(int i=0; i< ct_data.length;i++){
            if(!ct_data[i].equals("") && ct_data[i].charAt(0) != '#'){
                if(ct_data[i].charAt(0)=='(' || ct_data[i].charAt(0)==')' ||
                        ct_data[i].charAt(0)=='.' )
                    structure += ct_data[i];
                else
                    sequence += ct_data[i];
            }
            else                        /*construct header*/
                rna.header += ct_data[i] + "\n";
        }
        
        /*assume only one seq&struct per file*/
    
        /*check consistency*/
        if(sequence.length()!=structure.length()){
            System.out.println("Sequence & Structure length not match...");
            return null;
        }
        
        /*construct sequence*/
        int n = structure.length();      
       
        //rna.header += structure.length() + "  ENERGY = 0";

        Stack stk = new Stack();        
        for(int i=0;i<n;i++){
            
            char tmp = structure.charAt(i);
            rna.add(new CT_base(i+1,String.valueOf(sequence.charAt(i)),0));
            
            if(tmp == '(')
                stk.push(Integer.valueOf(i));   //put in left pair inside
            else
            if(tmp == ')'){   
                
                int index_L = ((Integer)stk.pop()).intValue();
                
                CT_base baseL = (CT_base)rna.sequence.get(index_L);
                CT_base baseR = (CT_base)rna.sequence.get(i);
                
                baseL.pair = baseR.index;
                baseR.pair = baseL.index;
            }
        }
 
        return rna;
    }
    
    /**
     * Load in a file into memory 
     */
    public String loadfile(String file_name){
        
        System.out.println("loading in " + file_name + "...");
        
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
        //m_data.append('#');
        return m_data.toString();
    }
    
}










