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

import Varna.varna.EmbeddedRNA;
import Varna.varna.Embedding;
import Varna.varna.RNASecStr;
import Varna.varna.VARNAPanel;
import java.text.ParseException;
import java.util.Vector;

/**
 * Java Class Representation of RNA Secondary Structure in CT Format
 * a sequence of neucliotides also containing pairing info. 
 * @author Denny Chen Dai
 */
public class CT{
    public String header = "";
    public Vector sequence = null; //the vector representing an RNA sequence containing a set of consecutive bases
    
    //GUI specific
    public boolean updated = true; //indication of whether the CT has just been modified
    
    /*thermodynamic energy level*/
    public int fitness = 0;
    
    /*local CT file location*/
    public String ct_file = ""; 
    
    /** Creates a new instance of CT */
    public CT() {
        sequence = new Vector();
    }
    
    /**
     * Modify sequence content
     * 
     * @param id  index in the bae
     * @param content nucliotide content
     */
    public void setSequence(int id, String content){
        for(int i=0;i<sequence.size();i++){
            CT_base base = (CT_base)sequence.get(i);
            if(base.index==id){
                base.neo = content;
                this.updated = true;
                break;
            }
        }
            
    }
    
    /**
     * Return the sequence content of the CT 
     * 
     * @return The full sequence content
     */
    public String sequence(){
        String seq = "";
        for(int i=0;i<sequence.size();i++)
            seq += ((CT_base)sequence.get(i)).neo;
        return seq;
    }
    
    
    /*produce the sequence string representation*/
    public String generateSequence(){
        char []seq = new char[sequence.size()];
        
        for(int i=0;i<sequence.size();i++){
            CT_base base = (CT_base)sequence.get(i);
            seq[i] = base.neo.charAt(0);
        }      
        return String.copyValueOf(seq);
    }
    
    /*produce the secondary structure in bracket format and return the string*/
    public String generateStruct(){
        char[] ss = new char[sequence.size()];
        
        for(int i=0;i<sequence.size();i++){
            CT_base base = (CT_base)sequence.get(i);
            if(base.pair==0)
                ss[i] = '.';
            else{
                if(base.index < base.pair)
                    ss[i] = '(';
                else
                    ss[i] = ')';
            }
        }
        
        return String.copyValueOf(ss);
    }

    /**
     * Populate a CT chains using only the sequence content (with no pairing)
     * 
     * @param seq contains the sequence content only - seq may contain blanks within, required
     *          to filter 
     */
    public void populate(String seq){
        if(sequence!=null)
            sequence.clear();
        else
            sequence = new Vector();
        
        /*filter blanks out of sequence content*/
        String data = "";
        String[] seq_set = seq.trim().split(" ");
        for(int i=0;i<seq_set.length;i++){
            data += seq_set[i].trim();
        }
        
        /*populate sequence vector*/
        for(int i=0;i<data.length();i++){
            CT_base base = new CT_base(i+1, String.valueOf(data.charAt(i)),0);
            sequence.add(base);
        }
        
        
    }
    
    
    /**
     * given two base index, remove the pairing relationship between them
     *
     *@base1 the index of the first base
     *@base2 the index of the second base
     */
    public void removeBond(String base1, String base2){
        System.out.println("Removing CT " + base1 + " --> " +  base2 );
    
        for(int i=0;i<sequence.size();i++)
        {
            CT_base base = (CT_base)sequence.get(i);
            
            /*add containt that sequence bond cannot be breaked*/
            
            if(base.index == Integer.parseInt(base1) || base.index == Integer.parseInt(base2))
                if(base.pair == Integer.parseInt(base1) || base.pair == Integer.parseInt(base2)){
                    base.pair = 0; //remove the bond
                }//
        }     
        this.updated = true; 
    }
    
