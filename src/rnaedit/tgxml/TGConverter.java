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
import Varna.varna.VARNAPanel;
import com.touchgraph.linkbrowser.TGLinkBrowser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import rnaedit.ct.CT;
import rnaedit.ct.CT_base;

/**
 * Convert from a read in CT class into touch graph xml format 
 *
 * @author Denny Chen Dai
 */
public class TGConverter {
    
    /*initial layerout for the structure graph*/
    private int pos_head_x = 100;
    private int pos_head_y = 0;
    private int pos_tail_x = 700;
    private int pos_tail_y = 300;
    private static String RNALIB = "RnaDVLib";
    
    /** Creates a new instance of TGConverter */
    public TGConverter() {
    }
    
    private int get_pos_x(int index, int size){
        if(index==0)
            return this.pos_head_x;
        else
        if(index==size)
            return this.pos_tail_x;
        else
            return this.pos_head_x + (int)((float)(this.pos_tail_x - this.pos_head_x) * (float)index/(float)size);
    }
    
    private int get_pos_y(int index, int size){
        if(index==0)
            return this.pos_head_y;
        else
        if(index==size)
            return this.pos_tail_y;
        else
            return  this.pos_head_y + (int)((float)(this.pos_tail_y - this.pos_head_y) * (float)index/(float)size);
    }
    
    /* read in CT class then convert them into TG XML format, returned in string */
    public String convert(CT data, String file_name, int VIEW_OPT){
        System.out.println("converting CT files to XML...");
        
        if(VIEW_OPT == TGLinkBrowser.VIEW_OPT_CIRCLE)
            data.computeLayout(VARNAPanel.DRAW_MODE_CIRCLE);
        else
            data.computeLayout(VARNAPanel.DRAW_MODE_RADIATE);
        
        String xml = "";
        String tmp = "";
        
        xml+= this.loadXmlHead();
        xml += "<TOUCHGRAPH_LB version=\"1.20\"> \n" ;
        
        xml +="<NODESET> \n";    
        
        
        /*add a 5' beginning node*/
        tmp = "\t <NODE nodeID=\"" +  0  +  "\"> \n";           
        tmp+= "\t\t <NODE_LOCATION x=\"" 
                    + (int)((CT_base)data.sequence.get(0)).pos_X + "\" y=\""
                    + (int)((CT_base)data.sequence.get(0)).pos_Y + "\" visible=\"true\"/> \n";
           
            tmp+= "\t\t <NODE_LABEL label=\"" + "5'" + "\" shape=\"" +  "4" 
                    +  "\" backColor=\"" + this.get_color("5'")  +  "\" textColor=\"FFFFFF\" fontSize=\"12\"/> \n";  
            tmp+= "\t </NODE> \n";
            xml += tmp; 
        /**************************8*/
        
        for(int i=0; i< data.sequence.size(); i++){
            CT_base base = (CT_base)data.sequence.get(i);
            
            
            
            tmp = "\t <NODE nodeID=\"" +  base.index  +  "\"> \n"; 
            
            tmp+= "\t\t <NODE_LOCATION x=\"" 
                    + (int)base.pos_X + "\" y=\""
                    +(int)base.pos_Y + "\" visible=\"true\"/> \n";
           
            tmp+= "\t\t <NODE_LABEL label=\"" + base.neo + "\" shape=\"" +  this.shape(base.neo) 
                    +  "\" backColor=\"" + this.get_color(base.neo)  +  "\" textColor=\"FFFFFF\" fontSize=\"12\"/> \n";  
            tmp+= "\t </NODE> \n";
           
            xml += tmp; 
        }
        
        /*add a 3'ending node*/
        tmp = "\t <NODE nodeID=\"" +  (((CT_base)data.sequence.lastElement()).index+1)  +  "\"> \n";           
        tmp+= "\t\t <NODE_LOCATION x=\"" 
                    + (int)((CT_base)data.sequence.get(data.sequence.size()-1)).pos_X + "\" y=\""
                    + (int)((CT_base)data.sequence.get(data.sequence.size()-1)).pos_Y + "\" visible=\"true\"/> \n";
           
            tmp+= "\t\t <NODE_LABEL label=\"" + "3'" + "\" shape=\"" +  "4" 
                    +  "\" backColor=\"" + this.get_color("3'")  +  "\" textColor=\"FFFFFF\" fontSize=\"12\"/> \n";  
            tmp+= "\t </NODE> \n";
            xml += tmp; 
        
        /**************************8*/
        
        xml+="</NODESET> \n\n";
        
        
        
        /*edges*/
        xml+="<EDGESET>\n";
        
        //for the paring edges 
        for(int i=0; i< data.sequence.size(); i++){
            CT_base base = (CT_base)data.sequence.get(i);
            if(base.pair <= 0)  //skip the one not paired 
                continue; 
            if(base.pair < base.index) //skip the reversed edge
                continue;
            tmp = "\t <EDGE fromID=\"" + base.index + "\" toID=\"" + base.pair;
            tmp+= "\"  type=\"3\" length=\"" + 20 + 
                    "\" visible=\"true\" color=\"0000B0\"/> \n";
            xml += tmp; 
        }
        
        xml+="\n";
        
        
        //for the seuqnece bond edges , use short length edge 
         for(int i=0; i< data.sequence.size(); i++){
            CT_base base = (CT_base)data.sequence.get(i);
            if(i == 0)  //skip the first 
                continue;
            /*add backward edges */
            tmp = "\t <EDGE fromID=\"" + (base.index -1) + "\" toID=\"" + base.index;
            tmp+= "\"  type=\"2\" length=\"" + 2 + 
                    "\" visible=\"true\" color=\"6F6F6F\"/> \n";
            xml += tmp; 
         }
        
        /*connect 5' to the first, 3' to the last */
        tmp = "\t <EDGE fromID=\"" + "0" + "\" toID=\"" + "1";
        tmp+= "\"  type=\"2\" length=\"" + 2 + "\" visible=\"true\" color=\"6F6F6F\"/> \n";
        xml += tmp; 
       
        
        tmp = "\t <EDGE fromID=\"" + ((CT_base)data.sequence.lastElement()).index + "\" toID=\"" + (((CT_base)data.sequence.lastElement()).index +1);
        tmp+= "\"  type=\"2\" length=\"" + 2 + "\" visible=\"true\" color=\"6F6F6F\"/> \n";
        xml += tmp;
        
        /*******************************************/
        
       
        xml+="</EDGESET> \n\n";
        
        xml+= this.parameter();
        xml+= "</TOUCHGRAPH_LB>\n";
        
        
        if(!file_name.equals(""))
            (new writeTGXml()).write(xml, file_name);
        
        return xml; 
    }

