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

import rnaedit.util.RString;

/*
 * Fasta.java
 * Created by Denny Chen Dai
 * Java Class Representation of FASTA object including (head, sequence) information
 *
 * Modified on May 6, 2008 
 *  - add in folding strucuture & energy information
 */
public class Fasta {
    
    public String head = ""; /*header contains FASTA header information*/
    public String sequence = "";
    /*number of sequence instance having identical sequence string (but may have different head..)*/
    public int count = 0; 
    public String ss = ""; /*folded secondary structure in bracket string notation*/
    public String energy = ""; /*energy level upon the current ss*/
  
    public String heu = ""; /*distance heuristics*/
    public String heu2 = "";/*native energy heuristics*/
    public long runtime = -1;/*runtime effort to locate this sequence in miliseconds*/
    public int layer = -1;/*layer information w.r.t. a given kernel sequence*/        
    public int distance = -1; /*structural distance information w.r.t. a target structures*/
    
    /* Creates a new instance of Fasta */
    public Fasta() {
    }
    public Fasta(String hd, String seq){
        this.head = hd;
        this.sequence = seq;
    }
    
    public Fasta(String hd, String seq, String ener){
        this.head = hd;
        this.sequence = seq;
        this.energy = ener;
    }
    
    public void display(){
        System.out.println("#" + head 
                + "\n" + sequence 
                + "   " + ss
                + "\nEnergy: " + energy 
                + "   Heu(S-Distance): " + heu
                + "   Heu2(N-Energy): " + heu2);
    }

    /*
     * Parse in string for the secondary structure folded, and the corresponding energy level
     *
     *@String data  The raw data input coming from RNAfold
     *    ((..))....()   (-20.3)
     *
     * This method needs further carefule
     * 
     * @param energy in string 
     */
    public String parse_ss(String data){
    
        int begin = data.lastIndexOf("(")+1;
        int end = data.lastIndexOf(")"); 
        
        try{
            this.energy = data.substring(begin,end);
        }
        catch(StringIndexOutOfBoundsException e){
            //System.err.println("error reading energy value");
        }
               
        try{
            this.ss = data.substring(0, data.lastIndexOf("(")-1);
        } 
        catch(StringIndexOutOfBoundsException e){
            //System.err.println("error reading secondary structure");
        }
        
        return this.energy;
    }
    
}