    /**
     * Add pairing to the CT structure
     * 
     * @param base1 Left pairing base index (begin with 1)
     * @param base2 Right pairing base index 
     */
    public void addBond(String base1, String base2){
        
        int diff = Math.abs(Integer.parseInt(base1) - Integer.parseInt(base2));
        if(diff <=1){
            System.out.println("pairing difference less than one, skip...");
            return;
        }
          
        System.out.println("Adding CT " + base1 + " --> " +  base2 );
         
        for(int i=0;i<sequence.size();i++)
        {
            CT_base base = (CT_base)sequence.get(i);
            if(base.index == Integer.parseInt(base1) || base.index == Integer.parseInt(base2))
                if(base.pair == 0){
                    base.pair = ( base.index==Integer.parseInt(base1) ? 
                                   Integer.parseInt(base2) : Integer.parseInt(base1) );       
                    
                    System.out.println(base.index + "==" + base.pair);
                }
        }       
        this.updated = true;
    }
    
    /*
     * Check if adding the bond between base1 & base2 is valid or not (allow phseudoknot)
     */
    public boolean validBond(String base1, String base2){
        
        
        boolean result = false;
        char L = '\0',R = '\0';
        
        
        for(int i=0;i<sequence.size();i++)
        {
            CT_base base = (CT_base)sequence.get(i);
            if(base.index == Integer.parseInt(base1) || base.index == Integer.parseInt(base2)){
                
                if(L=='\0')
                    L = base.neo.toUpperCase().charAt(0);
                else
                if(R=='\0')
                    R = base.neo.toUpperCase().charAt(0);
                /*pairing limit constraint*/
                if(base.pair == 0)  //this one is not paired, valid for adding bond
                    result = true;
                else{                //already paired, therefore not allow to add additional
                    result = false;
                    break;
                }
                
            }
        } 
        /*pairing constraint  AU  GC  GU*/ 
        if(!(L=='A' && R=='U' || L=='U' && R=='A' ||
                L=='G' && R=='C' || L=='C' && R=='G' ||
                L=='G' && R=='U' || L=='U' && R=='G'))
           result = false;
       
        //System.out.println(base1 + "--" + base2);
        
        /*pairing base difference constraint*/            
        if(Math.abs(Integer.parseInt(base1) - Integer.parseInt(base2)) <= 3){
            result = false;
            //System.out.println("invalid pairing detected!!");
        }
        
        return result;
    }
    
    /*
     * append a new ct base 
     */
    public void add(CT_base base){
        if(sequence != null)
            sequence.add(base);
    }
    
    /**
     * Display the sequence information again 
     */
    public void display(){
        System.out.println();
    }
    

    /**
     * Compute an initial graph layout  X&Y position for the structure
     * 
     */
    public void computeLayout(int drawMode){
       
        /**
         * First build the edit tree  then recursively determine the position
        ETree tree = new ETree();
        tree.parse(this);
        String layout = tree.automatic_layout();
        
        System.out.println("populating automatic layout...");
        System.out.append(layout);
         */
        
        /*parse in the structure and sequence content*/
        RNASecStr r=null;
        try{
            r = new RNASecStr(this.generateSequence(),this.generateStruct());
        }catch(ParseException e){
            System.err.println(e.getMessage());
            System.err.println("Invalid secondary structure at char "+e.getErrorOffset()+".");
            return;
        }
                
        /*build coordinates*/
       Embedding coords;
       switch(drawMode)
       {
           case VARNAPanel.DRAW_MODE_RADIATE:
               coords = EmbeddedRNA.drawRNASecStr(r);
               break;
           case VARNAPanel.DRAW_MODE_CIRCLE:
		coords = EmbeddedRNA.drawRNASecStrCircle(r);
		break;
           default : 
               coords = new Embedding();
               break;
       }
       
       /*extract coordinates into CT bases*/
       EmbeddedRNA rna = new EmbeddedRNA(r,coords);         
       for(int i=0;i<rna._em._coords.length;i++){
           CT_base base = (CT_base)this.sequence.get(i);
           base.pos_X = rna._em._coords[i].x;
           base.pos_Y = rna._em._coords[i].y;
       } 
       
    }
    
}






