    String get_color(String value){
                
  /*      value = value.toUpperCase();
        if(value.equals("A"))
            return "00A0F0";
        if(value.equals("C"))
            return "F05050";
        if(value.equals("G"))
            return "FF00FF";
        if(value.equals("U"))
            return "A04000";
        else
            return "FFFFFF";
    */
        return "FFFFFF";
    }
    
    int get_length(int base, int pair){
        if(Math.abs(base-pair) > 1)
            return 20;
        else
            return 2; 
    }
    
    int shape(String value){
        value = value.toUpperCase();
        if(value.equals("A"))
            return 4;
        if(value.equals("C"))
            return 4;
        if(value.equals("G"))
            return 4;
        if(value.equals("U"))
            return 4;
        else
            return 4;
    }
    
    String parameter(){
        
        return   " <PARAMETERS>\n" + 
                  " <PARAM name=\"offsetX\" value=\"627\"/>\n" + 
                  " <PARAM name=\"rotateSB\" value=\"0\"/>\n" +
                  " <PARAM name=\"zoomSB\" value=\"-7\"/>\n" + 
                  " <PARAM name=\"offsetY\" value=\"19\"/>\n" + 
                  " </PARAMETERS> \n";
        
    }
    
    /**
     * Load the xml header into memory 
     */
    public String loadXmlHead(){
        
        String file_name = RNALIB + File.separator + "TG.xml";
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
        m_data.append("\n\n");
        return m_data.toString();
    }
    
}
